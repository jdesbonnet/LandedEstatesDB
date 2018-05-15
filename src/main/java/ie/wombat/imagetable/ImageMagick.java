/*
 * (c) 2008 Joe Desbonnet. This file is released under BSD licence.
 */
package ie.wombat.imagetable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that acts as wrapper for native ImageMagick image manipulation. 
 * We use ImageMagick as Java does not offer anything that works 
 * as well. The is an unfortunate but necessary dependency on native
 * code.
 * 
 * @author Joe Desbonnet
 *
 */
public class ImageMagick {

	static Logger log = LoggerFactory.getLogger(ImageMagick.class);
	private static final String CONVERT_PROG = "convert";
	private static String convertProg = CONVERT_PROG;

	/**
	 * Perform general operation on input image.
	 * 
	 * @param inFile
	 * @param command
	 * @return
	 * @throws IOException
	 */
	public static File imageMagickOp(File inFile, String command)
			throws IOException {

		
		File tmpOutFile = ImageDB.newTmpFile();

		String[] p = command.split("\\s+");
		for (int i = 0; i < p.length; i++) {
			if (p[i].indexOf("$in") >= 0) {
				p[i] = p[i].replace("$in", inFile.getAbsolutePath());
				continue;
			}
			if (p[i].indexOf("$out") >= 0) {
				p[i] = p[i].replace("$out", tmpOutFile.getAbsolutePath());
				continue;
			}
		}

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < p.length; i++) {
			buf.append(p[i]);
			buf.append(" ");
		}
		log.info("exec: " + buf.toString());

		exec(p);
		
		// Handle anomalous situations (eg multilayer output, return correct file)
		return returnOutputFile (tmpOutFile);
		
	}

	/**
	 * Uses a Runtime.exec()to use ImageMagick to perform the given conversion
	 * operation. Returns true on success, false on failure. Does not check if
	 * either file exists.
	 * 
	 * @param in
	 *            Description of the Parameter
	 * @param out
	 *            Description of the Parameter
	 * @param newSize
	 *            Description of the Parameter
	 * @param quality
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static boolean convert(File in, File out, int width, int height,
			String outputType, int quality) throws IOException {

		if (quality < 0 || quality > 100) {
			quality = 75;
		}

		ArrayList<String> command = new ArrayList<String>(10);

		command.add(convertProg);
		command.add("-geometry");
		command.add(width + "x" + height);
		command.add("-quality");
		command.add("" + quality);
		command.add(in.getAbsolutePath());
		command.add(outputType + ":" + out.getAbsolutePath());

		System.out.println(command);

		return exec((String[]) command.toArray(new String[1]));
	}

	/**
	 * Tries to exec the command, waits for it to finish, logs errors if exit
	 * status is nonzero, and returns true if exit status is 0 (success).
	 * 
	 * @param command
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	static boolean exec(String[] command) throws IOException {
		Process proc;

		// May throw IOException
		proc = Runtime.getRuntime().exec(command);

		InputStream stdout = proc.getInputStream();
		OutputStream stdin = proc.getOutputStream();
		InputStream stderr = proc.getErrorStream();

		// System.out.println("Got process object, waiting to return.");

		int exitStatus;

		while (true) {
			try {
				exitStatus = proc.waitFor();
				break;
			} catch (java.lang.InterruptedException e) {
				System.out.println("Interrupted: Ignoring and waiting");
			}
		}
		if (exitStatus != 0) {
			System.out.println("Error executing command: " + exitStatus);
		}

		// Explicitly close stdin,stdout, stderr. If not then relying
		// on GC to do this. If many execs() are called in rapid succession
		// pipe resources may become exhausted before GC cleans up.
		// (get "too many open files" error from OS)
		stdin.close();
		stdout.close();
		stderr.close();

		return (exitStatus == 0);
	}

	/**
	 * Scale image. Remember to delete
	 * returned temporary file after use.
	 * 
	 * @param inFile
	 * @param w
	 * @param h
	 * @param outputType Any valid ImageMagick image type (eg jpeg | png etc)
	 * @param quality
	 * @return
	 * @throws IOException
	 */
	public static File scale(File inFile, int w,
			int h, String outputType, int quality) throws IOException {

		File tmpOutFile = ImageDB.newTmpFile();

		convert(inFile, tmpOutFile, w, h, outputType, 85);
		
		// Handle anomalous situations (eg multilayer output, return correct file)
		return returnOutputFile (tmpOutFile);
	
	}
	
	/**
	 * Sometimes ImageMagick operations don't produce output in exactly tmpOutFile.
	 * For example, in multi-layer images, outputs are suffixed with -n where n
	 * is the layer. This method analyses the output and returns the appropriate file
	 * while cleaning up any unnecessary files.
	 *
	 * @param tmpOutFile
	 * @return
	 * @throws IOException
	 */
	private static File returnOutputFile (File tmpOutFile) throws IOException {
		
		// First try the obvious location
		if (tmpOutFile.exists()) {
			return tmpOutFile;
		}
		
		log.warn("returnOutputFile(): " + tmpOutFile.getPath() + " not found. Assuming layered image.");
		
		// Multi-layer/animated images may have "-0", "-1" etc appended
		// to the output file name. Or appended before filetype suffix: ie fname-0.tmp! 
		// Use the first layer/frame.
		String fname = tmpOutFile.getName();
		File try2 = new File (tmpOutFile.getParent(), fname + "-0");
		if (! try2.exists()) {
			log.warn ("returnOutputFile(): " + try2.getPath() + " also not found.");
			if (fname.endsWith(".tmp") ) {
				try2 = new File (tmpOutFile.getParent(), fname.substring(0,fname.length()-4) + "-0.tmp");	
			}
		}
		if (! try2.exists() ) {
			log.warn ("returnOutputFile(): " + try2.getPath() + " not found. Giving up.");
			throw new IOException("output at "
					+ tmpOutFile.getPath() + " not found. Also tried "
					+ try2.getPath() + " but not found.");
		}
		if (try2.length() == 0) {
			throw new IOException("output at "
					+ try2.getPath() + " 0 bytes in length");
		}

		// Delete layers > 0 until no more layers found
		// Updated to try filename.tmp-n and filename-n.tmp formats
		for (int i = 1; i < 100; i++) {
			File f1 = new File (tmpOutFile.getParent(), fname + "-" + i);
			File f2 = new File (tmpOutFile.getParent(), fname.substring(0,fname.length()-4) + "-" + i + ".tmp");
			// Exit loop if neither f1 or f2 exist
			if ( ( ! f1.exists()) &&  ( ! f2.exists())) {
				break;
			}
			if (f1.exists()) {
				f1.delete();
			} 
			if (f2.exists()) {
				f2.delete();
			}
		}
		return try2;
	}
	
	/**
	 * Scale image so that it fills the bounding box w x h. Remember to delete
	 * returned temporary file after use.
	 * 
	 * @param inFile
	 * @param w
	 * @param h
	 * @param outputType
	 * @param quality
	 * @return
	 * @throws IOException
	 */
	public static File scaleToFill(File inFile, int w,
			int h, String outputType, int quality) throws IOException {

		if ( ! inFile.exists()) {
			throw new IOException ("input file " + inFile.getPath() + " does not exist");
		}
		
		if (inFile.length() == 0) {
			throw new IOException("input file " + inFile.getPath() + " must be > 0 bytes in size");
		}

		
		String command = "convert $in -resize x" + (h * 2) + " -resize "
				+ (w * 2) + "x<   -resize 50%  -gravity center " + "-crop " + w
				+ "x" + h + "+0+0 +repage  -quality " + quality + " " + outputType+":$out";

		File tmpOutFile = ImageMagick.imageMagickOp(inFile, command);

		if (! tmpOutFile.exists() ) {
			throw new IOException("error scaling image: output at "
					+ tmpOutFile.getPath() + " not found / not readable");
		}
		if (tmpOutFile.length() == 0) {
			throw new IOException("error scaling image: output at "
					+ tmpOutFile.getPath() + " 0 bytes in length");
		}

		return tmpOutFile;
	}
	
	//  convert yui.jpg -crop [125 x 83 + 218 + 91] yui-new.jpg
	
	public static File crop (File inFile, int x,
			int y, int w, int h, String outputType, int quality) 
		throws IOException {

		String command = "convert $in -crop [" + w
				+ "x" + h + "] +" + x + "+" + y + " jpeg:$out";

		File tmpOutFile = ImageMagick.imageMagickOp(inFile, command);

		if (!tmpOutFile.exists() || tmpOutFile.length() == 0) {
			throw new IOException("error cropping image: output at "
					+ tmpOutFile.getPath() + " no output or output 0 bytes in length");
		}

		return tmpOutFile;

	}


	public static void setConvertProg(String c) {
		convertProg = c;
	}

}
