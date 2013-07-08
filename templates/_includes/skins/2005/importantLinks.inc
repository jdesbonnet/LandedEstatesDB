<?
			if ((empty($_GET['language']) || $_GET['language'] != "gaeilge") && !stristr($_SERVER['HTTP_HOST'], "oegaillimh")) {
				$thisLanguage = 0;
				$querystring = "";
			} else {
				$thisLanguage = 1;
				$querystring = "?language=gaeilge";
			}
			$translation = array();
			$translation["about"] = array("About NUI Galway", "Faisn&eacute;is Faoi O&Eacute;, Gaillimh");
			$translation["intro"] = array("Introduction", "Faisn&eacute;is");
			$translation["introResearch"] = array("Research", "Taighde");
			$translation["introInternational"] = array("International", "Mic L&eacute;inn Idirn&aacute;isi&uacute;nta");
			$translation["introAdultEdu"] = array("Adult Education", "Oideachas Aosach");
			$translation["introAlumni"] = array("Alumni &amp; Friends", "Alumni &amp; Cairde");
			$translation["history"] = array("History", "Stair");
			$translation["title"] = array("Important Links", "Naisc Th&aacute;bhachtacha");
			$translation["maps"] = array("Maps", "Mapa&iacute;");
			$translation["travel"] = array("Travel &amp; Transport", "C&uacute;rsa&iacute; Taistil &amp; Iompair");
			$translation["views"] = array("Views of the Campus", "Radharcanna ar an gCampas");
			$translation["newsEvents"] = array("News &amp; Events", "Nuacht &amp; Imeachta&iacute;");
			$translation["press"] = array("News Archive", "Cartlann na bPreasr&aacute;iteas");
			$translation["pressArchive"] = array("News Archive", "Cartlann na bPreasr&aacute;iteas");
			$translation["diary"] = array("Events", "Dialann");
			$translation["coming"] = array("Future Events", "Le Teacht");
			$translation["pastEvents"] = array("Past Events", "At&aacute Thart");
			$translation["allEvents"] = array("All Events", "Gach Imeacht");
			$translation["vacancies1"] = array("Job Vacancies", "Postfhol&uacute;ntais");
			$translation["vacancies2"] = array("Job Vacancies", "Postfhol&uacute;ntais");
			$translation["publications"] = array("Publications", "Foilseach&aacute;in");
			$translation["library"] = array("Library", "Leabharlann");
			$translation["irish"] = array("Irish at NUI Galway", "An Ghaeilge San Ollscoil");
			$translation["scoil"] = array("Scoil na Gaeilge", "Scoil na Gaeilge");
			$translation["acadamh"] = array("Acadamh na hOllscola&iacute;ochta Gaeilge", "Acadamh na hOllscola&iacute;ochta Gaeilge");
			$translation["academicStaff"] = array("Academics &amp; Staff", "Acadóir&iacute; &amp; Comhalta&iacute; Foirne");
			$translation["onlineServices"] = array("Online Services", "Seirbh&iacute;s&iacute; ar L&iacute;ne");
			$translation["student"] = array("Student Online Services", "Mic L&eacute;inn");
			$translation["staff"] = array("Staff Online Services", "Comhalta&iacute; Foirne");
			$translation["search"] = array("Search", "Cuardaigh");
			$translation["campusdir"] = array("Campus Directory", "Eolaire Campais");
			$translation["searchEmail"] = array("Email Search", "Cuardach R&iacute;omhphoist");
			$translation["enquiries"] = array("Enquiries &amp; Contacts", "Fiosruithe / Teagmh&aacute;lacha");
			$translation["alumniAndFriends"] = array("Alumni &amp; Friends", "Alumni agus Cairde");
			$translation["foundation"] = array("University Foundation", "University Foundation");
			$translation["externalLinks"] = array("External Links", "Naisc");

			$expandableIndicator = ($isPortableDevice) ? "..." : "&nbsp;&rsaquo;&rsaquo;";
			$spacer = ($isPortableDevice) ? "" : "&nbsp;&nbsp;";
			
			//NOTE: 1) Class youarehere divs MUST start with 'links' followed by the first word of the clickable text.
			//      2) Link hrefs MUST be absolute - e.g. http://www.nuigalway.ie/about.php
			
			?>

<ul id="importantlinks">
  <li class="verdanabold">
    <?=$spacer . $translation["title"][$thisLanguage]?>
  </li>
  <li><span id="links<?=firstWord($translation["about"][$thisLanguage])?>" class="youarehere"></span> <a accesskey="7" tabindex="9" title="<?=$translation["about"][$thisLanguage]?> [access key '7']" href="http://www.nuigalway.ie/about/<?=$querystring?>">
    <?=$translation["about"][$thisLanguage] . $expandableIndicator?>
    </a>
    <?php
				if (stristr($REQUEST_URI, 'about') || stristr($REQUEST_URI, 'map')) {

				echo "\n<ul>\n<li><span id=\"links" . firstWord($translation["intro"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["intro"][$thisLanguage] . "\" href=\"http://www.nuigalway.ie/about/introduction.php$querystring\">" . $translation["intro"][$thisLanguage] . "</a>";
				
				if (stristr($REQUEST_URI, '/about/introduction')) {
						echo "<ul><li><span id=\"links" . firstWord($translation["introResearch"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["introResearch"][$thisLanguage] . "\" href=\"http://www.nuigalway.ie/about/introduction_research.php$querystring\">" . $translation["introResearch"][$thisLanguage] . "</a></li>";
						echo "<li><span id=\"links" . firstWord($translation["introInternational"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["introInternational"][$thisLanguage] . "\" href=\"http://www.nuigalway.ie/about/introduction_international.php$querystring\">" . $translation["introInternational"][$thisLanguage] . "</a></li>";
						echo "<li><span id=\"links" . firstWord($translation["introAdultEdu"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["introAdultEdu"][$thisLanguage] . "\" href=\"http://www.nuigalway.ie/about/introduction_adulteducation.php$querystring\">" . $translation["introAdultEdu"][$thisLanguage] . "</a></li>";
						echo "<li><span id=\"links" . firstWord($translation["introAlumni"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["introAlumni"][$thisLanguage] . "\" href=\"http://www.nuigalway.ie/about/introduction_alumni.php$querystring\">" . $translation["introAlumni"][$thisLanguage] . "</a></li></ul>";
				}
				
				echo "</li>";
				
				echo "\n<li><span id=\"links" . firstWord($translation["history"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["history"][$thisLanguage] . "\" href=\"http://www.nuigalway.ie/about/history.php$querystring\">" . $translation["history"][$thisLanguage] . "</a></li>";
				echo "<li><span id=\"links" . firstWord($translation["maps"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["maps"][$thisLanguage] . "\" href=\"http://www.nuigalway.ie/about/maps/$querystring\">" . $translation["maps"][$thisLanguage] . "</a></li>";
				echo "<li><span id=\"links" . firstWord($translation["travel"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["travel"][$thisLanguage] . "\" href=\"http://www.nuigalway.ie/about/getting_to_nuigalway.php$querystring\">" . $translation["travel"][$thisLanguage] . "</a></li>";
				echo "<li><span id=\"links" . firstWord($translation["views"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["views"][$thisLanguage] . "\" href=\"http://www.nuigalway.ie/about/campus_views.php$querystring\">" . $translation["views"][$thisLanguage] . "</a></li>\n</ul>";
				}
				?>
  </li>
  <li><span id="links<?=firstWord($translation["newsEvents"][$thisLanguage])?>" class="youarehere"></span> <a tabindex="10" title="<?=$translation["newsEvents"][$thisLanguage]?>" href="http://www.nuigalway.ie/news/<?=$querystring?>">
    <?=$translation["newsEvents"][$thisLanguage] . $expandableIndicator?>
    </a>
    <?php
				if (stristr($REQUEST_URI, 'news')) {
				echo "<ul><li><span id=\"links" . firstWord($translation["press"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["press"][$thisLanguage] . "\" href=\"http://www.nuigalway.ie/news/main_press.php$querystring\">" . $translation["press"][$thisLanguage] . "</a>";
					
					//if ((stristr($REQUEST_URI, '/main_press.php')) ||  (stristr($REQUEST_URI, '/press_archive.php'))) {
						//echo "<ul><li><span id=\"links" . firstWord($translation["pressArchive"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["pressArchive"][$thisLanguage] . "\" href=\"http://www.nuigalway.ie/news/press_archive.php$querystring\">" . $translation["pressArchive"][$thisLanguage] . "</a></li></ul>";
					//}
				echo "</li>";
				
				echo "<li><span id=\"links" . firstWord($translation["diary"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["diary"][$thisLanguage] . "\" href=\"http://www.nuigalway.ie/news/dialann.php$querystring\">" . $translation["diary"][$thisLanguage] . $expandableIndicator . "</a>";
				
				if (stristr($REQUEST_URI, '/news/dialann')) {
						echo "\n<ul>\n<li><span id=\"links" . firstWord($translation["coming"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["diary"][$thisLanguage] . "\" href=\"http://www.nuigalway.ie/news/dialann_coming.php$querystring\">" . $translation["coming"][$thisLanguage] . "</a></li>";
				echo "\n<li><span id=\"links" . firstWord($translation["pastEvents"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["diary"][$thisLanguage] . "\" href=\"http://www.nuigalway.ie/news/dialann_archive.php$querystring\">" . $translation["pastEvents"][$thisLanguage] . "</a></li>";
				echo "\n<li><span id=\"links" . firstWord($translation["allEvents"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["diary"][$thisLanguage] . "\" href=\"http://www.nuigalway.ie/news/dialann_all.php$querystring\">" . $translation["allEvents"][$thisLanguage] . "</a></li>\n</ul>";
				}
				echo "</li></ul>";
				}
				?>
  </li>
  <li><span id="links<?=firstWord($translation["library"][$thisLanguage])?>" class="youarehere"></span> <a tabindex="11" title="<?=$translation["library"][$thisLanguage]?>" href="http://www.library.nuigalway.ie/<?=$querystring?>">
    <?=$translation["library"][$thisLanguage]?>
    </a> </li>
  <li><span id="links<?=firstWord($translation["alumniAndFriends"][$thisLanguage])?>" class="youarehere"></span> <a tabindex="17" title="<?=$translation["alumniAndFriends"][$thisLanguage]?>" href="http://www.nuigalway.ie/alumni/<?=$querystring?>">
    <?=$translation["alumniAndFriends"][$thisLanguage]?>
    </a> </li>
  <li><span id="links<?=firstWord($translation["foundation"][$thisLanguage])?>" class="youarehere"></span> <a tabindex="18" title="<?=$translation["foundation"][$thisLanguage]?>" href="http://www.nuigalway.ie/foundation/<?=$querystring?>">
    <?=$translation["foundation"][$thisLanguage]?>
    </a> </li>
  <li><span id="links<?=firstWord($translation["irish"][$thisLanguage])?>" class="youarehere"></span> <a tabindex="11" title="<?=$translation["irish"][$thisLanguage]?>" href="http://www.nuigalway.ie/an_ghaeilge_san_ollscoil/<?=$querystring?>">
    <?=$translation["irish"][$thisLanguage]?>
    </a>
    <?php
				if (stristr($REQUEST_URI, 'an_ghaeilge_san_ollscoil')) {
				echo "\n<ul>\n<li><span id=\"links" . firstWord($translation["scoil"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["scoil"][$thisLanguage] . "\" href=\"http://www.nuigalway.ie/gaeilge/$querystring\">" . $translation["scoil"][$thisLanguage] . "</a></li>";
				echo "<li><span id=\"links" . firstWord($translation["acadamh"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["acadamh"][$thisLanguage] . "\" href=\"http://www.acadamh.oegaillimh.ie/$querystring\">" . $translation["acadamh"][$thisLanguage] . "</a></li>\n</ul>";
				}
				?>
  </li>
  <li><span id="links<?=firstWord($translation["academicStaff"][$thisLanguage])?>" class="youarehere"></span> <a tabindex="12" title="<?=$translation["academicStaff"][$thisLanguage]?>" href="http://www.nuigalway.ie/staff/<?=$querystring?>">
    <?=$translation["academicStaff"][$thisLanguage]?>
    </a> </li>
  <li><span id="links<?=firstWord($translation["onlineServices"][$thisLanguage])?>" class="youarehere"></span> <a accesskey="9" tabindex="13" title="<?=$translation["onlineServices"][$thisLanguage]?>  [access key '9']" href="http://www.nuigalway.ie/online_services/<?=$querystring?>">
    <?=$translation["onlineServices"][$thisLanguage] . $expandableIndicator?>
    </a>
    <?php
				if (stristr($REQUEST_URI, 'online_services')) {
				echo "\n<ul>\n<li><span id=\"links" . firstWord($translation["student"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["student"][$thisLanguage] . "\" href=\"http://www.nuigalway.ie/online_services/onlineservices_student.php$querystring\">" . $translation["student"][$thisLanguage] . "</a></li>";
				echo "<li><span id=\"links" . firstWord($translation["staff"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["staff"][$thisLanguage] . "\" href=\"http://www.nuigalway.ie/online_services/onlineservices_staff.php$querystring\">" . $translation["staff"][$thisLanguage] . "</a></li>\n</ul>";
				}
				?>
  </li>
  <li><span id="links<?=firstWord($translation["search"][$thisLanguage])?>" class="youarehere"></span> <a accesskey="8" tabindex="14" title="<?=$translation["search"][$thisLanguage]?>  [access key '8']" href="http://www.nuigalway.ie/search/<?=$querystring?>">
    <?=$translation["search"][$thisLanguage] . $expandableIndicator?>
    </a>
    <?php
				if (stristr($REQUEST_URI, '/search/')) {
				  echo "\n<ul>\n<li><span id=\"links" . firstWord($translation["searchEmail"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["searchEmail"][$thisLanguage] . "\" href=\"http://www.nuigalway.ie/search/email_search.php$querystring\">" . $translation["searchEmail"][$thisLanguage] . "</a></li></ul>";
                                  if (isOnCampus()) {
				    echo "\n<ul>\n<li><span id=\"links" . firstWord($translation["campusdir"][$thisLanguage]) . "\" class=\"youarehere\"></span><a title=\"" . $translation["campusdir"][$thisLanguage] . "\" href=\"http://www.nuigalway.ie/vp/sshr/internal/documents/campus_directory.pdf\">" . $translation["campusdir"][$thisLanguage] . "</a></li></ul>";
                                   }
				 }
				 ?>
  </li>
  <li><span id="links<?=firstWord($translation["enquiries"][$thisLanguage])?>" class="youarehere"></span> <a tabindex="15" title="<?=$translation["enquiries"][$thisLanguage]?>" href="http://www.nuigalway.ie/contacts.php<?=$querystring?>">
    <?=$translation["enquiries"][$thisLanguage]?>
    </a> </li>
  <li><span id="links<?=firstWord($translation["externalLinks"][$thisLanguage])?>" class="youarehere"></span> <a tabindex="19" title="<?=$translation["externalLinks"][$thisLanguage]?>" href="http://www.nuigalway.ie/links.php<?=$querystring?>">
    <?=$translation["externalLinks"][$thisLanguage]?>
    </a> </li>
	 <li><span id="links<?=firstWord($translation["vacancies2"][$thisLanguage])?>" class="youarehere"></span> <a tabindex="16" title="<?=$translation["vacancies2"][$thisLanguage]?>" href="http://www.nuigalway.ie/news/vacancies.php<?=$querystring?>">
    <?=$translation["vacancies2"][$thisLanguage]?>
    </a> </li>
 
</ul>
<?
function firstWord($string) {
  $words = explode(" ", trim($string));
	$word = $words[0] . $words[1];
  $firstWord = str_replace("&aacute;", "á", $word);
  $firstWord = str_replace("", "&amp;", $firstWord);
	$firstWord = str_replace("&eacute;", "é", $firstWord);
  $firstWord = str_replace("&oacute;", "ó", $firstWord);
  $firstWord = str_replace("&uacute;", "ú", $firstWord);
  $firstWord = str_replace("&Aacute;", "á", $firstWord);
  $firstWord = str_replace("&Eacute;", "é", $firstWord);
  $firstWord = str_replace("&Oacute;", "ó", $firstWord);
  $firstWord = str_replace("&Uacute;", "ú", $firstWord);
  $firstWord = str_replace("&iacute;", "í", $firstWord);
  $firstWord = str_replace("&Iacute;", "í", $firstWord);
  return($firstWord);
}
?>
