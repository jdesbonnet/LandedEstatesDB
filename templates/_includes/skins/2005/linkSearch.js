var LinkSearch = new Object();
//handlers for ajax searches so we can cancel them if needed
LinkSearch.CampusMapAJAXSearch = null;
LinkSearch.SiteMapAJAXSearch = null;
//other vars
LinkSearch.inputField = null;
LinkSearch.noFailedSearches = 0;
LinkSearch.matches = 0;
LinkSearch.delayedAJAX = null;
LinkSearch.tooltip = null;
LinkSearch.tooltipDelay = null;
LinkSearch.enterPressed = false;
if (document.location.href.indexOf('language=gaeilge') > 0 || document.location.href.indexOf('_irish.') > 0) {
	LinkSearch.language = "irish";
} else {
	LinkSearch.language = "english";	
}

LinkSearch.AJAXRead = function(url, handler, xmlhttp) {
  //var xmlhttp = new XMLHttpRequest();
  eval(xmlhttp + ' = new XMLHttpRequest();');
  eval(xmlhttp + ".open('GET', url, true);");
  eval(xmlhttp + ".onreadystatechange = function() {    try {  	  if (" + xmlhttp + ".readyState == 4) {		if (" + xmlhttp + ".status == 200) {	      handler(" + xmlhttp + ".responseXML);	    } else {		  /*503 - Application unavailable, 500 - Internal server error		  alert('A problem occurred while communicating with the server program.\\n\\nActual Error: ' + " + xmlhttp + ".status + ' in ' + url);*/	    }	  }    } catch (err) {	/*do nothing*/   	}  };");
  eval(xmlhttp + ".send('');");
}

LinkSearch.cancelSearch = function() {
    //check if AJAX searches are underway - if so cancel them...
    if (LinkSearch.CampusMapAJAXSearch) {
		try {
		  if (LinkSearch.CampusMapAJAXSearch.readyState != 4) {
			LinkSearch.CampusMapAJAXSearch.abort();  
		  }
		} catch (err) {
		 	//do nothing
			alert('Could not abort the campus map search');
		}
	}
	if (LinkSearch.SiteMapAJAXSearch) {
		try {
		  if (LinkSearch.SiteMapAJAXSearch.readyState != 4) {
			LinkSearch.SiteMapAJAXSearch.abort();  
		  }
		} catch (err) {
		 	//do nothing
			alert('Could not abort the A - Z search');
		}
    }
    //remove all previous results if there already...
	var searchResultsLabel = document.getElementById('searchresults');
	if (searchResultsLabel) {
		searchResultsLabel.parentNode.removeChild(searchResultsLabel);
		searchResultsLabel = null;
	}
	var searchResultsNotesLabel = document.getElementById('searchresultsnotes');
	if (searchResultsNotesLabel) {
		searchResultsNotesLabel.parentNode.removeChild(searchResultsNotesLabel);
		searchResultsNotesLabel = null;
	}
	var existingResults = document.getElementById('linkSearchResults');
	if (existingResults) {
		existingResults.parentNode.removeChild(existingResults);
		existingResults = null;
	}
	var sitemapResults = document.getElementById('lsSitemapSearchResults');
	if (sitemapResults) {
	    sitemapResults.parentNode.removeChild(sitemapResults);
		sitemapResults = null;
    }
	var campusMapResults = document.getElementById('lsCampusMapSearchResults');
	if (campusMapResults) {
	    campusMapResults.parentNode.removeChild(campusMapResults);
		campusMapResults = null;
    }
	//end of removal code
}

LinkSearch.showToolTip = function() {
	 var searchForm = document.getElementById('linkSearchForm'); 
	 if (searchForm && LinkSearch.noFailedSearches > 0) {
	 //To remove all unneccessary tips use the if statement, below:
	 //if (searchForm && LinkSearch.enterPressed) {
		if (LinkSearch.matches == 0 || LinkSearch.enterPressed) {
		  var tip = document.getElementById('lsTip');
	  	  if (tip) {
	  	    tip.parentNode.removeChild(tip);
	      }
		  var tipImg = document.createElement('img');
		  tipImg.src = "includes/skins/2005/images/bulb.gif";
		  tipImg.alt = (LinkSearch.language == "english") ? "Tip" : "Leid";
		  var tipspan = document.createElement('span');
		  tipspan.id = "lsTip";
		  tipspan.style.backgroundColor = "#ffffe1";
		  tipspan.style.border = "1px solid black";
		  tipspan.style.margin = "0px 0px 0px 5px";
		  tipspan.style.color = "black";
  		  tipspan.style.position = "absolute";
		  tipspan.appendChild(tipImg);
		  searchForm.appendChild(tipspan);
		  //newbr = brTag.cloneNode(true);
		  //linkSearch.lastChild.appendChild(newbr);
		  //searchForm.lastChild.appendChild(newbr);
		  var searchTip = document.createTextNode(LinkSearch.searchTips[LinkSearch.noFailedSearches]);
		  //linkSearch.lastChild.appendChild(searchTip);
		  searchForm.lastChild.appendChild(searchTip);
		  //searchForm.lastChild.appendChild(newbr);
		}
		
	 }	
}

LinkSearch.hideToolTip = function() {
  var tip = document.getElementById('lsTip');
  if (tip) {
    tip.parentNode.removeChild(tip);
  }
  return;
}

LinkSearch.searchPage = function(e) {
	if (LinkSearch.delayedAJAX != null) {
	  //system is waiting to call AJAX searches - cancel them because another letter has been typed in already
	  clearTimeout(LinkSearch.delayedAJAX);
	}
//	if (LinkSearch.tooltip != null) {
//	  clearTimeout(LinkSearch.tooltip);
//	}
//	if (LinkSearch.tooltipDelay != null) {
//	  clearTimeout(LinkSearch.tooltipDelay);
//	}

/*
	LinkSearch.searchTips = new Array();
	if (LinkSearch.language == "english") {
	  //searchTips[0] = "Tip: Type 3 or more letters to start a quick search for that phrase (results appear here).  If no match is found on this page, press Enter for a full site search (results replace this page).";
	//LinkSearch.searchTips[0] = " The 'Quick Search' will begin once you have typed two or more characters.";
	LinkSearch.searchTips[0] = " A 'Quick Search' is being performed as you type - for a full search, click on Go or press Enter";
	LinkSearch.searchTips[1] = " The 'Quick Search' searches as you type.  There is no need to press the 'Enter' button.";
	LinkSearch.searchTips[2] = " If a match is not found on this page, the 'Go' button appears, allowing you to search the entire site.";
	  LinkSearch.searchTips[3] = " The 'Quick Search' results show links containing your search word.  To search for normal text, click on the 'Go' button.";
	  LinkSearch.searchTips[4] = " 'Quick Search' results appear below your search words as you type.";
  	  LinkSearch.searchTips[5] = " 'Quick Search' results will appear without reloading the page.";
  	  LinkSearch.searchTips[6] = " All 'Quick Search' results will remain on the screen until you click on a link or change your search word.";
  	  LinkSearch.searchTips[7] = " The links on this page are scanned every time you type a letter.  Results appear on the screen immediately.";
	  LinkSearch.searchTips[8] = " The sitemap and campus map searches begin after you stop typing. Results appear in seconds.";
	  LinkSearch.searchTips[9] = " To clear the 'Quick search' results, delete your search word.";
	  LinkSearch.searchTips[10] = " Click on the 'Go' button to invoke a full site search.";
	  //LinkSearch.searchTips[4] = "Tip: Perhaps different wording is being used on this Web site - Try browsing the menu, above, or clicking on 'Go'.";
	}
	if (LinkSearch.language == "irish") {
	  LinkSearch.searchTips[0] = " Clóscríobh isteach na chéad 3 litir i do fhrása cuardaigh agus cuir gach litir nua leis diaidh ar ndiaidh.  Ní gá an Eochair Iontrála a bhrú.";
	  LinkSearch.searchTips[1] = " Úsáid an Mearchuardach i gcuid eile den L&aaucte;ithreán Gréasáin chun an chuid sin a chuardach.";
	  LinkSearch.searchTips[2] = " Ní aimsíonn an áis chuardaigh seo ach naisc a bhfuil d'fhocal cuardaigh iontu. Chun téacs (cé is móite de nascthéacs) a chuardach, cliceáil ar 'Go'.";
	  LinkSearch.searchTips[3] = " Ní thugann an áis chuardaigh seo ach eolas i dtaca le téacsnaisc sa chuid seo – tabharfar neamhaird ar íomhánna ar féidir cliceáil orthu.";
	  LinkSearch.searchTips[4] = " B’fhéidir go bhfuil foclaíocht éagsúil á húsáid ar an Láithreán Gréasáin seo – Déan iarracht an roghchlár thuas a bhrabhsáil, chun nasc níos ábhartha a aimsiú.";
	}
*/
    var searchPhrase = LinkSearch.inputField.value.toLowerCase();
	var searchForm = document.getElementById('linkSearchForm');
	var linkSearch = searchForm.parentNode;
    LinkSearch.cancelSearch();
	if (searchPhrase.length < 3) {
	  var resultCount = document.getElementById('lsResultCount');
	  if (resultCount) {
	    resultCount.parentNode.removeChild(resultCount);
	  }
	//  var submitButton = document.getElementById('smSearchButton');
	//  if (submitButton) {
	//	submitButton.parentNode.removeChild(submitButton);
	//  }
    
/*	  var tip = document.getElementById('lsTip');
      if (tip) {
	    tip.parentNode.removeChild(tip);
	  }
*/
	  return;
	}
/*	if (searchPhrase.length == 3) { //3 chars entered, so show first tip
		var tipspan = document.createElement('span');
		tipspan.style.backgroundColor = "#ffffe1";
		tipspan.style.border = "1px solid black";
		tipspan.style.color = "black";
		tipspan.style.position = "absolute";
		tipspan.style.zIndex = "1000";
		tipspan.id = "lsTip";
		searchForm.appendChild(tipspan);
	    var searchTip = document.createTextNode(LinkSearch.searchTips[0]);
	    searchForm.lastChild.appendChild(searchTip);
		LinkSearch.tooltipDelay = setTimeout('LinkSearch.hideToolTip()', 4000);
	}
*/
    //var sideNavDiv = document.getElementById('sidenav');
    //if (sideNavDiv) {
    // var sideNavLinks = sideNavDiv.getElementsByTagName('a');
	 var allLinks = document.getElementsByTagName('a');
	 var maxLinks = allLinks.length;
	 LinkSearch.matches = 0;
	 //var resultSet = document.createElement('span');
	 var resultSet = document.createElement('ul');
	 resultSet.id = "linkSearchResults";
	 linkSearch.appendChild(resultSet);	

	 var searchMsg = (LinkSearch.language == "english") ? " Searching for links on this page ..." : " Ag cuardach nasc ar an leathanach seo ..."; 
	 var searchingMsg = document.createTextNode(searchMsg);
	 var searchingLinks = document.createElement('li');
     searchingLinks.id = "lsResultCount";
	 searchingLinks.appendChild(searchingMsg);
	 resultSet.appendChild(searchingLinks);
	 
     //start searching
     var newLink = null;
	 var suppressImageText = false;
	 var useTitle = false;
	 var titleLinkText = "";
	 var brTag = document.createElement('br');
	 var liTag = document.createElement('li');
	 var displayedAlready = false;
     for (i=0; i < maxLinks; i++) {
	  suppressImageText = false;
	  useTitle = false;
	  //if (allLinks[i].parentNode.id != "linkSearchResults" && (allLinks[i].childNodes[0] && allLinks[i].childNodes[0].nodeType == 3)  && allLinks[i].href.charAt(allLinks[i].href.length -1) != "#") {
	  if (allLinks[i].parentNode.id != "linkSearchResults" && allLinks[i].childNodes[0] && allLinks[i].href.charAt(allLinks[i].href.length -1) != "#") {
	   linkTarget = allLinks[i].href.toLowerCase();
	   if (allLinks[i].childNodes[0].nodeType == 3) {
		 //link is text node
		 if ((allLinks[i].getAttribute("class") && allLinks[i].getAttribute("class") == "useTitle") || allLinks[i].className == "useTitle") {
			titleLinkText = allLinks[i].getAttribute("title");
			useTitle = true;
		 } else {
	     	linkText = allLinks[i].childNodes[0].nodeValue.toLowerCase();
		 }
	   } else {
		  if (allLinks[i].childNodes[0].nodeType == 1) {
			//link is an element - assume it's an image
			if (allLinks[i].className && allLinks[i].className.indexOf("textlink") > -1) {
			  suppressImageText = true;
			}
			if (allLinks[i].childNodes[0].alt) {
			  linkText = "Image - " + allLinks[i].childNodes[0].alt;
			} else {
			  linkText = "Image - Untitled";
			}
		  } else {
			 linkText = "Unknown link type";  
		  }
	   }
	   linkTitle = "";
	   if (allLinks[i].title) {
	     linkTitle = allLinks[i].title.toLowerCase();
	   }
	   if (linkTarget.indexOf(searchPhrase) > -1 || linkText.indexOf(searchPhrase) > -1 || linkTitle.indexOf(searchPhrase) > -1) {
		//If not one of the links in the result set + if a text link + if text found in the link target, text or title, 
		//add to the list of results (if not there already)
		
		//check if link has appeared in results already
		resultSet = document.getElementById('linkSearchResults');
		resultLinks = resultSet.getElementsByTagName('a');
		displayedAlready = false;
		var textToFind = linkText.toLowerCase();
		if (useTitle) {
		  textToFind = linkTitle.toLowerCase();	
		}
		if (suppressImageText) {
		  textToFind = linkText.replace(/Image - /, "").toLowerCase();
		}
		if (resultLinks) {
		  for (j=0; j < resultLinks.length; j++) {
			  //if (resultLinks[j].href == allLinks[i].href && resultLinks[j].childNodes[0].nodeValue.toLowerCase() == linkText.toLowerCase()) {
				if (resultLinks[j].href.indexOf(allLinks[i].href) > -1 && resultLinks[j].childNodes[0].nodeValue.toLowerCase() == textToFind) {
				displayedAlready = true;
				j = resultLinks.length;
			  }
		  }
		}
		if (!displayedAlready) {
		  LinkSearch.matches++;
		  if (linkText.indexOf('Image - ') > -1 || useTitle) {
			//if it's an image link or a text link where we've specified class="useTitle" (e.g. a 'Read On' link)
		  	var thisLinksText = linkText;
			if (suppressImageText) {
			  thisLinksText = (suppressImageText) ? linkText.replace(/Image - /, "") : linkText;
		    }
			if (useTitle) {
			  thisLinksText = titleLinkText;
		    }
			var newlinktext = document.createTextNode(thisLinksText);
			newLink = document.createElement('a');
			newLink.href = linkTarget;
			newLink.appendChild(newlinktext);
			newLI = document.createElement('li');
		  } else {
		    newLink = allLinks[i].cloneNode(true);
		    newLink.removeAttribute('id');
	        newLI = liTag.cloneNode(true);
		  }
		  newLI.appendChild(newLink);
		  resultSet.appendChild(newLI);
		}
	   }
	  } //end for
	  if (LinkSearch.language == "english") {
	    window.status = "Searching " + maxLinks + " links on this page: " + i;
	  }
	  if (LinkSearch.language == "irish") {
	    window.status = "Ag cuardach " + maxLinks + " nasc ar an leathanach seo: " + i;
	  }
     }
	 window.status = "Done";
	 if (LinkSearch.language == "english") {
	   var numberFound = document.createTextNode(" This Page: (" + LinkSearch.matches + " Links)");
	 }
	 if (LinkSearch.language == "irish") {
	   var numberFound = document.createTextNode(" Nasc a aimsíodh sa chuid seo: (" + LinkSearch.matches + ")");
	 }
	 var resultCount = document.getElementById('lsResultCount');
	 if (resultCount) {
		 //replace it
		 if (LinkSearch.language == "english") {
		   resultCount.childNodes[0].nodeValue = " This page: (" + LinkSearch.matches + " Links)";
		 }
		 if (LinkSearch.language == "irish") {
		   resultCount.childNodes[0].nodeValue = " Nasc a aimsíodh sa chuid seo: (" + LinkSearch.matches + ")";
		 }
	 } else {
	   //create it
	   //resultCount = document.createElement('span');
	   resultCount = document.createElement('li');
	   resultCount.id = "lsResultCount";
	   //newbr = brTag.cloneNode(true);
	   //resultCount.appendChild(newbr);
	   resultCount.appendChild(numberFound);
	   //searchForm.appendChild(resultCount);
	   resultSet.appendChild(resultCount);
	 }
/*//	 var searchButton = document.getElementById('smSearchButton');
	 if (LinkSearch.matches == 0) {
		LinkSearch.noFailedSearches ++;
		//add a search button to search the site
	    if (!searchButton) {
		  searchButton = document.createElement('input');
		  searchButton.type = "submit";
		  searchButton.name = "fullSearch";
		  searchButton.style.width = "30px";
		  if (LinkSearch.language == "english") { 
		    //searchButton.value = "Search Entire Site";
			searchButton.value = "Go";
		  }
		  if (LinkSearch.language == "irish") { 
		    //searchButton.value = "Cuardaigh an Láithreáin ar fad";
			searchButton.value = "Go";
		  }
		  searchButton.id = "smSearchButton";
		  searchButton.className = "sitemapSearchButton";
		  searchForm.appendChild(searchButton);
		}
	 
	 } else {
		//there was at least one match - remove search sitemap link
		if (searchButton) {
		  searchButton.parentNode.removeChild(searchButton);	
		}
	 }
*/
/*		var tip = document.getElementById('lsTip');
	  	if (tip) {
	  	  tip.parentNode.removeChild(tip);
	    }
*/
	 LinkSearch.enterPressed = false;
	 if (e) {
		if (e.keyCode && e.keyCode == 13) { 
	     //Enter was pressed - make tip on how to use the search appear
	     //LinkSearch.noFailedSearches = 1;  
	     //LinkSearch.enterPressed = true;
		 LinkSearch.cancelSearch();
		 return;
		}
	 }
	 
//	 if (LinkSearch.noFailedSearches == LinkSearch.searchTips.length) {
//		    LinkSearch.noFailedSearches = 1;
//	 }
	 //LinkSearch.showToolTip();
     var resultsText = (LinkSearch.language == "english") ? "Quick Search Results:" : "Toradh Mearchuardach:";
	 var resultsNoteText = (LinkSearch.language == "english") ? "(For full results, click 'Go')" : "(Brú ar 'Go' chun toradh iomlán a fheicáil)";
	 var resultsHeader = document.createTextNode(resultsText);
	 var resultsNote = document.createTextNode(resultsNoteText);
	 var resultsSpan = document.createElement('span');
	 var resultsNoteSpan = document.createElement('span');
	 var resultsNewbr = brTag.cloneNode(true);
	 var resultsNewbr2 = brTag.cloneNode(true);
	 resultsSpan.appendChild(resultsNewbr);
	 if (LinkSearch.noFailedSearches == 0) {
	   resultsSpan.appendChild(resultsNewbr2); //extra br needed when no tip is displayed
	 }
	 resultsSpan.appendChild(resultsHeader);
	 resultsSpan.id = "searchresults";
	 resultsNoteSpan.id = "searchresultsnotes";
	 //resultsSpan.style.fontWeight = "bold";
	 searchForm.appendChild(resultsSpan);
	 var resultsNewbr3 = brTag.cloneNode(true);
	 resultsNoteSpan.appendChild(resultsNewbr3);
	 resultsNoteSpan.appendChild(resultsNote);
	 searchForm.appendChild(resultsNoteSpan);
	 
	 //search sitemap
     //var sitemapHeader = (LinkSearch.language == "english") ? " Searching the SiteMap " : " Ag cuardach Léarscáil an tSuimh ";
	 var sitemapHeader = (LinkSearch.language == "english") ? " Searching the A to Z " : " Ag cuardach an A go Z ";
	 var searching = document.createTextNode(sitemapHeader);
	 var thisPagesResults = document.getElementById('linkSearchResults');
	 var sitemapResults = document.createElement('ul');
	 sitemapResults.id = "lsSitemapSearchResults";
	 var sitemapResultsHeader = document.createElement('li');
	 sitemapResultsHeader.id = "lsSearchingSitemap";
	 sitemapResultsHeader.appendChild(searching);
	 var searchImg = document.createElement('img');
	 searchImg.src = "includes/skins/2005/images/searching.gif";
	 sitemapResultsHeader.appendChild(searchImg);
	 sitemapResults.appendChild(sitemapResultsHeader);
	 linkSearch.appendChild(sitemapResults);
	 
	 //search campus map
     var campusMapHeader = (LinkSearch.language == "english") ? " Searching the Campus Map " : " Ag cuardach Mapa an gCampas ";
	 searching = document.createTextNode(campusMapHeader);
	 //thisPagesResults = document.getElementById('linkSearchResults');
	 var mapResults = document.createElement('ul');
	 mapResults.id = "lsCampusMapSearchResults";
	 var mapResultsHeader = document.createElement('li');
	 mapResultsHeader.id = "lsSearchingCampusMap";
	 mapResultsHeader.appendChild(searching);
	 var newImg = searchImg.cloneNode(true);
	 //searchImg.src = "includes/skins/2005/images/searching.gif";
	 mapResultsHeader.appendChild(newImg);
	 mapResults.appendChild(mapResultsHeader);
	 linkSearch.appendChild(mapResults);

     //call all AJAX searches when user stops typing for 1 second
	 LinkSearch.delayedAJAX = setTimeout('LinkSearch.delayedAJAXSearches()', 1000);
	 //show Tooltip for 5 seconds after 1 second delay
	// LinkSearch.tooltip = setTimeout('LinkSearch.showToolTip();', 1000);
//	 LinkSearch.tooltipDelay = setTimeout('LinkSearch.hideToolTip()', 4000);
     if (isMac && isMinIE5) {
	   redraw_page();
	 }
    //}
}

LinkSearch.delayedAJAXSearches = function() {
  var sitemapSearch = "/searchSitemapDB.php?searchwords=" + LinkSearch.inputField.value;
  if (LinkSearch.language != "english") {
	sitemapSearch += "&language=gaeilge";  
  }
  LinkSearch.AJAXRead(sitemapSearch, LinkSearch.sitemapResults, "LinkSearch.SiteMapAJAXSearch");
  LinkSearch.AJAXRead("/campus_map/ajaxSearch.php?q=" + LinkSearch.inputField.value, LinkSearch.campusMapResults, "LinkSearch.CampusMapAJAXSearch");
return;
}

LinkSearch.campusMapResults = function(xml) {
  var resultSet = xml.getElementsByTagName('item');
  var matchesFound = 0;
  var mapResultsList = document.getElementById('lsCampusMapSearchResults'); //ul to contain results
  if (resultSet && resultSet[0]) {
	for (LinkSearch.loopcnt=0; LinkSearch.loopcnt < resultSet.length; LinkSearch.loopcnt++) {
	  xmlLabel = resultSet[LinkSearch.loopcnt].getElementsByTagName('label');
	  xmlId = resultSet[LinkSearch.loopcnt].getElementsByTagName('id');
	  var resultLabel = new XMLSerializer().serializeToString(xmlLabel[0]);
	  var resultId = new XMLSerializer().serializeToString(xmlId[0]);
	  resultLabel = resultLabel.replace(/<\/?label>/g, "");
	  resultId = resultId.replace(/<\/?id>/g, "");	
	  //put links in the campus map results ul
	  var campusMapLink = document.createElement('a');
	  campusMapLink.href = "http://www.nuigalway.ie/campus_map/index.php?id=" + resultId + "&go=1";
	  var campusMapLinkText = document.createTextNode(resultLabel);
	  campusMapLink.appendChild(campusMapLinkText);
	  var campusMapResult = document.createElement('li');
	  campusMapResult.appendChild(campusMapLink);
	  mapResultsList.appendChild(campusMapResult);
    }
	matchesFound = resultSet.length;
  }
  //replace 'searching' label in quicksearch
  //var campusMapHeader = (LinkSearch.language == "english") ? " Campus Map: " + matchesFound + " Links" : " Mapa an gCampas: " + matchesFound + " Nasc";
  var campusMapHeader = (LinkSearch.language == "english") ? " Campus Map: (" + matchesFound + " Links)" : " Mapa an gCampas: (" + matchesFound + " Nasc)";
  var newHeaderText = document.createTextNode(campusMapHeader);
  var header = document.getElementById('lsSearchingCampusMap');
  if (header) {
    var oldHeaderText = header.firstChild;
	oldHeaderText.parentNode.removeChild(oldHeaderText);
	var oldHeaderImage = header.firstChild;
	if (oldHeaderImage) {
	  oldHeaderImage.parentNode.removeChild(oldHeaderImage);
	}
	header.appendChild(newHeaderText);
  }
  return;
}


LinkSearch.sitemapResults = function(xml) {
  var resultSet = xml.getElementsByTagName('item');
  var matchesFound = 0;
  var loopcnt;
  var sitemapResultsList = document.getElementById('lsSitemapSearchResults'); //ul to contain results
  if (resultSet && resultSet[0]) {
	for (loopcnt=0; loopcnt < resultSet.length; loopcnt++) {
	  
	  xmlLabel = resultSet[loopcnt].getElementsByTagName('label');
	  xmlUrl = resultSet[loopcnt].getElementsByTagName('url');
	  var resultLabel = new XMLSerializer().serializeToString(xmlLabel[0]);
	  var resultUrl = new XMLSerializer().serializeToString(xmlUrl[0]);
	  resultLabel = resultLabel.replace(/<\/?label>/g, "");
	  resultUrl = resultUrl.replace(/<\/?url>/g, "");
	  /*
	  var resultLabel = resultSet[loopcnt].childNodes[0].nodeValue;
	  var resultUrl = resultSet[loopcnt].childNodes[1].nodeValue;
	  */
	  //put links in the campus map results ul
	  var sitemapLink = document.createElement('a');
	  if (resultUrl.indexOf('" target="_blank') > -1) {
		resultUrl = resultUrl.replace(/" target="_blank/gi, "");
		sitemapLink.target = "_blank";
	  }
	  sitemapLink.href = resultUrl;
	  var sitemapLinkText = document.createTextNode(resultLabel);
	  sitemapLink.appendChild(sitemapLinkText);
	  var sitemapResult = document.createElement('li');
	  sitemapResult.appendChild(sitemapLink);
	  sitemapResultsList.appendChild(sitemapResult);
    }
	matchesFound = resultSet.length;
  }
  //replace 'searching' label in quicksearch
  //var sitemapHeader = (LinkSearch.language == "english") ? "  Sitemap: " + matchesFound + " Links" : " Léarscáil an tSuimh: " + matchesFound + " Nasc";
  var sitemapHeader = (LinkSearch.language == "english") ? "  A to Z: (" + matchesFound + " Links)" : " A go Z: (" + matchesFound + " Nasc)";
  var newHeaderText = document.createTextNode(sitemapHeader);
  var header = document.getElementById('lsSearchingSitemap');
  if (header) {
    var oldHeaderText = header.firstChild;
	oldHeaderText.parentNode.removeChild(oldHeaderText);
	var oldHeaderImage = header.firstChild;
	if (oldHeaderImage) {
	  oldHeaderImage.parentNode.removeChild(oldHeaderImage);
	}
	header.appendChild(newHeaderText);
  }
  return;
}


LinkSearch.cancelSubmit = function(e) {
  //var target = (window.event) ? window.event.srcElement.id : e.target.id;
  //if the submit button was clicked, allow submit.  If <enter> was pressed, don't.
  //if (target == "linkSearchForm" && e.keyCode != 13 && e.keyCode != 10) {
//  if (LinkSearch.matches == 0 && LinkSearch.inputField.value.length > 2) {
  if (LinkSearch.inputField.value.length > 2) {
	LinkSearch.cancelSearch();
	return true;
  }
  if (window.event) {
    if (window.event.cancelBubble) {
  	  window.event.cancelBubble = true;
    }
    if (window.event.returnValue) {
  	  window.event.returnValue = false;
    }
  }
  if (e) {
    if (e.stopPropagation) {
  	  e.stopPropagation();
    }
    if (e.preventDefault) {
  	  e.preventDefault();
    }
  }
  return false;
}

LinkSearch.init = function() {
  var searchForm = document.getElementById('linkSearchForm');
  if (searchForm) {
	var inputFields = searchForm.getElementsByTagName('input');
	if (inputFields) {
	  LinkSearch.inputField = inputFields[0];
	  addEvent(LinkSearch.inputField, 'keyup', LinkSearch.searchPage, false);
	  //stop <Enter> from submitting the form...
	  addEvent(searchForm, 'submit', LinkSearch.cancelSubmit, false);
	  //cancel search on page unload
	  addEvent(document, 'unload', LinkSearch.cancelSearch, false);
	  //call searchPage to show the tip
	  LinkSearch.searchPage(null);
	}
  }
  return;
}

