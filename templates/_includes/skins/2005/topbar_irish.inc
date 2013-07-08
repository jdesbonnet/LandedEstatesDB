<?php
$existingQS = str_replace("&language=gaeilge", "", $_SERVER['QUERY_STRING']);
$existingQS = str_replace("language=gaeilge", "", $existingQS);
$toggleQS = (empty($existingQS)) ? "" : "?" . convertAmpersands($existingQS);
$thisFile = (empty($PHP_SELF)) ? $_SERVER['HTTP_REFERER'] : $PHP_SELF;  //you must use referer on external sites running IIS
if (strpos($_SERVER['HTTP_HOST'], "oegaillimh.ie") > -1) {
  $thisDomain = "http://" . str_replace("oegaillimh", "nuigalway", $_SERVER['HTTP_HOST']) . $thisFile;
  $thisFile = $thisDomain;
}
$thisURL = $_SERVER['SERVER_NAME'] . $_SERVER['SCRIPT_NAME'];
if (!empty($_SERVER['QUERY_STRING'])) {
    //if (substr($_SERVER['HTTP_REFERER'], 0, 23) != "http://www.nuigalway.ie") {
	//  include("http://www.nuigalway.ie/includes/skins/2005/php/XHTMLify.inc");
	//}
    $thisURL .= "?" . convertAmpersands($_SERVER['QUERY_STRING']);
}
?>
	<div id="topbar">
		<span id="topbartext">
		  <img src="http://www.nuigalway.ie/images/language_grey.png" alt="Language Toggle Icon" width="11" height="11" /> <a title="View in English" href="<?=$thisFile . $toggleQS?>">View in English</a> &nbsp;
			<a title="Text Version" href="http://www.nuigalway.ie/parser.php?<?=$thisURL?>"><img src="http://www.nuigalway.ie/images/text_grey.png" alt="Text Icon" width="11" height="11" /> Leagan Téacs</a>&nbsp;
		  <span id="changeFont">
				<img src="http://www.nuigalway.ie/images/display_grey.png" alt="Display Icon" width="11" height="11" /> Zúmáil
		    <span id="reduceFont">
					<span id="reduceFontButton" class="cursorlinked" title="Zoom Out -">(-)</span>
		    </span>
		    <span id="increaseFont">
					<span id="increaseFontButton" class="cursorlinked" title="Zoom In +">(+)</span>
		    </span>
			<span id="changeStyle">
				&nbsp; Amharc ( <span id="cssColour" class="cursorlinked" title="Normal View"> Normálta</span> <span id="cssHiContrast" class="cursorlinked" title="High Contrast View"> Ardchodarsnacht</span> )
			</span>
		  </span>
		</span>
		<span id="topbartextright">
<?
$fullQS = (empty($_SERVER["QUERY_STRING"])) ? "" : "?" . convertAmpersands($_SERVER["QUERY_STRING"]);
?>
			<a accesskey="p" title="Priontáil [access key 'p']" href="http://www.nuigalway.ie/includes/skins/2005/php/printerfriendly.php<?=$fullQS?>" target="_blank"><img src="http://www.nuigalway.ie/images/print_box_grey.png" alt="Priontáil" width="11" height="11" /> Priontáil</a>&nbsp;
		  <a title="A go Z" href="http://www.nuigalway.ie/a2z/?language=gaeilge"><img src="http://www.nuigalway.ie/images/a2z_box_grey.png" alt="A go Z" width="11" height="11" /> A go Z</a>&nbsp;
			<a accesskey="m" title="Léarscáil an tSuímh [access key 'm']" href="http://www.nuigalway.ie/sitemap.php?language=gaeilge"><img src="http://www.nuigalway.ie/images/sitemap_box_grey.png" alt="Sitemap icon" width="11" height="11" /> Léarscáil an tSuímh</a> 
		</span>
	</div>