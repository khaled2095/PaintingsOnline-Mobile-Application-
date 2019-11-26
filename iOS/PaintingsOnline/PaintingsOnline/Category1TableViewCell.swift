//
//  ArtTableViewCell.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 30/3/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

//
//  CategoryTableViewCell.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 30/3/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit
import AlamofireImage
import Alamofire


class Category1TableViewCell: UITableViewCell {
    
    @IBOutlet weak var CImage: UIImageView!
    @IBOutlet weak var Title: UILabel!
    @IBOutlet weak var Artist: UILabel!
    @IBOutlet weak var Price: UILabel!
    @IBOutlet weak var Description: UILabel!
    var tmpCarts = [[String]]()
    var ID : String = ""
    var thisUrl : String = ""
    var MaxQuantity = 0
    var Quanitity = 1
    
    @IBOutlet weak var ImageView1: UIImageView!
    
    @IBOutlet weak var PriceB: UIButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        contentView.frame = contentView.frame.inset(by: UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 10))
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
    }
    
    @IBAction func SelectedMe(_ sender: Any) {
        if(isKeyPresentInUserDefaults(key: "ListofPaintings")){
            tmpCarts = UserDefaults.standard.array(forKey: "ListofPaintings") as! [[String]]
        }
        var count = 0
        for j in tmpCarts {
            if (j[6] == ID ) {
                count += 1
                let alert = UIAlertView()
                alert.title = "Error"
                alert.message = "Item is already in cart"
                alert.addButton(withTitle: "Understood")
                alert.show()
            }
        }
        if (count == 0 ){
            var tmpCart = [String]()
            tmpCart.append(Title.text!)
            tmpCart.append(PriceB.titleLabel!.text!)
            tmpCart.append(Artist.text!)
            tmpCart.append(thisUrl)
            tmpCart.append(String(MaxQuantity))
            tmpCart.append(Description.text!)
            tmpCart.append(ID)
            tmpCart.append(String(Quanitity))
            tmpCarts.append(tmpCart)
            UserDefaults.standard.set(tmpCarts, forKey: "ListofPaintings")
            
        }
    }
    
    func isKeyPresentInUserDefaults(key: String) -> Bool {
        return UserDefaults.standard.object(forKey: key) != nil
    }
    
}




