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
	// Stores variables used for key selection
	let selected = false,
		selectionNum = 0;
	// Uses jQuery to get references to the five possible suggestion divs
	// Reference are kept separate so as to facilitate selection
	const $onerow = $("#onerow");
	const $tworow = $("#tworow");
	const $threerow = $("#threerow");
	const $fourrow = $("#fourrow");
	const $fiverow = $("#fiverow");
	// Reference to error message field
	const $mainError = $("#mainError");
	// Reference to settings button
	const $setButton = $("#tsb");
	// Creates array to store all suggestion items
	const suggestions = [$onerow, $tworow, $threerow, $fourrow, $fiverow];
	// Reference to box that user types in to
	const $acbox = $("#ac-box");
	// Bind handler to the input box
	$("#ac-box").keyup(event => {
		// Clear all suggestions if text box is empty
		if ($acbox.val().length === 0) {
			clearSuggestions(suggestions);
		}
		// Only proceeds if arrow, enter key was not involved in change
		if (event.which != 38 && event.which != 40 && event.which != 13) {
			// Constructs object for POST request that stores input String
			const postParameters = {toCorrect: $acbox.val()};
			// Passes new String value to the AcHandler
			$.post("/correct", postParameters, responseJSON => {
				// Stores response object
				const responseObject = JSON.parse(responseJSON);
				// Updates all row values accordingly
				$onerow.html(responseObject.oneRow);
				$tworow.html(responseObject.twoRow);
				$threerow.html(responseObject.threeRow);
				$fourrow.html(responseObject.fourRow);
				$fiverow.html(responseObject.fiveRow);
				// Updates the error value, too
				$mainError.html(responseObject.mainError);
			});
		}		
		// Removes all current selections for standard key press
		if (selected && event.which != 40 && event.which != 38) {
			// If key is enter, apply selected changes
			if (event.which == 13) {
				document.getElementById("ac-box").value = 
					suggestions[selectionNum - 1].html();
				// Removes all existing suggestions through a roundabout process
				// I believe for timing reasons, the clearSuggestions() function
				// failed. Instead, I pretended I was fetching a new set 
				// of suggestions, but then ignored the response. Note that
				// manually setting all five html()s, as is done below, did
				// not work without the POST request.
				// Constructs object for POST request that stores input String
				const postResetParameters = {toCorrect: $acbox.val()};
				// Passes new String value to the AcHandler
				$.post("/correct", postResetParameters, responseJSON => {
					// Completely resets all values
					$onerow.html("");
					$tworow.html("");
					$threerow.html("");
					$fourrow.html("");
					$fiverow.html("");
				});
			}
			suggestions[selectionNum - 1].toggleClass('selected');
			suggestions[selectionNum - 1].toggleClass('suggestion');
			selected = false;
			selectionNum = 0;
		}
	});
	// Bind the up, down key handlers to the document
	$(document).keydown(event => {
		// If up or down is pressed
		/*
		if (event.which == 38 || event.which == 40) {
			if (selected) {
				selected = false;
				suggestions[selectionNum - 1].toggleClass('selected');
				suggestions[selectionNum - 1].toggleClass('suggestion');
				selectionNum = 0;
			}
		}
		*/
		// Look to change, remove, or act on selection
		// If the up arrow is pressed
		if (event.which == 38 && selected) {
			// while on top selection, deselect
			if (selectionNum == 1) {
				selected = false;
				selectionNum = 0;
				$onerow.toggleClass('selected');
				$onerow.toggleClass('suggestion');
			} else {
				// otherwise, move selection up
				suggestions[selectionNum - 1].toggleClass('selected');
				suggestions[selectionNum - 1].toggleClass('suggestion');
				selectionNum--;
				suggestions[selectionNum - 1].toggleClass('selected');
				suggestions[selectionNum - 1].toggleClass('suggestion');
			}
		}		
		// If down arrow is selected, and at least one suggestion
		// is present, then make a selection
		if (event.which == 40 && $onerow.html().length > 0) {
			// Calculates current number of options
			let numOptions = 0;
			for (let j = 0; j < suggestions.length; j++) {
				if (suggestions[j].html().length > 0) {
					numOptions++;
				}
			}
			// If not on the last option
			if (selectionNum < numOptions) {
				// If this is the first selection, alter variables
				if (selectionNum == 0) {
					// If it is not, then deselect current selection
				} else {
					suggestions[selectionNum - 1].toggleClass('selected');
					suggestions[selectionNum - 1].toggleClass('suggestion');
				}
				selected = true;
				selectionNum++;
				suggestions[selectionNum - 1].toggleClass('selected');
				suggestions[selectionNum - 1].toggleClass('suggestion');
			}
		}
	});
	// Bind the mouse-hover selection to every suggestion
	for (let i = 0; i < suggestions.length; i++) {
		// First, bind the mouse-on selector
		suggestions[i].mouseover(event => {
			// Checks for current selections to avoid
			if (selected) {
				// Toggle appearance
				suggestions[selectionNum - 1].toggleClass('selected');
				suggestions[selectionNum - 1].toggleClass('suggestion');
			}
			// Set designations true
			selected = true;
			selectionNum = i + 1;
			// Toggle appearance
			suggestions[selectionNum - 1].toggleClass('selected');
			suggestions[selectionNum - 1].toggleClass('suggestion');
		})
		// Then, bind the mouse-off deselector
		suggestions[i].mouseout(event => {
			// Checks for selected (should avoid in case of click)
			if (selected) {
				// Toggle appearance
				suggestions[selectionNum - 1].toggleClass('selected');
				suggestions[selectionNum - 1].toggleClass('suggestion');	
				// Set designations false
				selected = false;
				selectionNum = 0;
			}	
		})
	}
	// Then, activate the mouse-clicking functionality
	$(document).click(event => {
		if (selected) {
			// If a suggestion is selected, follow through on selection
			document.getElementById("ac-box").value = 
				suggestions[selectionNum - 1].html();
			// Removes all existing suggestions through a roundabout process
			// I believe for timing reasons, the clearSuggestions() function
			// failed. Instead, I pretended I was fetching a new set 
			// of suggestions, but then ignored the response. Note that
			// manually setting all five html()s, as is done below, did
			// not work without the POST request.
			// Constructs object for POST request that stores input String
			const postResetParameters = {toCorrect: $acbox.val()};
			// Passes new String value to the AcHandler
			$.post("/correct", postResetParameters, responseJSON => {
				// Completely resets all values
				$onerow.html("");
				$tworow.html("");
				$threerow.html("");
				$fourrow.html("");
				$fiverow.html("");
			});
			suggestions[selectionNum - 1].toggleClass('selected');
			suggestions[selectionNum - 1].toggleClass('suggestion');
			selected = false;
			selectionNum = 0;
		}
	})
	// Add the color changing scheme for the button
	$setButton.mouseover(event => {
		$setButton.toggleClass('toSettingsButton');
		$setButton.toggleClass('altButton');
	})
	$setButton.mouseout(event => {
		$setButton.toggleClass('toSettingsButton');
		$setButton.toggleClass('altButton');
	})
});

