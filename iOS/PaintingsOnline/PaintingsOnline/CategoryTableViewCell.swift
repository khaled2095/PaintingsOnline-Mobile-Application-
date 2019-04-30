//
//  CategoryTableViewCell.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 30/3/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit

class CategoryTableViewCell: UITableViewCell {

    
    @IBOutlet weak var CImage: UIImageView!
    @IBOutlet weak var Title: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
