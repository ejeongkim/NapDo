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

$result = mysql_query("SELECT * FROM COUPON WHERE id = '$param'");

// 결과 확인
// 실제 질의가 MySQL로 전해지고, 오류가 발생했을 경우입니다. 디버깅에 유용합니다.
if (!$result) {
    $message  = 'Invalid query: ' . mysql_error() . "\n";
    $message .= 'Whole query: ' . $query;
    die($message);
}


$row = mysql_fetch_array($result,MYSQL_ASSOC);

	echo "{";
	echo "\"data\": [{";
	echo "\"odate\": \"";
	echo $row['odate'];
	echo "\",";
	
	echo "\"edate\": \"";
	echo $row['edate'];
	echo "\"";

	echo "}]}";

mysql_close();

?>