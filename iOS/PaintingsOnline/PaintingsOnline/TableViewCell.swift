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
    
    var Purchase_id : String =  ""
    var viewController : OrdersViewController = OrdersViewController()
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    @IBAction func sendEmail(_ sender: Any) {
        self.viewController.SendMail(OrderNumber: Purchase_id,  Artist: Artist.text! )
    }
    
    
}



