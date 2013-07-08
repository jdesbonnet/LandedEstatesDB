<?php
$logostring = "";
if (!empty($_GET['language']) && $_GET['language'] == 'gaeilge') {
	$logostring = '?language=gaeilge';
	$marketingtext = 'marketingtext_irish.inc';
} else {
	$marketingtext = 'marketingtext.inc';
}
?>

<div class="linedots"><img alt="spacer" src="http://www.nuigalway.ie/images/spacer.png" height="1" /></div>
<div id="logoimage">
  <div id="logo"><a title="NUI Galway Home Page" href="http://www.nuigalway.ie/<?=$logostring?>"><img alt="NUI Galway Logo" src="http://www.nuigalway.ie/images/logos/<?=$logo?>" /></a></div>
  <div id="image"><div id="imagecover">
			<div id="imagecontent"><h1 id="imagecontenttext"></h1><div id="imagemarketingcontent"><?php include("/www/includes/$marketingtext"); ?></div></div> 
	</div></div>
  <?php
if (!isVlan(2) && !isRobot() && !$isPortableDevice) {
?>
 	<div id="browserWarning" class="errormessage"> <img src="http://www.nuigalway.ie/cms/images/caution.png" alt="Warning Icon" /> <p><strong>Warning:</strong> Your browser doesn't support all of the features in this Web site. Please view our <a title="NUI Galway Accessibility Information" href="http://www.nuigalway.ie/accessibility.php">accessibility page</a> for more details.</p></div>
  <?
}
if ($isPortableDevice) {
  //should only display this if pda ignores handheld.css and displays the logoimage div.  Blackberrys do this.
?>
  <div id="browserWarning" class="errormessage"> <img src="http://www.nuigalway.ie/cms/images/caution.png" alt="Warning Icon" />
    <p><strong>Warning:</strong> Your PDA's browser doesn't support all of the features in this Web site.</p>
  </div>
  <?
}
?>
</div>