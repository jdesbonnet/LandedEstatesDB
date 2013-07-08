		<div id="bulletinsSidenav">
		<?php
		  if ((empty($_GET['language']) || $_GET['language'] != "gaeilge") && !stristr($_SERVER['HTTP_HOST'], "oegaillimh")) {
			$bulletinTitle = "Internal Bulletins";
		  } else {
			$bulletinTitle = "Feasacháin";
			}
			?>
        <ul>
        <li class="verdanabold">&nbsp;&nbsp;<?=$bulletinTitle?><a href="#" class="whatisthis">&nbsp;&nbsp;[?]<span><strong>WHAT IS THIS?</strong> Important notices relating to the status of IT services - maintained by Computer Services.</span></a></li>
			  <? include("/www/includes/skins/2005/php/list_bulletins.php"); ?>
		    </ul>
		</div>