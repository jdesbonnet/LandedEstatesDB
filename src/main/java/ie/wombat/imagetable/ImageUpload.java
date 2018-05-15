package ie.wombat.imagetable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUpload {
	
	private static Logger log = LoggerFactory.getLogger(ImageUpload.class);
	
	private Properties params = new Properties();
	private List<FileItem> fileItems = new ArrayList<FileItem> ();
	private HttpServletRequest request;
	
	public ImageUpload (HttpServletRequest request) throws FileUploadException {
		this.request = request;
		init(request);
	}
	
	public Properties getProperties() {
		return params;
	}

	public List<FileItem> getFileItems () {
		return fileItems;
	}
	
	private void init (HttpServletRequest request) throws FileUploadException {
		
		DiskFileUpload upload = new DiskFileUpload();
		List<FileItem> items = upload.parseRequest(request);

		HashMap<String, String> param = new HashMap<String, String>();
		for (FileItem item : items) {
			if (item.isFormField()) {
				String name = item.getFieldName();
				String value = item.getString();
				param.put(name, value);
				// TODO: refactor -- parms and parm can be
				// the same object
				if (params!=null) {
					params.setProperty(name,value);
					log.info ("  param[" + name + "] = " + value);
				}
			} else {
				fileItems.add(item);
			}
		}
		

	}
}
