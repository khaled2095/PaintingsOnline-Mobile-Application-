<?php
  require_once 'Log.php';
  require_once 'config.php';



       
$Mac = strip_tags($_GET['Mac']);


$sql = "SELECT * FROM users WHERE Mac LIKE '$Mac'";
$res = $conn->query($sql);
$res1 = $res->num_rows;

if($res > 0 ) {
    
    
if (isset($_POST['Username']) && isset($_POST['Password']) && isset($_POST['Address']) && isset($_POST['Email']) && isset($_POST['Name']) && isset($_POST['Mac'])) {
        $Username = strip_tags($_POST['Username']);
    $Password = SHA1(strip_tags($_POST['Password']));
    $Address = strip_tags($_POST['Address']);
    $Email = strip_tags($_POST['Email']);
    $Name = strip_tags($_POST['Name']);
    $Bio = strip_tags($_POST['Bio']);
    $Image = strip_tags($_POST['Image']);
    


if(isset($_POST['NewPassword'])){
        $NewPassword = SHA1(strip_tags($_POST['NewPassword']));

         $sql = "UPDATE users SET password = '$NewPassword' WHERE username = '$Username'";
         
         $Message = "Update users Set password = $NewPassword Where username = $Username";
    
         What($Message,"updating password");
    
if(mysqli_query($conn, $sql)){
      $sql = "SELECT * FROM users WHERE username LIKE '$Username' OR Email LIKE '$Email'";
$res = $conn->query($sql);
$rows = array();
while($r = mysqli_fetch_assoc($res)) {
    $rows[] = $r;
}
print json_encode($rows , JSON_UNESCAPED_SLASHES);
} else{
        $data = 'Array[{"Response" : "Cannot connect to Database" }]';
    print json_encode($data);

}
}
else {
  $sql = "UPDATE users SET name = '$Name', Address = '$Address', Email = '$Email', Image = '$Image', Bio = '$Bio' WHERE username = '$Username'";
  
  
  
   $Message = "Update users Set name = $Name, Address = $Address, Email = $Email, Image = $Image, Bio = $Bio Where username = $Username";
    
         What($Message,"updating Account");
         
if(mysqli_query($conn, $sql)){
      $sql = "SELECT * FROM users WHERE username LIKE '$Username'";
$res = $conn->query($sql);
$rows = array();
while($r = mysqli_fetch_assoc($res)) {
    $rows[] = $r;
}
print json_encode($rows , JSON_UNESCAPED_SLASHES);
} else{
        $data = 'Array[{"Response" : "Cannot connect to Database" }]';
    print json_encode($data);

}

}
}
else{
    if (isset($_POST['Username']) && isset($_POST['ConvertMe'])) {
        
    		$subject = "Your request to be an artist has been recieved";
		$message = "
	
This is an automatic email, please do not reply. 
 
Thank for stepping up and wanting to be part of our creators family, We have recieved your request
And will be reviewing it soon, please be sure to read the terms and conditions of publishing and wish you amazing luck.

Best Wishes

"; 
         $Username = strip_tags($_POST['Username']);
         $sql = "SELECT * FROM users WHERE username LIKE '$Username'";
        $res = $conn->query($sql);
        $rows = array();
        $Email = "";
        while($r = mysqli_fetch_assoc($res)) {
                $rows[] = $r;
                $Email = $r['Email'];
        }
        
		$headers = 'From:support@Jrnan.info'."\r\n"; // Set from headers
		mail($Email, $subject, $message, $headers); // Send our email
		
       
        $sql = "UPDATE users SET usertype = 1  WHERE username = '$Username'";
        $res = $conn->query($sql);
        echo "Done";
}
}
}
?>
