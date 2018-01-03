package ie.wombat.template;

import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * Command line tool to substitute variables into a Velocity template and output to standard output.
 * Define variables with command line option -DparamName=paramValue
 * eg
 * 
 * <code>
 * java VelocityCommandLine -Da=b -DmyVar=blah /var/tmp/t.vm
 * </code>

 * @author Joe Desbonnet
 *
 */
public class VelocityCommandLine {

	public static void main (String[] arg) throws Exception {
		
		// Display help if no parameters
		if (arg.length == 0) {
			usage();
			return;
		}
		
		
		VelocityContext context = new VelocityContext();
		
		List<String> params= new ArrayList<String>();
		
		for (int i = 0; i < arg.length; i++) {
			if (arg[i].startsWith("-D")) {
				int eqIndex = arg[i].indexOf('=');
				String paramName = arg[i].substring(2,eqIndex);
				String paramValue = arg[i].substring(eqIndex+1);
				context.put (paramName, paramValue);
				//System.err.println (paramName + " = " + paramValue);
			} else if (	arg[i].startsWith("-P")) {
				String paramFileName = arg[i].substring(2);
				File paramFile = new File(paramFileName);
				Properties props = new Properties();
				FileReader r = new FileReader(paramFile);
				props.load(r);
				
				Iterator<Object> iter = props.keySet().iterator();
				while (iter.hasNext()) {
					String name = (String)iter.next();
					String value = props.getProperty(name);
					context.put(name, value);
				}
				
				r.close();
					
			} else {
				params.add(arg[i]);
			}
		}
		
		
		// Require at least one non-switch parameter
		if (params.size() == 0) {
			usage();
			return;
		}
		
		
		
		String templatePath = params.get(0);
		File templateFile = new File(templatePath);
		
		if ( ! templateFile.exists() ) {
			System.err.println ("Template file " + templateFile.getPath()  + " does not exist.");
			return;
		}
		
		File templateDir = templateFile.getParentFile();

		
		 Velocity.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, templateDir.getPath());
	     Velocity.setProperty(Velocity.ENCODING_DEFAULT,"UTF-8");
	     
	     Velocity.init();
	     
	     org.apache.velocity.Template template = Velocity.getTemplate(templateFile.getName(),"UTF-8");
	     
	     Writer w = new PrintWriter(System.out);
	     template.merge(context, w);
	     
	     w.flush();
	}
	
	private static void usage () {
		PrintStream out = System.err;
		out.println ("command line parameters and switches:");
		out.println ("[-Pproperties-file] [-Dvar=value [-Dvar2=value2 ... ]] template-file ");
	}
}
