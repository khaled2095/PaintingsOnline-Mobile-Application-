//
//  CartViewController.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 17/4/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage

class CartViewController: UIViewController , UITableViewDataSource, UITableViewDelegate{
    
    @IBOutlet weak var TableView2: UITableView!
    var ListOfPAintings = [[String]]()
    var CartImages = [UIImage]()
    var Quantities : [Int] = []
    var MaxQuantity = 0
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return ListOfPAintings.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier:"Cell", for: indexPath as IndexPath) as! CartTableViewCell
        cell.Name.text = ListOfPAintings[indexPath.row][0]
        cell.Price.text = ListOfPAintings[indexPath.row][1]
        cell.Artist.text = ListOfPAintings[indexPath.row][2]
        cell.Url.text = ListOfPAintings[indexPath.row][3]
        cell.MaxQuantity = Int(ListOfPAintings[indexPath.row][4])!
        cell.CurrentID = ListOfPAintings[indexPath.row][6]
        cell.Quantity.text = ListOfPAintings[indexPath.row][7]
        cell.QuantityInt = Int(ListOfPAintings[indexPath.row][7])!
        cell.Daddy = self
        if (CartImages.indexExists(indexPath.row)){
            cell.CImage.image = CartImages[indexPath.row]
        }
        else {
            cell.CImage.image = UIImage(named: "empty-image")
        }
        
        return cell
    }
    
    func Increment(ID : String , Count : Int) {
        for (index,s) in ListOfPAintings.enumerated() {
            if (s[6] == ID) {
            ListOfPAintings[index][7] = String(Count)
            }
        }
    UserDefaults.standard.set(ListOfPAintings, forKey: "ListofPaintings")
    }
    func Decrement(ID : String, Count : Int) {
        for (index,s) in ListOfPAintings.enumerated() {
            if (s[6] == ID) {
                ListOfPAintings[index][7] = String(Count)
            }
        }
    UserDefaults.standard.set(ListOfPAintings, forKey: "ListofPaintings")
    }
    
    func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true
    }
    
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if (editingStyle == .delete) {
            let alert = UIAlertController(title: "Are you sure?", message: "Are you sure you want to remove product from cart?", preferredStyle: .alert)
            let clearAction = UIAlertAction(title: "Remove", style: .destructive) { (alert: UIAlertAction!) -> Void in
                
                self.ListOfPAintings.remove(at: indexPath.row)
                UserDefaults.standard.set(self.ListOfPAintings, forKey: "ListofPaintings")
                self.reload(tableView: self.TableView2)
            }
            let cancelAction = UIAlertAction(title: "Cancel", style: .default) { (alert: UIAlertAction!) -> Void in
                //print("You pressed Cancel")
            }
            
            alert.addAction(clearAction)
            alert.addAction(cancelAction)
            
            present(alert, animated: true, completion:nil)
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
     
        }
    
    override func viewWillAppear(_ animated: Bool) {
        if(isKeyPresentInUserDefaults(key: "ListofPaintings")){
            ListOfPAintings = UserDefaults.standard.array(forKey: "ListofPaintings") as! [[String]]
        }
        for str in self.ListOfPAintings{
            self.CartImages.append(UIImage(named:"empty-image")!)
        }
        
        for (index, str) in self.ListOfPAintings.enumerated() {
            Alamofire.request(str[3]).responseImage { response in
                if let image = response.result.value {
                    self.CartImages[index] = image
                    DispatchQueue.main.async(execute: {self.reload(tableView: self.TableView2)})
                }
            }
        }
    }
    

    @IBAction func CheckOut(_ sender: Any) {
        var IDs : String = "?Paintings="
        for (index,s) in ListOfPAintings.enumerated() {
        if (index == 0){
        IDs += s[6]
        }
            else {
                IDs += "," + s[6]
            }
        }
        var strQuantity = ""
        for (index,s) in ListOfPAintings.enumerated() {
            if (index == 0){
                strQuantity += s[7]
            }
            else {
                strQuantity += "," + s[7]
            }
        }
        if (ListOfPAintings.count == 0 ) {
            let alert = UIAlertView()
            alert.title = "Error"
            alert.message = "You do not have any products in cart"
            alert.addButton(withTitle: "Understood")
            alert.show()
        }
        else {
        if(isKeyPresentInUserDefaults(key: "username")){
            UserDefaults.standard.removeObject(forKey: "ListofPaintings")
        var Username : String = UserDefaults.standard.value(forKey: "username") as! String
        var tempurl : String = "https://jrnan.info/Painting/Payment/index.php"
        tempurl.append(IDs + "&Quantity=" + strQuantity + "&Username=" + Username)
        guard let gitUrl = URL(string: tempurl.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!) else { return }
        UIApplication.shared.open(gitUrl)
    }
    else {
            let alert = UIAlertView()
            alert.title = "Please note that you need to be signed in to checkout."
            alert.addButton(withTitle: "Understood")
            alert.show()
    }
        }
    }
    
    func reload(tableView: UITableView) {
        let contentOffset = tableView.contentOffset
        tableView.reloadData()
        tableView.layoutIfNeeded()
        tableView.setContentOffset(contentOffset, animated: false)
        
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
