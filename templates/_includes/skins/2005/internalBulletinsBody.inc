		<?php
		  if ((empty($_GET['language']) || $_GET['language'] != "gaeilge") && !stristr($_SERVER['HTTP_HOST'], "oegaillimh")) {
			$bulletinTitle = "Internal Bulletins";
		  } else {
			$bulletinTitle = "Feasacháin";
			}
		?>
			
		<h2><?=$bulletinTitle?><a href="#" class="whatisthis">&nbsp;&nbsp;[?]<span><strong>WHAT IS THIS?</strong> Internal notices relating to the status of IT services for students and academic staff - maintained by Computer Services.</span></a></h2>
		<div id="bulletinsBody">
				<ul>
			  <? include("/www/includes/skins/2005/php/list_bulletins.php"); ?>
		    </ul>
		</div>