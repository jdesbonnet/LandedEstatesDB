<?php
if ((empty($_GET['language']) || $_GET['language'] != "gaeilge") && !stristr($_SERVER['HTTP_HOST'], "oegaillimh")) {
	$incYouAreHere = "<img alt=\"You are here\" class=\"breadcrumbimage\" src=\"http://www.nuigalway.ie/images/breadcrumb_youarehere.png\" />";
} else {
	$incYouAreHere = "<img alt=\"T&aacute; t&uacute; anseo\" class=\"breadcrumbimage\" src=\"http://www.nuigalway.ie/images/breadcrumb_youarehere_irish.png\" />";
}
?>
<div id="searcharea"><?=$incYouAreHere?></div>