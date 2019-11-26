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
$sql = "SELECT * FROM purchase WHERE Artist = 'by $Username'";



  $Message = "Select * From purchase Where Artist = by $Username";
          
    What($Message,"Artist Loaded his orders");
    
$res = $conn->query($sql);
$rows = array();
while($r = mysqli_fetch_assoc($res)) {
    $rows[] = $r;
}

print json_encode($rows , JSON_UNESCAPED_SLASHES);
}

}
?>
