<?php
if (isset($_GET['Username'])&&isset($_GET['Password'])) {
    $Username = strip_tags($_GET['Username']);
    $Password = SHA1(strip_tags($_GET['Password']));
    $conn = mysqli_connect('localhost','jrnaninf_Paint', 'Paint','jrnaninf_paintings');

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sql = "SELECT * FROM users WHERE username LIKE '$Username' and password LIKE '$Password'";
$res = $conn->query($sql);
$rows = array();
while($r = mysqli_fetch_assoc($res)) {
    $rows[] = $r;
}
print json_encode($rows);
} else {

}

?>
