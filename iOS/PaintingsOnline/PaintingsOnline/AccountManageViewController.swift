//
//  AccountManageViewController.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 11/4/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit
import Alamofire
import Foundation
import MessageUI
import ImagePicker

class AccountManageViewController: UIViewController , MFMailComposeViewControllerDelegate , UIImagePickerControllerDelegate, UINavigationControllerDelegate , ImagePickerDelegate , UITextViewDelegate{

    @IBOutlet weak var NameFireld: UITextField!
    
    @IBOutlet weak var EmailAddress: UITextField!
    
    @IBOutlet weak var AddressText: UITextField!
    
    
    @IBOutlet weak var SwitchToArtistButton: UIButton!
    
    @IBOutlet weak var NewPassword: UITextField!
    
    @IBOutlet weak var ConfirmPassword: UITextField!
    
    
    @IBOutlet weak var SellingPanelButton: UIButton!
    
    @IBOutlet weak var ModifyButtonsButton: UIButton!
    
    var Usernamestr : String = ""
    var Namestr : String = ""
    var Emailstr : String = ""
    var Addressstr : String = ""
    var Passwordstr : String = ""
    var UserTypestr : String = ""
    var NewPasswordstr :String = ""
    var ConfirmPasswordstr : String = ""
    var BioTxt : String = ""
    let imagePicker = UIImagePickerController()
    let imagePickerController = ImagePickerController()
    var SelctedImages : [UIImage] = [UIImage]()
    
    @IBOutlet weak var ImageView1: UIImageView!
    func wrapperDidPress(_ imagePicker: ImagePickerController, images: [UIImage]) {
    }
    
    func doneButtonDidPress(_ imagePicker: ImagePickerController, images: [UIImage]) {
        SelctedImages = images
        ImageView1.image = images[0]
        imagePickerController.dismiss(animated: true, completion: nil)
    }
    
    func cancelButtonDidPress(_ imagePicker: ImagePickerController) {
        imagePickerController.dismiss(animated: true, completion: nil)
    }
    
    
    func textViewDidBeginEditing(textView: UITextView) {
        BioText.text = String()
    }
    
    @IBAction func LoadImageTap(_ sender: UIButton) {
        
        imagePickerController.delegate = self as! ImagePickerDelegate
        present(imagePickerController, animated: true, completion: nil)
    }
    var MainURL = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        MainURL = UserDefaults.standard.string(forKey: "MainURL")!
        Usernamestr = UserDefaults.standard.value(forKey: "username") as! String
        Emailstr = UserDefaults.standard.value(forKey: "Email") as! String
        Namestr = UserDefaults.standard.value(forKey: "Name") as! String
        Addressstr = UserDefaults.standard.value(forKey: "Address") as! String
        Passwordstr = UserDefaults.standard.value(forKey: "Password") as! String
        BioTxt = UserDefaults.standard.value(forKey: "BioTxt") as! String
        
        if(UserDefaults.standard.value(forKey: "UserType") as! String == "0"){
            ModifyButtonsButton.isHidden = true
            SellingPanelButton.isHidden = true
            SwitchToArtistButton.isHidden = false
        }
        else {
            ModifyButtonsButton.isHidden = false
            SellingPanelButton.isHidden = false
            SwitchToArtistButton.isHidden = true
        }
        
        NameFireld.text = Namestr
        EmailAddress.text = Emailstr
        AddressText.text = Addressstr
        NewPassword.text = Passwordstr
        ConfirmPassword.text = Passwordstr
        BioText.text = BioTxt
        imagePicker.delegate = self
        BioText.delegate = self
    }
    

    
    
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(true, animated: animated)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        navigationController?.setNavigationBarHidden(false, animated: animated)
    }
    
    @IBAction func LogOut(_ sender: Any) {
       resetDefaults()
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
    
    var textViewClearedOnInitialEdit = false
    func textViewDidBeginEditing(_ textView: UITextView) {
        if (self.BioText.text == BioTxt)  {
            self.BioText.text = ""
        }
    }
    
    @IBAction func AddPost(_ sender: Any) {
        var AlertMessage : String = ""
     //   self.showSpinner1(onView: view)
        var ImageURL : [String] = [String]()
         //   showSpinner(onView: view)
                // the image in UIImage type
            let image = resize(ImageView1.image!)
                
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
                        print(responseString)
                        ImageURL.append(responseString)
                        if (ImageURL.count == 1) {
                            self.UpdateDetails(UIImag: ImageURL[0])
                        }
                    }
                }).resume()
            }
    
    @IBOutlet weak var BioText: UITextView!
    
    func UpdateDetails(UIImag : String) {
        let request = NSMutableURLRequest(url: NSURL(string: MainURL + "/UpdateAccount.php")! as URL)
        request.httpMethod = "POST"
        let postString = "Username=\(Usernamestr)&Password=\(Passwordstr)&Address=\(AddressText.text!)&Email=\(EmailAddress.text!)&Mac=\(String(describing: UIDevice.current.identifierForVendor!.uuidString))&Name=\(NameFireld.text!)&Image=\(UIImag)&Bio=\(BioText.text!)"
        request.setValue("application/x-www-form-urlencoded; charset=utf-8", forHTTPHeaderField: "Content-Type")
        request.httpBody = postString.data(using: String.Encoding.utf8)
        let task = URLSession.shared.dataTask(with: request as URLRequest) { data, response, error in
            guard let data = data else { return }
              print(String(data: data, encoding: String.Encoding.utf8) as String!)
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    self.Usernamestr = data.username!
                    self.Namestr = data.name!
                    self.Emailstr = data.Email!
                    self.Addressstr = data.Address!
                    self.UserTypestr = data.usertype!
                    self.BioTxt = data.Bio!
                    UserDefaults.standard.set(self.Usernamestr, forKey: "username")
                    UserDefaults.standard.set(self.Emailstr, forKey: "Email")
                    UserDefaults.standard.set(self.Namestr, forKey: "Name")
                    UserDefaults.standard.set(self.Addressstr, forKey: "Address")
                    UserDefaults.standard.set(self.UserTypestr, forKey: "UserType")
                    UserDefaults.standard.set(data.verified!, forKey: "vertified")
                    UserDefaults.standard.set(self.BioTxt, forKey: "BioTxt")
                }
            } catch let err {
                print("Err", err)
                
            }
            }
        task.resume()
        
        
    }
    
    
    @IBAction func UpdatePassword(_ sender: Any) {
        if (self.NewPassword.text! == self.ConfirmPassword.text!) {
            let request = NSMutableURLRequest(url: NSURL(string: MainURL + "/UpdateAccount.php")! as URL)
            request.httpMethod = "POST"
            let postString = "Username=\(Usernamestr)&Password=\(Passwordstr)&Address=\(AddressText.text!)&Email=\(EmailAddress.text!)&Mac=\(String(describing: UIDevice.current.identifierForVendor!.uuidString))&Name=\(NameFireld.text!)&NewPassword=\(self.NewPassword.text!)"
            print(postString)
            request.setValue("application/x-www-form-urlencoded; charset=utf-8", forHTTPHeaderField: "Content-Type")
            request.httpBody = postString.data(using: String.Encoding.utf8)
            let task = URLSession.shared.dataTask(with: request as URLRequest) { data, response, error in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    self.Usernamestr = data.username!
                    self.Namestr = data.name!
                    self.Emailstr = data.Email!
                    self.Addressstr = data.Address!
                    self.UserTypestr = data.usertype!
                    UserDefaults.standard.set(self.Usernamestr, forKey: "username")
                    UserDefaults.standard.set(self.Emailstr, forKey: "Email")
                    UserDefaults.standard.set(self.Namestr, forKey: "Name")
                    UserDefaults.standard.set(self.Addressstr, forKey: "Address")
                    UserDefaults.standard.set(self.UserTypestr, forKey: "UserType")
                    UserDefaults.standard.set(data.verified!, forKey: "vertified")
                }
            } catch let err {
                print("Err", err)
                
            }
        }
        task.resume()
        }
        else {
            let alert = UIAlertView()
            alert.title = "Passowrds do not match"
            alert.message = "Please insure that the passwords match"
            alert.addButton(withTitle: "understood")
            alert.show()
        }
    }
    
    @IBAction func SendMail(_ sender: Any) {
        let email = "admin@jrnan.info"
        if let url = URL(string: "mailto:\(email)") {
            if #available(iOS 10.0, *) {
                UIApplication.shared.open(url)
            } else {
                UIApplication.shared.openURL(url)
            }
        }
    }
    
    func isKeyPresentInUserDefaults(key: String) -> Bool {
        return UserDefaults.standard.object(forKey: key) != nil
    }
    
    override func touchesBegan(_ touches: Set<UITouch>,
                               with event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    func resetDefaults() {
        UserDefaults.standard.removeObject(forKey:"username")
        UserDefaults.standard.removeObject(forKey: "Email")
        UserDefaults.standard.removeObject( forKey: "Name")
        UserDefaults.standard.removeObject(forKey: "Address")
        UserDefaults.standard.removeObject(forKey: "UserType")
        UserDefaults.standard.removeObject( forKey: "verified")
        UserDefaults.standard.removeObject(forKey: "BioTxt")
        Goback((Any).self)
    }
    
    func Goback(_ sender: Any) {
        navigationController?.popToRootViewController(animated: true)
    }
    

}
