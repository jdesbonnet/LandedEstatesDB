<?php
if ((empty($_GET['language']) || $_GET['language'] != "gaeilge") && !stristr($_SERVER['HTTP_HOST'], "oegaillimh")) {
	$thisLanguage = 0;
	$querystring = "";
} else {
	$thisLanguage = 1;
	$querystring = "?language=gaeilge";
}
$translation = array();
$translation["home"] = array("Home", "Baile");
$translation["current"] = array("Current Students", "Mic L&eacute;inn Faoi L&aacute;thair");
$translation["future"] = array("Future Students", "Mic L&eacute;inn Amach Anseo");
$translation["faculties"] = array("Faculties &amp; Departments", "D&aacute;mha &amp; Ranna");
$translation["research"] = array("Research", "Taighde");
$translation["admin"] = array("Administration &amp; Services", "Riarach&aacute;n &amp; Seirbh&iacute;s&iacute;");
?>
<div id="prinav">
  <ul id="sidenav">
    <li id="navhome"><a accesskey="1" tabindex="3" title="<?=$translation['home'][$thisLanguage]?> [access key '1']" href="http://www.nuigalway.ie/<?=$querystring?>"><?=$translation['home'][$thisLanguage]?></a></li>
<?php
//set up a global var to keep track of open sub-nav containers and then create the list
$category_divs_open = 0;
$lastLevel = 0;
$usedNames = array();
?>