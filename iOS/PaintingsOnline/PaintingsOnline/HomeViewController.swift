//
//  ArtInCatViewController.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 30/3/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage
import Material
import CollectionViewSlantedLayout
import SearchTextField



protocol MyDataSendingDelegateProtocol {
    func sendDataToFirstViewController(myData: CartItem)
}

class HomeViewController: UIViewController , UITextFieldDelegate, UISearchBarDelegate, UISearchDisplayDelegate , UICollectionViewDataSource , UICollectionViewDelegate {
    
    @IBOutlet weak var SearchField: SearchTextField!
     var delegate: MyDataSendingDelegateProtocol? = nil
    func sendDataToFirstViewController(myData: CartItem) {
        self.tempCart = myData
    }
    
    //Type your Website Url here Eg https://jrnan.info/Painting/
    var MainURL = "https://jrnan.info/Paintings"
   
   
    @IBOutlet weak var collectionView: UICollectionView!
    let reuseIdentifier = "customViewCell"
    
    @IBOutlet weak var Slanted: UICollectionViewFlowLayout!
    
    @IBOutlet weak var Searxhtxt: UITextField!
    
    var ListofPaintings = [CartItem]()
    var ListOfPAintings = [[String]]()
    var Images = [UIImage]()
    var tempCart = CartItem()
    var ListOFSearch = [String]()
    override func viewWillAppear(_ animated: Bool) {
        var Count = 0
        if (isKeyPresentInUserDefaults(key: "ListofPaintings")){
            Count =  (UserDefaults.standard.array(forKey: "ListofPaintings")?.count)!
        }
        
        UserDefaults.standard.set(MainURL,forKey:"MainURL")
        MainURL = UserDefaults.standard.string(forKey: "MainURL")!
        collectionView.collectionViewLayout.invalidateLayout()
         collectionView.collectionViewLayout = Slanted
 
        SearchField.inlineMode = true
        reloadBar()
        
        labelWish.layer.borderColor = UIColor.clear.cgColor
        labelWish.layer.borderWidth = 2
        labelWish.layer.cornerRadius = labelWish.bounds.size.height / 2
        labelWish.textAlignment = .center
        labelWish.layer.masksToBounds = true
        labelWish.font = UIFont(name: "SanFranciscoText-Light", size: 11)
        labelWish.textColor = .white
        labelWish.backgroundColor = .red
        labelWish.text = "0"
        
        labelCart.layer.borderColor = UIColor.clear.cgColor
        labelCart.layer.borderWidth = 2
        labelCart.layer.cornerRadius = labelCart.bounds.size.height / 2
        labelCart.textAlignment = .center
        labelCart.layer.masksToBounds = true
        labelCart.font = UIFont(name: "SanFranciscoText-Light", size: 11)
        labelCart.textColor = .white
        labelCart.backgroundColor = .red
        labelCart.text = "0"
        
        let editButton   = UIButton(frame: CGRect(x: 0, y: 0, width: 18, height: 20))
        editButton.addTarget(self, action: #selector(didTapEditButton), for: .touchDown)
        editButton.setBackgroundImage(UIImage(named: "icons8-heart-30")!, for: .normal)
        editButton.addSubview(labelWish)
        editButton.tintColor = UIColor.black
        
        let CartButton   = UIButton(frame: CGRect(x: 0, y: 0, width: 18, height: 20))
        CartButton.addTarget(self, action: #selector(didTapCartButton), for: .touchDown)
        CartButton.setBackgroundImage(UIImage(named: "icons8-shopping-cart-30")!, for: .normal)
        CartButton.addSubview(labelCart)
        CartButton.tintColor = UIColor.black
        
        let rightBarButtomItem = UIBarButtonItem(customView: editButton)
        let rightBarButtomItem1 = UIBarButtonItem(customView: CartButton)
        navigationItem.rightBarButtonItems = [rightBarButtomItem, rightBarButtomItem1 ]
        reloadBar()
        let refreshControl = UIRefreshControl()
        refreshControl.addTarget(self, action:  #selector(sortArray), for: .valueChanged)
        self.collectionView.refreshControl = refreshControl
        self.Searxhtxt.delegate = self as! UITextFieldDelegate
        self.Searxhtxt.delegate = self
        
        var URL1 = MainURL + "/ShowPaintings.php?Search="
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
                        CartItem1.Quantity = 0
                        CartItem1.MaxQuantity = Int(data.Quantity!)!
                        
                        self.ListofPaintings.append(CartItem1)
                    }
                    
                    for (index, str) in self.ListofPaintings.enumerated() {
                        Alamofire.request(str.CartUrl).responseImage { response in
                            if let image = response.result.value {
                                DispatchQueue.main.async(execute: {
                                    self.ListofPaintings[index].ImageIMG = image
                                    self.collectionView.reloadData()
                                })
                            }
                        }
                    }
                }
                DispatchQueue.main.async {
                    self.collectionView.reloadData()
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
        collectionView.reloadData()
        GetJSON()
    }
    
    
    func GetJSON () {
        var URL1 =  MainURL +  "/Search.JSON"
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                if (gitData.count != self.ListofPaintings.count ){
                    for data in gitData {
                        let NewSearch = data.Search!.replacingOccurrences(of: "_", with: " ", options: .literal, range: nil)
                        self.ListOFSearch.append(NewSearch)
                        self.SearchField.filterStrings(self.ListOFSearch)
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
    
    
    @objc func sortArray() {
        ListofPaintings.removeAll()
        self.stopTheDamnRequests()
        let refreshControl = UIRefreshControl.self
        var URL1 = MainURL + "/ShowPaintings.php?Search="
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                if (gitData.count != self.ListOfPAintings.count ){
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
                                DispatchQueue.main.async(execute: {
                                self.ListofPaintings[index].ImageIMG = image
                                    self.collectionView.reloadData()
                                })
                            }
                        }
                    }
                }
                DispatchQueue.main.async { let offset = self.collectionView.contentOffset
                        self.collectionView.reloadData()
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
        collectionView.reloadData()
        collectionView.refreshControl?.endRefreshing()
    }
    
    let labelWish = UILabel(frame: CGRect(x: 10, y: -5, width: 20, height: 20))
    let labelCart = UILabel(frame: CGRect(x: 10, y: -5, width: 20, height: 20))
    
    func reloadBar() {
        if (isKeyPresentInUserDefaults(key: "WishList")){
            labelWish.text = String(UserDefaults.standard.array(forKey: "WishList")!.count)
        }
        if (isKeyPresentInUserDefaults(key: "CartItems")){
            labelCart.text = String((UserDefaults.standard.structArrayData(SizeToSave.self, forKey: "CartItems")).count)
        }
    }
    
    @objc func didTapEditButton(sender: AnyObject){
        performSegue(withIdentifier: "ShowWishList", sender: self)
    }
    
    
    @objc func didTapCartButton(sender: AnyObject){
        performSegue(withIdentifier: "ShowCart", sender: self)
    }
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        CheckForDiscount()
        
    }
    
    var Image1 : UIImage?  = nil
    func CheckForDiscount() {
       var tmpUrl : String = ""
        var URL1 = MainURL + "/LoadDiscount.php"
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                var DiscountInt : Int = 0
                UserDefaults.standard.set(DiscountInt, forKey: "Discount")
                for data in gitData {
                    print(data)
                    tmpUrl = data.Url!
                    DiscountInt = Int(data.Discount!)!
                    UserDefaults.standard.set(DiscountInt, forKey: "Discount")
                    Alamofire.request(tmpUrl).responseImage { response in
                        if let image = response.result.value {
                            DispatchQueue.main.async(execute: {
                                self.Image1 = image
                                
                                if (image != nil) {
                                self.performSegue(withIdentifier: "ShowOffer", sender: nil)
                                }
                            })
                        }
                    }
                }
                
            }
            catch let err {
                print("Err", err)
            }
            }.resume()
        
        
       
    }
    
    func textFieldDidEndEditing(_ textField: UITextField){
    }
    
   
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        // don't force `endEditing` if you want to be asked for resigning
        // also return real flow value, not strict, like: true / false
        SearchMe()
        return textField.endEditing(false)
    }
    
    override func touchesBegan(_ touches: Set<UITouch>,
                               with event: UIEvent?) {
        self.view.endEditing(true)
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
    
    func SearchMe () {
        stopTheDamnRequests()
        ListofPaintings.removeAll()
        collectionView.reloadData()
        var URL1 = MainURL + "/ShowPaintings.php?Search="
        var NewSearch = Searxhtxt.text!.replacingOccurrences(of: " ", with: "_", options: .literal, range: nil)
        URL1.append(NewSearch)
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
                    if (str.CartUrl != "") {
                    Alamofire.request(str.CartUrl).responseImage { response in
                        if let image = response.result.value {
                            DispatchQueue.main.async(execute: {
                            self.ListofPaintings[index].ImageIMG = image
                                self.collectionView.reloadData()
                            })
                        }
                    }
                    }
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "ShowOffer" {
            if let nextViewController = segue.destination as? DiscountViewController {
                nextViewController.Image1 = Image1!
            }
        }
    }
    
   
    func collectionView(_ collectionView: UICollectionView,numberOfItemsInSection sections: Int) -> Int {
        if ListofPaintings.count == 0 {
            collectionView.setEmptyView(title: "couldn't load Artworks", message: "Please check your internet connection and try again.")
        }
        else {
            collectionView.restore()
        }
        return ListofPaintings.count + 2
    }
    
    var cellID : Int = 0
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath){
        if (indexPath.row > 1){
        UserDefaults.standard.set(ListofPaintings[indexPath.row - 2].CartID, forKey: "CartIDFull")
        performSegue(withIdentifier:"showItem", sender: self)
        }
    }
    
    public func LoadFromKids() {
    performSegue(withIdentifier:"showItem", sender: self)
    }
  
    
    
    
 
    


    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if(indexPath.row == 0) { //If index of cell is less than the number of players then display the player
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "FeaturedList", for: indexPath) as! FeaturedCollectionViewCell
            cell.LoadFeatured()
            cell.Daddy = self
            return cell;
        }
        else if(indexPath.row == 1) { //If index of cell is less than the number of players then display the player
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "BestSelling", for: indexPath) as! BestSellingCollectionViewCell
            cell.LoadFeatured()
            cell.Daddy = self
            return cell;
        }
        else{//Else display DefaultCell
            
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifier, for: indexPath)
            as? CustomCollectionCell else {
                fatalError()
        }
        

        var NewIndexPath : Int = indexPath.row - 2
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
        
        cell.Title.text = "Artwork name: " + ListofPaintings[NewIndexPath].Cartname
        cell.thisUrl = ListofPaintings[NewIndexPath].CartUrl
        cell.Artist.text = "by " + ListofPaintings[NewIndexPath].CartArtist
        cell.CImage.image = ListofPaintings[NewIndexPath].ImageIMG
        cell.Description.text = ListofPaintings[NewIndexPath].Cartdescription
        cell.ID = ListofPaintings[NewIndexPath].CartID
      //  cell.Size.text = "Size: " + ListofPaintings[indexPath.row][7]
        cell.Daddy = self
        cell.MaxQuantity = ListofPaintings[NewIndexPath].MaxQuantity
            if (Int(UserDefaults.standard.value(forKey: "Discount") as! Int) != 0 ) {
            let attrString = NSMutableAttributedString(string: String(Int(Double(ListofPaintings[NewIndexPath].CartPrice)! / (1 - 0.1))) + "$", attributes: [NSAttributedString.Key.strikethroughStyle: NSUnderlineStyle.single.rawValue])
            
             let attrString1 = NSMutableAttributedString(string: "  " +  String(ListofPaintings[NewIndexPath].CartPrice) + "$")
            let attrString0 = NSMutableAttributedString(string: "Starting From: ")
            var combination = NSMutableAttributedString()
                
            combination = attrString0
            combination.append(attrString)
            combination.append(attrString1)
            
            cell.Price.attributedText = combination
            }
            else {
                cell.Price.text = ListofPaintings[NewIndexPath].CartPrice + "$"
            }
        return cell
        }
    }
    
    
}


extension Array {
    func indexExists(_ index: Int) -> Bool {
        return self.indices.contains(index)
    }
}




struct CartItem {
    var Cartname = ""
    var CartPrice = ""
    var CartArtist = ""
    var Cartdescription = ""
    var CartID = ""
    var CartUrl = ""
    var Quantity = 0
    var MaxQuantity = 0
    var ImageIMG = UIImage(named: "empty-image")!
}

