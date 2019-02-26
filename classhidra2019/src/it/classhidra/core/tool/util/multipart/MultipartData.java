
package it.classhidra.core.tool.util.multipart;


public abstract class MultipartData {

	private String sName;

	/**
	 * 
	 */
	public MultipartData(String string) {
		sName = string;
	}

	/**
	 * Returns the name of the element that this MultipartData corresponds to.
	 * 
	 * @return the name of the element that this MultipartData corresponds to.
	 */
	public String getName() {
		return sName;
	}

	/**
	 * Returns true if this MultipartData is a File.
	 * 
	 * @return true if this is a FilePart.
	 */
	public boolean isFile() {
		return false;
	}
  
	/**
	 * Returns true if this MultipartData is a Parameter.
	 * 
	 * @return true if this is a Parameter.
	 */
	public boolean isParameter() {
		return false;
	}

}
