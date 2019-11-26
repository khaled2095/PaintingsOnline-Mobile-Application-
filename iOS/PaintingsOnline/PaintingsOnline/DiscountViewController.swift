//
//  DiscountViewController.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 21/9/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit

class DiscountViewController: UIViewController {

    @IBOutlet weak var ImageView: UIImageView!
    var Image1 : UIImage? = nil
    @IBOutlet weak var Close: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        ImageView.image = Image1
        // Do any additional setup after loading the view.
    }
    @IBAction func GoBack(_ sender: Any) {
         dismiss(animated: true, completion: nil)
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
