//
//  CommentTableViewCell.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 14/9/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit

class CommentTableViewCell: UITableViewCell {
    @IBOutlet weak var TitleLbl: UILabel!
    @IBOutlet weak var RatingsLbl: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
