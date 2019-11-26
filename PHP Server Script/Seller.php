<?php header ('Content-type: text/html; charset=utf-8');
require_once 'Log.php';
 require_once 'config.php';
if (isset($_GET['Username'])) {
    $Username = strip_tags($_GET['Username']);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sql = "SELECT SUM(Price*Quantity)  AS Sum , MONTH(Date) AS Month from purchase WHERE Artist = 'by $Username' AND YEAR(Date) LIKE 2019 GROUP BY YEAR(Date), MONTH(Date)";


$Message = "Select SUM(Price*Quantity)  AS Sum , MONTH(Date) AS Month From purchase Where Artist = by $Username And YEAR(Date) Like 2019 Group By YEAR(Date), MONTH(Date)";

    What($Message,"Artist Loaded his monthly sales");
$res = $conn->query($sql);
$rows = array();
while($r = mysqli_fetch_assoc($res)) {
    $rows[] = $r;
}

print json_encode($rows , JSON_UNESCAPED_SLASHES);
}
if (isset($_GET['BestSeller'])) {

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sql = "SELECT SUM(Price*Quantity)  AS Sum , Artist from purchase WHERE YEAR(Date) LIKE 2019 GROUP BY Artist LIMIT 1";

$Message = "Select SUM(Price*Quantity)  As Sum , Artist From purchase Where YEAR(Date) Like 2019 Group By Artist Limit 1";

    What($Message,"Artist Loaded the best seller");
    
$res = $conn->query($sql);
$rows = array();
while($r = mysqli_fetch_assoc($res)) {
    $rows[] = $r;
}

print json_encode($rows , JSON_UNESCAPED_SLASHES);
}
?>
