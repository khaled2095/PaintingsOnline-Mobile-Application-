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

class CategoryViewController: UIViewController , UITableViewDataSource, UITableViewDelegate{
    
    @IBOutlet weak var TableView2: UITableView!
    var tmpListOfTitle = [String]()
    var tmpListOfurl = [String]()
    var ListOfTitle = [String]()
    var Images = [UIImage]()
    var tmpListOfTitleRoom = [String]()
    var tmpListOfurlRoom = [String]()
    var ListOfTitleRoom = [String]()
    var ImagesRoom = [UIImage]()
    @IBOutlet weak var Items: UIBarButtonItem!
    
    @IBOutlet weak var ChangeTypeObject: UISegmentedControl!
    
    
    override func viewWillAppear(_ animated: Bool) {
        if (isKeyPresentInUserDefaults(key: "ListofPaintings")){
        Items.addBadge(number: (UserDefaults.standard.array(forKey: "ListofPaintings")?.count)!)
        Items.updateBadge(number: UserDefaults.standard.array(forKey: "ListofPaintings")!.count)
    }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if (ChangeTypeObject.selectedSegmentIndex == 0) {
        return tmpListOfTitle.count
        }
        else{
        return tmpListOfTitleRoom.count
        }
    }
    
    @IBAction func ChangeType(_ sender: Any) {
        if (ChangeTypeObject.selectedSegmentIndex == 0) {
            LoadCategories()
        }
        else {
            LoadRooms()
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier:"CustomCell", for: indexPath as IndexPath) as! CategoryTableViewCell
        if (ChangeTypeObject.selectedSegmentIndex == 0) {
        cell.Title.text = ListOfTitle[indexPath.row]
        if (Images.indexExists(indexPath.row)){
        cell.CImage.image = Images[indexPath.row]
        }
        else {
            cell.CImage.image = UIImage(named: "empty-image")
        }
        return cell
        }
        else{
            cell.Title.text = ListOfTitleRoom[indexPath.row]
            if (ImagesRoom.indexExists(indexPath.row)){
                cell.CImage.image = ImagesRoom[indexPath.row]
            }
            else {
                cell.CImage.image = UIImage(named: "empty-image")
            }
            return cell
        }
        }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if (ChangeTypeObject.selectedSegmentIndex == 0) {
        UserDefaults.standard.set("https://jrnan.info/Painting/ShowPaintings.php?Category=", forKey: "URL")
        UserDefaults.standard.set(String(indexPath.row), forKey: "Selected")
        performSegue(withIdentifier: "ShowPainting", sender: (Any).self)
        }
        else {
            UserDefaults.standard.set("https://jrnan.info/Painting/ShowPaintings.php?Room=", forKey: "URL")
            UserDefaults.standard.set(String(indexPath.row), forKey: "Selected")
            performSegue(withIdentifier: "ShowPainting", sender: (Any).self)
        }
    }

    



    override func viewDidLoad() {
        Items.addBadge(number: 3)
        super.viewDidLoad()
        LoadCategories()
        
    }
    
    func isKeyPresentInUserDefaults(key: String) -> Bool {
        return UserDefaults.standard.object(forKey: key) != nil
    }

    func LoadCategories() {
        
        self.tmpListOfTitle.removeAll()
        self.tmpListOfurl.removeAll()
        self.ListOfTitle.removeAll()
        self.Images.removeAll()
        var URL1 = "https://jrnan.info/Painting/ShowCategory.php"
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    self.tmpListOfTitle.append((data.Name as! String))
                    self.tmpListOfurl.append((data.URL as! String))
                }
                
                for str in self.tmpListOfurl{
                    self.Images.append(UIImage(named:"empty-image")!)
                    self.ListOfTitle.append("")
                }
                
                for (index, str) in self.tmpListOfurl.enumerated() {
                    Alamofire.request(str).responseImage { response in
                        if let image = response.result.value {
                            self.Images[index] = image
                            self.ListOfTitle[index] = self.tmpListOfTitle[index]
                            DispatchQueue.main.async(execute: {self.reload(tableView: self.TableView2)})
                        }
                    }
                }
                DispatchQueue.main.async(execute: {self.reload(tableView: self.TableView2)})
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }

    
    func LoadRooms() {
        self.tmpListOfTitleRoom.removeAll()
        self.tmpListOfurlRoom.removeAll()
        self.ListOfTitleRoom.removeAll()
        self.ImagesRoom.removeAll()
        var URL1 = "https://jrnan.info/Painting/ShowRoom.php"
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    self.tmpListOfTitleRoom.append((data.Name as! String))
                    self.tmpListOfurlRoom.append((data.URL as! String))
                }
                
                for str in self.tmpListOfurlRoom{
                    self.ImagesRoom.append(UIImage(named:"empty-image")!)
                    self.ListOfTitleRoom.append("")
                }
                for (index, str) in self.tmpListOfurlRoom.enumerated() {
                    Alamofire.request(str).responseImage { response in
                        if let image = response.result.value {
                            self.ImagesRoom[index] = image
                            self.ListOfTitleRoom[index] = self.tmpListOfTitleRoom[index]
                            DispatchQueue.main.async(execute: {let offset = self.TableView2.contentOffset
                                self.reloadTableOnMain(with: offset)})
                        }
                    }
                }
                DispatchQueue.main.async(execute: {let offset = self.TableView2.contentOffset
                    self.reloadTableOnMain(with: offset)})
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    
    func reloadTableOnMain(with offset: CGPoint = CGPoint.zero){
        DispatchQueue.main.async { [weak self] () in
            
            self?.TableView2.reloadData()
            self?.TableView2.layoutIfNeeded()
            self?.TableView2.contentOffset = offset
        }
    }
    

    func reload(tableView: UITableView) {
        
        let contentOffset = tableView.contentOffset
        tableView.reloadData()
        tableView.layoutIfNeeded()
        tableView.setContentOffset(contentOffset, animated: false)
        
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
