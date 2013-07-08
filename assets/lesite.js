
function getMapPoints ()  {
	var o = map.getBoundsLatLon();
	map.removeMarkersAll();
	var url = "mapquery.jsp?latmin=" + o.LatMin + "&latmax=" + o.LatMax 
	+ "&lonmin=" + o.LonMin + "&lonmax=" + o.LonMax;
	YAHOO.util.Connect.asyncRequest('GET', url, 
		{ success: function(o) {
			// Trusted JSON source, use eval for performance
			var points = eval ("(" + o.responseText + ")");
			drawPoints(points);
		},
		failure: function (o) {alert (o.statusText);}
		}
	); 
}

function drawPoints (points) {
	var ypoints = [];
	for (var i = 0; i < points.length; i++) {
		ypoints[i] =  new YGeoPoint(points[i].lat, points[i].lon);
		map.addOverlay(createMarker(points[i]));
	}
}

function createMarker(point) {
	var ypoint = new YGeoPoint(point.lat,point.lon);
    var marker = new YMarker(ypoint,markerImage); 
    marker.setSmartWindowColor("blue");
    var bubbleText = "<a href=\"property-show.jsp?id=" 
    	+ point.id + "\">" + point.name + "</a>";
    marker.addAutoExpand(bubbleText);
    YEvent.Capture(marker,EventsList.MouseClick, function() { 
    	marker.openSmartWindow(bubbleText) 
    }); 
    return marker; 
}

function showObject (o) {
	var msg="";
	for (i in o) {
		msg += i + ";\n";
	}
	alert (msg);
}



/**
 * Draw map for show house page. Use by adding this function to the window
 * onload handler.
 * @param lat
 * @param lon
 * @return
 */
function drawHouseMap(lat,lon) {

	var myPoint = new YGeoPoint(lat,lon);
	// Add controls
	map.addPanControl(); 
	map.addZoomLong(); 
	map.addTypeControl();

	map.drawZoomAndCenter(myPoint, 3);
	var marker = new YMarker(myPoint);
	marker.addLabel("X");
	map.addOverlay(marker);
}

/**
 * Draw map for map page. Use by adding this function to the window
 * onload handler.
 * @return
 */
function drawMapPageMap () {
	YEvent.Capture(map, "endMapDraw", getMapPoints);
	YEvent.Capture(map, "endPan", getMapPoints);
	map.addZoomLong();
	map.drawZoomAndCenter (new YGeoPoint(53.3,-9), 8);
}


