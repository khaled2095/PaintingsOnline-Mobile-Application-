<?php
require_once 'config.php';
require_once '../Log.php';
if (isset($_POST['painting_id']) && isset($_POST['artist']) && isset($_POST['buyer']) && isset($_POST['Address']) && isset($_POST['PaintingUrl']) && isset($_POST['Price']) && isset($_POST['CartID']) && isset($_POST['Quantity'])&& isset($_POST['TXN_ID']) && isset($_POST['painting_name']) && isset($_POST['Size'])) {
    $painting_id = strip_tags($_POST['painting_id']);
    $artist = strip_tags($_POST['artist']);
    $buyer = strip_tags($_POST['buyer']);
    $Address = strip_tags($_POST['Address']);
    $PaintingUrl = strip_tags($_POST['PaintingUrl']);
    $Price = strip_tags($_POST['Price']);
    $CartID = strip_tags($_POST['CartID']);
    $Quantity = strip_tags($_POST['Quantity']);
    $TXN_ID = strip_tags($_POST['TXN_ID']);
    $Size  = strip_tags($_POST['Size']);
    $Email = strip_tags($_POST['Email']);
    $painting_name = strip_tags($_POST['painting_name']);
    $Status1 = strip_tags($_POST['Status1']);
$status = "Finalised";
$date=date('Y-m-d H:i:s');
  $sql1 = "insert into purchase (painting_name,paintings_id,status,Artist,Buyer,Address,PaintingUrl,Size,CartID,Price,Quantity,TXN_ID,Date) values ('$painting_name','$painting_id','$status','$artist','$buyer','$Address','$PaintingUrl','$Size','$CartID','$Price','$Quantity','$TXN_ID','$date')";
  
  
  
    $Message = "Insert Into purchase (painting_name,paintings_id,status,Artist,Buyer,Address,PaintingUrl,Size,CartID,Price,Quantity,TXN_ID,Date) Values ($painting_name,$painting_id,$status,$artist,$buyer,$Address,$PaintingUrl,$Size,$CartID,$Price,$Quantity,$TXN_ID,$date)";
    
    What($Message,"Insert Bought Painting to Cart");
  
        $resultset = mysqli_query($conn, $sql1) or die("database error:". mysqli_error($conn));
        
          $sql2 = "UPDATE Sizes SET Quantity = Quantity - '$Quantity'  WHERE painting_id = '$painting_id' AND Size = '$Size'";
        $resultset2 = mysqli_query($conn, $sql2) or die("database error:". mysqli_error($conn));
        
         $sql3 = "UPDATE paintings SET BuyCount = BuyCount + '$Quantity'  WHERE painting_id = '$painting_id'";
        $resultset4 = mysqli_query($conn, $sql3) or die("database error:". mysqli_error($conn));
        
        
$str2 = substr($artist, 3);        
 $sql10 = "SELECT Email FROM users WHERE username LIKE '$str2'";
$resultset10 = mysqli_query($conn, $sql10) or die("database error:". mysqli_error($conn));
if ($resultset10 !== false) {
    $row=mysqli_fetch_array($resultset10);
    $SellerEmail = $row[0];
    echo $SellerEmail;
}




        
        	$subject = "Purchase Completed Successfully";
		$message = "

This is an automatic email, please do not reply. 
 
Purchase of your painting has been successful, thank you for supporting PaintingsOnline:
<html>
<body> 
<table> 
<tr>
<th>Painting name</th>
<th>Size</th> 
<th>Price</th>
<th>Quantity</th>
<th>reciept number</th>
<th>Image</th>
</tr>
<tr>
<td>$painting_name</td> 
<td>$Size</td> 	
<td>$Price</td> 	
<td>$Quantity</td> 	
<td>$TXN_ID</td> 
<td><img src=$PaintingUrl height='42' width='42'></td> 	
</tr> 
</table> 
</body>
</html>

The Painting will be shipped to paintingsOnline HeadQuarters at 15, Kings Street, Melbourne CBD , Victoria, Australia.
"; 

		$headers = 'From:support@Jrnan.info'."\r\n"; // Set from headers
		$headers .= "MIME-Version: 1.0" . "\r\n"; 
        $headers .= "Content-type:text/html;charset=UTF-8" . "\r\n"; 
        $headers .= "Cc: $SellerEmail" . "\r\n";
		mail($Email, $subject, $message, $headers); // Send our email
		

}
else {
    echo "missing details";
}
?>
       