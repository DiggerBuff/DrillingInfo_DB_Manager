(function() { // this is a closure that we wrap our JavaScript in

	// this is our MODULE declaration line
	// is is called "store" and has no dependencies
	var app = angular.module('interface', []);
	

	// this is our CONTROLLER named "StoreController" it must be capitalized
	// the code in function here is what will be executed when StoreController
	// is called
	app.controller('InterfaceController', function() {
		// set the gems array as a property of our CONTROLLER
		// the property we name products
		this.panels = panelSet;
	});

	var panelSet = [ 
	{
		name : 'Panel1',
		description : 'The first panel deals with scanning for updates. Is there a Transform/plugins folder? Great - let’s look at the jars in it and see if any newer versions (or perhaps brand new jars) are available.  Either display to the user that they are fully up-to-date or they need to download and install newer jars. Give them a button of some form to do that. This would replace the older version jars in Transform/plugins folder.',
		canShow : true
		
	}, 
	{
		name : 'Panel2',
		description : 'The second panel deals with repairs. You can use the get methods exposed in the DI service (localhost:9898/transform/repair) to find out what repairs are available. What you do next is up to you, you could present some form of a UI that allows the user to see the list and run the detect method by calling that REST api (post to /transform/repair/repair name/detect). This will return a true or false. If you get a true back then you should expose an option to run the repair for the user (post to /transform/repair/repair name/repair).',
		canShow : true
		
	},
	{
		name : 'Panel3',
		description : 'The third panel would deal with system control. A way to test an echo command or shutdown the services. ',
		canShow : true
		
	}];
})();