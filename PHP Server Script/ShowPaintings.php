<?php header ('Content-type: text/html; charset=utf-8');
if (isset($_GET['Category'])) {
    $Category = strip_tags($_GET['Category']);
    $conn = mysqli_connect('localhost','jrnaninf_Paint', 'Paint','jrnaninf_paintings');

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sql = "SELECT * FROM paintings WHERE Painting_category LIKE $Category";
$res = $conn->query($sql);
$rows = array();
while($r = mysqli_fetch_assoc($res)) {
    $rows[] = $r;
}

print json_encode($rows , JSON_UNESCAPED_SLASHES);
} 
else if (isset($_GET['Room'])) {
    $Room = strip_tags($_GET['Room']);
    $conn = mysqli_connect('localhost','jrnaninf_Paint', 'Paint','jrnaninf_paintings');

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sql = "SELECT * FROM paintings WHERE Room LIKE $Room";
$res = $conn->query($sql);
$rows = array();
while($r = mysqli_fetch_assoc($res)) {
    $rows[] = $r;
}

print json_encode($rows , JSON_UNESCAPED_SLASHES);
}
else {
  $conn = mysqli_connect('localhost','jrnaninf_Paint', 'Paint','jrnaninf_paintings');

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sql = "SELECT * FROM paintings LIMIT 15";
$res = $conn->query($sql);
$rows = array();
while($r = mysqli_fetch_assoc($res)) {
    $rows[] = $r;
}
print json_encode($rows , JSON_UNESCAPED_SLASHES);
}

?>
