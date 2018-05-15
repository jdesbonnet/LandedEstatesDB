package ie.wombat.landedestates;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class BackupServlet extends HttpServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet (HttpServletRequest request, 
			HttpServletResponse response) 
	throws IOException {
		response.setContentType("application/binary");
		
	
		OutputStream out = response.getOutputStream();
		ZipOutputStream zout = new ZipOutputStream(out);
		ZipEntry ze = new ZipEntry("landedestates.dump");
		zout.putNextEntry(ze);
		
		DBExport.mysqlDump(zout);
		zout.closeEntry();
		zout.close();
		out.close();
	}
}
