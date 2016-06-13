(function() { // this is a closure that we wrap our JavaScript in

	// this is our MODULE declaration line
	// it is called "store" and has no dependencies
	var app = angular.module('myModule', []);
	
	
	// this is our CONTROLLER named "MyController" it must be capitalized
	// the code in function here is what will be executed when MyController
	// is called
	
	// we are registering the controller with the module by passing the function
	app.controller("MyController", function($scope) {
		// set the gems array as a property of our CONTROLLER
		// the property we name products
		this.panels = panelSet;
	});
		
		
	var panelSet = [ 
	{
		name : 'Scan For Updates/Replace (Our Service)',
		description : 'The first panel deals with scanning for updates. Is there a Transform/plugins folder? \
					   Great - let’s look at the jars in it and see if any newer versions (or perhaps brand new jars) are available. \   Either display to the user that they are fully up-to-date or they need to download and install newer jars.  \
						Give them a button of some form to do that. This would replace the older version jars in Transform/plugins folder.',
		buttonID : 'Button One',
		buttonName : 'One' ,
		buttonLabel : 'First',
		doStuff : function() {
			alert('hello1');
						
			var xhr = new XMLHttpRequest();
			
			xhr.open('GET', "http://138.67.186.221:50986/", true);
			xhr.send();
			
			xhr.addEventListener("readystatechange", processRequest, false);
			
			xhr.onreadystatechange = processRequest;
			
			function processRequest(e) 
				{
				
					if (xhr.readyState == 4 && xhr.status == 200) 
					{
						var response = JSON.parse(xhr.responseText);
						alert(response.ip);
					}
				}
			}, 
		canShow : true
		
		
	}, 
	{
		name : 'Detect/Repair (DI Service)',
		description : 'The second panel deals with repairs. You can use the get methods exposed in the DI service (localhost:9898/transform/repair) \
						to find out what repairs are available. What you do next is up to you, you could present some form of a UI that allows the \
						user to see the list and run the detect method by calling that REST api (post to /transform/repair/repair name/detect). \
						This will return a true or false. If you get a true back then you should expose an option to run the repair for the user \
						(post to /transform/repair/repair name/repair).',
		buttonID : 'Button Two',
		buttonName : 'Two' ,
		buttonLabel : 'Second',
		doStuff : function() {
			alert('hello2');
			
			var xhr = new XMLHttpRequest();
			
			xhr.open('GET', "http://138.67.186.220:54591/", true);
			xhr.send();
			
			xhr.addEventListener("readystatechange", processRequest, false);
			
			xhr.onreadystatechange = processRequest;
			
			function processRequest(e) 
				{
				
					if (xhr.readyState == 4 && xhr.status == 200) 
					{
						var response = JSON.parse(xhr.responseText);
						alert(response.ip);
					}
				}
			}, 
		canShow : true
		
	},
	{
		name : 'System Control',
		description : 'The third panel would deal with system control. A way to test an echo command or shutdown the services. ',
		buttonID : 'Button Three',
		buttonName : 'Three' ,
		buttonLabel : 'Third',
		doStuff : function() {
			alert('hello3');
			
			var xhr = new XMLHttpRequest();
			
			xhr.open('GET', "http://138.67.186.221:50986/", true);
			xhr.send();
			
			xhr.addEventListener("readystatechange", processRequest, false);
			
			xhr.onreadystatechange = processRequest;
			
			function processRequest(e) 
				{
				
					if (xhr.readyState == 4 && xhr.status == 200) 
					{
						var response = JSON.parse(xhr.responseText);
						alert(response.ip);
					}
				}
			
			} , 
		canShow : true
		
	}];
	
	$scope.panelSet = panelSet;
		
})();