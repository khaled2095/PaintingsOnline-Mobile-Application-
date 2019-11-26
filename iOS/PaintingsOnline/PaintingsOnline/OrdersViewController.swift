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
import Foundation
import MessageUI
import MaterialComponents.MaterialCards

class OrdersViewController:UIViewController,UITableViewDataSource,UITableViewDelegate , MFMailComposeViewControllerDelegate {
    
    @IBOutlet weak var TableView2: UITableView!

    var ListOfPaintings = [CartItem4]()
    var Images = [UIImage]()


    
   func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
    if ListOfPaintings.count == 0 {
        tableView.setEmptyView(title: "Your order history is empty", message: "You can track the purchases in this page.")
    }
    else {
        tableView.restore()
    }
    return ListOfPaintings.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier:"CustomCell", for: indexPath as IndexPath) as! TableViewCell
        cell.Price.text = "Price: " + String(Int(ListOfPaintings[indexPath.row].CartPrice)! * Int(ListOfPaintings[indexPath.row].Quantity)!) + " Qt:" + ListOfPaintings[indexPath.row].Quantity
        cell.Status.text = "Status: " + ListOfPaintings[indexPath.row].Status
        cell.Artist.text = "Artist: " + ListOfPaintings[indexPath.row].CartArtist
        cell.Purchase_id = ListOfPaintings[indexPath.row].PurchaseId
        cell.What.text = ListOfPaintings[indexPath.row].Cartname
        cell.Rating = Int(ListOfPaintings[indexPath.row].rating)!
        cell.viewController = self
        cell.ImageToShow = ListOfPaintings[indexPath.row].ImageIMG
        cell.CImage!.image = ListOfPaintings[indexPath.row].ImageIMG
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 228.0;//Choose your custom row height
    }
    
     @objc func sortArray() {
        self.ListOfPaintings.removeAll()
        
        var URL1 = MainURL + "/Orders.php?Username="
        if(isKeyPresentInUserDefaults(key: "username")){
            URL1 += UserDefaults.standard.value(forKey: "username") as! String
        }
        guard let gitUrl = URL(string: URL1.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                    
                
                for data in gitData {
                    var CartIte : CartItem4 = CartItem4()
                    CartIte.CartUrl = data.PaintingUrl as! String
                    CartIte.Status = data.status as! String
                    CartIte.CartPrice = data.Price as! String
                    CartIte.CartArtist = data.Artist as! String
                    CartIte.PurchaseId = data.purchase_id as! String
                    CartIte.Quantity = data.Quantity as! String
                    CartIte.rating = data.Rating as! String
                    CartIte.Buyer = data.Buyer as! String
                    CartIte.Cartname = data.painting_name as! String
                    CartIte.ImageIMG = UIImage(named:"empty-image")!
                    self.ListOfPaintings.append(CartIte)
                }
                
                for (index, str) in self.ListOfPaintings.enumerated() {
                    Alamofire.request(str.CartUrl).responseImage { response in
                        if let image = response.result.value {
                            self.ListOfPaintings[index].ImageIMG = image
                            DispatchQueue.main.async(execute: {let offset = self.TableView2.contentOffset
                                self.reloadTableOnMain(with: offset)})
                        }
                    }
                }
                
                DispatchQueue.main.async(execute: {let offset = self.TableView2.contentOffset
                    self.reloadTableOnMain(with: offset)})
            }
            catch let err {
                print("Err", err)
            }
            }.resume()
        
        TableView2.reloadData()
        TableView2.refreshControl?.endRefreshing()
    }
    var MainURL = ""
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidLoad()
        MainURL = UserDefaults.standard.string(forKey: "MainURL")!
        let refreshControl = UIRefreshControl()
        TableView2.rowHeight = 200
        TableView2.estimatedRowHeight = 200
        refreshControl.addTarget(self, action:  #selector(sortArray), for: .valueChanged)
        self.TableView2.refreshControl = refreshControl
        sortArray()
    }
    
    func isKeyPresentInUserDefaults(key: String) -> Bool {
        return UserDefaults.standard.object(forKey: key) != nil
    }
    
    
    func reloadTableOnMain(with offset: CGPoint = CGPoint.zero){
        DispatchQueue.main.async { [weak self] () in
            self?.TableView2.reloadData()
            self?.TableView2.layoutIfNeeded()
            self?.TableView2.contentOffset = offset
        }
    }

    func SendMail(OrderNumber : String , Artist : String ) {
        let composeVC = MFMailComposeViewController()
        composeVC.mailComposeDelegate = self
        // Configure the fields of the interface.
        composeVC.setToRecipients(["admin@jrnan.info"])
        composeVC.setSubject("Refrence Order Number:" + OrderNumber)
        composeVC.setMessageBody("To whom it matters, I bought a painting from " + Artist + " and this is my feedback", isHTML: false)
        // Present the view controller modally.
        self.present(composeVC, animated: true, completion: nil)
    }
    
    func mailComposeController(_ controller: MFMailComposeViewController, didFinishWith result: MFMailComposeResult, error: Error?) {
        controller.dismiss(animated: true, completion: nil)
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
    
    public func RateMe() {
        performSegue(withIdentifier: "ShowMeRating", sender: AnyObject.self)
        
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?)
    {
        if segue.destination is RatingViewController
        {
            let vc = segue.destination as? RatingViewController
            vc?.vc = self;
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


private var handle: UInt8 = 0;

extension UIBarButtonItem {
    private var badgeLayer: CAShapeLayer? {
        if let b: AnyObject = objc_getAssociatedObject(self, &handle) as AnyObject? {
            return b as? CAShapeLayer
        } else {
            return nil
        }
    }
    
}




struct CartItem4 {
    var Cartname = ""
    var CartPrice = ""
    var CartArtist = ""
    var Cartdescription = ""
    var CartID = ""
    var CartUrl = ""
    var Quantity = ""
    var MaxQuantity = 0
    var ImageIMG = UIImage(named: "empty-image")!
    var Status = ""
    var rating = ""
    var PurchaseId = ""
    var Buyer = ""
    
}


