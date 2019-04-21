<?php

$conn = mysqli_connect('localhost','jrnaninf_Paint', 'Paint','jrnaninf_paintings');
if (isset($_GET['Username']) && isset($_GET['Password']) && isset($_GET['Address']) &&isset($_GET['Email']) && isset($_GET['Name']) && isset($_GET['Usertype'])) {
    $Username = strip_tags($_GET['Username']);
    $Password = SHA1(strip_tags($_GET['Password']));
    $Address = strip_tags($_GET['Address']);
    $Email = strip_tags($_GET['Email']);
    $Name = strip_tags($_GET['Name']);
    $Usertype = strip_tags($_GET['Usertype']);

    $sql = "SELECT * FROM users WHERE username LIKE '$Username' OR Email LIKE '$Email'";
$res = $conn->query($sql);
if (mysqli_num_rows($res) > 0 ) {
       $sql = "SELECT * FROM users WHERE id LIKE 100";
  $res = $conn->query($sql);
$rows = array();
while($r = mysqli_fetch_assoc($res)) {
    $rows[] = $r;
}
print json_encode($rows , JSON_UNESCAPED_SLASHES);

}
 else {
      $sql = "INSERT INTO users (username, name, Email, Address, password, usertype)
    VALUES ('$Username','$Name','$Email','$Address','$Password','$Usertype')";

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
}
else {
    $data = 'Array[{"Response" : "Missing Details" }]';
    print json_encode($data);
}

?>
