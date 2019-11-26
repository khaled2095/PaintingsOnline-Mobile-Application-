//
//  CustomCollectionCell.swift
//  CollectionViewSlantedLayout
//
//  Created by Yassir Barchi on 28/02/2016.
//  Copyright © 2016 CocoaPods. All rights reserved.
//

import Foundation

import UIKit

import CollectionViewSlantedLayout

let yOffsetSpeed: CGFloat = 150.0
let xOffsetSpeed: CGFloat = 100.0

class Category2CollectionCell: CollectionViewSlantedCell {
    
    @IBOutlet weak var CImage: UIImageView!
    var Daddy : CategoryViewController = CategoryViewController()
    @IBOutlet weak var Title: UILabel!
    @IBOutlet weak var Artist: UILabel!
    @IBOutlet weak var Price: UILabel!
    @IBOutlet weak var Size: UILabel!
    @IBOutlet weak var Description: UILabel!
    var tmpCarts = [[String]]()
    var ID : String = ""
    var thisUrl : String = ""
    var MaxQuantity = 0
    var Quanitity = 1
    @IBOutlet weak var PriceB: UIButton!
    var TmpCart = CartItem()
    
    
    
    private var gradient = CAGradientLayer()
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
    }
    
    public func updateWith(superHero: String){
        Title.text = superHero
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        if let backgroundView = backgroundView {
            gradient.frame = backgroundView.bounds
        }
    }
    
    var image: UIImage = UIImage() {
        didSet {
            CImage.image = image
        }
    }
    
    var imageHeight: CGFloat {
        return (CImage.image!.size.height) ?? 0.0
    }
    
    var imageWidth: CGFloat {
        return (CImage.image!.size.width) ?? 0.0
    }
    
    func offset(_ offset: CGPoint) {
        CImage.frame = CImage.bounds.offsetBy(dx: offset.x, dy: offset.y)
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
        Daddy.reloadBar()
    }
    
    func isKeyPresentInUserDefaults(key: String) -> Bool {
        return UserDefaults.standard.object(forKey: key) != nil
    }
    
}
