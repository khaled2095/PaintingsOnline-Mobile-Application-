//
//  CreditCardViewController.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 11/9/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit


class CreditCardViewController: UIViewController , UIWebViewDelegate{
    @IBOutlet weak var Cancel: UIButton!

    var MainURL = ""
    
    var Email :  String = ""
    var amount : Int = 1
    var Times : Int = 0 ;
    
    override func viewDidLoad() {
        super.viewDidLoad()
        MainURL = UserDefaults.standard.string(forKey: "MainURL")!
        Email = UserDefaults.standard.value(forKey: "Email") as! String
        // Do any additional setup after loading the view.
        var Request : URLRequest = URLRequest(url: URL(string: MainURL + "/Pay/index.php")!)

    }
    
    @IBAction func CancelCard(_ sender: Any) {
        self.dismiss(animated: true)
    }
    
    func webViewDidFinishLoad(_ webView: UIWebView) {
        if (webView.request?.mainDocumentURL == URL(string:MainURL + "/Pay/process.php")) {
            webView.isHidden = true
        }
    
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
