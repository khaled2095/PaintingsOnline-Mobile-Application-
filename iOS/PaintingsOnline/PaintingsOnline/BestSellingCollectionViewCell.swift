//
//  FeaturedCollectionViewCell.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 12/9/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage


class BestSellingCollectionViewCell: UICollectionViewCell , UICollectionViewDelegate, UICollectionViewDataSource  {
    var ListofPaintings = [CartItem]()
    @IBOutlet weak var CollectionView1: UICollectionView!
    let reuseIdentifier2 = "BestSelling"
    var MainURL = ""
    var Daddy : HomeViewController = HomeViewController()
    public func LoadFeatured() {
        MainURL = UserDefaults.standard.string(forKey: "MainURL")!
        CollectionView1.dataSource = self
        CollectionView1.delegate = self
    //    ListofPaintings.removeAll()
        CollectionView1.reloadData()
        var URL1 = MainURL +  "/ShowPaintings.php?Search=&Type=BestSelling"
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                if (gitData.count != self.ListofPaintings.count ){
                    for data in gitData {
                        var CartItem1 = CartItem()
                        CartItem1.Cartname = data.painting_name!
                        CartItem1.CartPrice = data.painting_price!
                        CartItem1.CartArtist = data.painting_artist!
                        CartItem1.Cartdescription = data.painting_description!
                        CartItem1.CartID = data.painting_id!
                        CartItem1.CartUrl = data.painting_url!
                        CartItem1.ImageIMG = UIImage(named: "empty-image")!
                        CartItem1.Quantity = 0
                        CartItem1.MaxQuantity = Int(data.Quantity!)!
                        self.ListofPaintings.append(CartItem1)
                    }
                    for (index, str) in self.ListofPaintings.enumerated() {
                        if (str.CartUrl != "") {
                            Alamofire.request(str.CartUrl).responseImage { response in
                                if let image = response.result.value {
                                    DispatchQueue.main.async(execute: {
                                        self.ListofPaintings[index].ImageIMG = image
                                        self.CollectionView1.reloadData()
                                    })
                                }
                            }
                        }
                    }
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
        
        CollectionView1.reloadData()
    }
    
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifier2, for: indexPath)
            as? BestSellingInnerCollectionCell else {
                fatalError()
        }
        
        cell.contentView.layer.cornerRadius = 5.0
        cell.contentView.layer.borderWidth = 5.0
        
        cell.contentView.layer.borderColor = UIColor.clear.cgColor
        cell.contentView.layer.masksToBounds = true
        
        cell.layer.shadowColor = UIColor.gray.cgColor
        cell.layer.shadowOffset = CGSize(width: 0, height: 2.0)
        cell.layer.shadowRadius = 2.0
        cell.layer.shadowOpacity = 1.0
        cell.layer.masksToBounds = false
        cell.layer.shadowPath = UIBezierPath(roundedRect:cell.bounds, cornerRadius:cell.contentView.layer.cornerRadius).cgPath
        
        
        cell.TmpCart.Cartname = ListofPaintings[indexPath.row].Cartname
        cell.TmpCart.CartUrl = ListofPaintings[indexPath.row].CartUrl
        cell.TmpCart.CartArtist = ListofPaintings[indexPath.row].CartArtist
        cell.TmpCart.ImageIMG = ListofPaintings[indexPath.row].ImageIMG
        cell.TmpCart.Cartdescription = ListofPaintings[indexPath.row].Cartdescription
        cell.TmpCart.CartID = ListofPaintings[indexPath.row].CartID
        cell.TmpCart.MaxQuantity = ListofPaintings[indexPath.row].MaxQuantity
        cell.TmpCart.Quantity = 1
        
        cell.Title.text = "Artwork name: " + ListofPaintings[indexPath.row].Cartname
        cell.thisUrl = ListofPaintings[indexPath.row].CartUrl
        cell.Artist.text = "by " + ListofPaintings[indexPath.row].CartArtist
        cell.CImage.image = ListofPaintings[indexPath.row].ImageIMG
        cell.Description.text = ListofPaintings[indexPath.row].Cartdescription
        cell.ID = ListofPaintings[indexPath.row].CartID
        //  cell.Size.text = "Size: " + ListofPaintings[indexPath.row][7]
        cell.MaxQuantity = ListofPaintings[indexPath.row].MaxQuantity
        if (Int(UserDefaults.standard.value(forKey: "Discount") as! Int) != 0 ) {
            let attrString = NSMutableAttributedString(string: String(Int(Double(ListofPaintings[indexPath.row].CartPrice)! / (1 - 0.1))) + "$", attributes: [NSAttributedString.Key.strikethroughStyle: NSUnderlineStyle.single.rawValue])
            
            let attrString1 = NSMutableAttributedString(string: "  " +  String(ListofPaintings[indexPath.row].CartPrice) + "$")
            let attrString0 = NSMutableAttributedString(string: "Starting From: ")
            var combination = NSMutableAttributedString()
            
            combination = attrString0
            combination.append(attrString)
            combination.append(attrString1)
            
            cell.Price.attributedText = combination
        }
        else {
            cell.Price.text = ListofPaintings[indexPath.row].CartPrice + "$"
        }
        
        return cell
        
    }
    
    func collectionView(_ collectionView: UICollectionView,numberOfItemsInSection sections: Int) -> Int {
        return ListofPaintings.count
    }
    
    var cellID : Int = 0
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath){
        UserDefaults.standard.set(ListofPaintings[indexPath.row].CartID, forKey: "CartIDFull")
       Daddy.LoadFromKids()
    }
    
    
    
    
}
