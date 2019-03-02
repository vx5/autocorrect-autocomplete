<!DOCTYPE html>
	<head>
		<title>${targetName}</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
	</head>
	<body>

		<h1 id="main-header">${targetName}</h1>


     	<!-- Where results will be stored via innerHTML -->
     	<div id="targetLinks">
     	${targetLinks}
     	</div>
     	<br><br>
     	<form method="GET" action="/path">
		<button class="returnbutton" type="submit">
       	Perform another bacon request
        </button>
     	</form>

	</body>
</html>