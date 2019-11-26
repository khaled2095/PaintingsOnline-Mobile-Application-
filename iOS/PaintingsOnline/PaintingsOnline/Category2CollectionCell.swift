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


class Category2CollectionCell: CollectionViewSlantedCell {
    let yOffsetSpeed: CGFloat = 150.0
    let xOffsetSpeed: CGFloat = 100.0

    @IBOutlet weak var CImage: UIImageView!
    var Daddy : ArtInCatViewController = ArtInCatViewController()
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
   
    
    func isKeyPresentInUserDefaults(key: String) -> Bool {
        return UserDefaults.standard.object(forKey: key) != nil
    }
    
}
