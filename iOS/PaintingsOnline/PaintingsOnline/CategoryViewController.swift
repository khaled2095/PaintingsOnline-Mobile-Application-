//
//  CategoryViewController.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 29/3/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage
import CollectionViewSlantedLayout
import IGListKit

class CategoryViewController: UIViewController , UICollectionViewDataSource , UICollectionViewDelegate , UICollectionViewDelegateFlowLayout{
    
    var tmpListOfTitle = [String]()
    var tmpListOfurl = [String]()
    var ListOfTitle = [String]()
    var Images = [UIImage]()
    var tmpListOfTitleRoom = [String]()
    var tmpListOfurlRoom = [String]()
    var ListOfTitleRoom = [String]()
    var ImagesRoom = [UIImage]()
    
    var Items1 = [Category]()

    @IBOutlet weak var ChangeTypeObject: UISegmentedControl!
    
    @IBOutlet weak var CollectionViewLayout: UICollectionViewLayout!
    
    @IBOutlet weak var collectionView: UICollectionView!
    var MainURL = ""
    let reuseIdentifier = "customViewCell"
    
    let labelWish = UILabel(frame: CGRect(x: 10, y: -5, width: 20, height: 20))
    let labelCart = UILabel(frame: CGRect(x: 10, y: -5, width: 20, height: 20))
    
    override func viewWillAppear(_ animated: Bool) {
        collectionView.isPrefetchingEnabled = false
        MainURL = UserDefaults.standard.string(forKey: "MainURL")!
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
    
    
    @IBAction func ChangeType(_ sender: Any) {
        if (ChangeTypeObject.selectedSegmentIndex == 0) {
        LoadCategories()
        }
        else {
        LoadRooms()
        }
    }
    

    
   



    override func viewDidLoad() {
        super.viewDidLoad()
        MainURL = UserDefaults.standard.string(forKey: "MainURL")!
            LoadCategories()
    }
    
    

    func LoadCategories() {
        stopTheDamnRequests()
        Items1.removeAll()
        collectionView.reloadData()
          let offset = self.collectionView.contentOffset
        var URL1 = MainURL + "/ShowCategory.php"
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    var TempCat : Category = Category()
                    TempCat.Name = data.Name as! String
                    TempCat.Image = data.URL as! String
                    self.Items1.append(TempCat)
                }
                DispatchQueue.main.async {
                for (index, str) in self.Items1.enumerated() {
                    Alamofire.request(str.Image).responseImage { response in
                        if let image = response.result.value {
                            self.Items1[index].ImageIMG = image
                            self.collectionView.reloadData()
                        }
                    }
                    DispatchQueue.main.async(execute: {
                        self.collectionView.reloadData()
                    })
                }
            }
            }
            catch let err {
                print("Err", err)
            }
            }.resume()
       
    }

    
    func LoadRooms() {
        stopTheDamnRequests()
        Items1.removeAll()
        collectionView.reloadData()
        var URL1 = MainURL + "/ShowRoom.php"
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    var TempCat : Category = Category()
                    TempCat.Name = data.Name as! String
                    TempCat.Image = data.URL as! String
                    self.Items1.append(TempCat)
                }
                DispatchQueue.main.async {
                for (index, str) in self.Items1.enumerated() {
                    Alamofire.request(str.Image).responseImage { response in
                        if let image = response.result.value {
                            self.Items1[index].ImageIMG = image
                            self.collectionView.reloadData()
                        }
                    }
                     DispatchQueue.main.async(execute: {
                    self.collectionView.reloadData()
                    })
                }
                }} catch let err {
                print("Err", err)
            }
            }.resume()
       
    }
    
    func reloadTableOnMain(){
        DispatchQueue.main.async { [weak self] () in
            self?.collectionView.reloadData()
            self?.collectionView.collectionViewLayout.invalidateLayout()
             self?.collectionView.layoutIfNeeded()
            self?.collectionView.layoutSubviews() // <-- here it is :)
        }
    }
    
    
    
    func collectionView(_ collectionView: UICollectionView,numberOfItemsInSection sections: Int) -> Int {
         if (ChangeTypeObject.selectedSegmentIndex == 0) {
            return  GetCountOfCategories();
        }
         else {
            return GetCountOfRooms();
        }
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

    func GetCountOfRooms()-> Int {
        return Items1.count
    }
    func GetCountOfCategories()-> Int {
        return Items1.count
    }
  
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath){
        if (ChangeTypeObject.selectedSegmentIndex == 0) {
            UserDefaults.standard.set(MainURL + "/ShowPaintings.php?Category=", forKey: "URL")
            UserDefaults.standard.set(String(indexPath.row), forKey: "Selected")
            performSegue(withIdentifier: "ShowPainting", sender: (Any).self)
        }
        else {
            UserDefaults.standard.set(MainURL + "/ShowPaintings.php?Room=", forKey: "URL")
            UserDefaults.standard.set(String(indexPath.row), forKey: "Selected")
            performSegue(withIdentifier: "ShowPainting", sender: (Any).self)
        }
    }
    
    
    func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "showItem" {
            if let destination = segue.destination as? AllPaintingViewController{
                let cell = sender as! CustomCollectionCell
                let selectedData = [cell.ID,cell.Artist,cell.Description.text,cell.PriceB.titleLabel?.text,cell.MaxQuantity] as! [String]
                // postedData is the variable that will be sent, make sure to declare it in YourDestinationViewController
                destination.strItem = selectedData
            }
        }
    }
    
    
    

    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifier, for: indexPath)
            as? CategoryTableViewCell else {
                
                fatalError()
        }
        
        cell.contentView.layer.cornerRadius = 10
        cell.contentView.layer.borderWidth = 1.0
        
        cell.contentView.layer.borderColor = UIColor.clear.cgColor
        cell.contentView.layer.masksToBounds = true
        
        cell.layer.shadowColor = UIColor.gray.cgColor
        cell.layer.shadowOffset = CGSize(width: 0, height: 2.0)
        cell.layer.shadowRadius = 2.0
        cell.layer.shadowOpacity = 1.0
        cell.layer.masksToBounds = false
        cell.layer.shadowPath = UIBezierPath(roundedRect:cell.bounds, cornerRadius:cell.contentView.layer.cornerRadius).cgPath
        
        if (ChangeTypeObject.selectedSegmentIndex == 0 && Items1.count > indexPath.row) {
            cell.Title.text = Items1[indexPath.row].Name
            cell.CImage.image = Items1[indexPath.row].ImageIMG
            return cell
        }
        else if (ChangeTypeObject.selectedSegmentIndex == 1 && Items1.count > indexPath.row){
            cell.Title.text = Items1[indexPath.row].Name
            cell.CImage.image = Items1[indexPath.row].ImageIMG
            return cell
        }
        else {
            return (collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifier, for: indexPath)
                as? CategoryTableViewCell)!
        }
    }
    
    func collectionView(collectionView: UICollectionView,
                        layout collectionViewLayout: UICollectionViewLayout,
                        sizeForItemAtIndexPath indexPath: NSIndexPath) -> CGSize {
        return CGSize(width: 300, height: 100)
    }
    
    func collectionView(_ collectionView: UICollectionView,
                        layout collectionViewLayout: UICollectionViewLayout,
                        minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 1.0
    }
    
    func collectionView(_ collectionView: UICollectionView, layout
        collectionViewLayout: UICollectionViewLayout,
                        minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 1.0
    }
    

    /*
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}

extension CAShapeLayer {
    func drawCircleAtLocation(location: CGPoint, withRadius radius: CGFloat, andColor color: UIColor, filled: Bool) {
        fillColor = filled ? color.cgColor : UIColor.white.cgColor
        strokeColor = color.cgColor
        let origin = CGPoint(x: location.x - radius, y: location.y - radius)
        path = UIBezierPath(ovalIn: CGRect(origin: origin, size: CGSize(width: radius * 2, height: radius * 2))).cgPath
    }
}

private var handle: UInt8 = 0;

extension UIBarButtonItem {
    private var badgeLayer: CAShapeLayer? {
        if let b: AnyObject = objc_getAssociatedObject(self, &handle) as AnyObject? {
            return b as? CAShapeLayer
        } else {
            return nil
        }
    }
    
    
    func addBadge(number: Int, withOffset offset: CGPoint = CGPoint.zero, andColor color: UIColor = UIColor.red, andFilled filled: Bool = true) {
        guard let view = self.value(forKey: "view") as? UIView else { return }
        
        badgeLayer?.removeFromSuperlayer()
        
        var badgeWidth = 8
        var numberOffset = 4
        
        if number > 9 {
            badgeWidth = 12
            numberOffset = 6
        }
        
        // Initialize Badge
        let badge = CAShapeLayer()
        let radius = CGFloat(7)
        let location = CGPoint(x: view.frame.width - (radius + offset.x), y: (radius + offset.y))
        badge.drawCircleAtLocation(location: location, withRadius: radius, andColor: color, filled: filled)
        view.layer.addSublayer(badge)
        
        // Initialiaze Badge's label
        let label = CATextLayer()
        label.string = "\(number)"
        label.alignmentMode = CATextLayerAlignmentMode.center
        label.fontSize = 11
        label.frame = CGRect(origin: CGPoint(x: location.x - CGFloat(numberOffset), y: offset.y), size: CGSize(width: badgeWidth, height: 16))
        label.foregroundColor = filled ? UIColor.white.cgColor : color.cgColor
        label.backgroundColor = UIColor.clear.cgColor
        label.contentsScale = UIScreen.main.scale
        badge.addSublayer(label)
        
        // Save Badge as UIBarButtonItem property
        objc_setAssociatedObject(self, &handle, badge, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
    }
    
  
    
    
    func updateBadge(number: Int) {
        if let text = badgeLayer?.sublayers?.filter({ $0 is CATextLayer }).first as? CATextLayer {
            text.string = "\(number)"
        }
    }
    
    func removeBadge() {
        badgeLayer?.removeFromSuperlayer()
    }
}

struct Category {
     var Name : String = ""
     var Image : String = ""
    var ImageIMG : Image = UIImage(named: "empty-image")!
}


