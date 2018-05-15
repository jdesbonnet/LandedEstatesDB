<%@page
import="java.util.Date"
import="java.util.Calendar"
import="java.text.SimpleDateFormat"
%><%

if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	// backup timestamp
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmm");
    
    // call servlet to make actual backup. This allows the browser to easily 
    // save the backup as something like lebackup-20060311-1220.zip
    Date now = Calendar.getInstance().getTime();
	response.sendRedirect (request.getContextPath()+"/backup/lebackup-" + df.format(now) + ".zip");
%>