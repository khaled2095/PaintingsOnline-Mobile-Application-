<?php
$conn = mysqli_connect('localhost','jrnaninf_Paint', 'Paint','jrnaninf_paintings');
//Store transaction information into database from PayPal
session_start();
$CardID = $_SESSION["CardID"];

$sql1 = "UPDATE purchase SET status = 'Paid' WHERE CartID ='$CardID'";

 $resultset2 = mysqli_query($conn, $sql1) or die("database error:". mysqli_error($conn));
 $sql = "SELECT * FROM purchase WHERE CartID ='$CardID' LIMIT 1";
    $res = $conn->query($sql);
    $rows = array();
while($r = mysqli_fetch_assoc($res)) {
    echo $r['Buyer'];
    $last_insert_id = mysqli_insert_id($conn);    
    $username = $r['Buyer'];
	$sql1 = "SELECT * FROM users WHERE username LIKE '$username' LIMIT 1";
    $res1 = $conn->query($sql1);
    $rows = array();
    while($r = mysqli_fetch_assoc($res1)) {
    $to_email = $r['Email'];
    echo $to_email;
    $subject = 'Payment successful';
    $message = 'You payment has been successfully finalised, please refer to the orders page of the application to see details. and expect the paintings to reached you in two weeks';
    $headers = 'From: admin@jrnan.info';
    mail($to_email,$subject,$message,$headers);
    }
}
 

    

?>
	<h1>Your payment has been successful.</h1>
	<h2>Please Go to the app to view the orders.</h2>
    <a href="com.paintingsOnline://">Back To App</a>
<?php
?>