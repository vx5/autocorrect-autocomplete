<!DOCTYPE html>
  <head>
  	<title>Autocorrect</title>
  	<meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/acmain.css"/>
    <link rel = "icon" href = "css/ACLogo.png" />
  </head>
  <body>
    <!-- Adds the relevant JS libraries and script -->
    <script src="js/jquery-3.1.1.js"></script>
    <script src="js/autocorrect.js"></script>
    <!-- Main area of Autocorrect box -->
    <!-- Please note that the title is styled with the "Good Times" font by Raymond Larabie -->
  	<h1 id="main-header">Autocorrect</h1>
    <h4 id="load-instruction">Please remember to visit the settings page before trying to obtain suggestions<br>
    You should load at least one corpora, and turn on at least 1 of the 4 generation and sorting settings</h4>
    <div id="parent-div">
      <input type="text" id="ac-box" name="toCorrect" size="75">
      <div class="suggestion" id="onerow">${oneRow}</div>
      <div class="suggestion" id="tworow">${twoRow}</div>
      <div class="suggestion" id="threerow">${threeRow}</div>
      <div class="suggestion" id="fourrow">${fourRow}</div>
      <div class="suggestion" id="fiverow">${fiveRow}</div>
      <h4 id="mainError">${mainError}</h4>
    </div>  
    <div id="buttonDiv">
      <form method="GET" action="/autocorrect/settings">
        <button id= "tsb" class="toSettingsButton" type="submit">
        Settings
        </button>
      </form>
    </div>
  </body>
</html>