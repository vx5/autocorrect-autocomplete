// Waits for document to load before running Script
$(document).ready(() => {
	// Sends a request that returns the current values of the settings, 
	// in order to update the settings page with the current settings
	$.get("/getsettings", responseJSON => {
		// Stores response object
		const responseObject = JSON.parse(responseJSON);
		// Updates all settings values accordingly
		document.getElementById("prefix").value = responseObject.prefixVal;
		document.getElementById("whitespace").value = responseObject.whitespaceVal;
		document.getElementById("smart").value = responseObject.smartVal;
		document.getElementById("led").value = responseObject.ledVal;
		// Parses led value to an integer, uses it to set the range value
		if (parseInt(responseObject.ledVal) > 0) {
			$("#rangeVal").html(responseObject.ledVal);
		} else {
			$("#rangeVal").html("Off");
		}
	})
	// Checks for updates to any and all elements to update update the "save" message
	$("#settings-form").change(function() {
		// Gets all current settings values (to look for changes)
		$.get("/getsettings", responseJSON => {
			// Stores response object
			const responseObject = JSON.parse(responseJSON);
			// Check for any differences in values
			if (document.getElementById("prefix").value.valueOf() != responseObject.prefixVal.valueOf() ||
				document.getElementById("whitespace").value.valueOf() != responseObject.whitespaceVal.valueOf() ||
				document.getElementById("smart").value.valueOf() != responseObject.smartVal.valueOf() ||
				document.getElementById("led").value.valueOf() != responseObject.ledVal.valueOf() ||
				document.getElementById("filepaths").value.valueOf() != "".valueOf()) {
				// Update the message to warn the user of unsaved changes
				$("#err-msg").html("You have unsaved changes in the form!");
				$("#win-msg").html("");
			} else {
				// Update the message to a success message
				$("#err-msg").html("");
				$("#win-msg").html("All settings up-to-date!");
			}
		})
		// Parses led value to an integer, uses it to set the range value
		if (parseInt(document.getElementById("led").value) > 0) {
			$("#rangeVal").html(document.getElementById("led").value);
		} else {
			$("#rangeVal").html("Off");
		}	
	});
	// Adds color-change and cursor effects for all three buttons on "acsettings.ftl" template
	// References all three buttons
	const buttons = [$("#save-button"), $("#reset-button"), $("#return-button")];
	// Attaches toggler for mouseover to each button
	for (let i = 0; i < buttons.length; i++) {
		// Binds both color-change handlers
		buttons[i].mouseover(event => {
			buttons[i].toggleClass('settings-button');
			buttons[i].toggleClass('altButton');
		})
		buttons[i].mouseout(event => {
			buttons[i].toggleClass('settings-button');
			buttons[i].toggleClass('altButton');
		})
	}
});
