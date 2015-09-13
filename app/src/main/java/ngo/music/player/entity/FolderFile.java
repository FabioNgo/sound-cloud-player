package ngo.music.player.entity;

public class FolderFile {

	private String filename;
	private boolean isFile;
	public FolderFile( String filename, boolean isFile) {
		// TODO Auto-generated constructor stub
		this.setFilename(filename);
		this.setFile(isFile);
	}
	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * @return the isFile
	 */
	public boolean isFile() {
		return isFile;
	}
	/**
	 * @param isFile the isFile to set
	 */
	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}
	
	

}
