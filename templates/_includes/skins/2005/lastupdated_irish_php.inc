	<br /><span class="arialblacksmall">Nuashonraíodh an leathanach seo <?php echo translateDate(date( "F d, Y.", getlastmod() ));?></span>
	
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