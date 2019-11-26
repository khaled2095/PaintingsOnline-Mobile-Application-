//
//  MyOrdersTableViewCell.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 17/9/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit

class MyOrdersTableViewCell: UITableViewCell {

    @IBOutlet weak var NameLbl: UILabel!
    @IBOutlet weak var QuantityLbl: UILabel!
    @IBOutlet weak var StatusLbl: UILabel!
    @IBOutlet weak var PaidArtist: UILabel!
    var ThisId : String = ""
    var MainClass : BarsExample = BarsExample()
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    @IBAction func SetAsShipped(_ sender: Any) {
        MainClass.SetAsShipped(ID: ThisId)
    }
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
