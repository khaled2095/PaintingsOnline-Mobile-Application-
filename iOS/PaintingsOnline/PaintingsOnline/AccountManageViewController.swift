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

class AccountManageViewController: UIViewController , MFMailComposeViewControllerDelegate {

    @IBOutlet weak var NameFireld: UITextField!
    
    @IBOutlet weak var EmailAddress: UITextField!
    
    @IBOutlet weak var AddressText: UITextField!
    
    
    @IBOutlet weak var NewPassword: UITextField!
    
    @IBOutlet weak var ConfirmPassword: UITextField!
    
    var Usernamestr : String = ""
    var Namestr : String = ""
    var Emailstr : String = ""
    var Addressstr : String = ""
    var Passwordstr : String = ""
    var UserTypestr : String = ""
    var NewPasswordstr :String = ""
    var ConfirmPasswordstr : String = ""
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        Usernamestr = UserDefaults.standard.value(forKey: "username") as! String
        Emailstr = UserDefaults.standard.value(forKey: "Email") as! String
        Namestr = UserDefaults.standard.value(forKey: "Name") as! String
        Addressstr = UserDefaults.standard.value(forKey: "Address") as! String
        Passwordstr = UserDefaults.standard.value(forKey: "Password") as! String
        
        
        NameFireld.text = Namestr
        EmailAddress.text = Emailstr
        AddressText.text = Addressstr
        NewPassword.text = Passwordstr
        ConfirmPassword.text = Passwordstr
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
    
    @IBAction func UpdateDetails(_ sender: Any) {
        let URL1 : String = "https://jrnan.info/Painting/UpdateAccount.php?Username=" +  Usernamestr + "&Password=" + Passwordstr + "&Address=" + AddressText.text! + "&Email=" + EmailAddress.text! + "&Name=" + NameFireld.text!

        guard let gitUrl = URL(string: URL1.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!)  else {
            return }
        
       let task =  URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
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
    
    
    @IBAction func UpdatePassword(_ sender: Any) {
        if (self.NewPassword.text! == self.ConfirmPassword.text!) {
        let URL1 : String = "https://jrnan.info/Painting/UpdateAccount.php?Username=" +  Usernamestr + "&Password=" + Passwordstr + "&Address=" + AddressText.text! + "&Email=" + EmailAddress.text! + "&Name=" + NameFireld.text! + "&NewPassword=" + self.NewPassword.text!
        
        guard let gitUrl = URL(string: URL1.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!)  else {
            return }
            
            DispatchQueue.main.async { // Correct
                 UserDefaults.standard.set(self.NewPassword, forKey: "Password")
            }
            
        let task =  URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
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
        let defaults = UserDefaults.standard
        let dictionary = defaults.dictionaryRepresentation()
        dictionary.keys.forEach { key in
            defaults.removeObject(forKey: key)
        }
        
        Goback((Any).self)
    }
    
    
    func Goback(_ sender: Any) {
        navigationController?.popToRootViewController(animated: true)
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
