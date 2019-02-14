<!DOCTYPE html>
  <head>
  	<title>Settings | Autocorrect</title>
  	<meta name="viewport" content="width=device-width, initial-scale=1">
  </head>
  <body>
  	<h1 id="page-header">${title}</h1>
  	<!-- SECTION: Altering settings -->
    <h4>Make your changes, and then click the Save new settings button below</h4>
  	<form method="POST" action="/autocorrect/settings/saved" id="settings-form">
      <!-- Inputs for altering generation / sorting settings -->
      <h2 class="sub-heading">Generation and Sorting</h2>

      <!-- Inputs for altering corpora settings -->
      <h2 class="sub-heading">Corpora</h2>
      <!-- Lists all current corpora -->
      <h4>All corpora currently loaded:</h4>
      <p>
        <#list corpora as corpus>
        corpus<br>
        </#list>
      </p>
      <!-- Asks for new corpora -->
      List all new corpora to add by listing their filepaths, <b>separated by newlines</b>
      <p>
      All paths are assumed to start with "data/autocorrect/"<br>
      <textarea name="filepaths" form="settings-form" rows="3" cols="45">filename.txt</textarea>
      </p>
      or example, input:<br>"sherlock.txt"<br>"norton.txt"<br>"great_expectations.txt"<br>
      <!-- Button for saving the settings -->
      <button id = "save-settings" type="submit">
      Save new settings
      </button>      
    </form>
    <!-- SECTION: Button to reset all settings to default -->
    <form method="GET" action="/autocorrect/settings/default">
      <button id = "reset-settings" type="submit">
      Reset settings to default
      </button>
    </form> 
    <!-- Confirmation message to user -->
    <!--<p>confirm-setting/p> -->
  	<!-- SECTION: Return to main page -->
    <h2 class="sub-header">Return to Main Page</h2>
    <h4>Make sure you save your settings above first!</h4>
  	<!-- Button that sends us back to the main page -->
    <form method="GET" action="/autocorrect">
      <button id = "back-to-main" type="submit">
      Return to main
      </button>
    </form>
  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old browsers.  -->
</html>