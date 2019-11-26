<?php header ('Content-type: text/html; charset=utf-8');
  require_once 'Log.php';
   require_once 'config.php';
if (isset($_GET['painting_id'])) {
    $Username = strip_tags($_GET['painting_id']);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sql = "SELECT Rating,Comment FROM purchase WHERE paintings_id = '$Username'";

  $Message = "Select Rating,Comment From purchase Where paintings_id = $Username";
    What($Message,"Loaded Comment of painting");
         
$res = $conn->query($sql);
$rows = array();
while($r = mysqli_fetch_assoc($res)) {
    $rows[] = $r;
}

print json_encode($rows , JSON_UNESCAPED_SLASHES);
}
?>
