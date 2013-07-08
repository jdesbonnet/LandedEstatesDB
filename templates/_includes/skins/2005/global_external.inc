<?
echo "<" . "?xml version=\"1.0\" encoding=\"iso-8859-1\"?" . ">";
include("http://www.nuigalway.ie/includes/skins/2005/dtd-current.inc");
include("http://www.nuigalway.ie/generic/codelib/IPQuery.inc");
include("http://www.nuigalway.ie/includes/skins/2005/php/XHTMLify_external.inc");

if ((empty($_GET['language']) || $_GET['language'] != "gaeilge") && !stristr($_SERVER['HTTP_HOST'], "oegaillimh")) {
	$querystring = "";
	$incHeader = "http://www.nuigalway.ie/includes/skins/2005/topbar.inc?" . $_SERVER['QUERY_STRING'];
	$incSearch = "http://www.nuigalway.ie/includes/skins/2005/search.inc";
	$logo = "logo.png";
	$incLinkSearch = "http://www.nuigalway.ie/includes/skins/2005/linkSearch_html.inc";
	$incFooterGreyBar = "http://www.nuigalway.ie/includes/skins/2005/greybar.inc";
	$incFooterLastUpdated = "http://www.nuigalway.ie/includes/skins/2005/lastupdated_php.inc";
	
} else {
	$querystring = $_SERVER['QUERY_STRING'];
	$incHeader = "http://www.nuigalway.ie/includes/skins/2005/topbar_irish.inc?" . $querystring;
	$incSearch = "http://www.nuigalway.ie/includes/skins/2005/search_irish.inc";
	$logo = "logo_irish.png";
	$incLinkSearch = "http://www.nuigalway.ie/includes/skins/2005/linkSearch_html_irish.inc";
	$incFooterGreyBar = "http://www.nuigalway.ie/includes/skins/2005/greybar_irish.inc";
	$incFooterLastUpdated = "http://www.nuigalway.ie/includes/skins/2005/lastupdated_irish_php.inc";
}
?>