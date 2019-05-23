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

class OrdersViewController:UIViewController,UITableViewDataSource,UITableViewDelegate , MFMailComposeViewControllerDelegate {
    
    @IBOutlet weak var TableView2: UITableView!

    var tmpListOfPrices = [String]()
    var tmpListOfStatus = [String]()
    var tmpListOfurl = [String]()
    var ListOfArtists = [String]()
    var ListOfBuyers = [String]()
    var ListOfWhat = [String]()
    var ListOfStatus = [String]()
    var ListOfPurchaseID = [String]()
    var Images = [UIImage]()
    var ListOfQuantities = [String]()
    
   func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return ListOfArtists.count
    }
    
     func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier:"CustomCell", for: indexPath as IndexPath) as! TableViewCell
        cell.Price.text = "Price: " + String(Int(tmpListOfPrices[indexPath.row])! * Int(ListOfQuantities[indexPath.row])!) + " Qt:" + ListOfQuantities[indexPath.row]
        cell.Status.text = "Status: " + tmpListOfStatus[indexPath.row]
        cell.Artist.text = "Artist: " + ListOfArtists[indexPath.row]
        cell.Purchase_id = ListOfPurchaseID[indexPath.row]
        cell.What.text = ListOfWhat[indexPath.row]
        cell.viewController = self
        print(self.Images.count)
        if (Images.indexExists(indexPath.row)){
            cell.CImage.image = Images[indexPath.row]
        }
        else {
            cell.CImage.image = UIImage(named: "empty-image")
        }
        return cell
    }
     @objc func sortArray() {
       
        
        var URL1 = "https://jrnan.info/Painting/Orders.php?Username="
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
                if (gitData.count != self.tmpListOfPrices.count) {
                    
                    self.tmpListOfPrices.removeAll()
                    self.tmpListOfurl.removeAll()
                    self.Images.removeAll()
                    self.ListOfArtists.removeAll()
                    self.ListOfBuyers.removeAll()
                    self.ListOfWhat.removeAll()
                    self.ListOfStatus.removeAll()
                    self.ListOfPurchaseID.removeAll()
                    self.ListOfQuantities.removeAll()
                    
                for data in gitData {
                    self.tmpListOfurl.append((data.PaintingUrl as! String))
                    self.tmpListOfStatus.append((data.status as! String))
                    self.tmpListOfPrices.append((data.Price as! String))
                    self.tmpListOfStatus.append((data.status as! String))
                    self.ListOfArtists.append((data.Artist as! String))
                    self.ListOfPurchaseID.append((data.purchase_id as! String))
                    self.ListOfQuantities.append((data.Quantity as! String))
                    self.ListOfBuyers.append((data.Buyer as! String))
                    if ((data.Buyer as! String) == (UserDefaults.standard.value(forKey: "username") as! String)){
                        self.ListOfWhat.append("Bought")
                    }
                    else{
                        self.ListOfWhat.append("Sold")
                    }
                }
                
                
                for str in self.tmpListOfurl{
                    self.Images.append(UIImage(named:"empty-image")!)
                }
                
                for (index, str) in self.tmpListOfurl.enumerated() {
                    Alamofire.request(str).responseImage { response in
                        if let image = response.result.value {
                            self.Images[index] = image
                            DispatchQueue.main.async(execute: {let offset = self.TableView2.contentOffset
                                self.reloadTableOnMain(with: offset)})
                        }
                    }
                }
                
                DispatchQueue.main.async(execute: {let offset = self.TableView2.contentOffset
                    self.reloadTableOnMain(with: offset)})
                }
            }
            catch let err {
                print("Err", err)
            }
            }.resume()
        
        TableView2.reloadData()
        TableView2.refreshControl?.endRefreshing()
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        LoadCategories()
        let refreshControl = UIRefreshControl()
        refreshControl.addTarget(self, action:  #selector(sortArray), for: .valueChanged)
        self.TableView2.refreshControl = refreshControl
    }
    
    override func viewDidAppear(_ animated: Bool) {

        var URL1 = "https://jrnan.info/Painting/Orders.php?Username="
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
                if (gitData.count != self.ListOfQuantities.count){
                    self.LoadCategories()
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    
    func isKeyPresentInUserDefaults(key: String) -> Bool {
        return UserDefaults.standard.object(forKey: key) != nil
    }
    func LoadCategories() {
        stopTheDamnRequests()
        self.tmpListOfPrices.removeAll()
        self.tmpListOfurl.removeAll()
        self.Images.removeAll()
        self.ListOfArtists.removeAll()
        self.ListOfBuyers.removeAll()
        self.ListOfWhat.removeAll()
        self.ListOfStatus.removeAll()
        self.ListOfPurchaseID.removeAll()
        self.ListOfQuantities.removeAll()
        
        var URL1 = "https://jrnan.info/Painting/Orders.php?Username="
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
            self.tmpListOfurl.append((data.PaintingUrl as! String))
            self.tmpListOfStatus.append((data.status as! String))
            self.tmpListOfPrices.append((data.Price as! String))
            self.tmpListOfStatus.append((data.status as! String))
            self.ListOfArtists.append((data.Artist as! String))
            self.ListOfPurchaseID.append((data.purchase_id as! String))
            self.ListOfQuantities.append((data.Quantity as! String))
            self.ListOfBuyers.append((data.Buyer as! String))
                    if ((data.Buyer as! String) == (UserDefaults.standard.value(forKey: "username") as! String)){
                        self.ListOfWhat.append("Bought")
                    }
                    else{
                      self.ListOfWhat.append("Sold")
                    }
                }
                
       
                for str in self.tmpListOfurl{
                    self.Images.append(UIImage(named:"empty-image")!)
                }
                
                for (index, str) in self.tmpListOfurl.enumerated() {
                    Alamofire.request(str).responseImage { response in
                        if let image = response.result.value {
                            self.Images[index] = image
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

    func SendMail(OrderNumber : String , Artist : String ) {
        
        let composeVC = MFMailComposeViewController()
        composeVC.mailComposeDelegate = self
        // Configure the fields of the interface.
        composeVC.setToRecipients(["admin@shit.com"])
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
