// INPUT: Array of suggestions
// OUTPUT: Nothing
// ACTION: Clears all suggestion texts
function clearSuggestions(suggestions) {
	for (let i = 0; i < suggestions.length; i++) {
		suggestions[i].html("");
	}
}
// Waits for document to load before running Script
$(document).ready(() => {
	// Stores all important references for both boxes
	const $firstActor = $("#topBox");
	const $secondActor = $("#botBox");
	const $pathError = $("#pathError");
	const $pathResults = $("#pathResults");
	// Stores all suggestions
	const topSuggestions = [$("#1Trow"), $("#2Trow"), $("#3Trow"), $("#4Trow"), $("#5Trow")];
	const botSuggestions = [$("#1Brow"), $("#2Brow"), $("#3Brow"), $("#4Brow"), $("#5Brow")];
	// Places autocorrect on the first actor box
	$("#topBox").keyup(event => {
		// Clears all suggestions, if appropriate
		if ($firstActor.val().length === 0) {
			clearSuggestions(topSuggestions);
		} else {
			const postParameters = {boxType: "0", currentStr: $firstActor.val()};
			// Passes new String value to the AcHandler
			$.post("/actorcorrect", postParameters, responseJSON => {
				// Stores response object
				const responseObject = JSON.parse(responseJSON);
				// Updates all row values accordingly
				topSuggestions[0].html(responseObject.OneTrow);
				topSuggestions[1].html(responseObject.TwoTrow);
				topSuggestions[2].html(responseObject.ThreeTrow);
				topSuggestions[3].html(responseObject.FourTrow);
				topSuggestions[4].html(responseObject.FiveTrow);
				// Updates the error value, too
				$pathError.html(responseObject.pathError);
			});
		}
	});
	// Places autocorrect on the second actor box
	$("#botBox").keyup(event => {
		// Clears all suggestions, if appropriate
		if ($secondActor.val().length === 0) {
			clearSuggestions(botSuggestions);
			$pathError.html("");
		} else {
			const postParameters = {boxType: "1", currentStr: $secondActor.val()};
			// Passes new String value to the AcHandler
			$.post("/actorcorrect", postParameters, responseJSON => {
				// Stores response object
				const responseObject = JSON.parse(responseJSON);
				// Updates all row values accordingly
				botSuggestions[0].html(responseObject.OneBrow);
				botSuggestions[1].html(responseObject.TwoBrow);
				botSuggestions[2].html(responseObject.ThreeBrow);
				botSuggestions[3].html(responseObject.FourBrow);
				botSuggestions[4].html(responseObject.FiveBrow);
				// Updates the error value, too
				if (responseObject.pathError.length > 0) {
					$pathError.html("Error: " + responseObject.pathError);
				} else {
					$pathError.html(responseObject.pathError);
				}
			});
		}
	});
	// Places autocorrect on the second actor box
	$("#botBox").keyup(event => {
		// Clears all suggestions, if appropriate
		if ($secondActor.val().length === 0) {
			clearSuggestions(botSuggestions);
		}

	});
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