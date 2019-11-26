<?php header ('Content-type: text/html; charset=utf-8');
  require_once 'Log.php';
   require_once 'config.php';
if (isset($_GET['painting_id'])) {
    $painting_id = strip_tags($_GET['painting_id']);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sql = "SELECT * FROM Pictures WHERE painting_id = '$painting_id'";

  $Message = "Select * From Pictures Where painting_id = $painting_id";
    What($Message,"Loaded pictures of image");
    
$res = $conn->query($sql);
$rows = array();
while($r = mysqli_fetch_assoc($res)) {
    $rows[] = $r;
}
}
print json_encode($rows , JSON_UNESCAPED_SLASHES);
?>
