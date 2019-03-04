<!DOCTYPE html>
	<head>
		<title>${targetName}</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
	</head>
	<body>

		<h1 id="main-header">${targetName}</h1>

		<h3>${targetCounterType}:</h3>
     	<!-- Where results will be stored via innerHTML -->
     	<div id="targetLinks">
     	${targetLinks}
     	</div>
     	<br><br>
     	<form method="GET" action="/bacon">
		<button class="returnbutton" type="submit">
       	Perform another bacon request
        </button>
     	</form>

	</body>
</html>