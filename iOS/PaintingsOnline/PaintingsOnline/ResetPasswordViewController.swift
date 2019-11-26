//
//  ResetPasswordViewController.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 8/9/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit
import MaterialComponents

class ResetPasswordViewController: UIViewController , UITextFieldDelegate {

    
    @IBOutlet weak var ResetPasswordBtn: UIButton!
    @IBOutlet weak var EmailAddress: UITextField!
    @IBOutlet weak var Resulttxt: UILabel!
    var MainURL = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        EmailAddress.delegate = self
        MainURL = UserDefaults.standard.string(forKey: "MainURL")!
        Resulttxt.text = "A recover password Email will be sent to this address"
        // Do any additional setup after loading the view.
    }
    
    @IBAction func ResetME(_ sender: Any) {
        self.view.endEditing(true)
        if (isValidEmail(emailStr: EmailAddress.text!)) {
            Resulttxt.text = "Waiting for response from server."
            var URL1 = MainURL + "/recoverPassword.php?Email="
        URL1.append(EmailAddress.text!)
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let string = String(data: data, encoding: String.Encoding.utf8)
                DispatchQueue.main.async {
                self.Resulttxt.text = string
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
        else {
             Resulttxt.text = "The Email you have entered is invalid"
        }
    }
    func LoadCategories() {
   
    }

    
    func isValidEmail(emailStr:String) -> Bool {
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        
        let emailPred = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailPred.evaluate(with: emailStr)
    }
    override func touchesBegan(_ touches: Set<UITouch>,
                               with event: UIEvent?) {
        self.view.endEditing(true)
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
