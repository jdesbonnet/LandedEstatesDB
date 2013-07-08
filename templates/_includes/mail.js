// Simple Anti Spam Crawler Function
// 21 May 2003
// Cillian Joy - webeditor@nuigalway.ie
// Could be made more secure by having and array of emails or domains that email can only be sent to

var MAIL_SERVER = "@nuigalway.ie";
//var MIS_MAIL_SERVER = "@mis.nuigalway.ie";
//var IRISH_MAIL_SERVER = "@";
//var IRISH_MIS_MAIL_SERVER = "@mis.";

function mail(name) {
    if (name == "webeditor") {
		NO_SPAM = "mailto:" + name + MAIL_SERVER + "?subject=NUI Galway Web Site Enquiry";
		document.location.href = NO_SPAM;	
	} else {
		if (name.match("@")) {
		  NO_SPAM = "mailto:" + name;
		  document.location.href = NO_SPAM;
	    } else {
		  if (name.indexOf("*") > 0) {
		    NO_SPAM = "mailto:" + name.replace(/\*/g, "@");
		    document.location.href = NO_SPAM;
		  } else {
		    NO_SPAM = "mailto:" + name + MAIL_SERVER;
		    document.location.href = NO_SPAM;
		  }
		}
	}
}