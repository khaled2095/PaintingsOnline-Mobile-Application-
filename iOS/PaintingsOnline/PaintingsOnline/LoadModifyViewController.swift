//
//  LoadModifyViewController.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 17/9/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit
import ImageSlideshow
import iOSDropDown
import ImagePicker
import Alamofire
import AlamofireImage

class LoadModifyViewController: UIViewController , ImagePickerDelegate , UIImagePickerControllerDelegate, UINavigationControllerDelegate , UITextFieldDelegate   {
    
    func wrapperDidPress(_ imagePicker: ImagePickerController, images: [UIImage]) {
    }
    var MainURL = ""
    @IBAction func DeletePainting(_ sender: Any) {
        var URL1 = MainURL + "/Update.php?painting_id=" + CartID + "&Delete="
        URL1 += "&Mac=" + String(describing: UIDevice.current.identifierForVendor!.uuidString)
        guard let gitUrl = URL(string: URL1.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
            } catch let err {
                print("Err", err)
            }
            self.removeSpinner()
            }.resume()
        
    }
    
    func doneButtonDidPress(_ imagePicker: ImagePickerController, images: [UIImage]) {
        SelctedImages = images
        ImagesReady.removeAll()
        var Imagezz : [InputSource] = [InputSource]()
        for imagez in images {
            Imagezz.append(ImageSource(image: imagez))
            ImagesReady.append(imagez)
        }
        SliderShow.setImageInputs(Imagezz)
        imagePickerController.dismiss(animated: true, completion: nil)
    }
    
    func cancelButtonDidPress(_ imagePicker: ImagePickerController) {
        imagePickerController.dismiss(animated: true, completion: nil)
    }
    var SelctedImages : [UIImage] = [UIImage]()
     let imagePickerController = ImagePickerController()
    let imagePicker = UIImagePickerController()
    @IBOutlet weak var SliderShow: ImageSlideshow!
    @IBOutlet weak var ArtworkName: UITextField!
    
    @IBOutlet weak var Category: DropDown!
    @IBOutlet weak var Room: DropDown!
    @IBOutlet weak var RoomTxt: UITextField!
    @IBOutlet weak var CategoryTxt: UITextField!
    
    @IBOutlet weak var ArtWorkDescription: UITextView!
    var Images : [InputSource] = [InputSource]()
    var ImagesReady : [UIImage] = [UIImage]()
    var paintingURl = [String]()
    var SizesHolder : [Sizes3] = [Sizes3]()
    var CartID : String! = ""
    var tmpListOfTitle : [String] = []
    var SelectedIndex : Int = 0
    var DidUpload : Bool = false
    var ListOfSizes = [SizeFull]()
    var tmpListOfTitleRoom : [String] = []
    var SelectedIndexRoom : Int = 0
    @IBOutlet weak var Tap: UIView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        MainURL = UserDefaults.standard.string(forKey: "MainURL")!
        let tap = UITapGestureRecognizer(target: self, action: #selector(self.LoadImageTap(_:)))
        Tap.addGestureRecognizer(tap)
        
        Tap.layer.cornerRadius = 5.0
        Tap.layer.borderWidth = 5.0
        
        Tap.layer.borderColor = UIColor.clear.cgColor
        Tap.layer.masksToBounds = true
        
        Tap.layer.shadowColor = UIColor.gray.cgColor
        Tap.layer.shadowOffset = CGSize(width: 0, height: 2.0)
        Tap.layer.shadowRadius = 2.0
        Tap.layer.shadowOpacity = 1.0
        Tap.layer.masksToBounds = false
        Tap.layer.shadowPath = UIBezierPath(roundedRect:Tap.bounds, cornerRadius:Tap.layer.cornerRadius).cgPath
        
        
        
        
        CartID = UserDefaults.standard.string(forKey: "CartIDFull")
        LoadImages()
        LoadSizes()
        SearchMe()
        LoadCategories()
        LoadRooms()
    }
    
    
    
    func LoadCategories() {
        var URL1 = MainURL + "/ShowCategory.php"
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    self.tmpListOfTitle.append((data.Name as! String))
                    self.Category.optionArray = self.tmpListOfTitle
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    
    
    func LoadRooms() {
        var URL1 = MainURL + "/ShowRoom.php"
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    self.tmpListOfTitleRoom.append((data.Name as! String))
                    self.Room.optionArray = self.tmpListOfTitleRoom
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    
    
    @IBAction func LoadImageTap(_ sender: UIButton) {
        imagePickerController.delegate = self as! ImagePickerDelegate
        present(imagePickerController, animated: true, completion: nil)
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
    
    func LoadImages () {
        var URL1 = MainURL + "/LoadPictures.php?painting_id="
        URL1.append(CartID!)
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    self.paintingURl.append(data.painting_url!)
                    var ImageTmp =
                    self.Images.append(ImageSource(image: UIImage(named:"empty-image")!))
                    self.addImages()
                }
                for (index, element) in self.paintingURl.enumerated() {
                    Alamofire.request(element).responseImage { response in
                        if let image = response.result.value {
                            DispatchQueue.main.async {
                                self.Images[index] = (ImageSource(image: image))
                                self.ImagesReady.append(image)
                                self.addImages()
                            }
                        }
                    }
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    
    func addImages(){
        DispatchQueue.main.async {
            self.SliderShow.setImageInputs(self.Images)
            self.SelctedImages =  self.ImagesReady
        }
    }
    func SearchMe () {
        var URL1 = MainURL + "/ShowPaintings.php?Search="
        URL1.append(CartID!)
        print(URL1)
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    DispatchQueue.main.async {
                        self.ArtworkName.text = data.painting_name!
                        self.ArtWorkDescription.text = data.painting_description!
                    }
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    
    
    func LoadSizes () {
        var URL1 = MainURL + "/LoadSizes.php?painting_id="
        URL1.append(CartID!)
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                var FullSizes = [SizeFull]()
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    var tmpCell = SizeFull()
                    tmpCell.Price = data.Price!
                    tmpCell.Size = data.Size!
                    tmpCell.Quantity = data.Quantity!
                    FullSizes.append(tmpCell)
                    UserDefaults.standard.setStructArray(FullSizes, forKey: "fullSizes")
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    
    
    
    func AddToServer(URL2 : [String]) {
        var Sizes = ""
        var Prices = ""
        var Quantities = ""
        for Sizess in ListOfSizes {
            Sizes += Sizess.Size + ","
            Prices += Sizess.Price + ","
            Quantities += Sizess.Quantity + ","
        }
        var URL3 : String = ""
        for URLZ in URL2 {
            URL3 += URLZ
            URL3 += ","
        }
        
        var URL1 = MainURL + "/Update.php?painting_id=" + CartID + "&painting_url=" + URL3 + "&painting_name=" + ArtworkName.text! + "&painting_price=" + Prices + "&painting_description=" + ArtWorkDescription.text!
        URL1.append("&painting_artist=" + (UserDefaults.standard.value(forKey: "username") as! String))
        URL1.append("&Password=" + (UserDefaults.standard.value(forKey: "Password")as! String))
        URL1.append("&Painting_category=" + String(SelectedIndex))
        URL1.append("&Room=" + String(SelectedIndexRoom))
        URL1.append("&Size=" + Sizes)
        URL1.append("&Quantity=" + Quantities)
        URL1 += "&Mac=" + String(describing: UIDevice.current.identifierForVendor!.uuidString)
        print(URL1)
        guard let gitUrl = URL(string: URL1.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                print(String(decoding: data, as: UTF8.self))
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
            self.removeSpinner()
            }.resume()
    }
    
    public func  convertImageToBase64String(image : UIImage ) -> String
    {
        let strBase64 =  image.pngData()!.base64EncodedString()
        return strBase64
    }
    
    func isKeyPresentInUserDefaults(key: String) -> Bool {
        return UserDefaults.standard.object(forKey: key) != nil
    }
    
    @IBAction func Update(_ sender: Any) {
        
        if (isKeyPresentInUserDefaults(key: "fullSizes")){
            ListOfSizes  = UserDefaults.standard.structArrayData(SizeFull.self, forKey: "fullSizes")
        }
        
        var AlertMessage : String = ""
        
        self.showSpinner1(onView: view)
        var ImageURL : [String] = [String]()
        
        if (ArtworkName.text == "" ) {
            AlertMessage.append("Painting Name is required")
        }
        if (ListOfSizes.count == 0 ) {
            AlertMessage.append("\n Please Set at least one size")
        }
        if (ArtWorkDescription.text == "" ) {
            AlertMessage.append("\n You have to descripe the painting")
        }
        if  (SelctedImages.count < 1 ) {
            AlertMessage.append("\n You have to choose a picture")
        }
        if (AlertMessage == ""){
            showSpinner(onView: view)
            for imagez in SelctedImages {
                // the image in UIImage type
                let image = resize(imagez)
                
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
                        if (ImageURL.count == self.SelctedImages.count) {
                            self.AddToServer(URL2: ImageURL)
                        }
                    }
                }).resume()
            }
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
    
    
    
    
    @IBAction func AddPrices(_ sender: Any) {
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}

