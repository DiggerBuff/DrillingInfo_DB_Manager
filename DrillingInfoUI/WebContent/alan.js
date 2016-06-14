function scanUpdates() {
	document.getElementById("updateButton").style.visibility="hidden";
	document.getElementById("updates").innerHTML = "<option>--None--</option>";

	var xmlHttp = new XMLHttpRequest();
	var url = "http://138.67.186.221:9898/";

	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			document.getElementById("info1").innerHTML = "Updates found.";
			var jars = JSON.parse(xmlHttp.responseText);
			addUpdates(jars);
			document.getElementById("updateButton").style.visibility="visible";
		}
		else if (xmlHttp.readyState == 4 && xmlHttp.status == 204) {
			document.getElementById("info1").innerHTML = "No updates found.";
		}
	}

	xmlHttp.open("GET", url, true);
	xmlHttp.send();
	document.getElementById("info1").innerHTML = "Scanning for updates..."
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
	//alert("Sending request");
	var xmlHttp = new XMLHttpRequest();
	var url = "http://138.67.186.221:9898/replace/";

	//alert("Sent request");
	xmlHttp.onreadystatechange = function () {
		//alert("State change");
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			document.getElementById("info1").innerHTML = "Updates downloaded.";
			document.getElementById("updateButton").style.visibility="hidden";
		}
		else if (xmlHttp.readyState == 4 && xmlHttp.status >= 400) {
			document.getElementById("info1").innerHTML = "Error downloading updates.";
		}
	}

	xmlHttp.open("GET", url, true);
	xmlHttp.send();

	//alert("ended");
}