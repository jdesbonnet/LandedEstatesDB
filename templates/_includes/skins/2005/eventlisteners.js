function changeLinks() {
  if (document.location.href.indexOf('redwood.nuigalway.ie') > -1) {
    //ON REDWOOD ONLY - changes all links to www.nuigalay.ie to redwood.nuigalway.ie	
     if (document.location.href.indexOf('htsearch') < 1) {
  	   var allLinks = document.getElementsByTagName('a');
       for (i=0; i < allLinks.length; i++) {
  	     var linkTarget = allLinks[i].href;
	      allLinks[i].href = linkTarget.replace(/www.nuigalway.ie/, "redwood.nuigalway.ie");
       }
	 }
  }
  //Automatically set the image text content
  if (document.getElementById) {
	var imgTextDiv = document.getElementById('imagecontenttext');
	if (imgTextDiv) {
	  //if there is a sidenav, get the dept name
      var deptNameText = "";
	  var snSubMenu = document.getElementById('snSubMenu');
	  if (snSubMenu) {
//alert('there is a sidenav');
		 var activeLI = snSubMenu.parentNode;
		 if (activeLI && activeLI.className == "activeLI") {
//alert('there is an active li');
			//var deptName = snSubMenu.prevSibling;
			var deptName = activeLI.childNodes[1];
			if (deptName) {
//alert('there is a previous sibling');
			  //is it a link or a text node
			  if (deptName.nodeType == 3) {
//alert('it is a text node');
			    //text node
				deptNameText = deptName.nodeValue;
			  } else {
//alert('it is NOT a text node, its nodetype is ' + deptName.nodeType + ' and its tagname is ' + deptName.tagName.toLowerCase());
			    if (deptName.nodeType == 1 && deptName.tagName.toLowerCase() == "a") {
//alert('it is an A tag');
				  //element node and an <a> tag, so get the text within it
				  if (deptName.firstChild) {
//alert('it has text in it');
				    deptNameText = deptName.firstChild.nodeValue;  
				  }
				}
			  }
			}
		 }
	  }
	  if (deptNameText == "") {
	    //no sidenav, so use default
	    deptNameText = "National University of Ireland, Galway";
	  }
	  //now set the image text
	  deptNameText = deptNameText.replace(/rsaquo/gi, "nbsp");
	  deptNameText = deptNameText.replace(/›/gi, "");
	  var ImgTxt = document.createTextNode(deptNameText);
	  imgTextDiv.appendChild(ImgTxt);
	}
  }
  //Set the 'you are here' pointer
  youAreHere();
  return;
}


//global settings:
var sizeIncrements = 1; 	//number of pixels to increase/decrease the font by
var maxFontSizeIncrements = 5; //number of times to allow the 'increase font' button to be pressed
var cookieDaysToLive = 30;    //number of days before cookies expire.

//system global variables:
var fontMagnificationFactor = 0;
var today = new Date();
var cookieExpires = new Date(today.getTime() + (cookieDaysToLive * 86400000));
openSubMenu = null;
openChild = "";

function suppressErrors() {
  return;
}

function getCookie(name) {
    var start = document.cookie.indexOf(name+"=");
    var len = start+name.length+1;
    if ((!start) && (name != document.cookie.substring(0,name.length))) return null;
    if (start == -1) return null;
    var end = document.cookie.indexOf(";",len);
    if (end == -1) end = document.cookie.length;
    return unescape(document.cookie.substring(len,end));
}


function setCookie(name,value,expires,path,domain,secure) {
    document.cookie = name + "=" +escape(value) +
        ( (expires) ? ";expires=" + cookieExpires.toGMTString() : "") +
        ( (path) ? ";path=" + path : "") + 
        ( (domain) ? ";domain=" + domain : "") +
        ( (secure) ? ";secure" : "");
    return;
}

function checkBrowser() {
	//In windows: Firefox 1 is rv 1.7.5 and Netscape 7 is rv 1.7.2 so the isMinMoz1_6 boolean checks for both of these
	//Firefox on Mac is rv 1.7.12 so the same rule applies
	if (((isMinIE5_5 || isMinOpera8 || isMinMoz1_6 || isMinSafari3 ) && isWin) || ((isMinMoz1_6 || isMinOpera8) && isUnix) || ((isMinSafari1 ||  isMinMoz1_6 || isMinOpera8) && isMac)) {
		return;
	} else {
	    if (document.getElementById) {
		  var warningDiv = document.getElementById("browserWarning");
		  if (warningDiv) {
		    warningDiv.style.display = "block";	
		  }
		}
	}
	return;
}

function setVisible(lyrName,state) {
 if (state == 1) {
   //make visible
   var option = 'visible';
   var position = 'relative';
 } else if(state == 0) {
   //make invisible
   var option = 'hidden';
   var position = 'absolute';
 } else if(state == 2) {
    //switch visibility mode
    if (document.getElementById){
      if (document.getElementById(lyrName).style.visibility) { 
         if (document.getElementById(lyrName).style.visibility == 'hidden') {
        var option = 'visible';
        var position = 'relative';
      } else {
         var option = 'hidden';
         var position = 'absolute';
      }
    }
    } else if (document.layers) {
      if (document.layers[lyrName].visibility == 'hide') { 
        var option = 'visible';
        var position = 'relative';
      }  else {
        var option = 'hidden';
        var position = 'absolute';
      }
    } else if (document.all) {
          if (document.all(lyrName).style.visibility == 'hidden') {
        var option = 'visible';
        var position = 'relative';
      } else {
         var option = 'hidden';
         var position = 'absolute';
      }
   }
 } else {
      var option = 'inherit';
	  var position = 'inherit';
 }
    var element = document.getElementById[lyrName];
    if(document.getElementById && element) {
      document.getElementById(lyrName).style.visibility=option;
      if (state == 2) {
        document.getElementById(lyrName).style.position=position;
      }
    } else if(document.layers) {
	  element = document.layers[lyrName];
	  if (element) {
        document.layers[lyrName].visibility=option;
        if (state == 2) {
          document.layers[lyrName].position=position;
        }
	  }
    } else if(document.all) {
	  element = document.all(lyrName);
	  if (element) {
        document.all(lyrName).style.visibility=option;
        if (state == 2) {
          document.all(lyrName).style.position=position;
        }
	  }
    }
}


function youAreHere() {
  var thisPage = document.location.href;
  var hashPos = thisPage.indexOf('#');
  if (hashPos > -1) {
	  thisPage = thisPage.slice(0, hashPos);
  }
  var sidebar = document.getElementById("leftbar");
  if (sidebar) {
  //Top Level Nav - not needed - CSS highlights the section you are in
  if (!document.getElementsByTagName)
  	return;
	
  //SideNav
  //if container div exists, find the link containing thisPage and then setVisible(parent.firstChild, 1)
  var sideNavDiv = document.getElementById('sidenav');
  var linkFound = false;
  var firstLinkInDir = null;
  //figure out the directory this file is in (in case there is no link in the nav to this file)
  var lastSlashPos = thisPage.lastIndexOf('/');
  var thisDir = thisPage.substr(0,lastSlashPos+1);
  if (sideNavDiv) {
   var sideNavLinks = sideNavDiv.getElementsByTagName('a');
   for (i=0; i < sideNavLinks.length; i++) {
	var linkTarget = sideNavLinks[i].href;
	if (linkTarget.indexOf(thisDir) > -1 && linkTarget.indexOf('#') < 0 && firstLinkInDir == null) {
	  firstLinkInDir = i;	
	}
	if (linkTarget == thisPage) {
	  linkFound = true;
	  //The links parent's (li) first child needs to be made visible (if it's not a category)...
	  if (linkTarget.charAt(linkTarget.length-1) != "#" && sideNavLinks[i].parentNode.parentNode.id != "sidenav") {
		var youAreHereSpan = sideNavLinks[i].parentNode.childNodes[0];
		var displayMode = (isMinIE5) ? "inline" : "block";
		youAreHereSpan.style.display = displayMode;
	  }
	  //if this page appears in a sub-nav, make the sub nav (and its parent if neccessary) visible
      var subMenu = sideNavLinks[i].parentNode.parentNode;
	  if (subMenu.parentNode.parentNode.id && subMenu.parentNode.parentNode.id.indexOf("submenu") > -1) {
 		//show the parent as well
		subMenu.parentNode.parentNode.style.display = "block";
		openChildren(subMenu);
		openSubMenu = subMenu.parentNode.parentNode;
		openChild = subMenu;
	  } else {
		//you only need to show this div
		if (subMenu.id != "snSubMenu") {
		  openSubMenu = subMenu;
		} else {
		  openSubMenu = null;	
		}
	  }
	  subMenu.style.display = "block";
	  //open all this sub menu's sub menu's
//	  if (subMenu.id.indexOf("submenu") > -1) {
//		  openChildren(subMenu);
//	  }
	  redraw_page();
	  i = sideNavLinks.length;
	}
   } //for
   if (!linkFound && firstLinkInDir != null) {
	 //this page was not found in sidenav but another link in the same directory was, so show it
	 //if this page appears in a sub-nav, make the sub nav (and its parent if neccessary) visible
      var subMenu = sideNavLinks[firstLinkInDir].parentNode.parentNode;
	  if (subMenu.parentNode.parentNode.id && subMenu.parentNode.parentNode.id.indexOf("submenu") > -1) {
 		//show the parent as well
		subMenu.parentNode.parentNode.style.display = "block";
		openChildren(subMenu);
		openSubMenu = subMenu.parentNode.parentNode;
		openChild = subMenu;
	  } else {
		//you only need to show this div
		if (subMenu.id != "snSubMenu") {
		  openSubMenu = subMenu;
		} else {
		  openSubMenu = null;	
		}
	  }
	  subMenu.style.display = "block";
	  //open all this sub menu's sub menu's
	  if (subMenu.id.indexOf("submenu") > -1) {
		  openChildren(subMenu);
	  }
//	  if (openSubMenu.id == "snSubMenu") {
//		openSubMenu = null;  
//	  }
	  redraw_page();
   }
  } //if sidenavdiv
  //Important Links
  var importantLinksDiv = document.getElementById('importantlinks');
  if (importantLinksDiv) {
   var importantLinks = importantLinksDiv.getElementsByTagName('a');
   for (i=0; i < importantLinks.length; i++) {
	var linkTarget = importantLinks[i].href;
	linkTarget = linkTarget.replace(/\?.+/, ""); //don't let querystrings stop the page match
	thisPage = thisPage.replace(/\?.+/, "");
    //strip out index.* from both linkTarget and thisPage
	linkTarget = linkTarget.replace(/index\.....?/, "");
	thisPage = thisPage.replace(/index\.....?/, "");
	if (linkTarget.indexOf(thisPage) > -1 && (thisPage != 'http://www.nuigalway.ie/' && thisPage != 'http://redwood.nuigalway.ie/')) {
	  //The youarehere div for each link has id = 'links*******' (where ******* is the first word of the clickable text)
	  //var clickableText = importantLinks[i].childNodes[0].nodeValue;
	  //var words = clickableText.split(' ');
	  //var selectedID = "links" + words[0];
	  //var cmd = "setVisible('links" + words[0] + "', 1);";
	  //eval(cmd);
	  var youAreHereSpan = importantLinks[i].parentNode.childNodes[0];
	  var displayMode = (isMinIE5) ? "inline" : "block";
	  youAreHereSpan.style.display = displayMode;
	  i = importantLinks.length;
	}
   }
  }
  } // end if sidebar
  return;
}

function openChildren(menu) {
	var allChildren = menu.getElementsByTagName('ul');
	if (allChildren) {
		for (i=0; i < allChildren.length; i++) {	
			allChildren[i].style.display = "block";
		}
	}
	return;
}

function closeChildren(menu) {
	var allChildren = menu.getElementsByTagName('ul');
	if (allChildren) {
		for (i=0; i < allChildren.length; i++) {	
				allChildren[i].style.display = "none";
		}
	}
	return;
}

function getMouseCoords(e) {
//get the mouse position on the page
	  var posx = 0;
	  var posy = 0;
	  if (!e) var e = window.event;
	  if (e.pageX || e.pageY) 	{
		posx = e.pageX;
		posy = e.pageY;
	  }
	  else if (e.clientX || e.clientY) 	{
		posx = e.clientX + document.body.scrollLeft
			+ document.documentElement.scrollLeft;
		posy = e.clientY + document.body.scrollTop
			+ document.documentElement.scrollTop;
	  }	
	  // posx and posy contain the mouse position relative to the document
	  return([posx,posy]);
}

function scrollToElementPosition(e, elemID) {
    var offsetTrail = document.getElementById(elemID);
	if (offsetTrail) {
      var offsetLeft = 0;
      var offsetTop = 0;
      while (offsetTrail) {
        offsetLeft += offsetTrail.offsetLeft;
        offsetTop += offsetTrail.offsetTop;
        offsetTrail = offsetTrail.offsetParent;
      }
      if (navigator.userAgent.indexOf("Mac") != -1 && 
        typeof document.body.leftMargin != "undefined") {
        offsetLeft += document.body.leftMargin;
        offsetTop += document.body.topMargin;
      }
   	  //check if element is already on the screen - if so, don't scroll
	  var xy = getMouseCoords(e);
	  //alert('mouseY=' + xy[1] + ', screenY=' + e.screenY + ', linksNewPosistion: ' + offsetTop);
	  if ((xy[1] - e.screenY) > offsetTop) {
	    // if posy is greater than the screen height, the user has scrolled down so scroll up to the newly opened menu
	    if (window && window.scrollTo) {
   	      window.scrollTo(offsetLeft, offsetTop-30);
	    }
	  }
	}
    return;
}

function showHideMenu(e, menu) {
  var action = (menu.style.display == "none" || menu.style.display == "") ? "block" : "none";
//alert('Open Sub Menu is ' + openSubMenu.id);
  if (action == "block") {
	if (menu.parentNode.parentNode == openSubMenu && openSubMenu != null) {
		//If you are opening a subnav within this subnav or a top level subnav
//debug
//var divID = menu.parentNode.parentNode.id;
//alert('This Menu is ' + menu.id);
//alert('Closing children of ' + divID); 
//end debug
		closeChildren(menu.parentNode.parentNode);
		menu.style.display = "block";
		openChild = menu;
	} else {
	  //if you are opening a different subnav
  	    if (openSubMenu && openSubMenu.id != "sidenav") {
		  //close the sub nav that was open already
		  closeChildren(openSubMenu);
		  openChild = "";
		  openSubMenu.style.display = "none";
	    }
  	    menu.style.display = "block";
		openSubMenu = menu;
		//openChildren(menu);
		scrollToElementPosition(e, menu.id);
	}
  } else {
		//you are closing the first level subnav
		closeChildren(menu);
		menu.style.display = "none";
  }
  return;
}

function toggleSubMenus(e) {
  var thisLink = window.event ? window.event.srcElement : e ? e.target : null;
  if (!thisLink) return;
  if (thisLink.nodeName.toLowerCase() != "a")
    thisLink = thisLink.parentNode;
  var elementsID = thisLink.parentNode.childNodes[0].id + "submenu";
  var subMenuID = document.getElementById(elementsID);
  if (thisLink.parentNode.childNodes[0].className && thisLink.parentNode.childNodes[0].className == "active") {
	//it's the top category - not collapsable
  } else {
    showHideMenu(e, subMenuID);
  }
  //cancel the click
  if (window.event) {
	  window.event.cancelBubble = true;
	  window.event.returnValue = false;
  }
  if (e && e.stopPropagation) {
	  e.stopPropagation();
	  e.preventDefault();
  }
  redraw_page();
  return false;
}

function redraw_page() {
  //Firefox, Netscape 7 and Safari 2 don't render the xhtml correctly after clicking on a link in the dhtml menu - so change the   
  //stylesheet (to the current one) to force the browser to render the screen properly
  //if ((isWin && isMinIE5 && !isMinIE5_5) || (isMac && isMinSafari2)) {
  if ((isMinMoz1_6)) {
	usersSelectedStyle = getCookie("style");
    if (usersSelectedStyle == null) {
    	usersSelectedStyle = 'Default';
	}
	changeStyle(usersSelectedStyle);
  }
  //IE 5.01 doesn't support the mouseovers properly, so load a style sheet with them removed
  if (isWin && isMinIE5 && !isMinIE5_5) {
	changeStyle("IE501");  
  }
  return;
}

function clearSearchBox(e) {
  if (document.userDefined.searchBox.value == 'Search Web site' || document.userDefined.searchBox.value == 'Cuardaigh') { 
	document.userDefined.searchBox.value='';
  }
  return;
}

function changeTextSizeRelatively(extraPixels) {
  //FOR ALL ELEMENTS WITH A PARTICULAR ID, ADD OR SUBTRACT A FEW PIXELS
  if (!document.getElementsByTagName)
    return;
  var body = document.getElementsByTagName('body')[0];
  var tags = body.getElementsByTagName('*');
  var browserSupportsAttributesDotID = false;
  var bSaveCookie = true;
  var reduceFont = document.getElementById("reduceFont");
  var increaseFont = document.getElementById("increaseFont");
  for (i = 0; i < tags.length; i++) {
   if (tags[i].tagName != "!" && tags[i].attributes.id) {
	browserSupportsAttributesDotID = true;
    var thisID = tags[i].attributes.id.value.substr(0,10);
	if (thisID == 'changeable') {
      if (tags[i].currentStyle) {
        var currentSize = parseInt(tags[i].currentStyle.fontSize.replace(/p./, ""));
      } else {
        var currentSize = parseInt(tags[i].style.fontSize.replace(/p./, ""));
      }
      if (isNaN(currentSize)) {
        //This only happens for elements with no font-size specified in the style sheet
        currentSize = 12;
      }
      
	  if (fontMagnificationFactor == 0) {
		reduceFont.style.color = "#bbbbbb";
      }
	  if (fontMagnificationFactor < 0) {
		fontMagnificationFactor = 0;
		reduceFont.style.color = "#bbbbbb";
		extraPixels = 0;
	  }
      if (fontMagnificationFactor == 1) {
		reduceFont.style.color = "#000000";
      }
	  if (fontMagnificationFactor > maxFontSizeIncrements) {
		fontMagnificationFactor = maxFontSizeIncrements;
		increaseFont.style.color = "#bbbbbb";
		extraPixels = 0;
	  }
      if (fontMagnificationFactor == maxFontSizeIncrements) {
		increaseFont.style.color = "#bbbbbb";
      }
      if (fontMagnificationFactor == maxFontSizeIncrements-1) {
		increaseFont.style.color = "#000000";
      }
      tags[i].style.fontSize = currentSize + extraPixels + "px";
    }
   }
  }
  if (!browserSupportsAttributesDotID) {
	alert("Sorry!\n\nYour browser does not support the selective changing of font sizes.\n\nMost browsers allow you to zoom the entire page.\n\nKonquerer or Safari Users: Hold down the 'Ctrl' or 'Apple' key and then press '+' to increase the font size or '-' to reduce it.\n\nOpera Users: Press the '+' and '-' buttons on your number pad.");
	setVisible('changeFont', 0);
	bSaveCookie = false;
  }
  if (bSaveCookie) {
    setCookie("fontSize", fontMagnificationFactor, cookieExpires, "/", "nuigalway.ie");
    //check that the cookie was saved - if not, warn the user that font size settings wont be remembered
    savedCookie = getCookie("fontSize");
    //if (savedCookie == null) {
      //alert('WARNING:\n\nYour browser does not accept cookies.  Cookies are required by this Web site to remember your preferred display settings.\n\nIf you do not enable cookies for this site, the display settings will return to their default when you click on a link or return to this site later.');
    //}
  }
  return;
}


function fontSizeListener(e) {	
  if (isMinIE5) {
	var buttonClicked = e.srcElement.id;
  } else {
	var buttonClicked = e.target.id;
  }
  if (buttonClicked == "increaseFontButton") {
    fontMagnificationFactor++;
	changeTextSizeRelatively(sizeIncrements);
  } else {
    fontMagnificationFactor--; 
	changeTextSizeRelatively(sizeIncrements * -1);
  }
  return;
}


function changeCss(e) {
  if (isMinIE5) {
	var buttonClicked = e.srcElement.id;
  } else {
	var buttonClicked = e.target.id;
  }
  if (isMac && e.target && e.target.nodeType && e.target.nodeType == 3) { //circumvent Safari bug
  	buttonClicked = e.target.parentNode.id;
  }
  if (buttonClicked == "cssColour") {
    changeStyle('Default');
    setCookie('style', 'Default', cookieExpires, "/", "nuigalway.ie");
  } else {
 	changeStyle('highContrast');
 	setCookie('style', 'highContrast', cookieExpires, "/", "nuigalway.ie");
  }
  return;
}

function logoRightClickListener(e) {	
  if (e && e.button == 2) {
    //its a right-click
	if (e.preventDefault) {
	  e.preventDefault();
	}
	if (e.stopPropagation) {
	  e.stopPropagation();
	}
	if (window.event && window.event.returnValue) {
	  window.event.returnValue = false;
	}
	if (window.event && window.event.cancelBubble) {
	  window.event.cancelBubble = true;
	}
	if (confirm('Do you wish to download the official NUI Galway Logo?')) {
      document.location.href="/vp/logo.html";
    }
  }
  return false;
}

function getAllSheets() {
	if( !window.ScriptEngine && navigator.__ice_version ) { return document.styleSheets; }
	if( document.getElementsByTagName ) { var Lt = document.getElementsByTagName('link'), St = document.getElementsByTagName('style');
	} else if( document.styleSheets && document.all ) { var Lt = document.all.tags('LINK'), St = document.all.tags('STYLE');
	} else { return []; } for( var x = 0, os = []; Lt[x]; x++ ) {
		var rel = Lt[x].rel ? Lt[x].rel : Lt[x].getAttribute ? Lt[x].getAttribute('rel') : '';
		if( typeof( rel ) == 'string' && rel.toLowerCase().indexOf('style') + 1 ) { os[os.length] = Lt[x]; }
	} for( var x = 0; St[x]; x++ ) { os[os.length] = St[x]; } return os;
}

function changeStyle() {
	window.userHasChosen = window.MWJss;
	for( var x = 0, ss = getAllSheets(); ss[x]; x++ ) {
		if( ss[x].title ) { 
		  ss[x].disabled = true; 
		}
		for( var y = 0; y < arguments.length; y++ ) { 
		  if( ss[x].title == arguments[y] ) { 
		    ss[x].disabled = false; 
	      } 
		}
	}
}

function hideStyleToggle() {
  var changeStyleText = document.getElementById('changeStyle');
  changeStyleText.style.display = "none";
  return;
}

function hideTextZoom() {
  var changeFontText = document.getElementById('changeFont');
  changeFontText.style.display = "none";  
  return;
}

function editThisPage(e) {
  var itemClicked = (e.srcElement) ? e.srcElement.id : e.target.id;
	if (itemClicked != "qlheader" && itemClicked != "qlimage") {
    setTimeout('hideQuickLinks();', 200);
  }
  //if the user right-clicks while holding down shift, edit this page.  Otherwise, do nothing
  var shiftKeyPressed = (window.event) ? window.event.shiftKey : e.shiftKey;
  var buttonClicked = (window.event) ? window.event.button : e.button;
  rightButtonClicked = (buttonClicked == 2) ? true: false;
  if (shiftKeyPressed && rightButtonClicked) {
    //stop default action from right-click
	if (window.event) {
	  if (window.event.cancelBubble) window.event.cancelBubble = true;
	  if (window.event.returnValue) window.event.returnValue = false;
	}
	if (e) {
	  if (e.stopPropagation) e.stopPropagation();
	  if (e.preventDefault) e.preventDefault();
	}
	//launch the editor in a new window
	var filePath = document.location.href.replace(/http:.+nuigalway.ie/i, "/www");
	if (document.location.href.indexOf("redwood.nuigalway.ie/") > 0) {
	  var fileToOpen = "http://redwood.nuigalway.ie/cms/edit.php?file=" + filePath;
	} else {
	  var fileToOpen = "http://www.nuigalway.ie/cms/edit.php?file=" + filePath;
	}
	if (document.all || document.getElementByID) {
         var xMax = screen.width, yMax = screen.height;
    } else {
       if (document.layers) {
           var xMax = window.outerWidth, yMax = window.outerHeight;
       } else {
           var xMax = 640, yMax=480;
       }
    }
    var xOffset = (xMax - 600)/2;
    var yOffset = (yMax - 400)/2;
    CMSwindow = window.open(fileToOpen,'CMSpopup','toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no,width=' + xMax + ',height=' + yMax + ',screenX='+xOffset+',screenY='+yOffset+',top='+yOffset+',left='+xOffset+'');
	setTimeout('CMSwindow.focus();', 1000);
  }
  return;  
}

function hideQuickLinks() {
	var quickLinkUL = document.getElementById('QLinkUL'); 
	if (quickLinkUL) {
	 quickLinkUL.style.display = "none";
	 var QLinkHeader = document.getElementById('qlheader');
	 if (QLinkHeader) {
		 var QLImages = QLinkHeader.getElementsByTagName('img');
		 QLImages[0].src = "http://www.nuigalway.ie/images/arrows/arrow_divdropdown_lightmaroon_closed.png";
	 }
	}
	return;
}

function showHideQuickLinks(e) {
	var quickLinkUL = document.getElementById('QLinkUL'); 
	if (quickLinkUL) {
	 quickLinkUL.style.display = (quickLinkUL.style.display == "none" || quickLinkUL.style.display == "") ? "block" : "none";
	 var QLinkHeader = document.getElementById('qlheader');
	 if (QLinkHeader) {
		 var QLImages = QLinkHeader.getElementsByTagName('img');
		 QLImages[0].src = (QLImages[0].src == "http://www.nuigalway.ie/images/arrows/arrow_divdropdown_lightmaroon_closed.png") ? "http://www.nuigalway.ie/images/arrows/arrow_divdropdown_lightmaroon_open.png" : "http://www.nuigalway.ie/images/arrows/arrow_divdropdown_lightmaroon_closed.png";
	 }
	}
}

function addListeners(e) {
  if (!document.getElementById || !document.getElementsByTagName) {
    //browser doesn't support DHTML required for text zoom and CSS switching so hide the functionality from the user...
    setVisible('changeFont', 0);
    return;
  }
  //Text Size needs to be set to that of the previous page - check the cookie and resize before setting up the listeners...
  usersSelectedFontSize = getCookie("fontSize");
  if (usersSelectedFontSize != null) {
    fontMagnificationFactor = parseInt(usersSelectedFontSize);
    if (fontMagnificationFactor > 0) {
      changeTextSizeRelatively(sizeIncrements * fontMagnificationFactor);
    } else {
		var reduceFont = document.getElementById("reduceFont");
		reduceFont.style.color = "#bbbbbb";
	}
  }
  //Selected CSS must be preserved - check the cookie, apply the style and change the select box...
  usersSelectedStyle = getCookie("style");
  if (usersSelectedStyle != null && usersSelectedStyle != 'Default') {
	changeStyle(usersSelectedStyle);
  }
  
  // set up the event listeners here
  var biggerFontButton = document.getElementById('increaseFontButton');
  addEvent(biggerFontButton, 'click', fontSizeListener, false);
  var smallerFontButton = document.getElementById('reduceFontButton');
  addEvent(smallerFontButton, 'click', fontSizeListener, false);
  addEvent(window, 'error', suppressErrors, false);
  var colourButton = document.getElementById('cssColour');
  addEvent(colourButton, 'click', changeCss, false);
  var hiContrastButton = document.getElementById('cssHiContrast');
  addEvent(hiContrastButton, 'click', changeCss, false);
  if (document.userDefined && document.userDefined.searchBox) {
    oSearchBox = document.userDefined.searchBox;
    addEvent(oSearchBox, 'focus', clearSearchBox, false);
  }
  var logoDiv = document.getElementById('logo');
  if (logoDiv) {
    addEvent(logoDiv, 'mousedown', logoRightClickListener, false);
  }
  addEvent(document, 'mousedown', editThisPage, false);
  //dropdown quick links
  var quickLinkDiv = document.getElementById('quicklinksdivdropdown');
  if (quickLinkDiv) {
	var quickLinkTitle = document.getElementById('qlheader'); //quickLinkDiv.firstChild.firstChild;
	if (quickLinkTitle) {
		addEvent(quickLinkTitle, 'mousedown', showHideQuickLinks, false);
		//showHideQuickLinks(); //hide the quicklinks on load
	}
  }
    
  checkBrowser();
  //BROWSER SPECIFIC STYLESHEET MODS...
  //sidenav sub-menu display toggle event listeners
  if ((isWin && isMinIE5 && !isMinIE5_5)) {
	//if IE 5.01 - disable a:hovers on sidenav (because it can't handle them properly)
	changeStyle("IE501");
	hideTextZoom();
	hideStyleToggle();
  }
  if (isMac && isMinIE5) {
	//IE 5+ on Mac doesn't support the style and font changes so suppress them
	hideTextZoom();
	hideStyleToggle();
  }
  //END BROWSER SPECIFIC MODS
    var sideNavDiv = document.getElementById('sidenav');
    if (sideNavDiv) {
     var sideNavLinks = sideNavDiv.getElementsByTagName('a');
     for (i=0; i < sideNavLinks.length; i++) {
	  var linkTarget = sideNavLinks[i].href;
	  if (linkTarget.indexOf("#") == linkTarget.length-1) {
	    addEvent(sideNavLinks[i], 'click', toggleSubMenus, false);
	  }
     }
    }
	
	LinkSearch.init(); //in linkSearch.js

  //If IE, schedule an event cache flush on page unload
  var testElement = document.getElementById('cssColour');
  if (testElement.attachEvent) {
	addEvent(window, 'unload', EventCache.flush, false);
  }
  //if it is IE pre version 7, reposition the quick links div
  if ((isWin && isMinIE5 && !isMinIE7)) {
    var QLDiv = document.getElementById('quicklinksdivdropdown');
	if (QLDiv) {
		QLDiv.style.top = "5px";
		QLDiv.style.margin = "5px 0px 0px 115px";
	}
  }

  //last thing: insert random images into page
  var rightbarImageHolder = document.getElementById("rightbarimage");
  if (rightbarImageHolder) {
	randomSmallImage();  
  }
  var imageHolder = document.getElementById("image");
  if (imageHolder) {
    randomImage();
  }
  return;
}

function addEvent(elm, evType, fn, useCapture) {
  // cross-browser event handling for IE5+, NS6+ and Mozilla/Gecko
  if (elm.addEventListener) {
	    elm.addEventListener(evType, fn, useCapture);
    return true;
  } else if (elm.attachEvent) {
    var r = elm.attachEvent('on' + evType, fn);
	EventCache.add(elm, evType, fn);
    return r;
  } else {
    elm['on' + evType] = fn;
  }
  return;
}

addEvent(window, 'load', changeLinks, false);
addEvent(window, 'load', addListeners, false);