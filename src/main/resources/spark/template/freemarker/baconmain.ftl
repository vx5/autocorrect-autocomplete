<!DOCTYPE html>
	<head>
		<title>Bacon</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
	</head>
	<body>
		<!-- Adds the relevant JS libraries and script -->
    	<script src="js/jquery-3.1.1.js"></script>
    	<script src="js/baconmain.js"></script>

		<h1 id="main-header">Bacon</h1>

		<div id="overDiv">

    	<div id="topDiv">
      	<input type="text" id="topBox" name="topBox" size="40">
      	<div class="suggestion" id="1Trow">${OneTrow}</div>
      	<div class="suggestion" id="2Trow">${TwoTrow}</div>
      	<div class="suggestion" id="3Trow">${ThreeTrow}</div>
      	<div class="suggestion" id="4Trow">${FourTrow}</div>
      	<div class="suggestion" id="5Trow">${FiveTrow}</div>
    	</div>

    	<div id="botDiv">
      	<input type="text" id="botBox" name="botBox" size="40">
      	<div class="suggestion" id="1Brow">${OneBrow}</div>
      	<div class="suggestion" id="2Brow">${TwoBrow}</div>
      	<div class="suggestion" id="3Brow">${ThreeBrow}</div>
      	<div class="suggestion" id="4Brow">${FourBrow}</div>
      	<div class="suggestion" id="5Brow">${FiveBrow}</div>
    	</div>

    	<h4 id="pathError">${pathError}</h4>

        <button id="pathbutton" class="baconbutton">
       	Find path!
        </button>
        <br><br>
     	<!-- Where results will be stored via innerHTML -->
     	<div id="pathResults">${pathResults}</div>

     	</div>

	</body>
</html>