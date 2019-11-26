<?php header ('Content-type: text/html; charset=utf-8');
 require_once 'config.php';
include 'Admin/Discount.php';
require_once 'Log.php';
if (isset($_GET['painting_id'])) {
    if (isset($_GET['Size'])) {
        $Size = $_GET['Size'];
        $painting_id = strip_tags($_GET['painting_id']);
        if ($conn->connect_error) {
            die("Connection failed: " . $conn->connect_error);
        }
        $sql = "SELECT * FROM Sizes WHERE painting_id = '$painting_id' AND Quantity > 0 OR Size = '$Size' AND Quantity > 0";
        
          $Message = "Select * From Sizes Where painting_id = $painting_id And Quantity > 0 or Size = $Size And Quantity > 0";
          
          What($Message,"Loaded Sizes of images");
    
        $res = $conn->query($sql);
        $rows = array();
        while($r = mysqli_fetch_assoc($res)) {
            $rows[] = $r;
        }
        foreach($rows as $key => $value)
        {
        $rows[$key]['painting_price'] = strval(round($rows[$key]['painting_price'] * ((100 - $Discount) / 100)));
        }
        }
        
    else {
    $painting_id = strip_tags($_GET['painting_id']);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sql = "SELECT * FROM Sizes WHERE painting_id = '$painting_id' AND Quantity > 0";

  $Message = "Select * From Sizes Where painting_id = $painting_id And Quantity > 0";
          
    What($Message,"Loaded Sizes of images");
$res = $conn->query($sql);
$rows = array();
while($r = mysqli_fetch_assoc($res)) {
    $rows[] = $r;
}
foreach($rows as $key => $value)
{
    $rows[$key]['Price'] = strval(round($rows[$key]['Price'] * ((100 - $Discount) / 100)));
}
}
print json_encode($rows , JSON_UNESCAPED_SLASHES);
}
?>
