//
//  WishListViewController.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 9/9/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage

class WishListViewController: UIViewController , UICollectionViewDelegate, UICollectionViewDataSource {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if ListofPaintings.count == 0 {
            collectionView.setEmptyView(title: "You don't have any Items in your wish list", message: "Add more items to wish list to view them later.")
        }
        else {
            collectionView.restore()
        }
        return ListofPaintings.count
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        UserDefaults.standard.set(ListofPaintings[indexPath.row].CartID, forKey: "CartIDFull")
        performSegue(withIdentifier:"ShowPainting", sender: self)
        
    }
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifier, for: indexPath)
            as? CustomCollectionCell else {
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
        cell.Daddy1 = self as WishListViewController
        cell.Title.text = "Artwork name: " + ListofPaintings[indexPath.row].Cartname
        cell.thisUrl = ListofPaintings[indexPath.row].CartUrl
        cell.Artist.text = "by " + ListofPaintings[indexPath.row].CartArtist
        cell.CImage.image = ListofPaintings[indexPath.row].ImageIMG
        cell.Description.text = ListofPaintings[indexPath.row].Cartdescription
        cell.ID = ListofPaintings[indexPath.row].CartID
        //  cell.Size.text = "Size: " + ListofPaintings[indexPath.row][7]
        cell.MaxQuantity = ListofPaintings[indexPath.row].MaxQuantity
        if (ListofPaintings[indexPath.row].MaxQuantity < 1) {
            cell.PriceB.setTitle("Out Of Stock", for: .normal)
            cell.PriceB.isEnabled = false
            
        }
        else{
            cell.Price.text = "Starting From: " + String(ListofPaintings[indexPath.row].CartPrice) + "$"
            cell.PriceB.setTitle(ListofPaintings[indexPath.row].CartPrice + "$", for: .normal)
            cell.PriceB.isEnabled = true
        }
        
        return cell
    }
    var ListofPaintings = [CartItem]()
    let reuseIdentifier = "customViewCell"
    @IBOutlet weak var CollectionView: UICollectionView!
    var TmpWishList = [String]()
    var MainURL = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        MainURL = UserDefaults.standard.string(forKey: "MainURL")!
        if (isKeyPresentInUserDefaults(key: "WishList")){
            TmpWishList = UserDefaults.standard.array(forKey: "WishList") as! [String]
        }
        for CartID1 in TmpWishList {
            SearchMe(CartID1: CartID1)
        }
        
        
    }
    
    func DeleteMe(CartID1 : String){
        for (index, element) in ListofPaintings.enumerated() {
            print(CartID1 , element)
            if (element.CartID == CartID1) {
                ListofPaintings.remove(at: index)
            }
        }
        for (index, element) in TmpWishList.enumerated() {
            print(CartID1 , element)
            if (element == CartID1) {
                TmpWishList.remove(at: index)
                UserDefaults.standard.set(TmpWishList, forKey: "WishList")
                CollectionView.reloadData()
            }
        }
    }
    
    
    func SearchMe (CartID1 : String) {
        var URL1 = MainURL + "/ShowPaintings.php?Search="
        URL1.append(CartID1)
        print(URL1)
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    DispatchQueue.main.async {
                        var CartItem1 = CartItem()
                        CartItem1.Cartname = data.painting_name!
                        CartItem1.CartPrice = data.painting_price!
                        CartItem1.CartArtist = data.painting_artist!
                        CartItem1.Cartdescription = data.painting_description!
                        CartItem1.CartID = data.painting_id!
                        CartItem1.CartUrl = data.painting_url!
                        CartItem1.Quantity = 0
                        CartItem1.MaxQuantity = Int(data.Quantity!)!
                        self.ListofPaintings.append(CartItem1)
                        self.CollectionView.reloadData()
                    }
                }
                
                for (index, element) in self.ListofPaintings.enumerated() {
                    Alamofire.request(element.CartUrl).responseImage { response in
                        if let image = response.result.value {
                            self.ListofPaintings[index].ImageIMG = image
                            DispatchQueue.main.async(execute: {
                                self.CollectionView.reloadData()
                            })
                        }
                    }
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    
    
    
    func isKeyPresentInUserDefaults(key: String) -> Bool {
        return UserDefaults.standard.object(forKey: key) != nil
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}


