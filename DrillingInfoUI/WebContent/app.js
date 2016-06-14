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
	
	$scope.panelSet = panelSet;
		
})();