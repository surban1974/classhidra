
package it.classhidra.core.tool.util.multipart;

import java.io.*;


public interface MultipartFileRenamePolicy {

	/**
	 * Returns a File object holding a new name for the specified file.
	 *
	 * @see MultipartDataFile#writeTo(File fileOrDirectory)
	 * 
	 * @param file
	 * @return
	 */
	public File rename(File file);

}
