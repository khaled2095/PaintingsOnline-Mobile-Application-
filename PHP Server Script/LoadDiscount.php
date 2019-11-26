<?php header ('Content-type: text/html; charset=utf-8');
 require_once 'config.php';
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sql = "SELECT * FROM Discount LIMIT 1";
$res = $conn->query($sql);
$rows = array();
while($r = mysqli_fetch_assoc($res)) {
    $rows[] = $r;
}
print json_encode($rows , JSON_UNESCAPED_SLASHES);
?>
