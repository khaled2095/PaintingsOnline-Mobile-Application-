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
import Stripe
import BraintreeDropIn
import Braintree


class CartViewController: UIViewController , UITableViewDataSource, UITableViewDelegate{
    
    @IBOutlet weak var TableView2: UITableView!
    var ListOfPAintings = [[String]]()
    var CartImages = [UIImage]()
    var Quantities : [Int] = []
    var MaxQuantity = 0
    var CartItems : [SizeToSave] = [SizeToSave]()
    var totalAmountInt : Int = 0
    @IBOutlet weak var TotalAmount: UITextField!
    @IBOutlet weak var ChkoutButton: UIButton!
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if CartItems.count == 0 {
            tableView.setEmptyView(title: "Your Cart is empty", message: "You will see all the items added to cart in this page.")
        }
        else {
            tableView.restore()
        }
        return CartItems.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier:"Cell", for: indexPath as IndexPath) as! CartTableViewCell
        cell.Name.text = "Artist: " + CartItems[indexPath.row].painting_name
        cell.Price.text = CartItems[indexPath.row].painting_price + "$"
        cell.Artist.text = "By:" + CartItems[indexPath.row].painting_artist
        cell.Url.text = CartItems[indexPath.row].painting_Image
        cell.MaxQuantity = Int(CartItems[indexPath.row].painting_quantity)!
        cell.CurrentID = CartItems[indexPath.row].WhatID
        cell.Quantity.text = "1"
        cell.QuantityInt = 1
        cell.Daddy = self
        cell.Sizelbl.text = CartItems[indexPath.row].painting_size
        if (CartImages.indexExists(indexPath.row)){
            cell.CImage.image = CartImages[indexPath.row]
        }
        else {
            cell.CImage.image = UIImage(named: "empty-image")
        }
        
        return cell
    }
    

    
    func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true
    }
    
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if (editingStyle == .delete) {
            let alert = UIAlertController(title: "Are you sure?", message: "Are you sure you want to remove product from cart?", preferredStyle: .alert)
            let clearAction = UIAlertAction(title: "Remove", style: .destructive) { (alert: UIAlertAction!) -> Void in
                self.CartItems.remove(at: indexPath.row)
                UserDefaults.standard.setStructArray(self.CartItems,forKey:"CartItems")
                self.CalculateTotal()
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
    
    
    public func Increment(ID: Int){
        Quantities[ID] += 1
        CalculateTotal()
    }
    
    public func Decrement(ID: Int){
       Quantities[ID] -= 1
        CalculateTotal()
    }
    
    func CalculateTotal() {
        var TotalCount = 0
        totalAmountInt = 0
        for (index, element) in CartItems.enumerated(){
            totalAmountInt += (Int(element.painting_price)! * Quantities[index])
            ChkoutButton.setTitle("Check Out   " + String(totalAmountInt) + "$" , for: .normal)
            TotalAmount.text = String(totalAmountInt)
        }
        if (totalAmountInt == 0) {
            TotalAmount.isEnabled = false
        }
        else {
            TotalAmount.isEnabled = true
        }
    }
    var MainURL = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        MainURL = UserDefaults.standard.string(forKey: "MainURL")!
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        if (isKeyPresentInUserDefaults(key: "CartItems")){
          var  TmpCartItems  = UserDefaults.standard.structArrayData(SizeToSave.self, forKey: "CartItems");
            var IndexCnt = 0
            for CartIt in TmpCartItems {
                var CurentIT = CartIt
                CurentIT.WhatID = String(IndexCnt)
                CartItems.append(CurentIT)
                CartImages.append(UIImage(named: "empty-image")!)
                Quantities.append(1)
                IndexCnt += 1
            }
            CalculateTotal()
            
            for (index, str) in self.CartItems.enumerated() {
                Alamofire.request(str.painting_Image).responseImage { response in
                    if let image = response.result.value {
                        self.CartImages[index] = image
                        DispatchQueue.main.async{
                            self.TableView2.reloadData()
                    }
                }
            }
            
        }
        }
        else {
            CalculateTotal()
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

    let toKinizationKey = "sandbox_gpwbc8yj_m5jfgdsggqkp5yyw"
    
    @IBAction func pay(_ sender: Any) {
        if (CartItems.count == 0 ) {
              self.show(message: "You dont have any items in cart")
        }
        else {
        let request =  BTDropInRequest()
        let dropIn = BTDropInController(authorization: toKinizationKey, request: request)
        
        { [unowned self] (controller, result, error) in
            
            if let error = error {
                self.show(message: error.localizedDescription)
                
            } else if (result?.isCancelled == true) {
                self.show(message: "Transaction Cancelled")
                
            } else if let nonce = result?.paymentMethod?.nonce, let amount = self.TotalAmount.text {
                self.sendRequestPaymentToServer(nonce: nonce, amount: amount)
            }
            controller.dismiss(animated: true, completion: nil)
        }
        
        self.present(dropIn!, animated: true, completion: nil)
        }
    }
    
    func sendRequestPaymentToServer(nonce: String, amount: String) {
        let paymentURL = URL(string: MainURL + "/Pay2/pay.php")!
        var request = URLRequest(url: paymentURL)
        request.httpBody = "payment_method_nonce=\(nonce)&amount=\(amount)".data(using: String.Encoding.utf8)
        request.httpMethod = "POST"
        
        URLSession.shared.dataTask(with: request) { [weak self] (data, response, error) -> Void in
            guard let data = data else {
                self?.show(message: error!.localizedDescription)
                return
            }
            
            guard let result = try? JSONSerialization.jsonObject(with: data, options: []) as? [String: Any], let success = result?["success"] as? Bool, success == true else {
                self!.SucceePayment()
                self?.show(message: "Successfully charged. Thanks So Much :)")
                
                return
            }
            self?.show(message: "Transaction failed. Please try again.")
            }.resume()
    }
    
    
    func SucceePayment() {
        var CartID : String = UUID().uuidString
        for (index,CartItem) in CartItems.enumerated() {
            let request = NSMutableURLRequest(url: NSURL(string: MainURL + "/Payment/index.php")! as URL)
            request.httpMethod = "POST"
            let postString = "painting_id=\(CartItem.painting_id)&artist=\(CartItem.painting_artist)&Price=\(CartItem.painting_price)&painting_name=\(CartItem.painting_name)&Size=\(CartItem.painting_size)&CartID=\(CartID)&Quantity=\(Quantities[index])&TXN_ID=\(CartID)&PaintingUrl=\(CartItem.painting_Image)&Email=\(UserDefaults.standard.value(forKey: "Email") as! String)&buyer=\(UserDefaults.standard.value(forKey: "username") as! String)&Address=\(UserDefaults.standard.value(forKey: "Address") as! String)"
            request.setValue("application/x-www-form-urlencoded; charset=utf-8", forHTTPHeaderField: "Content-Type")
            request.httpBody = postString.data(using: String.Encoding.utf8)
            
            let task = URLSession.shared.dataTask(with: request as URLRequest) { data, response, error in
                guard let data1 = data else { return }
                do {
                    print("OutPut From Server: " + String(decoding: data1, as: UTF8.self))
                    self.CartItems.removeAll()
                    self.Quantities.removeAll()
                UserDefaults.standard.removeObject(forKey: "CartItems")
                    DispatchQueue.main.async {
                    self.TableView2.reloadData()
                    }
                    self.CalculateTotal()
                } catch let err {
                    print("Err", err)
                }
            }
            task.resume()
            
        }
    }


    func show(message: String) {
        DispatchQueue.main.async {
            
            let alertController = UIAlertController(title: message, message: "", preferredStyle: .alert)
            alertController.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
            self.present(alertController, animated: true, completion: nil)
        }
    }
    
}

extension CartViewController: STPAddCardViewControllerDelegate {
    
    func addCardViewControllerDidCancel(_ addCardViewController: STPAddCardViewController) {
        navigationController?.popViewController(animated: true)
    }
    
    func addCardViewController(_ addCardViewController: STPAddCardViewController,
                               didCreateToken token: STPToken,
                               completion: @escaping STPErrorBlock) {
        
        StripeClient.shared.completeCharge(with: token, amount: totalAmountInt) { result in
            switch result {
            case .success:
                completion(nil)
                
                let alertController = UIAlertController(title: "Congrats",
                                                        message: "Your payment was successful!",
                                                        preferredStyle: .alert)
                let alertAction = UIAlertAction(title: "OK", style: .default, handler: { _ in
                    self.navigationController?.popViewController(animated: true)
                })
                alertController.addAction(alertAction)
                self.present(alertController, animated: true)
            case .failure(let error):
                completion(error)
            }
        }
    }
}



struct PaintingCartItem {
    var Image : String = ""
    var UIImage1 = UIImage(named:"empty-image")
    var Name : String = ""
    var Size : String = ""
    var Price : String = ""
}
