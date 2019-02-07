<#assign content>

<h1 id="page header"> Find stars near you! </h1>

<form method="GET" action="/stars">
<p>
<button id = "reset" type="submit">
Reset this page and start a completely new search
</button>
</p>
</form>


<p> <h2 class="subheader">Step 1: Load stars through .csv file:</h2>

<form method="POST" action="/starsloaded">
	Choose a .csv file at the following file path:
	data/stars/<input type="text" name="filename" size="15" required><br><br>	
	<button id = "submitcsv" type="submit">
	Upload these stars!
	</button>

</form><br>

<p id="default">${default}</p>

<p id="loadconfirm">${loadconfirm}</p>

<p id="loaderror">${loaderror}</p>


<p> <h2 class="subheader">Step 2: Fill out and submit a command</h2>
<form method="POST" action="/commandrun">
  <p>
  Choose a type of command:
  <select id="command choice" name="command choice">
  <option>neighbors</option>
  <option>radius</option>
  </select><br>
  </p>
  
  <p>
  Specify the bound (an integer number of neighbors<br>
  for the "neighbors" command, a float radius to <br>
  search within for the "radius" command):<br>
  <input type="text" name="bound" size="5" required><br>
  </p>
  
  <p>
  Specify the center of the search by Star name or a point, represented by coordinates
  in float form:<br>
  </p>
  <p>
  Name of star:<br>
  <input type="text" name="starname" size="15"><br>
  </p>
  <p>OR</p>
  <p>
  Coordinates:<br>
  X: <input type="text" name="xcoord" size="6">
  Y: <input type="text" name="ycoord" size="6">
  Z: <input type="text" name="zcoord" size="6"><br>
  </p>
  
  <button id = "submitcmd" type="submit">
  Submit this command!
  </button>
   
</form>
</p>

<p id="cmderror">${cmderror}</p>

<p>${listheader}<p>




<table style="width:90%" align="center">
<#list cmdoutput as star>
<tr>
<#list star as element>
<th id="table-element">${element}</th>
</#list>
</tr>
</#list>
</table><br>

</#assign>
<#include "main.ftl">
