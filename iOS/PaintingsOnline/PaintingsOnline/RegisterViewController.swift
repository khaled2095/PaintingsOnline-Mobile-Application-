//
//  RegisterViewController.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 2/4/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit

class RegisterViewController: UIViewController {

    @IBOutlet weak var Username: UITextField!
    @IBOutlet weak var FullName: UITextField!
    @IBOutlet weak var Email: UITextField!
    @IBOutlet weak var Address: UITextField!
    @IBOutlet weak var Password: UITextField!
    var Response : String = ""
    var UserType : Int = 0
    @IBOutlet weak var GobackBtn: UIButton!
    @IBOutlet weak var lblResponse: UILabel!
    @IBOutlet weak var Segments: UISegmentedControl!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        var frameRect : CGRect = Username.frame;
        frameRect.size.height = 40; // <-- Specify the height you want here.
       
        Username.frame = frameRect;
        // Do any additional setup after loading the view.
    }
    
    @IBAction func ChangeUserType(_ sender: Any) {
        if (Segments.selectedSegmentIndex == 0 ){
            UserType = 0
        }
        else {
            UserType = 1
        }
        
    }
    
    
    override func touchesBegan(_ touches: Set<UITouch>,
                               with event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    @IBAction func RegisterMe(_ sender: Any) {
        self.showSpinner(onView: self.view)
        var URL1 = "https://jrnan.info/Painting/Register.php?Username=" + Username.text! + "&Password=" + Password.text! + "&Address=" + Address.text! + "&Email=" + Email.text! + "&Name=" + FullName.text! + "&Usertype="
        URL1.append(String(UserType))
        guard let gitUrl = URL(string: URL1.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    self.removeSpinner()
                    var Username1 : String = data.username as! String
                    if (Username1 == "Null") {
                        DispatchQueue.main.async {
                         self.GobackBtn.setTitle("sorry, username or email already exist", for: .normal)
                         self.GobackBtn.isHidden = false
                        self.GobackBtn.isEnabled = false
                        }
                    }
                    else {
                        DispatchQueue.main.async {
                        self.GobackBtn.setTitle("Welcome to the family, you can log in now", for: .normal)
                        self.GobackBtn.isHidden = false
                        self.GobackBtn.isEnabled = true
                        }
                    }
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
        
    }
    

     @IBAction func Goback(_ sender: Any) {
            navigationController?.popToRootViewController(animated: true)
     }
    
   /*
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
