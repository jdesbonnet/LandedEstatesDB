	<div class="footertext">T� an leagan cuimsitheach Gaeilge � r�iteach.</div>
	<div id="footer">
		<div id="footerleft">
		</div>
		<div id="footerright">
			<ul>
				<li><a title="S�anadh" href="http://www.nuigalway.ie/disclaimer.php?language=gaeilge">S�anadh</a></li>
				<li><a title="R�iteas Pr�obh�ideachta" href="http://www.nuigalway.ie/privacy.php?language=gaeilge">R�iteas Pr�obh�ideachta</a></li>
				<li><a title="C�ipcheart" href="http://www.nuigalway.ie/copyright.php?language=gaeilge">C�ipcheart</a></li>
				<li><a title="Fiosruithe / Teagmh�lacha" href="http://www.nuigalway.ie/contacts.php?language=gaeilge">Fiosruithe / Teagmh�lacha</a></li>
				<li><a title="Inrochtaineacht [access key '0']" accesskey="0" href="http://www.nuigalway.ie/accessibility.php?language=gaeilge">Inrochtaineacht</a></li>
			</ul>
		</div>
	</div>
		
	<div id="valid">Ollscoil na h&Eacute;ireann, Gaillimh, B&oacute;thar na hOllscoile, Gaillimh, &Eacute;ire. Teil: +353 (0) 91 524411<br /><span class="arialblacksmall">Nuashonra�odh an leathanach seo <!--ar an--> <?php echo translateDate(date( "F d, Y.", getlastmod() ));?>&nbsp;&nbsp;&nbsp;&nbsp;</span></div>
	
<?
function translateDate($sDate) {
  $sDate = str_replace("January", "Eanair", $sDate);
  $sDate = str_replace("February", "Feabhra", $sDate);
  $sDate = str_replace("March", "M&aacute;rta", $sDate);
  $sDate = str_replace("April", "Aibre&aacute;in", $sDate);
  $sDate = str_replace("May", "I&uacute;il", $sDate);
  $sDate = str_replace("June", "L&uacute;nasa", $sDate);
  $sDate = str_replace("July", "Bealtaine", $sDate);
  $sDate = str_replace("August", "Meitheamh", $sDate);
  $sDate = str_replace("September", "Me&aacute;n F&oacute;mhair", $sDate);
  $sDate = str_replace("October", "Deireadh F&oacute;mhair", $sDate);
  $sDate = str_replace("November", "Samhain", $sDate);
  $sDate = str_replace("December", "Nollaig", $sDate);
  return $sDate;
}
?>