package ngo.music.player.Controller;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.Observable;

import ngo.music.player.View.MusicPlayerMainActivity;

/**
 * Created by fabiongo on 1/8/2016.
 */
public class WaveFormController extends Observable {
    private static WaveFormController instance;
    private int mNumFrames;
    private int[] mFrameGains;
    private int samplesPerFrame;
    private int[] mFrameLens;
    private int[] mFrameOffsets;

    public static WaveFormController getInstance(){
        if(instance == null){
            instance = new WaveFormController();
        }
        return instance;
    }
    private ProgressListener mProgressListener = null;

    public int getSamplesPerFrame() {
        return 8192;
    }

    public int getNumFrames() {
        return mNumFrames;
    }

    public int[] getFrameGains() {
        return mFrameGains;
    }


    // Progress listener interface.
    public interface ProgressListener {
        /**
         * Will be called by the SoundFile class periodically
         * with values between 0.0 and 1.0.  Return true to continue
         * loading the file or recording the audio, and false to cancel or stop recording.
         */
        boolean reportProgress(double fractionComplete);
    }

    public void ReadFile(File inputFile)
            throws java.io.FileNotFoundException,
            java.io.IOException {
        long start = System.currentTimeMillis();
        MediaExtractor extractor = new MediaExtractor();
        MediaFormat format = null;
        int i;

        File mInputFile = inputFile;
        String[] components = mInputFile.getPath().split("\\.");
        String mFileType = components[components.length - 1];
        int mFileSize = (int) mInputFile.length();
        extractor.setDataSource(mInputFile.getPath());
        int numTracks = extractor.getTrackCount();
        // find and select the first audio track present in the file.
        for (i = 0; i < numTracks; i++) {
            format = extractor.getTrackFormat(i);
            if (format.getString(MediaFormat.KEY_MIME).startsWith("audio/")) {
                extractor.selectTrack(i);
                break;
            }
        }
        int mChannels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
        int mSampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        // Expected total number of samples per channel.
        int expectedNumSamples =
                (int) ((format.getLong(MediaFormat.KEY_DURATION) / 20000000.f) * mSampleRate + 0.5f);

        MediaCodec codec = MediaCodec.createDecoderByType(format.getString(MediaFormat.KEY_MIME));
        codec.configure(format, null, null, 0);

        codec.start();

        int decodedSamplesSize = 0;  // size of the output buffer containing decoded samples.
        byte[] decodedSamples = null;
        ByteBuffer[] inputBuffers = codec.getInputBuffers();
        ByteBuffer[] outputBuffers = codec.getOutputBuffers();
        int sample_size;
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        long presentation_time;
        int tot_size_read = 0;
        boolean done_reading = false;

        // Set the size of the decoded samples buffer to 1MB (~6sec of a stereo stream at 44.1kHz).
        // For longer streams, the buffer size will be increased later on, calculating a rough
        // estimate of the total size needed to store all the samples in order to resize the buffer
        // only once.
        ByteBuffer mDecodedBytes = ByteBuffer.allocate(1 << 20);
        Boolean firstSampleData = true;
        while (true) {
            // read data from file and feed it to the decoder input buffers.
            int inputBufferIndex = codec.dequeueInputBuffer(1000);
            if (!done_reading && inputBufferIndex >= 0) {
                sample_size = extractor.readSampleData(inputBuffers[inputBufferIndex], 0);
                if (firstSampleData
                        && format.getString(MediaFormat.KEY_MIME).equals("audio/mp4a-latm")
                        && sample_size == 2) {
                    // For some reasons on some devices (e.g. the Samsung S3) you should not
                    // provide the first two bytes of an AAC stream, otherwise the MediaCodec will
                    // crash. These two bytes do not contain music data but basic info on the
                    // stream (e.g. channel configuration and sampling frequency), and skipping them
                    // seems OK with other devices (MediaCodec has already been configured and
                    // already knows these parameters).
                    extractor.advance();
//                    tot_size_read += sample_size;
                } else if (sample_size < 0) {
                    // All samples have been read.
                    codec.queueInputBuffer(
                            inputBufferIndex, 0, 0, -1, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                    done_reading = true;
                } else {
                    presentation_time = extractor.getSampleTime();
                    codec.queueInputBuffer(inputBufferIndex, 0, sample_size, presentation_time, 0);
                    extractor.advance();
                    tot_size_read += sample_size;

                }
                firstSampleData = false;
            }

            // Get decoded stream from the decoder output buffers.
            int outputBufferIndex = codec.dequeueOutputBuffer(info, 100);
            if (outputBufferIndex >= 0 && info.size > 0) {
                if (decodedSamplesSize < info.size) {
                    decodedSamplesSize = info.size;
                    decodedSamples = new byte[decodedSamplesSize];
                }
                outputBuffers[outputBufferIndex].get(decodedSamples, 0, info.size);
                outputBuffers[outputBufferIndex].clear();
                // Check if buffer is big enough. Resize it if it's too small.
                if (mDecodedBytes.remaining() < info.size) {
                    // Getting a rough estimate of the total size, allocate 20% more, and
                    // make sure to allocate at least 5MB more than the initial size.
                    int position = mDecodedBytes.position();
                    int newSize = (int) ((position * (1.0 * mFileSize / tot_size_read)) * 1.2);
                    if (newSize - position < info.size + 5 * (1 << 20)) {
                        newSize = position + info.size + 5 * (1 << 20);
                    }
                    ByteBuffer newDecodedBytes = null;
                    // Try to allocate memory. If we are OOM, try to run the garbage collector.
                    int retry = 10;
                    while (retry > 0) {
                        try {
                            newDecodedBytes = ByteBuffer.allocate(newSize);
                            break;
                        } catch (OutOfMemoryError oome) {
                            // setting android:largeHeap="true" in <application> seem to help not
                            // reaching this section.
                            retry--;
                        }
                    }
                    if (retry == 0) {
                        // Failed to allocate memory... Stop reading more data and finalize the
                        // instance with the data decoded so far.
                        break;
                    }
                    //ByteBuffer newDecodedBytes = ByteBuffer.allocate(newSize);
                    mDecodedBytes.rewind();
                    newDecodedBytes.put(mDecodedBytes);
                    mDecodedBytes = newDecodedBytes;
                    mDecodedBytes.position(position);
                }
                mDecodedBytes.put(decodedSamples, 0, info.size);
                codec.releaseOutputBuffer(outputBufferIndex, false);
            } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                outputBuffers = codec.getOutputBuffers();
            } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                // Subsequent data will conform to new format.
                // We could check that codec.getOutputFormat(), which is the new output format,
                // is what we expect.
            }
            if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0
                    || (mDecodedBytes.position() / (2 * mChannels)) >= expectedNumSamples) {
                // We got all the decoded data from the decoder. Stop here.
                // Theoretically dequeueOutputBuffer(info, ...) should have set info.flags to
                // MediaCodec.BUFFER_FLAG_END_OF_STREAM. However some phones (e.g. Samsung S3)
                // won't do that for some files (e.g. with mono AAC files), in which case subsequent
                // calls to dequeueOutputBuffer may result in the application crashing, without
                // even an exception being thrown... Hence the second check.
                // (for mono AAC files, the S3 will actually double each sample, as if the stream
                // was stereo. The resulting stream is half what it's supposed to be and with a much
                // lower pitch.)
                break;
            }
        }

        int mNumSamples = mDecodedBytes.position() / (mChannels*2);  // One sample = 2 bytes.
        mDecodedBytes.rewind();
        mDecodedBytes.order(ByteOrder.LITTLE_ENDIAN);
        ShortBuffer mDecodedSamples = mDecodedBytes.asShortBuffer();
        int mAvgBitRate = (int) ((mFileSize * 8) * ((float) mSampleRate / mNumSamples) / 1000);

        extractor.release();
        extractor = null;
        codec.stop();
//        long stop = System.currentTimeMillis();

//        Log.i("1st step",String.valueOf((stop-start))+" ms");
        codec.release();
        codec = null;
        // Temporary hack to make it work with the old version.
        mNumFrames = mNumSamples / getSamplesPerFrame();
        if (mNumSamples % getSamplesPerFrame() != 0) {
            mNumFrames++;
        }
        mFrameGains = new int[mNumFrames];
        mFrameLens = new int[mNumFrames];
        mFrameOffsets = new int[mNumFrames];
        int j;
        int gain, value;
        int frameLens = (int) ((1000 * mAvgBitRate / 8) *
                ((float) getSamplesPerFrame() / mSampleRate));
//        start = System.currentTimeMillis();

        for (i = 0; i < mNumFrames; i++) {
            gain = -1;
            for (j = 0; j < getSamplesPerFrame(); j++) {

                value = java.lang.Math.abs(mDecodedSamples.get());



                if (gain < value) {
                    gain = value;
                }
            }
            mFrameGains[i] = (int) Math.sqrt(gain);  // here gain = sqrt(max value of 1st channel)...
            mFrameLens[i] = frameLens;  // totally not accurate...
            mFrameOffsets[i] = (int) (i * (1000 * mAvgBitRate / 8) *  //  = i * frameLens
                    ((float) getSamplesPerFrame() / mSampleRate));
        }
        mDecodedSamples.rewind();
//        stop = System.currentTimeMillis();
//        Log.i("2nd step",String.valueOf((stop-start))+" ms");
        this.setChanged();
        MusicPlayerMainActivity.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyObservers();
            }
        });

    }

}
