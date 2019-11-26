<?php header ('Content-type: text/html; charset=utf-8');
require_once 'Log.php';
 require_once 'config.php';
if (isset($_GET['Username']) && isset($_GET['Mac'])) {
    
    
$Mac = strip_tags($_GET['Mac']);


$sql = "SELECT * FROM users WHERE Mac LIKE '$Mac'";
$res = $conn->query($sql);
$res1 = $res->num_rows;

if($res > 0 ) {
    
    $Username = strip_tags($_GET['Username']);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sql = "UPDATE purchase SET status = 'Shipped to Paintings Online' WHERE purchase_id LIKE '$Username'";

     $Message = "Update purchase Set status = Shipped to Paintings Online Where purchase_id Like $Username";
          
          What($Message,"Artist Marked his painting as shipped");
          
$res = $conn->query($sql);
}
}
?>
