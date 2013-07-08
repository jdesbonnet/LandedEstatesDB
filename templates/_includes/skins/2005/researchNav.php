<?php 
include("/www/includes/skins/2005/php/sidenav_display.php");
include("/www/includes/skins/2005/sidenav_top.inc"); 
//Future Students
echo "    <li id=\"navfuture\"><a accesskey=\"2\" tabindex=\"4\" title=\"" . $translation['future'][$thisLanguage] . " [access key '2']\" href=\"http://www.nuigalway.ie/future_students/" . $querystring . "\">" . $translation['future'][$thisLanguage] . "</a></li>";
//Current Students
echo "\n    <li id=\"navcurrent\"><a accesskey=\"3\" tabindex=\"5\" title=\"" . $translation['current'][$thisLanguage] . " [access key '3']\" href=\"http://www.nuigalway.ie/current_students/" . $querystring . "\">" . $translation['current'][$thisLanguage] . "</a></li>";
//Faculties & Departments
echo "\n    <li id=\"navfacdept\"><a accesskey=\"4\" tabindex=\"6\" title=\"" . $translation['faculties'][$thisLanguage] . " [access key '4']\" href=\"http://www.nuigalway.ie/faculties_departments/" . $querystring . "\">" . $translation['faculties'][$thisLanguage] . "</a></li>";
//Research
echo "\n    <li id=\"navresearch\"><a accesskey=\"5\" tabindex=\"7\" title=\"" . $translation['research'][$thisLanguage] . " [access key '5']\" href=\"http://www.nuigalway.ie/research/" . $querystring ."\">" . $translation['research'][$thisLanguage] . "</a>";

echo "\n      <ul>";
drillDown(5, 3, "");
if ($category_divs_open > 0) {
  for ($i = 0; $i < $category_divs_open; $i++) {
    echo "\n      </ul>\n      </li>";
  }
  $category_divs_open = 0;
}
echo "\n      </ul>\n    </li>";

//Administration & Services
echo "\n    <li id=\"navadmin\"><a accesskey=\"6\" tabindex=\"8\" title=\"" . $translation['admin'][$thisLanguage] . " [access key '6']\" href=\"http://www.nuigalway.ie/administration_services/" . $querystring . "\">" . $translation['admin'][$thisLanguage] . "</a></li>";
echo "\n  </ul>\n</div>";
?>