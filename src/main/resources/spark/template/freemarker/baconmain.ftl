<!DOCTYPE html>
	<head>
		<title>Bacon</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="css/baconmain.css">
	</head>
	<body>
		<!-- Adds the relevant JS libraries and script -->
    	<script src="js/jquery-3.1.1.js"></script>
    	<script src="js/baconmain.js"></script>

		<h1 id="main-header">Bacon</h1>

		<div id="overDiv">

    	<div id="topDiv">
    	Enter the actor who is the start of the path:
      	<input type="text" id="topBox" name="topBox" size="45">
      	<div class="suggestion" id="1Trow">${OneTrow}</div>
      	<div class="suggestion" id="2Trow">${TwoTrow}</div>
      	<div class="suggestion" id="3Trow">${ThreeTrow}</div>
      	<div class="suggestion" id="4Trow">${FourTrow}</div>
      	<div class="suggestion" id="5Trow">${FiveTrow}</div>
    	</div>

    	<div id="botDiv">
    	Enter the actor whom you want the path to end at:
      	<input type="text" id="botBox" name="botBox" size="45">
      	<div class="suggestion" id="1Brow">${OneBrow}</div>
      	<div class="suggestion" id="2Brow">${TwoBrow}</div>
      	<div class="suggestion" id="3Brow">${ThreeBrow}</div>
      	<div class="suggestion" id="4Brow">${FourBrow}</div>
      	<div class="suggestion" id="5Brow">${FiveBrow}</div>
    	</div>

    	</div>

        <button id="pathbutton" class="baconbutton">
       	Find path!
        </button>
        <br><br>

        <h4 id="pathError">${pathError}</h4>
     	<!-- Where results will be stored via innerHTML -->
     	<div id="pathResults">${pathResults}</div>

	</body>
</html>