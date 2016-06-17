function scanUpdates() {
	document.getElementById("updateButton").style.visibility="hidden";
	document.getElementById("updates").innerHTML = "<option>New Plugins</option>";

	var xmlHttp = new XMLHttpRequest();


	var url = "http://localhost:9999/";

	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			document.getElementById("info1").innerHTML = "Updates found.";
			var jars = JSON.parse(xmlHttp.responseText);
			addUpdates(jars);
			document.getElementById("updateButton").style.visibility="visible";
		}
		else if (xmlHttp.readyState == 4 && xmlHttp.status == 204) {
			document.getElementById("info1").innerHTML = "No updates found.";
			document.getElementById("updates").innerHTML = "<option>--None--</option>";

		}
	}

	xmlHttp.open("GET", url, true);
	xmlHttp.send();
	document.getElementById("info1").innerHTML = "Scanning for updates...";
}

function addUpdates(jars) {
	document.getElementById("updates").innerHTML = "<option>--All Updates--</option>";
	var select = document.getElementById("updates"); 
	for(var i = 0; i < jars.length; i++) 
	{
	    var jar = jars[i];
	    var el = document.createElement("option");
	    el.textContent = jar.name;
	    el.value = jar.name;
	    select.appendChild(el);
	}
}

function getUpdates() {
	var xmlShutdown = new XMLHttpRequest();
	var urlShutdown = "http://localhost:9898/transform/server?shutdown=true";

	xmlShutdown.onreadystatechange = function () {
		if (xmlShutdown.readyState == 4 && xmlShutdown.status == 200) {
			setTimeout(getUpdatesPart2, 6000); /* Needed this wait because the DIServer takes 5 seconds to shut down*/
		}
	}

	xmlShutdown.open("POST", urlShutdown, true);
	xmlShutdown.send();
}

function getUpdatesPart2() {
	var xmlHttp = new XMLHttpRequest();
	var url = "http://localhost:9999/replace/";

	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			document.getElementById("info1").innerHTML = "Updates downloaded.";
			document.getElementById("updateButton").style.visibility="hidden";
			document.getElementById("repairs").innerHTML = "<option>--None--</option>";
			document.getElementById("info2").innerHTML = "Finding new repairs.";
			startDIServer();
			
			document.getElementById("updates").innerHTML = "<option>--None--</option>";
		}
		else if (xmlHttp.readyState == 4 && xmlHttp.status >= 400) {
			document.getElementById("info1").innerHTML = "Error downloading updates.";
		}
	}

	xmlHttp.open("GET", url, true);
	xmlHttp.send();
}

function getRepairs() {
	var xmlHttp = new XMLHttpRequest();
	var url = "http://localhost:9898/transform/repair/";

	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			document.getElementById("repairs").innerHTML = "<option value='0'>--None--</option>";
			document.getElementById("info2").innerHTML = "Repair options found.";
			var options = JSON.parse(xmlHttp.responseText);
			addRepairs(options);
			//document.getElementById("updateButton").style.visibility="visible";
		}
		else if (xmlHttp.readyState == 4 && xmlHttp.status == 204) {
			document.getElementById("info2").innerHTML = "No repair options found.";
			document.getElementById("repairs").innerHTML = "<option value='0'>--None--</option>";

		}
	}

	xmlHttp.open("GET", url, true);
	xmlHttp.send();
}

function addRepairs(options) {
	var select = document.getElementById("repairs");
	for(var i = 0; i < options.length; i++) 
	{
	    var option = options[i];
	    var el = document.createElement("option");
	    el.id
	    el.value = i + 1;
	    el.textContent = option;
	    select.appendChild(el);
	}
}

function startDIServer() {
	var xmlHttp = new XMLHttpRequest();

	var url = "http://localhost:9999/system/restart";

	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			//alert("DIServer started");
		}
		else if (xmlHttp.readyState == 4 && xmlHttp.status == 500) {
			var options = JSON.parse(xmlHttp.responseText);
			document.getElementById("info1").innerHTML = options;
		}
	}

	xmlHttp.open("GET", url, true);
	xmlHttp.send();
}

function shutdownDIServer() {
	var xmlShutdown = new XMLHttpRequest();
	var urlShutdown = "http://localhost:9898/transform/server?shutdown=true";

	xmlShutdown.onreadystatechange = function () {
		if (xmlShutdown.readyState == 4 && xmlShutdown.status == 200) {
			setTimeout(getUpdatesPart2, 6000); /* Needed this wait because the DIServer takes 5 seconds to shut down*/
		}
	}

	xmlShutdown.open("POST", urlShutdown, true);
	xmlShutdown.send();
}

function loadDetectRepar(value) {
	if (value != 0) {
		document.getElementById("detect").style.visibility="visible";
		document.getElementById("repair").style.visibility="visible";
	}
	else {
		document.getElementById("detect").style.visibility="hidden";
		document.getElementById("repair").style.visibility="hidden";
	}
}

function detect() {
	var xmlHttp = new XMLHttpRequest();
	var repairs = document.getElementById("repairs");
	var text = repairs.options[repairs.selectedIndex].text;

	var url = "http://localhost:9898/transform/repair/" + text + "/detect";
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			var options = JSON.parse(xmlHttp.responseText);
			alert(options);
		}
		else if (xmlHttp.readyState == 4 && xmlHttp.status == 404) {
			document.getElementById("info2").innerHTML = "Detect not found";
		}
	}

	xmlHttp.open("POST", url, true);
	xmlHttp.send();
}

function repair() {
	var xmlHttp = new XMLHttpRequest();
	var repairs = document.getElementById("repairs");
	var text = repairs.options[repairs.selectedIndex].text;

	var url = "http://localhost:9898/transform/repair/" + text +"/repair";
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			var options = JSON.parse(xmlHttp.responseText);
			alert(options);
		}
		else if (xmlHttp.readyState == 4 && xmlHttp.status == 404) {
			document.getElementById("info2").innerHTML = "Repair not found";
		}
	}

	xmlHttp.open("POST", url, true);
	xmlHttp.send();
}

function shutdown() {
	shutdownDIServer();
	var xmlHttp = new XMLHttpRequest();

	var url = "http://localhost:9999/system/shutdown";

	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {

		}
		else if (xmlHttp.readyState == 4 && xmlHttp.status == 500) {
			var options = JSON.parse(xmlHttp.responseText);
			document.getElementById("info1").innerHTML = options;
		}
	}

	xmlHttp.open("GET", url, true);
	xmlHttp.send();
}