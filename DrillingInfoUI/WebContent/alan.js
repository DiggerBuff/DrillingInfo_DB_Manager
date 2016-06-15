function scanUpdates() {
	document.getElementById("updateButton").style.visibility="hidden";
	document.getElementById("updates").innerHTML = "<option>New plugins</option>";

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
	//alert("Sending request");
	var xmlHttp = new XMLHttpRequest();
	var xmlShutdown = new XMLHttpRequest();
	var url = "http://localhost:9999/replace/";
	var urlShutdown = "http://localhost:9898/transform/server?shutdown=true";

	//alert("Sent request");
	xmlHttp.onreadystatechange = function () {
		//alert("State change");
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			document.getElementById("info1").innerHTML = "Updates downloaded.";
			document.getElementById("updateButton").style.visibility="hidden";
			
			//This is where we likely need to restart the DI_Code server/script
			xmlShutdown.open("POST", urlShutdown, true);
			xmlShutdown.send();
			
			document.getElementById("updates").innerHTML = "<option>--None--</option>";
		}
		else if (xmlHttp.readyState == 4 && xmlHttp.status >= 400) {
			document.getElementById("info1").innerHTML = "Error downloading updates.";
		}
	}

	xmlHttp.open("GET", url, true);
	xmlHttp.send();

	//alert("ended");
}

function getRepairs() {
	//alert("aoenut");
	var xmlHttp = new XMLHttpRequest();
	var url = "http://localhost:9898/transform/repair/";

	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			document.getElementById("repairs").innerHTML = "";
			document.getElementById("info2").innerHTML = "Repair options found.";
			var options = JSON.parse(xmlHttp.responseText);
			addRepairs(options);
			//document.getElementById("updateButton").style.visibility="visible";
		}
		else if (xmlHttp.readyState == 4 && xmlHttp.status == 204) {
			document.getElementById("info2").innerHTML = "No repair options found.";
			document.getElementById("repairs").innerHTML = "<option>--None--</option>";

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
	    el.textContent = option;
	    select.appendChild(el);
	}
}