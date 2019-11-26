//
//  CartTableViewCell.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 5/5/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit

class CartTableViewCell: UITableViewCell {

    @IBOutlet weak var Artist: UILabel!
    @IBOutlet weak var Name: UILabel!
    @IBOutlet weak var Price: UILabel!
    @IBOutlet weak var Quantity: UILabel!
    @IBOutlet weak var CImage: UIImageView!
    @IBOutlet weak var Url: UILabel!
    var Daddy : CartViewController = CartViewController()
    var CurrentID : String = ""
    var CartListOfID : [String] = []
    var Quantitities : [Int] = []
    var QuantityInt : Int = 1
    var MaxQuantity : Int = 1
    
    @IBOutlet weak var Sizelbl: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
    @IBAction func AddQauntity(_ sender: Any) {
        if (MaxQuantity > QuantityInt){
            QuantityInt += 1
            Quantity.text = String(QuantityInt)
            Daddy.Increment(ID: Int(CurrentID)!)
        }
    }
    
    func isKeyPresentInUserDefaults(key: String) -> Bool {
        return UserDefaults.standard.object(forKey: key) != nil
    }
    
    @IBAction func reduceQuantity(_ sender: Any) {
        if(QuantityInt > 1) {
            QuantityInt -= 1
            Quantity.text = String(QuantityInt)
            Daddy.Decrement(ID: Int(CurrentID)!)
        }
    }
}
