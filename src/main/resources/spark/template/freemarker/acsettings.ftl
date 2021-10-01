<!DOCTYPE html>
  <head>
  	<title>Settings | Autocorrect</title>
  	<meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="/css/acsettings.css">
    <link rel = "icon" href = "/css/ACLogo.png" />
  </head>
  <body>
    <!-- Adds the relevant JS libraries and script -->
    <script src="/js/jquery-3.1.1.js"></script>
    <script src="/js/acsettings.js"></script>
  	<h1 id="settings-header">Settings</h1>
  	<!-- SECTION: Altering settings -->
    <h4 id="settings-subheader">Make your changes, and then click the <i>Save new settings</i> button below</h4><br>
  	<form method="POST" action="/autocorrect/settings/save" id="settings-form">
      <!-- Inputs for altering generation / sorting settings -->
      <div id="settingsDiv">
      <div class="subHeaderDiv">Generation and Sorting</div>
      <!-- Input for prefix setting -->
      <div class="settingDiv">
      Prefix setting:
      <select id="prefix" name="prefix">
        <option value="off">off</option>
        <option value="on">on</option>
      </select>
      </div>
      <!-- Input for whitespace setting -->
      <div class="settingDiv">
      Whitespace setting: 
      <select id="whitespace" name="whitespace">
        <option value="off">off</option>
        <option value="on">on</option>
      </select>
      </div>
      <!-- Input for smart setting -->
      <!-- TEST id="smart" before class def below -->
      <div class="settingDiv">
      Smart ranking setting:
      <select id="smart" name="smart">
        <option value="off">off</option>
        <option value="on">on</option>
      </select>
      </div>
      <!-- Input for led setting -->
      <div class="settingDiv">
      LED setting:
      <div>
      <b id="rangeVal"></b> &nbsp <input id="led" type="range" min="0" max="5" value="0" name="led">
      </div>
      </div>
      </div><br><br>


      <div id="corporaDiv">
      <!-- Inputs for altering corpora settings -->
      <div class="subHeaderDiv">Load Corpora</div>
      <!-- Lists all current corpora -->
      
      <div id="corpSettingDiv">Loaded so far: (scrolls)<div id="corporaListDiv"><#list corpora as corpus><i class="corpusItem">${corpus}</i></#list></div></div>
      <!-- Asks for new corpora -->
      <div id="enterCorporaDiv">
      List all new corpora to add by listing their filepaths, separated by commas.<br>
      For those with server access, all paths are assumed to start with "data/autocorrect/"<br><br>
      <textarea id="filepaths" name="filepaths" form="settings-form" rows="3" cols="45"></textarea><br><br>
      Options include sherlock.txt, great_expectations.txt, restaurants.txt<br>
      </div>
      </div><br><br>
      <!-- Div that holds final buttons -->
      <div id="buttonDiv">
      <!-- Button for saving the settings -->
      <button id="save-button" class = "settings-button" type="submit">
      Save new settings
      </button> 
      <form method="GET" action="/autocorrect/settings/default">
      <button id="reset-button" class = "settings-button" type="submit">
      Reset settings
      </button>
      </form> 
      </div>    
    </form><br>
    <!-- SECTION: Button to reset all settings to default --> 
    <!-- Confirmation / error messages to user -->
    <p id="win-msg">${win}</p>
    <p id="err-msg">${err}</p>
  	<!-- Button that sends us back to the main page -->
    <form method="GET" action="/autocorrect">
      <button id="return-button" class = "settings-button" type="submit">
      Return to main
      </button>
    </form>
  </body>
</html>