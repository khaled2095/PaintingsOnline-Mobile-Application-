//
//  FullDetailsViewController.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 6/9/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit
import ImageSlideshow
import AlamofireImage
import Alamofire
class FullDetailsViewController: UIViewController , UIPickerViewDelegate, UIPickerViewDataSource , UITextFieldDelegate , UITableViewDelegate , UITableViewDataSource, UICollectionViewDataSource, UICollectionViewDelegate {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return ListofPaintings1.count
    }
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
         UserDefaults.standard.set(ListofPaintings1[indexPath.row].CartID, forKey: "CartIDFull")
         reloadViewFromNib()
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifier1, for: indexPath)
            as? FeaturedInnerCollectionCell else {
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
        
        
        cell.TmpCart.Cartname = ListofPaintings1[indexPath.row].Cartname
        cell.TmpCart.CartUrl = ListofPaintings1[indexPath.row].CartUrl
        cell.TmpCart.CartArtist = ListofPaintings1[indexPath.row].CartArtist
        cell.TmpCart.ImageIMG = ListofPaintings1[indexPath.row].ImageIMG
        cell.TmpCart.Cartdescription = ListofPaintings1[indexPath.row].Cartdescription
        cell.TmpCart.CartID = ListofPaintings1[indexPath.row].CartID
        cell.TmpCart.MaxQuantity = ListofPaintings1[indexPath.row].MaxQuantity
        cell.TmpCart.Quantity = 1
        cell.Title.text = "Artwork name: " + ListofPaintings1[indexPath.row].Cartname
        cell.thisUrl = ListofPaintings1[indexPath.row].CartUrl
        cell.Artist.text = "by " + ListofPaintings1[indexPath.row].CartArtist
        cell.CImage.image = ListofPaintings1[indexPath.row].ImageIMG
        cell.Description.text = ListofPaintings1[indexPath.row].Cartdescription
        cell.ID = ListofPaintings1[indexPath.row].CartID
        //  cell.Size.text = "Size: " + ListofPaintings[indexPath.row][7]
        cell.MaxQuantity = ListofPaintings1[indexPath.row].MaxQuantity
        if (Int(UserDefaults.standard.value(forKey: "Discount") as! Int) != 0 ) {
            let attrString = NSMutableAttributedString(string: String(Int(Double(ListofPaintings1[indexPath.row].CartPrice)! / (1 - 0.1))) + "$", attributes: [NSAttributedString.Key.strikethroughStyle: NSUnderlineStyle.single.rawValue])
            
            let attrString1 = NSMutableAttributedString(string: "  " +  String(ListofPaintings1[indexPath.row].CartPrice) + "$")
            let attrString0 = NSMutableAttributedString(string: "Starting From: ")
            var combination = NSMutableAttributedString()
            
            combination = attrString0
            combination.append(attrString)
            combination.append(attrString1)
            
            cell.Price.attributedText = combination
        }
        else {
            cell.Price.text = ListofPaintings1[indexPath.row].CartPrice + "$"
        }
        
        return cell
    }
    
    
    var ListofPaintings = [CartItem]()
    var Imagestmp : [Image] = [Image]()
    var ListofPaintings1 = [CartItem]()
    @IBOutlet weak var CollectionView1: UICollectionView!
    let reuseIdentifier1 = "customViewCell"
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if Comments.count == 0 {
            tableView.setEmptyView(title: "This Product has not been rated", message: "")
        }
        else {
            CommentTable.restore()
        }
        return Comments.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
         let cell = tableView.dequeueReusableCell(withIdentifier:"CommentCell", for: indexPath as IndexPath) as! CommentTableViewCell
        cell.TitleLbl.text = Comments[indexPath.row]
        var RatingText : String = ""
        if (Ratings[indexPath.row] > 0) {
        for i in 0...Ratings[indexPath.row] - 1  {
            RatingText += "⭐️"
        }
        }
        cell.RatingsLbl.text = RatingText
        return cell
    }
    
    
    
    var AllIDs = [String]()
    
    public func LoadRelatedIDs(Name : String) {
        AllIDs.removeAll()
        var URL1 = MainURL + "/RelatedProducts.php?Username="
        URL1.append(Name)
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                    for data in gitData {
                        self.SearchMeRelated(ID: data.painting_id!)
                    }
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    
    
    func SearchMeRelated (ID : String) {
        ListofPaintings1.removeAll()
        var URL1 = MainURL + "/ShowPaintings.php?Search="
        URL1.append(ID)
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
                        self.ListofPaintings1.append(CartItem1)

                        self.ListofPaintings1 = self.removeDuplicates(Elements: self.ListofPaintings1)
                    }
                DispatchQueue.main.async {
                    for (index, str) in self.ListofPaintings1.enumerated() {
                    Alamofire.request(str.CartUrl).responseImage { response in
                        if let image = response.result.value {
                            
                            self.ListofPaintings1[index].ImageIMG = image
                            self.CollectionView1.reloadData()
                        }
                    }
                    }
                 self.CollectionView1.reloadData()
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    
    
    func removeDuplicates(Elements : [CartItem])-> [CartItem] {
        var result = [CartItem]()
        for value in Elements {
            var isExist = false
            for x in result {
                if (x.CartID == value.CartID) {
                  isExist = true
                }
            }
            if (isExist == false) {
              result.append(value)
            }
        }
        return result
    }
    
    

    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return SizesHolder.count
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return (SizesHolder[row].Size)
    }
    
    
    @IBOutlet weak var CommentTable: UITableView!
    
    @IBOutlet weak var ShowFullImages: ImageSlideshow!
    var SelectedIndex : Int = 0
    var Images : [InputSource] = [InputSource]()
    var paintingURl = [String]()
    var CartID : String! = ""
    var Describtion : String = ""
    var Artist : String = ""
    var Name : String = ""
    var Ratings : [Int] = [Int]()
    var Comments : [String] = [String]()
    var SizesHolder : [Sizes3] = [Sizes3]()
    @IBOutlet weak var Describtion1: UITextView!
    @IBOutlet weak var NameLbl: UILabel!
    @IBOutlet weak var SizeBtn: UIButton!
    var toolBar = UIToolbar()
    var MiniPictures = [String]()
    @IBOutlet weak var AddToCart: UIButton!
    @IBOutlet weak var AddToWish: UIButton!
    @IBOutlet weak var RatingLabel: UILabel!
    var WishListButton: UIBarButtonItem!
    var CartListButton: UIBarButtonItem!
    
    @IBOutlet weak var ArtistButton: UIButton!
    
    
    var thePicker = UIPickerView()
    let labelWish = UILabel(frame: CGRect(x: 10, y: -5, width: 20, height: 20))
    let labelCart = UILabel(frame: CGRect(x: 10, y: -5, width: 20, height: 20))
    var MainURL = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        MainURL = UserDefaults.standard.string(forKey: "MainURL")!
        ListofPaintings.removeAll()
        ListofPaintings1.removeAll()
        Imagestmp.removeAll()
        
        
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
        
        CartID = UserDefaults.standard.string(forKey: "CartIDFull")
        
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
      
        thePicker.delegate = self
        SearchMe()
        LoadSizes ()
        LoadComments ()
        LoadImages()
        SizeBtn.backgroundColor = UIColor.clear
        SizeBtn.borderColor = UIColor.gray
        SizeBtn.layer.borderWidth = 1
        SizeBtn.layer.cornerRadius = 5.0
        SizeBtn.layer.shadowRadius = 2.0
        SizeBtn.layer.shadowOffset = CGSize(width: 1, height: -1)
        SizeBtn.layer.shadowOpacity = 0.1
        
        AddToCart.layer.cornerRadius = 2.0
        AddToWish.layer.cornerRadius = 2.0
        reloadBar()
        let gestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(FullDetailsViewController.didTap))
        ShowFullImages.addGestureRecognizer(gestureRecognizer)
        var TmpWishList = [String]()
        if (isKeyPresentInUserDefaults(key: "WishList")){
            TmpWishList = UserDefaults.standard.array(forKey: "WishList") as! [String]
            if(TmpWishList.contains(CartID)){
                AddToWish.setTitle("Remove From WishList", for: .normal)
            }
            else {
                AddToWish.setTitle("Add To WishList", for: .normal)
            }
        }
        
        
    }
    
    @objc func didTap() {
        ShowFullImages.presentFullScreenController(from: self)
    }
    func isKeyPresentInUserDefaults(key: String) -> Bool {
        return UserDefaults.standard.object(forKey: key) != nil
    }
    
    
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
    
    @IBAction func AddToWishList(_ sender: Any) {
       var TmpWishList = [String]()
        if (isKeyPresentInUserDefaults(key: "WishList")){
            TmpWishList = UserDefaults.standard.array(forKey: "WishList") as! [String]
        }
        if (TmpWishList.contains(CartID)){
            let TmpIndex = TmpWishList.firstIndex(of: CartID)
            TmpWishList.remove(at: TmpIndex!)
             UserDefaults.standard.setValue(TmpWishList, forKey: "WishList")
            AddToWish.setTitle("Add To WishList", for: .normal)
        }
        else {
        TmpWishList.append(CartID)
        UserDefaults.standard.setValue(TmpWishList, forKey: "WishList")
        AddToWish.setTitle("Remove From WishList", for: .normal)
        }
      reloadBar()
    }
    
    
    
    @IBAction func OpenPicker(_ sender: Any) {
        
            thePicker = UIPickerView.init()
            thePicker.delegate = self
            thePicker.backgroundColor = UIColor.white
            thePicker.setValue(UIColor.black, forKey: "textColor")
            thePicker.autoresizingMask = .flexibleWidth
           thePicker.contentMode = .center
            thePicker.frame = CGRect.init(x: 0.0, y: UIScreen.main.bounds.size.height - 300, width: UIScreen.main.bounds.size.width, height: 300)
            self.view.addSubview(thePicker)
            
            toolBar = UIToolbar.init(frame: CGRect.init(x: 0.0, y: UIScreen.main.bounds.size.height - 300, width: UIScreen.main.bounds.size.width, height: 50))
            toolBar.barStyle = .blackTranslucent
            toolBar.items = [UIBarButtonItem.init(title: "Done", style: .done, target: self, action: #selector(doneClick))]
            self.view.addSubview(toolBar)
        
    }
    func LoadImages () {
        var URL1 = MainURL + "/LoadPictures.php?painting_id="
        URL1.append(CartID!)
        MiniPictures.removeAll()
        paintingURl.removeAll()
        var PicturesArray = [UIImage]()
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    self.paintingURl.append(data.painting_url!)
                    self.MiniPictures.append(data.small_url!)
                        self.Images.append(ImageSource(image: UIImage(named:"empty-image")!))
                              self.addImages()
                }
                for (index, element) in self.paintingURl.enumerated() {
                    Alamofire.request(element).responseImage { response in
                        if let image = response.result.value {
                            self.Images[index] = (ImageSource(image: image))
                            self.addImages()
                            PicturesArray.append(image)
                             UserDefaults.standard.setImage(image: PicturesArray[0], forKey: "SavedImage")
                        }
                    }
                }
                self.LoadRelatedIDs(Name: self.MiniPictures[0])
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    
    
    func LoadComments () {
        var URL1 = MainURL + "/LoadComments.php?painting_id="
        URL1.append(CartID!)
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
                    self.Comments.append(data.Comment!)
                    self.Ratings.append(Int(data.Rating!)!)
                    self.CommentTable.reloadData()
                    }
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    
    var ArtistID : String = ""
        
    func SearchMe () {
        var URL1 = MainURL + "/ShowPaintings.php?Search="
        URL1.append(CartID!)
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
                    self.NameLbl.text = data.painting_name!
                    self.ArtistID = data.painting_artist!
                    self.ArtistButton.setTitle("by " + data.painting_artist!, for: .normal)
                    self.Describtion1.text = data.painting_description!
                    }
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    
    
    func LoadSizes () {
        var URL1 = MainURL + "/LoadSizes.php?painting_id="
        URL1.append(CartID!)
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    var TmpHolder = Sizes3() ;
                    TmpHolder.Price = Int(data.Price!)!
                    TmpHolder.Size = data.Size!
                    TmpHolder.Quantity = Int(data.Quantity!)!
                    self.SizesHolder.append(TmpHolder)
                    
                    DispatchQueue.main.async {
                        self.thePicker.reloadAllComponents()
                        self.SizeBtn.setTitle(self.SizesHolder[0].Size, for: .normal)
                        self.UpdateAddToCart ()
                    }
                  
                    
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    
    var isInCart : Bool = false;
    func UpdateAddToCart () {
        isInCart = false
        var SizeToSave1 : [SizeToSave] = [SizeToSave]()
        if (isKeyPresentInUserDefaults(key: "CartItems")){
            SizeToSave1  = UserDefaults.standard.structArrayData(SizeToSave.self, forKey: "CartItems");
            self.AddToCart.setTitle("Add To Cart (" + String(self.SizesHolder[0].Price) + "$)", for: .normal)
            }
        
            for size1 in SizeToSave1 {
                if (size1.painting_id == CartID) {
                    AddToCart.setTitle("Remove From Cart", for: .normal)
                    isInCart = true;
                }
            }
        }
    

    @IBAction func AddToCart(_ sender: Any) {
        UpdateAddToCart()
        var SizeToSave1 : [SizeToSave] = [SizeToSave]()
        if (isKeyPresentInUserDefaults(key: "CartItems")){
            SizeToSave1  = UserDefaults.standard.structArrayData(SizeToSave.self, forKey: "CartItems");
        }
        if (isInCart) {
            for (index, size1) in SizeToSave1.enumerated(){
                if (size1.painting_id == CartID) {
                    SizeToSave1.remove(at:index)
                    break
                }
            }
            UserDefaults.standard.setStructArray(SizeToSave1,forKey:"CartItems")
            UpdateAddToCart()
            reloadBar()
        }
        else {
            let TempSize = SizeToSave(painting_id: CartID, painting_size: self.SizeBtn.title(for: .normal)!,painting_Image: self.paintingURl[0],painting_price : String(self.SizesHolder[SelectedIndex].Price) , painting_artist:  (self.ArtistButton.titleLabel?.text!)!, painting_name: self.NameLbl.text!, painting_quantity : String(self.SizesHolder[SelectedIndex].Quantity), WhatID : "" )
            SizeToSave1.append(TempSize)
            UserDefaults.standard.setStructArray(SizeToSave1,forKey:"CartItems")
            reloadBar()
            UpdateAddToCart()
        }
      
       
    }
    
    @IBAction func GoToArtist(_ sender: Any) {
    UserDefaults.standard.set(ArtistID, forKey: "ArtistID")
        performSegue(withIdentifier: "ShowArtist", sender: nil)
    }
    
  
  
    
    var SizeString : String = ""
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        SelectedIndex = Int(row)
        AddToCart.setTitle("Add To Cart (" + String(SizesHolder[row].Price) + "$)", for: .normal)
         SizeBtn.setTitle(SizesHolder[row].Size , for: .normal)
    }
    
   
    
    @objc func doneClick() {
        toolBar.removeFromSuperview()
       thePicker.removeFromSuperview()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        reloadBar()
    }
    
    func addImages(){
        DispatchQueue.main.async {
            self.ShowFullImages.setImageInputs(self.Images)
        }
    }
    

}


struct Sizes3 {
    var Size : String = ""
    var Price : Int = 0
    var Quantity : Int = 0
}



extension UITextField {
    func setIcon(_ image: UIImage) {
        let iconView = UIImageView(frame:
            CGRect(x: 10, y: 5, width: 40, height: 20))
        iconView.image = image
        let iconContainerView: UIView = UIView(frame:
            CGRect(x: 20, y: 0, width: 50, height: 30))
        iconContainerView.addSubview(iconView)
        leftView = iconContainerView
        leftViewMode = .always
    }
}



struct SizeToSave: Codable {
    var painting_id: String = ""
    var painting_size : String = ""
    var painting_Image :String = ""
    var painting_price : String = ""
    var painting_artist : String = ""
    var painting_name : String = ""
    var painting_quantity : String = ""
    var WhatID : String = ""
}


extension UIViewController {
    func reloadViewFromNib() {
        let parent = view.superview
        view.removeFromSuperview()
        view = nil
        parent?.addSubview(view) // This line causes the view to be reloaded
    }
}


extension UserDefaults {
    func imageForKey(key: String) -> UIImage? {
        var image: UIImage?
        if let imageData = data(forKey: key) {
            image = NSKeyedUnarchiver.unarchiveObject(with: imageData) as? UIImage
        }
        return image
    }
    func setImage(image: UIImage?, forKey key: String) {
        var imageData: NSData?
        if let image = image {
            imageData = NSKeyedArchiver.archivedData(withRootObject: image) as NSData?
        }
        set(imageData, forKey: key)
    }
}
