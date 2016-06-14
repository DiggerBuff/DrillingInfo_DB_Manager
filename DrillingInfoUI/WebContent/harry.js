function scanUpdates() {
	alert("Scanning");
		
	var xhr = new XMLHttpRequest();

	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4 && xhr.status == 200) {
	
			var jars = JSON.parse(xhr.responseText);
			
			var select = document.getElementById("oldJars"); 

			for(var i = 0; i < jars.length; i++) {
			    var jar = jars[i];
			    var el = document.createElement("option");
			    el.textContent = jar.name;
			    el.value = jar.name;
			    select.appendChild(el);						
			}

			document.getElementById("updateButton").style.visibility = "visible";

		}
	}

	xhr.open('GET', "http://138.67.186.221:9898", true);
	xhr.send();
}