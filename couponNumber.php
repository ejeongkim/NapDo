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
$USED = $_REQUEST['USED'];

mysql_query("SET NAMES 'utf8'");

$result = mysql_query("SELECT * FROM COUPON");

// ��� Ȯ��
// ���� ���ǰ� MySQL�� ��������, ������ �߻����� ����Դϴ�. ����뿡 �����մϴ�.
if (!$result) {
    $message  = 'Invalid query: ' . mysql_error() . "\n";
    $message .= 'Whole query: ' . $query;
    die($message);
}


$num = mysql_num_rows($result);

echo "{\"data\":[";
echo "{\"number\" :";
echo "\"";
echo $num;
echo "\"}";
echo "]}";

mysql_close();

?>