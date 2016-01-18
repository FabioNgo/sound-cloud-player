package ngo.music.player.helper;

import java.util.Arrays;

/**
 * Do statistics like compute variance, sd ....
 * Created by fabiongo on 1/9/2016.
 */
public class Statistics
{
    double[] data;
    int size;

    public Statistics(double[] data)
    {
        this.data = data;
        size = data.length;
    }

    public Statistics(byte[] bytes) {
        this.data = new double[bytes.length];
        for(int i=0;i<data.length;i++){
            data[i] = bytes[i];
        }
        size = data.length;
    }

    public double getMean()
    {
        double sum = 0.0;
        for(double a : data)
            sum += a;
        return sum/size;
    }

    public double getVariance()
    {
        double mean = getMean();
        double temp = 0;
        for(double a :data)
            temp += (mean-a)*(mean-a);
        return temp/size;
    }

    public double getStdDev()
    {
        return Math.sqrt(getVariance());
    }

    public double median()
    {
        Arrays.sort(data);

        if (data.length % 2 == 0)
        {
            return (data[(data.length / 2) - 1] + data[data.length / 2]) / 2.0;
        }
        else
        {
            return data[data.length / 2];
        }
    }
}