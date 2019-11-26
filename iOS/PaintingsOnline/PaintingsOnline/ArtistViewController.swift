//
//  ArtistViewController.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 20/9/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit
import AlamofireImage
import Alamofire

class ArtistViewController: UIViewController , UICollectionViewDelegate, UICollectionViewDataSource {
    
    var ListofPaintings = [CartItem]()
    @IBOutlet weak var CollectionView: UICollectionView!
    
    func collectionView(_ collectionView: UICollectionView,numberOfItemsInSection sections: Int) -> Int {
        if ListofPaintings.count == 0 {
            CollectionView.setEmptyView(title: "This Category/Room is empty", message: "Please Check another Category/Room")
        }
        else {
            CollectionView.restore()
        }
        return ListofPaintings.count
    }
    
    var cellID : Int = 0
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath){
        UserDefaults.standard.set(ListofPaintings[indexPath.row ].CartID, forKey: "CartIDFull")
        performSegue(withIdentifier:"Modify", sender: self)
    }
    
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "customViewCell", for: indexPath)
            as? Category2CollectionCell else {
                fatalError()
        }
        
        
        var NewIndexPath : Int = indexPath.row
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
        
        
        cell.TmpCart.Cartname = ListofPaintings[NewIndexPath].Cartname
        cell.TmpCart.CartUrl = ListofPaintings[NewIndexPath].CartUrl
        cell.TmpCart.CartArtist = ListofPaintings[NewIndexPath].CartArtist
        cell.TmpCart.ImageIMG = ListofPaintings[NewIndexPath].ImageIMG
        cell.TmpCart.Cartdescription = ListofPaintings[NewIndexPath].Cartdescription
        cell.TmpCart.CartID = ListofPaintings[NewIndexPath].CartID
        cell.TmpCart.MaxQuantity = ListofPaintings[NewIndexPath].MaxQuantity
        cell.TmpCart.Quantity = 1
        //  cell.Daddy = self
        cell.Title.text = "Artwork name: " + ListofPaintings[NewIndexPath].Cartname
        cell.thisUrl = ListofPaintings[NewIndexPath].CartUrl
        cell.Artist.text = "by " + ListofPaintings[NewIndexPath].CartArtist
        cell.CImage.image = ListofPaintings[NewIndexPath].ImageIMG
        cell.Description.text = ListofPaintings[NewIndexPath].Cartdescription
        cell.ID = ListofPaintings[NewIndexPath].CartID
        //  cell.Size.text = "Size: " + ListofPaintings[indexPath.row][7]
        cell.MaxQuantity = ListofPaintings[NewIndexPath].MaxQuantity
        if (ListofPaintings[NewIndexPath].MaxQuantity < 1) {
            cell.PriceB.setTitle("Out Of Stock", for: .normal)
            cell.PriceB.isEnabled = false
        }
        else{
            cell.Price.text = "Starting From: " + String(ListofPaintings[NewIndexPath].CartPrice) + "$"
            cell.PriceB.setTitle(ListofPaintings[NewIndexPath].CartPrice + "$", for: .normal)
            cell.PriceB.isEnabled = true
        }
        
        return cell
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        LoadAccount()
        sortArray()
        // Do any additional setup after loading the view.
    }
    
    
    @IBOutlet weak var ArtistNameLbl: UILabel!
    
    @IBOutlet weak var ArtistDecribtion: UITextView!
    
    @IBOutlet weak var ArtistImageView: UIImageView!
    var MainURL = ""
    func LoadAccount() {
        MainURL = UserDefaults.standard.string(forKey: "MainURL")!
        let refreshControl = UIRefreshControl.self
        var URL1 = MainURL + "/LoadAccount.php?Username="
        URL1.append(UserDefaults.standard.value(forKey: "ArtistID") as! String)
        var ArtistBio : String = ""
        var ArtistImage : String = ""
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                    for data in gitData {
                        ArtistImage = data.Image!
                        ArtistBio = data.Bio!
                    }
                    Alamofire.request(ArtistImage).responseImage { response in
                        if let image = response.result.value {
                             DispatchQueue.main.async {
                            self.ArtistImageView.image = image
                            self.ArtistNameLbl.text = UserDefaults.standard.value(forKey: "ArtistID") as! String
                            self.ArtistDecribtion.text = ArtistBio
                            }
                        }
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    @objc func sortArray() {
        ListofPaintings.removeAll()
        let refreshControl = UIRefreshControl.self
        var URL1 = "https://jrnan.info/Painting/ShowMyPaintings.php?Artist="
        URL1.append(UserDefaults.standard.value(forKey: "ArtistID") as! String)
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                    for data in gitData {
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
                    }
                    
                    for (index, str) in self.ListofPaintings.enumerated() {
                        Alamofire.request(str.CartUrl).responseImage { response in
                            if let image = response.result.value {
                                self.ListofPaintings[index].ImageIMG = image
                                DispatchQueue.main.async(execute: {
                                    self.CollectionView.reloadData()
                                })
                            }
                        }
                    }
                
                DispatchQueue.main.async {
                    self.CollectionView.reloadData()
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
        CollectionView.reloadData()
        CollectionView.refreshControl?.endRefreshing()
    }
    
    func stopTheDamnRequests(){
        if #available(iOS 9.0, *) {
            Alamofire.SessionManager.default.session.getAllTasks { (tasks) in
                tasks.forEach{ $0.cancel() }
            }
        } else {
            Alamofire.SessionManager.default.session.getTasksWithCompletionHandler { (sessionDataTask, uploadData, downloadData) in
                sessionDataTask.forEach { $0.cancel() }
                uploadData.forEach { $0.cancel() }
                downloadData.forEach { $0.cancel() }
            }
        }
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
