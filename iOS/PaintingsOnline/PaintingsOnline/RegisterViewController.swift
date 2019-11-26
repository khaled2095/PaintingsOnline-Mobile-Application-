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
    
    
    @IBAction func ShowAgreement(_ sender: Any) {
        var message = "By accessing or using the Services and systems of Paintings Online (Including the UI interactions available in IOS and Android Operating systems) and their communications with the service hosting the service, you agree to the concluded terms and conditions of use:"
        
        message.append("\n\n"+"1- The logo, design and software used is in whole an ownership of its respected owners and the user will only be allowed to interact with the system without copying, distribution or manipulation of the source code and Company Identity.")

        message.append("\n\n"+"2- Paintings Online is solely and agent/Medium/connection between the Artists and their peers of buyers and does not hold any Legal liability (Civil/Criminal/Enforcement or notarial form), and the uploaders/sellers of painting should take the full responsibility in case of any copyright/fraud/theft.")
        
        message.append("\n\n"+"3- In addition to term number 2, Paintings Online has the uncontrolled power to Suspend, cancel, Delete, or unapprove any account regardless of its nature (User/Artist) to what it sees suitable for the best interest of the Artist community.")
        
        message.append("\n\n"+"4- Paintings Online, Is a free to use service and is provided as is with no usage warranties and can be discontinued at any given time without prior notice.")
        
        message.append("\n\n"+"5- In case of any Damage/Loss/miss-location or Extreme delay of goods and shipments, The insurance company agreed with will handle the expenses and compensations caused to the user.")
        message.append("\n\n"+"6- The User might not use any (Loophole, Glitch, Unintended feature, Flaw, miscommunication) found in the system to his own unlawful gain (This will be judged by commonsense and to what the law define as electrical fraud or by what marked by society as unlawful).")
        message.append("\n\n"+"7- Information collected through the app might be shared with third parties, Including Shipping companies and payment gateways but will not be shared with advertisement companies and any company that is expected to misuse the information.")
        message.append("\n\n"+"8- This app is developed and executed for educational purposes and the ownership of its content is preserved to its original developers.")
        
        let alert = UIAlertController(title: "User Agreement", message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Agree", style: .default, handler: nil))
            present(alert, animated: true, completion: nil)
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
