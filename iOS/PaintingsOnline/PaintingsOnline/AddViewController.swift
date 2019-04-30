import UIKit

class AddViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    @IBOutlet var imageView: UIImageView!
    
    let imagePicker = UIImagePickerController()
    
    @IBOutlet weak var ArtWorkName: UITextField!
    @IBOutlet weak var ArtWorkPrice: UITextField!
    @IBOutlet weak var ArtWorkDescribtion: UITextView!
    @IBOutlet weak var txtCategory: UITextField!
    @IBOutlet weak var artistname: UITextField!
    @IBOutlet weak var password: UITextField!
    
    var DidUpload : Bool = false
    
    @IBAction func LoadImageTap(_ sender: UIButton) {
        imagePicker.allowsEditing = false
        imagePicker.sourceType = .photoLibrary
        
        present(imagePicker, animated: true, completion: nil)
    }
 
    override func viewDidLoad() {
        super.viewDidLoad()
        
        imagePicker.delegate = self
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
            guard let image = imageView.image else { return  }
            
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
        let alert = UIAlertView()
        alert.title = "Missing Details"
        alert.message = AlertMessage
        alert.addButton(withTitle: "Understood")
        alert.show()
        }
    }
    
    
    
    func AddToServer(URL2 : String) {
        var URL1 = "https://jrnan.info/Painting/testUpload.php?painting_url=" + URL2 + "&painting_name=" + ArtWorkName.text! + "&painting_price=" + ArtWorkPrice.text! + "&painting_description=" + ArtWorkDescribtion.text! + "&painting_artist=" + artistname.text! + "&Painting_category=" + txtCategory.text! + "&Password=" + password.text!
        print (URL1);
        
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    }




extension UIImage {
    var png: Data? {
        return self.pngData()
    }
}
