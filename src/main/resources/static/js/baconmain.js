// Waits for document to load before running Script
$(document).ready(() => {
	// Stores all important references for both boxes
	const $firstActor = $("#topBox");
	const $secondActor = $("#botBox");
	const $pathError = $("#pathError");
	const $pathResults = $("#pathResults");
	// Places clickhandler on the path generation button
	document.getElementById("pathbutton").addEventListener("click",function() {
		// Constructs object for POST request that stores input String
		const postParameters = {
			firstActor: $firstActor.val(),
			secondActor: $secondActor.val()
		};
		// Sends JSON request
		$.post("/path", postParameters, responseJSON => {
			// Clears existing pathError content
			$pathError.html("");
			// Stores response object
			const responseObject = JSON.parse(responseJSON);
			// Check for case of error message
			if (responseObject.pathError.length > 0) {
				// Set the pathError appropriately
				$pathError.html("Error in finding path: " + responseObject.pathError);
			} 
			// Otherwise, populate the path below
			else {
				// Initializes String to be used as innerHTML
				let endHTML = "";
				// Stores the paths 
				let paths = responseObject.paths;
				// Iterates through each row to be added
				for (let i = 0, len = paths.length; i < len; i++) {
					// Stores the given row to be added
					let firstActorName = paths[i][0];
					let firstActorURL = paths[i][1];
					let secondActorName = paths[i][2];
					let secondActorURL = paths[i][3];
					let filmName = paths[i][4];
					let filmURL = paths[i][5];
					// Stores the respective element HTML
					let firstActorLink = "<a href=/bacon/actor/" + firstActorURL + ">" + firstActorName + "</a>";
					let secondActorLink = "<a href=/bacon/actor/" + secondActorURL + ">" + secondActorName + "</a>";
					let filmLink = "<a href=/bacon/film/" + filmURL + ">" + filmName + "</a>";
					// Adds HTML links to the endHTML variable
					endHTML += firstActorLink + " -> " + secondActorLink + " : " + filmLink + "<br>";
				}
				// Store all links in given div
				$pathResults.html(endHTML);
			}
		});
	});

});