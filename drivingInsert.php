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

$v1 = $_REQUEST['PARAM1'];
$v2 = $_REQUEST['PARAM2'];
$v3 = $_REQUEST['PARAM3'];
$v4 = $_REQUEST['PARAM4'];

mysql_query("SET NAMES 'utf8'");

$result = mysql_query("INSERT INTO DRIVING (start, end, date, distance) VALUES ('$v1','$v2','$v3','$v4')");

// ��� Ȯ��
// ���� ���ǰ� MySQL�� ��������, ������ �߻����� ����Դϴ�. ����뿡 �����մϴ�.
if (!$result) {
    $message  = 'Invalid query: ' . mysql_error() . "\n";
    $message .= 'Whole query: ' . $query;
    die($message);
}

mysql_close();
