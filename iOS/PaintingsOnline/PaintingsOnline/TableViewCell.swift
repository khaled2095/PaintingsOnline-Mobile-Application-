//
//  TableViewCell.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 4/5/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit
import MessageUI
import Foundation


class TableViewCell: UITableViewCell, MFMailComposeViewControllerDelegate {
    @IBOutlet weak var Artist: UILabel!
    @IBOutlet weak var Price: UILabel!
    @IBOutlet weak var Status: UILabel!
    @IBOutlet weak var CImage: UIImageView!
    @IBOutlet weak var What: UILabel!
    
    @IBOutlet weak var Star1: UIButton!
    @IBOutlet weak var Star2: UIButton!
    @IBOutlet weak var Star3: UIButton!
    @IBOutlet weak var Star4: UIButton!
    @IBOutlet weak var Star5: UIButton!
    public var ImageToShow : UIImage!
    
    var Rating : Int = 0
    
    
    var Purchase_id : String =  ""
    var viewController : OrdersViewController = OrdersViewController()
    
    override func awakeFromNib() {
        super.awakeFromNib()
        CImage.contentMode = .scaleAspectFit
        CImage.image = ImageToShow
        
    }
    
    
    
    @IBAction func sendEmail(_ sender: Any) {
        self.viewController.SendMail(OrderNumber: Purchase_id,  Artist: Artist.text! )
    }
    
    @IBAction func Review(_ sender: Any) {
        UserDefaults.standard.set(Purchase_id,forKey: "purchase_id")
        viewController.RateMe()
    }
    
}



