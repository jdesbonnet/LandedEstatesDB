	<div class="footertext">Tá an leagan cuimsitheach Gaeilge á réiteach.</div>
	<div id="footer">
		<div id="footerleft">
		</div>
		<div id="footerright">
			<ul>
				<li><a title="Séanadh" href="http://www.nuigalway.ie/disclaimer.php?language=gaeilge">Séanadh</a></li>
				<li><a title="Ráiteas Príobháideachta" href="http://www.nuigalway.ie/privacy.php?language=gaeilge">Ráiteas Príobháideachta</a></li>
				<li><a title="Cóipcheart" href="http://www.nuigalway.ie/copyright.php?language=gaeilge">Cóipcheart</a></li>
				<li><a title="Fiosruithe / Teagmhálacha" href="http://www.nuigalway.ie/contacts.php?language=gaeilge">Fiosruithe / Teagmhálacha</a></li>
				<li><a title="Inrochtaineacht [access key '0']" accesskey="0" href="http://www.nuigalway.ie/accessibility.php?language=gaeilge">Inrochtaineacht</a></li>
			</ul>
		</div>
	</div>
		
	<div id="valid">Ollscoil na h&Eacute;ireann, Gaillimh, B&oacute;thar na hOllscoile, Gaillimh, &Eacute;ire. Teil: +353 (0) 91 524411<br /><span class="arialblacksmall">Nuashonraíodh an leathanach seo <!--ar an--> <?php echo translateDate(date( "F d, Y.", getlastmod() ));?>&nbsp;&nbsp;&nbsp;&nbsp;</span></div>
	
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