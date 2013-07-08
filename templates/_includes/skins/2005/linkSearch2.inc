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
		 <!-- <form id="linkSearchForm" action="http://www.googlesyndicatedsearch.com/u/oegaillimh" method="get" target="_blank"> -->
		  <form id="linkSearchForm" action="http://redwood.nuigalway.ie/cgi-bin/htsearch" method="post">
		    <!--<?=$lsIntro?>-->
			<label for="quickSearchBox"><input id="quickSearchBox" type="text" name="words" /></label>
		  </form>
		</div>