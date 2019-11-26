//
//  RatingViewController.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 24/8/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit

class RatingViewController: UIViewController, UITextViewDelegate {

    
    @IBOutlet weak var Star: UIButton!
    @IBOutlet weak var Star2: UIButton!
    @IBOutlet weak var Star3: UIButton!
    @IBOutlet weak var Star4: UIButton!
    @IBOutlet weak var Star5: UIButton!
    
    @IBOutlet weak var Comment: UITextView!
    var Rating : Int = 0;
    
    var vc : OrdersViewController = OrdersViewController() ;
    var MainURL = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        Comment.delegate = self
        MainURL = UserDefaults.standard.string(forKey: "MainURL")!
        // Do any additional setup after loading the view.
    }
    
    func textViewDidBeginEditing(_ textView: UITextView) {
    self.Comment.text = ""

    }
    
    @IBAction func Set1Star(_ sender: Any) {
        Rating = 1;
    Star.setImage(UIImage(named: "icons8-star-60-2.png"), for: .normal)
    Star2.setImage(UIImage(named: "icons8-star-60.png"), for: .normal)
    Star3.setImage(UIImage(named: "icons8-star-60.png"), for: .normal)
    Star4.setImage(UIImage(named: "icons8-star-60.png"), for: .normal)
    Star5.setImage(UIImage(named: "icons8-star-60.png"), for: .normal)
    }
    
    @IBAction func Set2Star(_ sender: Any) {
        Rating = 2;
        Star.setImage(UIImage(named: "icons8-star-60-2.png"), for: .normal)
        Star2.setImage(UIImage(named: "icons8-star-60-2.png"), for: .normal)
        Star3.setImage(UIImage(named: "icons8-star-60.png"), for: .normal)
        Star4.setImage(UIImage(named: "icons8-star-60.png"), for: .normal)
        Star5.setImage(UIImage(named: "icons8-star-60.png"), for: .normal)
    }
    
    @IBAction func Set3Star(_ sender: Any) {
        Rating = 3;
        Star.setImage(UIImage(named: "icons8-star-60-2.png"), for: .normal)
        Star2.setImage(UIImage(named: "icons8-star-60-2.png"), for: .normal)
        Star3.setImage(UIImage(named: "icons8-star-60-2.png"), for: .normal)
        Star4.setImage(UIImage(named: "icons8-star-60.png"), for: .normal)
        Star5.setImage(UIImage(named: "icons8-star-60.png"), for: .normal)
    }
    
    @IBAction func Set4Star(_ sender: Any) {
        Rating = 4;
        Star.setImage(UIImage(named: "icons8-star-60-2.png"), for: .normal)
        Star2.setImage(UIImage(named: "icons8-star-60-2.png"), for: .normal)
        Star3.setImage(UIImage(named: "icons8-star-60-2.png"), for: .normal)
        Star4.setImage(UIImage(named: "icons8-star-60-2.png"), for: .normal)
        Star5.setImage(UIImage(named: "icons8-star-60.png"), for: .normal)
    }
    
    @IBAction func Set5Star(_ sender: Any) {
        Rating = 5;
        Star.setImage(UIImage(named: "icons8-star-60-2.png"), for: .normal)
        Star2.setImage(UIImage(named: "icons8-star-60-2.png"), for: .normal)
        Star3.setImage(UIImage(named: "icons8-star-60-2.png"), for: .normal)
        Star4.setImage(UIImage(named: "icons8-star-60-2.png"), for: .normal)
        Star5.setImage(UIImage(named: "icons8-star-60-2.png"), for: .normal)
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */
    
    @IBAction func Cancel(_ sender: Any) {
         performSegueToReturnBack()
    }
    
    
    @IBAction func Submit(_ sender: Any) {
        let request = NSMutableURLRequest(url: NSURL(string: MainURL + "/PostReview.php")! as URL)
        request.httpMethod = "POST"
        let postString = "purchase_id=\(UserDefaults.standard.string(forKey: "purchase_id")!)&Rating=\(String(Rating))&Comment=\(Comment.text!)"
        print(postString)
        request.setValue("application/x-www-form-urlencoded; charset=utf-8", forHTTPHeaderField: "Content-Type")
        request.httpBody = postString.data(using: String.Encoding.utf8)
        let task = URLSession.shared.dataTask(with: request as URLRequest) { data, response, error in
            guard let data = data else { return }
            do {
                print(data)
            } catch let err {
                print("Err", err)
            }
            }.resume()
        performSegueToReturnBack()
    }
    
    override func touchesBegan(_ touches: Set<UITouch>,
                               with event: UIEvent?) {
        self.view.endEditing(true)
    }

    
}



extension UIViewController {
    func performSegueToReturnBack()  {
        if let nav = self.navigationController {
            nav.popViewController(animated: true)
        } else {
            self.dismiss(animated: true, completion: nil)
        }
    }
}
