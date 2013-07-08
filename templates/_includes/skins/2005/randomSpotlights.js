function showRandomSpotlights() {
  var noInserted = 0;
  var spotlight = new Array();
  var divs = document.getElementsByTagName('div');
  for (i=0; i < divs.length; i++) {
    if (divs[i].className == "randomSpotLight") {
	  spotlight[noInserted] = i;
	  noInserted++;
	}
  }
  if (noInserted >= noRandomSpotlights && noInserted > 0) {
    var noDisplayed = 0;
	while (noDisplayed < noRandomSpotlights) {
      var ranNum= Math.floor(Math.random() * spotlight.length);
	  var randomDiv =spotlight[ranNum];
      if (divs[randomDiv].style.display != "block") {
	    divs[randomDiv].style.display = "block";
		noDisplayed++;
  	  }
	} 
  }
  //after randomisation, add event for viewing all
  var viewAllButton = document.getElementById("viewAllSpotlights");
  if (viewAllButton) {
    addEvent(viewAllButton, 'click', showAllSpotlights, false);
  }
}

function showAllSpotlights() {
  var divs = document.getElementsByTagName('div');
  for (i=0; i < divs.length; i++) {
    if (divs[i].className == "randomSpotLight") {
	  divs[i].style.display = "block";
	}
  }
  var viewAllButton = document.getElementById("viewAllSpotlights");
  if (viewAllButton) {
    viewAllButton.style.display = "none";
  }
  return;
}

addEvent(window, 'load', showRandomSpotlights, false);