<?php 
require_once("cw.utils.php"); 
/**
 TODO : ajouter un système de filtre pour ne montrer que les war interessantes.
*/

?>
<html>
<head>
<style>
body {
 margin: 0;
}
#fixedbar {
text-align: center;
	margin:0px;
   position: fixed;
   top: 0px;
   width: 100%;
   height: 40px;
   background-color: #FFFF99;
   color:#000099;
}
#fixedbar2{
text-align: center;
	margin:0px;
   position: fixed;
   bottom: 0px;
   width: 100%;
   height: 40px;
   background-color: #FFFF99;
   color:#000099;
}
#tableau{
	left:3%;
	width:93%;
	top:41px;
	position: absolute;
	border: 0;
}
tr:hover{
	background-color: #8FE997;
}
td{
	text-align:center;
		border-width: 1px;
	border-style: dotted;
	border-color: black;
}
#champ{
	width: 80%;
	top: 9px;
	position: relative;
}
#options{
	
}
select{
	text-align:center;
}
</style>
<head>
<body>

<table id="tableau" CELLSPACING="0" CELLPADDING="0" >
	<thead>
	<tr><td>Time</td><td>Type</td><td>Level</td><td>Server</td><td>User</td><td>Message</td></tr>
	</thead>
	<?php
		if(!isset($_GET['nbjopt']) || $_GET['nbjopt']==0) $nbjoueurs = '%';
		else $nbjoueurs = $_GET['nbjopt'];
		
		if(!isset($_GET['srvopt']) || $_GET['srvopt']==0) $srvopt = '%';
		else $srvopt = $_GET['srvopt'];
		
		if(!isset($_GET['lvlopt']) || $_GET['lvlopt']==0) $lvlopt = '%';
		else $lvlopt = $_GET['lvlopt'];
		
		$now = mktime();
		$sql = mysql_connect('127.0.0.1', 'cww', '355U7HYzfYEybw8f') or die(mysql_error());
		$db = mysql_select_db('clanwar', $sql) or die(mysql_error());
		$query = "SELECT * FROM `wars` WHERE 1";
		if($nbjoueurs != '%')
			$query .= " AND nbjoueurs = '$nbjoueurs'";
		if($srvopt != '%')	
			$query .= " AND serv = '$srvopt'";
		if($lvlopt != '%')
			$query .= " AND lvl = '$lvlopt'";
		$query .= " ORDER BY `when` DESC LIMIT 0 , 30";
		echo "<!-- $query -->";
		$result = mysql_query($query) or die(mysql_error());
		
		
		while($row = mysql_fetch_array($result)){
			$stamp = strtotime($row['when']);
			$ago = distanceOfTimeInWords($stamp, $now);
			$level = getLevelFromCode($row['lvl']);
			$server = getServerFromCode($row['serv']);
			echo "<tr OnClick=\"document.form.champ.value='/msg $row[user] '; document.form.champ.select();\">";
			echo "<td>$ago</td>";
			echo "<td>$row[nbjoueurs] vs $row[nbjoueurs]</td>";
			echo "<td>$level</td>";
			echo "<td>$server</td>";
			echo "<td>$row[user]</td>";
			echo "<td>$row[msg]</td></tr>";
		}
	?>
</table>
<div id="fixedbar">
<form name="form">
<input id="champ" type="text" value="" name="champ"/>
</form>
</div>

<div id="fixedbar2">
<form name="options" METHOD="GET" action="clanwar.php">
I seek : </label>
<SELECT NAME="lvlopt">
<?php
$lvllist = array('-all level-', 'NOOB', 'LOW', 'LOW+', 'MID', 'MID+', 'GOOD', 'SKILLED', 'ROXOR');
for($i=0; $i<9; $i++){
	echo "<OPTION ".(($_GET['lvlopt']==$i)?"SELECTED ":"")."VALUE=\"$i\">$lvllist[$i]</OPTION>";
}
?>
</SELECT>
<SELECT NAME="srvopt">
<?php
$srvlist = array('-all serv-', 'I DON\'T HAVE ANY', 'I HAVE A SERVER');
for($i=0; $i<3; $i++){
	echo "<OPTION ".(($_GET['srvopt']==$i)?"SELECTED ":"")."VALUE=\"$i\">$srvlist[$i]</OPTION>";
}
?>
</SELECT>

<SELECT NAME="nbjopt">
<?php
echo "<OPTION ".(($_GET['nbjopt']==0)?"SELECTED ":"")."VALUE=\"0\">-nombre de joueurs-</OPTION>";
for($i=1; $i<=16; $i++){
	echo "<OPTION ".(($_GET['nbjopt']==$i)?"SELECTED ":"")."VALUE=\"$i\">$i joueurs</OPTION>";
}
?>
</SELECT>
<input type="submit">
</form>
</div>
</div>

</body>
</html>