<?php header ('Content-type: text/html; charset=utf-8');
require_once 'Log.php';
 require_once 'config.php';
if (isset($_POST['purchase_id'])) {
    $Rating = strip_tags($_POST['Rating']);
    $Comment = strip_tags($_POST['Comment']);
    $Purchase_id = strip_tags($_POST['purchase_id']);
    
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sql = "UPDATE purchase SET Rating = '$Rating', Comment = '$Comment' WHERE purchase_id = '$Purchase_id'";


  $Message = "Update purchase Set Rating = $Rating, Comment = $Comment Where purchase_id = $Purchase_id";
What($Message,"User Posted Review");
print($Purchase_id);
$res = $conn->query($sql);
$rows = array();
}

?>
