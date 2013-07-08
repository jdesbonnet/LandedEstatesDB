<?php
/*
Gets called like this (from the home page):

if ($_GET['language'] != "gaeilge" && !stristr($_SERVER['HTTP_HOST'], "oegaillimh")) {
	$incRecentlyUpdated = "Recently Updated Pages";
} else {
	$incRecentlyUpdated = "Recently Updated Pages";
}

$leadingXHTML = "<h2>$incRecentlyUpdated</h2>\n\t";
$leadingXHTML .= "<p><div class=\"bodytext\">";
$trailingXHTML = "\n\t</div>";
displayRecentlyUpdated('', 7, $leadingXHTML, $trailingXHTML, false);
*/

function displayRecentlyUpdated($filterString, $maxNo, $leadingHTML = "", $trailingHTML = "", $shortDispNames = true, $maxLength=55) {
  $command = "grep -i \"$filterString\" /www/recentupdates.txt";
  //$command = "grep -i \"ncbes\" /www/recentupdates.txt";
  exec($command, $output);
    if (!empty($output[0])) {
      echo $leadingHTML;
      if ($maxNo == 0) {
        $max = count($output);
      } else {
        $max = $maxNo;
      }

      if ($maxNo > 0 && count($output) > $maxNo) {
        //display random results...
        srand(time());
        $arrayMax = count($output);
        $pickedLinks = "";
        for ($i=0; $i < $max; $i++) {
          $randomNo = (rand()%$arrayMax);
          displayLinks($output[$randomNo], $max, $shortDispNames, $maxLength);
        }
      } else {
        //display all links...
        for ($i =0; $i < $max; $i++) {
		  if ($i < count($output)) {
            displayLinks($output[$i], $max, $shortDispNames, $maxLength);
		  }
        }
      }
      echo $trailingHTML;

    }
}

function displayLinks($link, $max, $shortDispNames, $maxlength) {
         global $pickedLinks;
         $url = str_replace("/www/", "http://www.nuigalway.ie/", $link);
         $startpos = strrpos($link, "/");
         $shortDispName = substr($link, $startpos+1);
         $dispName = $url;

           //If it's a subdirectory, make the long display names a bit more user friendly...
           if (!empty($url)) {
		   if (strpos($url, "/", 8) > 0) {
             //don't include faculties_departments in the dispname - unless there is no department name...
             
			 //get the top level name (e.g. 'Research')
			 $start = strpos($url, "/", 8)+1;
             $end = strpos($url, "/", $start);
             $length = $end - $start;
             $FirstLevelUnit = capitalise_words(str_replace("_", " ", substr($url, $start , $length)));
			 //Strip out the top level name to get the unit name...
             $tempURL = str_replace("/faculties_departments/", "/", $url);
             $tempURL = str_replace("/administration_services/", "/", $tempURL);
             $tempURL = str_replace("/prosepective_students/", "/", $tempURL);
             $tempURL = str_replace("/research/", "/", $tempURL);
             $tempURL = str_replace("/student_life/", "/", $tempURL);
             $start = strpos($tempURL, "/", 8)+1;
             $end = strpos($tempURL, "/", $start);
             $length = $end - $start;
             $unit = capitalise_words(str_replace("_", " ", substr($tempURL, $start , $length)));
			 if (empty($unit)) {
			   $unit = $FirstLevelUnit;
			 }
			 if (!empty($unit)) {
			   $unit .= " - ";
			 } 
             if (stristr($dispName, "index.")) {
               $lastslash = strrpos($url, "/");
               $secondlastslash = strrpos(substr($url, 0, $lastslash-2) , "/");
               $siteName = capitalise_words(substr(str_replace("_", " ", $url), $secondlastslash + 1, $lastslash - $secondlastslash - 1));
               if ($siteName != $unit) {
                 //$dispName = $unit . $siteName . " Home Page";
				 $dispName = $siteName . " Home Page";
                 $shortDispName = $siteName . " Home Page";
               } else {
                 //$dispName = $unit . " Home Page";
				 $dispName = " Home Page";
                 $shortDispName = $siteName . " Home Page";
               }
             } else {
               //$dispName = $unit . capitalise_first(substr($tempURL, $end+1));
			   $dispName = capitalise_first(substr($tempURL, $end+1));
             } 
           } else {
             //it's not a subdirectory...
             if (stristr($dispName, "index.")) {
                $shortDispName = "Home Page";
                $dispName = "Home Page";
             }
           }
           }  

         //Undesired directories / files have already been filtered out by /usr/local/etc/recentlyupdated_blacklist

         //display the link if it hasn't been displayed before...
         if (!empty($url)) {
  		   if (!stristr($pickedLinks, $url)) {
            // echo "\n\t\t<a href=\"$url\" title=\"$url\">";
			// $aTag = "<a href=\"$url\" title=\"$url\">";
			echo "\n\t\t";
			if (!$shortDispNames) {
			 $unit = str_replace("Prospective Students", "Future Students", $unit);
			 echo str_replace("Student Life", "Current Students", $unit);
			}
			$maxlength -= strlen($unit);
			echo "<a href=\"$url\" title=\"$url\">";
			 if (strlen($dispName) > $maxlength) {
			   $dispName = $shortDispName;
			 }
			 if (strlen($dispName) > $maxlength) {
			   //if shortname is longer than maxlength, change to 'Unit: ...hfhjdasklsdfka/hasdffsalh.html'
			   //$dispName = str_replace($unit, "", $dispName);
               $dispName = "..." . substr($dispName, strlen($dispName)-($maxlength-3));
            }
			 $displayName = ($shortDispNames) ? $shortDispName : $dispName;
			 $displayName = str_replace("Prospective Students", "Future Students", $displayName);
			 echo str_replace("Student Life", "Current Students", $displayName);
             echo "</a><br />";
           }
           $pickedLinks .= $url . ",";
		 }
}

function capitalise_words($string) {
  //works like ucwords but doesn't force the rest of the string to lower case
  $newString = "";
  $wordArray = explode(" ", $string);  
  for ($i = 0; $i < count($wordArray); $i++) {
    $newString .= capitalise_first($wordArray[$i]) . " ";
  }
  return trim($newString);
}

function capitalise_first($string) {
  //works like ucfirst but doesn't force the rest of the string to lower case
  return strtoupper(substr($string, 0, 1)) . substr($string, 1);
}
?>
