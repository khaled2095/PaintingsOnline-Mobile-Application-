<?php header ('Content-type: text/html; charset=utf-8');
require_once 'config.php';
include 'Admin/Discount.php';
require_once 'Log.php';
if (isset($_GET['Category'])) {
    $Category = strip_tags($_GET['Category']);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sql = "SELECT * FROM paintings WHERE Painting_category LIKE $Category AND Verified = 1 AND Quantity > 0 ORDER BY Painting_date DESC";


   $Message = "Select * From paintings Where Painting_category Like $Category And Verified = 1 And Quantity > 0 Order By Painting_date Desc";
   
          What($Message,"Loading Paintings");
          
          
$res = $conn->query($sql);
$rows = array();
while($r = mysqli_fetch_assoc($res)) {
    $rows[] = $r;
}

foreach($rows as $key => $value)
{
    $rows[$key]['painting_price'] = strval(round($rows[$key]['painting_price'] * ((100 - $Discount) / 100)));
}

print json_encode($rows , JSON_UNESCAPED_SLASHES);
} 
else if (isset($_GET['Room'])) {
    $Room = strip_tags($_GET['Room']);


if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sql = "SELECT * FROM paintings WHERE Room LIKE $Room AND Verified = 1 AND Quantity > 0 ORDER BY Painting_date DESC";


$Message = "Select * From paintings Where Room Like $Room And Verified = 1 And Quantity > 0 Order By Painting_date Desc";
   
    What($Message,"Loading Paintings");
    
    
$res = $conn->query($sql);
$rows = array();
while($r = mysqli_fetch_assoc($res)) {
    $rows[] = $r;
}

foreach($rows as $key => $value)
{
   $rows[$key]['painting_price'] = strval(round($rows[$key]['painting_price'] * ((100 - $Discount) / 100)));
}

print json_encode($rows , JSON_UNESCAPED_SLASHES);
}

else if (isset($_GET['Search'])) {
    $Search = strip_tags($_GET['Search']);
    
     if (isset($_GET['Type'])) {
        $TmpType = strip_tags($_GET['Type']);
        $Type = urlencode($TmpType);
        if ($Type == "Featured") {

        if ($conn->connect_error) {
            die("Connection failed: " . $conn->connect_error);
        }
        $sql = "SELECT * FROM paintings WHERE Featured LIKE 1 AND Quantity > 0 ORDER BY Painting_date DESC";
        
        $res = $conn->query($sql);
        $rows = array();
        while($r = mysqli_fetch_assoc($res)) {
            $rows[] = $r;
        }

        foreach($rows as $key => $value)
        {
            $rows[$key]['painting_price'] = strval(round($rows[$key]['painting_price'] * ((100 - $Discount) / 100)));
        }


        print json_encode($rows , JSON_UNESCAPED_SLASHES);
        }
        
        if ($Type == "BestSelling") {


        if ($conn->connect_error) {
            die("Connection failed: " . $conn->connect_error);
        }
        $sql = "SELECT * FROM paintings WHERE Quantity > 0 ORDER BY BuyCount DESC LIMIT 5";
        
     
        $res = $conn->query($sql);
        $rows = array();
        while($r = mysqli_fetch_assoc($res)) {
            $rows[] = $r;
        }

        foreach($rows as $key => $value)
        {
            $rows[$key]['painting_price'] = strval(round($rows[$key]['painting_price'] * ((100 - $Discount) / 100)));
        }


        print json_encode($rows , JSON_UNESCAPED_SLASHES);
        }

        }
        
        
    else {
    $specialSearch = "";
    if ($specialSearch != ""){
    $formdata = array(
	      'id' => rand(),
	      'Search' => $specialSearch
	   );
	   
    $myFile = "Search.JSON";
       //Get data from existing json file
	   $jsondata = file_get_contents($myFile);

	   // converts json data into array
	   $arr_data = json_decode($jsondata, true);
	   
	   // Push user data to array
	   
	   array_push($arr_data,$formdata);
	   
       //Convert updated array to JSON
	   $jsondata = json_encode($arr_data, JSON_PRETTY_PRINT);
	   
	   //write json data into data.json file
	   if(file_put_contents($myFile, $jsondata)) {
	    } 
    }
	    
    
	   
  


  

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sql = "SELECT * FROM paintings WHERE painting_description LIKE '%$Search%' AND Quantity > 0 OR painting_artist LIKE '%$Search%' AND Quantity > 0 OR painting_name LIKE '%$Search%' AND Quantity > 0 OR  painting_id LIKE '%$Search%' AND Quantity > 0 ORDER BY Painting_date DESC";


 $Message = "Select * From paintings Where painting_description Like '%$Search%' And Quantity > 0 Or painting_artist Like '%$Search%' And Quantity > 0 Or painting_name Like '%$Search%' And Quantity > 0 Or  painting_id Like '%$Search%' And Quantity > 0 Order By Painting_date Desc";
 
        What($Message,"Searched Paintings");
        
        
$res = $conn->query($sql);
$rows = array();
while($r = mysqli_fetch_assoc($res)) {
    $rows[] = $r;
}

foreach($rows as $key => $value)
{
   $rows[$key]['painting_price'] = strval(round($rows[$key]['painting_price'] * ((100 - $Discount) / 100)));
}


print json_encode($rows , JSON_UNESCAPED_SLASHES);
}
}


else {


if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sql = "SELECT * FROM paintings WHERE Verified = 1 ORDER BY Painting_date DESC";

 $Message = "Select * From paintings Where Verified = 1 Order By Painting_date Desc";
     What($Message,"Loaded Paintings");
        
$res = $conn->query($sql);
$rows = array();
while($r = mysqli_fetch_assoc($res)) {
    $rows[] = $r;
}

foreach($rows as $key => $value)
{
   $rows[$key]['painting_price'] = strval(round($rows[$key]['painting_price'] * ((100 - $Discount) / 100)));
}


print json_encode($rows , JSON_UNESCAPED_SLASHES);
}

?>
