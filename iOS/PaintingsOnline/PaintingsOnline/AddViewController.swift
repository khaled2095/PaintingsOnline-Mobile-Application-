import UIKit
import iOSDropDown

class AddViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    @IBOutlet var imageView: UIImageView!
    

    let imagePicker = UIImagePickerController()
    
    @IBOutlet weak var ArtWorkName: UITextField!
    @IBOutlet weak var ArtWorkPrice: UITextField!
    @IBOutlet weak var ArtWorkDescribtion: UITextView!
    @IBOutlet weak var categorytxt: DropDown!
    @IBOutlet weak var Roomtxt: DropDown!
    @IBOutlet weak var Size: UITextField!
    @IBOutlet weak var Quantity: UITextField!
    
    var tmpListOfTitle : [String] = []
    var SelectedIndex : Int = 0
    var DidUpload : Bool = false
    
    var tmpListOfTitleRoom : [String] = []
    var SelectedIndexRoom : Int = 0
  
    
    @IBAction func LoadImageTap(_ sender: UIButton) {
        imagePicker.allowsEditing = false
        imagePicker.sourceType = .photoLibrary
        
        present(imagePicker, animated: true, completion: nil)
    }
 
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow), name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide), name: UIResponder.keyboardWillHideNotification, object: nil)
        
        LoadCategories()
        LoadRooms()
        
        imagePicker.delegate = self
        
        var frameRect : CGRect = ArtWorkName.frame;
        frameRect.size.height = 40; // <-- Specify the height you want here.
        ArtWorkName.frame = frameRect;
        
        frameRect = ArtWorkPrice.frame;
        frameRect.size.height = 40; // <-- Specify the height you want here.
        ArtWorkPrice.frame = frameRect;
        
        frameRect = Size.frame;
        frameRect.size.height = 40; // <-- Specify the height you want here.
        Size.frame = frameRect;
       
        frameRect = Size.frame;
        frameRect.size.height = 40; // <-- Specify the height you want here.
        Size.frame = frameRect;
        
       categorytxt.optionArray = tmpListOfTitle
      categorytxt.optionIds = [1,1,1,1,1,1,1,1,1,1,1,1]
        
        Roomtxt.optionArray = tmpListOfTitleRoom
        Roomtxt.optionIds = [1,1,1,1,1,1,1,1,1,1,1]
        //Its Id Values and its optional
        // The the Closure returns Selected Index and String
       categorytxt.didSelect{(selectedText , index ,id) in
        self.SelectedIndex = index
    }
       Roomtxt.didSelect{(selectedTextRoom , index ,id) in
            self.SelectedIndexRoom = index
        }
    }
    
    func LoadCategories() {
        var URL1 = "https://jrnan.info/Painting/ShowCategory.php"
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    self.tmpListOfTitle.append((data.Name as! String))
                    self.categorytxt.optionArray = self.tmpListOfTitle
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    
    func LoadRooms() {
        var URL1 = "https://jrnan.info/Painting/ShowRoom.php"
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    self.tmpListOfTitleRoom.append((data.Name as! String))
                    self.Roomtxt.optionArray = self.tmpListOfTitleRoom
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    
    
    
    func UpdateMe() {
         if (isKeyPresentInUserDefaults(key: "username")) {
            let URL1 = "https://jrnan.info/Painting/Login.php?Username=" + (UserDefaults.standard.value(forKey: "username") as! String) + "&Password=" +  (UserDefaults.standard.value(forKey: "Password") as! String)
            
        guard let gitUrl = URL(string: URL1.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!) else { return }
        let task = URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    DispatchQueue.main.async { // Correct
                    var Username = data.username!
                    var Name = data.name!
                    var Email = data.Email!
                    var Address = data.Address!
                    var UserType = data.usertype!
                    var verified = data.verified!
                    UserDefaults.standard.set(Username, forKey: "username")
                    UserDefaults.standard.set(Email, forKey: "Email")
                    UserDefaults.standard.set(Name, forKey: "Name")
                    UserDefaults.standard.set(Address, forKey: "Address")
                    UserDefaults.standard.set(UserType, forKey: "UserType")
                    UserDefaults.standard.set(data.verified!, forKey: "verified")
                }
                }
            } catch let err {
                print("Err", err)
            }
        }
        task.resume()
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        UpdateMe()
        categorytxt.isEnabled = false ;
        ArtWorkName.isEnabled = false;
        ArtWorkPrice.isEnabled = false;
        ArtWorkDescribtion.isEditable = false ;
        categorytxt.isEnabled = false ;
        Size.isEnabled = false ;
        Quantity.isEnabled = false;
        Roomtxt.isEnabled = false;
        if (isKeyPresentInUserDefaults(key: "username")) {
            if (String(UserDefaults.standard.value(forKey: "UserType") as! String) != "1"){
                let alert = UIAlertView()
                alert.title = "Invalid cridentials"
                alert.message = "You are logged in with a costumer account please change your account to artist."
                alert.addButton(withTitle: "Understood")
                alert.show()
            }
            else {
                if (String(UserDefaults.standard.value(forKey: "verified") as! String) != "1") {
                    let alert = UIAlertView()
                    alert.title = "Verification needed"
                    alert.message = "You are not vertified yet, we are doing our best to get you onboard."
                    alert.addButton(withTitle: "Understood")
                    alert.show()
                }
                else {
                    categorytxt.isEnabled = true ;
                    ArtWorkName.isEnabled = true;
                    ArtWorkPrice.isEnabled = true;
                    ArtWorkDescribtion.isEditable = true ;
                    categorytxt.isEnabled = true ;
                    Size.isEnabled = true ;
                    Quantity.isEnabled = true;
                    Roomtxt.isEnabled = true;
                    
                }
            }
        }
        else{
            let alert = UIAlertView()
            alert.title = "Invalid cridentials"
            alert.message = "It doesn't seem that you are logged it, please log in with an artist account"
            alert.addButton(withTitle: "Understood")
            alert.show()
        }
    }
    
    @objc func keyboardWillShow(notification: NSNotification) {
        if (Size.isEditing || Quantity.isEditing ){
        if let keyboardSize = (notification.userInfo?[UIResponder.keyboardFrameBeginUserInfoKey] as? NSValue)?.cgRectValue {
            if self.view.frame.origin.y == 0 {
                self.view.frame.origin.y -= keyboardSize.height
            }
        }
        }
    }
    
    @objc func keyboardWillHide(notification: NSNotification) {
        if self.view.frame.origin.y != 0 {
            self.view.frame.origin.y = 0
        }
    }
    
    func isKeyPresentInUserDefaults(key: String) -> Bool {
        return UserDefaults.standard.object(forKey: key) != nil
    }
    
    // MARK: - UIImagePickerControllerDelegate Methods
    
   func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        if let pickedImage = info[UIImagePickerController.InfoKey.originalImage] as? UIImage {
            imageView.contentMode = .scaleAspectFit
            imageView.image = pickedImage
        }
        
        dismiss(animated: true, completion: nil)
    DidUpload = true
    }
    override func touchesBegan(_ touches: Set<UITouch>,
                               with event: UIEvent?) {
        self.view.endEditing(true)
    }
    private func imagePickerControllerDidCancel(picker: UIImagePickerController) {
        dismiss(animated: true, completion: nil)
    }
    
    public func  convertImageToBase64String(image : UIImage ) -> String
    {
        let strBase64 =  image.pngData()!.base64EncodedString()
        return strBase64
    }
    
 
    @IBAction func AddPost(_ sender: Any) {
        var AlertMessage : String = ""
        
        self.showSpinner1(onView: view)
        
        if (ArtWorkName.text == "" ) {
            AlertMessage.append("Painting Name is required")
        }
        if (ArtWorkPrice.text == "" ) {
            AlertMessage.append("\n Painting Price can't be empty")
        }
        if (ArtWorkDescribtion.text == "" ) {
            AlertMessage.append("\n You have to descripe the painting")
        }
        if  (DidUpload == false ) {
            AlertMessage.append("\n You have to choose a picture")
        }
        if (AlertMessage == ""){
     
            
            // the image in UIImage type
            let image = resize(imageView.image as! UIImage)
            
            let filename = "avatar.png"
            
            // generate boundary string using a unique per-app string
            let boundary = UUID().uuidString
            
            let fieldName = "reqtype"
            let fieldValue = "fileupload"
            
            let fieldName2 = "userhash"
            let fieldValue2 = "a1389172b85b4b0261e76a76b"
            
            let config = URLSessionConfiguration.default
            let session = URLSession(configuration: config)
            
            // Set the URLRequest to POST and to the specified URL
            var urlRequest = URLRequest(url: URL(string: "https://catbox.moe/user/api.php")!)
            urlRequest.httpMethod = "POST"
            
            // Set Content-Type Header to multipart/form-data, this is equivalent to submitting form data with file upload in a web browser
            // And the boundary is also set here
            urlRequest.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
            
            var data = Data()
            
            // Add the reqtype field and its value to the raw http request data
            data.append("\r\n--\(boundary)\r\n".data(using: .utf8)!)
            data.append("Content-Disposition: form-data; name=\"\(fieldName)\"\r\n\r\n".data(using: .utf8)!)
            data.append("\(fieldValue)".data(using: .utf8)!)
            
            // Add the userhash field and its value to the raw http reqyest data
            data.append("\r\n--\(boundary)\r\n".data(using: .utf8)!)
            data.append("Content-Disposition: form-data; name=\"\(fieldName2)\"\r\n\r\n".data(using: .utf8)!)
            data.append("\(fieldValue2)".data(using: .utf8)!)
            
            // Add the image data to the raw http request data
            data.append("\r\n--\(boundary)\r\n".data(using: .utf8)!)
            data.append("Content-Disposition: form-data; name=\"fileToUpload\"; filename=\"\(filename)\"\r\n".data(using: .utf8)!)
            data.append("Content-Type: image/png\r\n\r\n".data(using: .utf8)!)
            data.append(image.pngData()!)
            
            // End the raw http request data, note that there is 2 extra dash ("-") at the end, this is to indicate the end of the data
            // According to the HTTP 1.1 specification https://tools.ietf.org/html/rfc7230
            data.append("\r\n--\(boundary)--\r\n".data(using: .utf8)!)
            
            // Send a POST request to the URL, with the data we created earlier
            session.uploadTask(with: urlRequest, from: data, completionHandler: { responseData, response, error in
                if(error != nil){
                    let alert = UIAlertView()
                    alert.title = "Error"
                    alert.message = error!.localizedDescription
                    alert.addButton(withTitle: "Understood")
                    alert.show()
                }
                
                guard let responseData = responseData else {
                    let alert = UIAlertView()
                    alert.title = "Error"
                    alert.message = "couldn't communicate with server"
                    alert.addButton(withTitle: "Understood")
                    alert.show()
                    return
                }
                
                if let responseString = String(data: responseData, encoding: .utf8) {
                    print("uploaded to: \(responseString)")
                    self.AddToServer(URL2: responseString)
                }
            }).resume()
        }
        else {
           removeSpinner1()
        let alert = UIAlertView()
        alert.title = "Missing Details"
        alert.message = AlertMessage
        alert.addButton(withTitle: "Understood")
        alert.show()
            
        }
        self.removeSpinner1()
    }
    
    var vSpinner : UIView?
    
    func AddToServer(URL2 : String) {
        var URL1 = "https://jrnan.info/Painting/testUpload.php?painting_url=" + URL2 + "&painting_name=" + ArtWorkName.text! + "&painting_price=" + ArtWorkPrice.text! + "&painting_description=" + ArtWorkDescribtion.text!
        URL1.append("&painting_artist=" + (UserDefaults.standard.value(forKey: "username") as! String))
        URL1.append("&Password=" + (UserDefaults.standard.value(forKey: "Password")as! String))
        URL1.append("&Painting_category=" + String(SelectedIndex))
        URL1.append("&Room=" + String(SelectedIndexRoom))
        URL1.append("&Size=" + Size.text!)
        URL1.append("&Quantity=" + Quantity.text!)
        print(URL1)
        guard let gitUrl = URL(string: URL1.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                 DispatchQueue.main.async { // Correct
                if (data.count == 0) {
                    let alert = UIAlertView()
                    alert.title = "Error"
                    alert.message = "We couldnt finalise your request"
                    alert.addButton(withTitle: "Understood")
                    alert.show()
                }
                else {
                    let alert = UIAlertView()
                    alert.title = "success"
                    alert.message = "successfully uploaded the image"
                    alert.addButton(withTitle: "Understood")
                    alert.show()
                }
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    
    func resize(_ image: UIImage) -> UIImage {
        var actualHeight = Float(image.size.height)
        var actualWidth = Float(image.size.width)
        let maxHeight: Float = 400.0
        let maxWidth: Float = 400.0
        var imgRatio: Float = actualWidth / actualHeight
        let maxRatio: Float = maxWidth / maxHeight
        let compressionQuality: Float = 0.5
        //50 percent compression
        if actualHeight > maxHeight || actualWidth > maxWidth {
            if imgRatio < maxRatio {
                //adjust width according to maxHeight
                imgRatio = maxHeight / actualHeight
                actualWidth = imgRatio * actualWidth
                actualHeight = maxHeight
            }
            else if imgRatio > maxRatio {
                //adjust height according to maxWidth
                imgRatio = maxWidth / actualWidth
                actualHeight = imgRatio * actualHeight
                actualWidth = maxWidth
            }
            else {
                actualHeight = maxHeight
                actualWidth = maxWidth
            }
        }
        let rect = CGRect(x: 0.0, y: 0.0, width: CGFloat(actualWidth), height: CGFloat(actualHeight))
        UIGraphicsBeginImageContext(rect.size)
        image.draw(in: rect)
        let img = UIGraphicsGetImageFromCurrentImageContext()
        let imageData = img?.jpegData(compressionQuality: CGFloat(compressionQuality))
        UIGraphicsEndImageContext()
        return UIImage(data: imageData!) ?? UIImage()
    }
    
    }




extension UIImage {
    var png: Data? {
        return self.pngData()
    }
}

  var vSpinner1 : UIView?

extension UIViewController {
    func showSpinner1(onView : UIView) {
        let spinnerView = UIView.init(frame: onView.bounds)
        spinnerView.backgroundColor = UIColor.init(red: 0.5, green: 0.5, blue: 0.5, alpha: 0.5)
        let ai = UIActivityIndicatorView.init(style: .whiteLarge)
        ai.startAnimating()
        ai.center = spinnerView.center
        
        DispatchQueue.main.async {
            spinnerView.addSubview(ai)
            onView.addSubview(spinnerView)
        }
        
        vSpinner1 = spinnerView
    }
    
    
    
    func removeSpinner1() {
        DispatchQueue.main.async {
            vSpinner1?.removeFromSuperview()
            vSpinner1 = nil
        }
    }
}


