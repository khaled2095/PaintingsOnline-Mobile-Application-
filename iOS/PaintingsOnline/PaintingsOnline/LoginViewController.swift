//
//  LoginViewController.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 2/4/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit

class LoginViewController: UIViewController {

    var Username : String = ""
    var Password : String = ""
    var Name : String = ""
    var Email : String = ""
    var Address : String = ""
    var UserType : String = ""
    var verified : String = ""
    @IBOutlet weak var txtUsername: UITextField!
    @IBOutlet weak var txtPassword: UITextField!
    @IBOutlet weak var Loading: UIActivityIndicatorView!
    @IBOutlet weak var response: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
   
        // Do any additional setup after loading the view.
    }
    
    override func touchesBegan(_ touches: Set<UITouch>,
                               with event: UIEvent?) {
        self.view.endEditing(true)
    }
    var MainURL = ""
    override func viewWillAppear(_ animated: Bool) {
       
        if (isKeyPresentInUserDefaults(key: "username")) {
            performSegue(withIdentifier: "LoggidIn", sender: AnyObject.self)
        }
    }
    
    
    func isKeyPresentInUserDefaults(key: String) -> Bool {
        return UserDefaults.standard.object(forKey: key) != nil
    }

    @IBAction func LoginAction(_ sender: Any) {
         MainURL = UserDefaults.standard.string(forKey: "MainURL")!
        let request = NSMutableURLRequest(url: NSURL(string: MainURL + "/Login.php")! as URL)
        request.httpMethod = "POST"
        let postString = "Username=\(txtUsername.text!)&Mac=\(String(describing: UIDevice.current.identifierForVendor!.uuidString))&Password=\(txtPassword.text!)"
        print(postString)
        request.setValue("application/x-www-form-urlencoded; charset=utf-8", forHTTPHeaderField: "Content-Type")
        request.httpBody = postString.data(using: String.Encoding.utf8)
        UserDefaults.standard.set(txtPassword.text, forKey: "Password")
        
       let task = URLSession.shared.dataTask(with: request as URLRequest) { data, response, error in
        guard let data = data else { return }
            print(data)
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                if (gitData.count == 0) {
                    DispatchQueue.main.async { // Correct
                    let alert = UIAlertView()
                    alert.title = "Error"
                    alert.message = "username and password do not match any account!"
                    alert.addButton(withTitle: "Understood")
                    alert.show()
                    }
                }
                else {
                    
                }
                for data in gitData {
                    self.Username = data.username!
                    self.Name = data.name!
                    self.Email = data.Email!
                    self.Address = data.Address!
                    self.UserType = data.usertype!
                    self.verified = data.verified!
                    
                    UserDefaults.standard.set(self.Username, forKey: "username")
                    UserDefaults.standard.set(self.Email, forKey: "Email")
                    UserDefaults.standard.set(self.Name, forKey: "Name")
                UserDefaults.standard.set(self.Address, forKey: "Address")
                    UserDefaults.standard.set(self.UserType, forKey: "UserType")
                UserDefaults.standard.set(data.verified!, forKey: "verified")
                UserDefaults.standard.set(data.Bio!, forKey: "BioTxt")
                    DispatchQueue.main.async {
                     self.viewWillAppear(true)
                    }
              }
            } catch let err {
                print("Err", err)
            }
            }
       
        task.resume()
    }
}


var vSpinner : UIView?

extension UIViewController {
    func showSpinner(onView : UIView) {
        let spinnerView = UIView.init(frame: onView.bounds)
        spinnerView.backgroundColor = UIColor.init(red: 0.5, green: 0.5, blue: 0.5, alpha: 0.5)
        let ai = UIActivityIndicatorView.init(style: .whiteLarge)
        ai.startAnimating()
        ai.center = spinnerView.center
        
        DispatchQueue.main.async {
            spinnerView.addSubview(ai)
            onView.addSubview(spinnerView)
        }
        
        vSpinner = spinnerView
    }
    
    func removeSpinner() {
        DispatchQueue.main.async {
            vSpinner?.removeFromSuperview()
            vSpinner = nil
        }
    }
}

