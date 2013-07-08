package ie.wombat.landedestates;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

public class BarcodeServlet extends HttpServlet {

	private static final String BC_MIMETYPE = "image/png";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet (HttpServletRequest request, 
			HttpServletResponse response) 
	throws IOException {
		
		String pathInfo = request.getPathInfo();
		String[] p = pathInfo.split("/");
		if (p.length < 2) {
			response.setContentType("text/plain");
			response.setStatus(500);
			response.getOutputStream().println ("error: correct format /bc/(type)/(code)");
			return;
		}
		
		String type = p[1];
		String code = p[2];
		
		if (code == null) {
			code="0000";
		}
		
		//		Create the barcode bean
		Code39Bean bean = new Code39Bean();

		final int dpi = 150;
		
		/*
		 * Set the narrow bar two pixels
		 */
		bean.setModuleWidth(UnitConv.in2mm(2.0f / dpi)); 
		bean.setWideFactor(3);
		bean.setBarHeight(UnitConv.in2mm(20.0f / dpi));
		bean.doQuietZone(false);

		response.setContentType(BC_MIMETYPE);
		OutputStream out = response.getOutputStream();
		
		try {
		    //Set up the canvas provider for monochrome JPEG output 
		    BitmapCanvasProvider canvas = new BitmapCanvasProvider(
		            out, BC_MIMETYPE, dpi, BufferedImage.TYPE_BYTE_BINARY, false);

		    //Generate the barcode
		    bean.generateBarcode(canvas, code);

		    //Signal end of generation
		    canvas.finish();
		} finally {
		    out.close();
		}
	}
}
