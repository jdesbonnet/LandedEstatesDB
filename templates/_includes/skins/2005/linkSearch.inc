<?
			if ((empty($_GET['language']) || $_GET['language'] != "gaeilge") && !stristr($_SERVER['HTTP_HOST'], "oegaillimh")) {
				$thisLanguage = 0;
				$querystring = "";
				$lsTitle = "Search Web Site";
				$lsIntro = "Search for links to:";
				$whatisthisTitle = "WHAT IS THIS?";
			} else {
				$thisLanguage = 1;
				$querystring = "?language=gaeilge";
				$lsTitle = "Cuardaigh";
				$lsIntro = "Cuardaigh naisc chuig:";
				$whatisthisTitle = "CAD &Eacute; SEO?";
			}
?>
		<div id="linkSearch">
		  <div id="linkSearchTitle">&nbsp;&nbsp;<?=$lsTitle?><a href="#" class="whatisthis">&nbsp;&nbsp;&nbsp;&nbsp;[?]<span><strong><?=$whatisthisTitle?></strong><br />Searches this and other areas of the Web site as you type. Results appear on this page.<br/>Also searches the entire Web site (with results on a new page) if you press enter or click on 'Go'.</span></a></div>
		  <form id="linkSearchForm" action="http://search.nuigalway.ie/search" method="get">
			<label for="quickSearchBox"><input id="quickSearchBox" type="text" name="q" /></label>
			<label for="goButton"><input id="goButton" name="btnG" type="image" value="Go" src="http://www.nuigalway.ie/images/go_button.png" alt="Go Button" class="button" /></label>
      <input type="hidden" name="site" value="default_collection" />
      <input type="hidden" name="client" value="default_frontend" />
      <input type="hidden" name="output" value="xml_no_dtd" />
      <input type="hidden" name="proxystylesheet" value="default_frontend" />
      </form>
		</div>