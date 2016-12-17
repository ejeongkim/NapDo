<?php  

session_start();

$db_host = "localhost";
$db_user = "napdo";
$db_pass = "rhaxodn1";
$db_name = "napdo";

function sql_connect($db_host, $db_user, $db_pass, $db_name)
{
    $result = mysql_connect($db_host, $db_user, $db_pass) or die(mysql_error());
    mysql_select_db("$db_name") or die(mysql_error());
    return $result;
}

function sql_query($sql)
{
    global $connect;
    $result = @mysql_query($sql);

    return $result;
}

$connect = sql_connect($db_host, $db_user, $db_pass, $db_name);
$param = $_REQUEST['PARAM'];
mysql_query("SET NAMES 'utf8'");

$result = mysql_query("SELECT * FROM DRIVING WHERE id = '$param'");
$row = mysql_fetch_array($result,MYSQL_ASSOC);

	echo "{";
	echo "\"data\": [{";
	echo "\"start\": \"";
	echo urlencode($row['start']);
	echo "\",";
	
	echo "\"end\": \"";
	echo urlencode($row['end']);
	echo "\",";

	echo "\"date\": \"";
	echo $row['date'];
	echo "\",";

	echo "\"distance\": \"";
	echo $row['distance'];
	echo "\"";

	echo "}]}";

mysql_close();

?>