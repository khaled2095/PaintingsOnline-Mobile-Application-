//
//  SizesViewController.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 8/9/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit

class SizesViewController: UIViewController , UITableViewDelegate , UITableViewDataSource {
    
    @IBOutlet weak var table1: UITableView!
    var FullSizes = [SizeFull]()
    
    @IBOutlet weak var SizeTxt: UITextField!
    
    @IBOutlet weak var Height: UITextField!
    @IBOutlet weak var PriceTxt: UITextField!
    @IBOutlet weak var QuantityTxt: UITextField!
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if FullSizes.count == 0 {
            tableView.setEmptyView(title: "You have not added any sizes", message: "Fill in the three fields and click Add")
        }
        else {
            tableView.restore()
        }
        return FullSizes.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
             let cell = table1.dequeueReusableCell(withIdentifier: "CELL", for: indexPath)
        cell.textLabel!.text = "Size:" + FullSizes[indexPath.row].Size + "   Price:" + FullSizes[indexPath.row].Price + "   Quantity:" + FullSizes[indexPath.row].Quantity
        return cell
    }
    
    
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            print("Deleted")
            self.FullSizes.remove(at: indexPath.row)
            UserDefaults.standard.setStructArray(FullSizes, forKey: "fullSizes")
            table1.reloadData()
        }
    }
    @IBAction func GoBack(_ sender: Any) {
        performSegueToReturnBack()
    }
    
    override func touchesBegan(_ touches: Set<UITouch>,
                               with event: UIEvent?) {
        self.view.endEditing(true)
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        if (isKeyPresentInUserDefaults(key: "fullSizes")){
       FullSizes  = UserDefaults.standard.structArrayData(SizeFull.self, forKey: "fullSizes")
        }
        table1.reloadData()
    }
    
    
    func isKeyPresentInUserDefaults(key: String) -> Bool {
        return UserDefaults.standard.object(forKey: key) != nil
    }
    
    @IBAction func addSize(_ sender: Any) {
        if ((PriceTxt.text?.isEmpty)! || (SizeTxt.text?.isEmpty)! || (Height.text?.isEmpty)! || (QuantityTxt.text?.isEmpty)!) {
            var alert = UIAlertController(title: "Missing", message: "Missing Data , Please make sure to fill all the fields", preferredStyle: UIAlertController.Style.alert)
            alert.addAction(UIAlertAction(title: "Ok", style: UIAlertAction.Style.default, handler: nil))
            self.present(alert, animated: true, completion: nil)
        }
        else {
        var tmpCell = SizeFull()
        tmpCell.Price = PriceTxt.text!
        tmpCell.Size = SizeTxt.text! + "cm X" + Height.text! + "cm"
        tmpCell.Quantity = QuantityTxt.text!
        FullSizes.append(tmpCell)
        UserDefaults.standard.setStructArray(FullSizes, forKey: "fullSizes")
        table1.reloadData()
        }
    }
    

}
struct SizeFull : Codable {
    var Size = ""
    var Price = ""
    var Quantity = ""
}

extension UserDefaults {
    open func setStruct<T: Codable>(_ value: T?, forKey defaultName: String){
        let data = try? JSONEncoder().encode(value)
        set(data, forKey: defaultName)
    }
    
    open func structData<T>(_ type: T.Type, forKey defaultName: String) -> T? where T : Decodable {
        guard let encodedData = data(forKey: defaultName) else {
            return nil
        }
        
        return try! JSONDecoder().decode(type, from: encodedData)
    }
    
    open func setStructArray<T: Codable>(_ value: [T], forKey defaultName: String){
        let data = value.map { try? JSONEncoder().encode($0) }
        
        set(data, forKey: defaultName)
    }
    
    open func structArrayData<T>(_ type: T.Type, forKey defaultName: String) -> [T] where T : Decodable {
        guard let encodedData = array(forKey: defaultName) as? [Data] else {
            return []
        }
        return encodedData.map { try! JSONDecoder().decode(type, from: $0) }
    }
}


extension UITableView {
    func setEmptyView(title: String, message: String) {
        let emptyView = UIView(frame: CGRect(x: self.center.x, y: self.center.y, width: self.bounds.size.width, height: self.bounds.size.height))
        let titleLabel = UILabel()
        let messageLabel = UILabel()
        titleLabel.translatesAutoresizingMaskIntoConstraints = false
        messageLabel.translatesAutoresizingMaskIntoConstraints = false
        titleLabel.textColor = UIColor.black
        titleLabel.font = UIFont(name: "HelveticaNeue-Bold", size: 18)
        messageLabel.textColor = UIColor.lightGray
        messageLabel.font = UIFont(name: "HelveticaNeue-Regular", size: 17)
        emptyView.addSubview(titleLabel)
        emptyView.addSubview(messageLabel)
        titleLabel.centerYAnchor.constraint(equalTo: emptyView.centerYAnchor).isActive = true
        titleLabel.centerXAnchor.constraint(equalTo: emptyView.centerXAnchor).isActive = true
        messageLabel.topAnchor.constraint(equalTo: titleLabel.bottomAnchor, constant: 20).isActive = true
        messageLabel.leftAnchor.constraint(equalTo: emptyView.leftAnchor, constant: 20).isActive = true
        messageLabel.rightAnchor.constraint(equalTo: emptyView.rightAnchor, constant: -20).isActive = true
        titleLabel.text = title
        messageLabel.text = message
        messageLabel.numberOfLines = 0
        messageLabel.textAlignment = .center
        // The only tricky part is here:
        self.backgroundView = emptyView
        self.separatorStyle = .none
    }
    func restore() {
        self.backgroundView = nil
        self.separatorStyle = .singleLine
    }
}



extension UICollectionView {
    func setEmptyView(title: String, message: String) {
        let emptyView = UIView(frame: CGRect(x: self.center.x, y: self.center.y, width: self.bounds.size.width, height: self.bounds.size.height))
        let titleLabel = UILabel()
        let messageLabel = UILabel()
        titleLabel.translatesAutoresizingMaskIntoConstraints = false
        messageLabel.translatesAutoresizingMaskIntoConstraints = false
        titleLabel.textColor = UIColor.black
        titleLabel.font = UIFont(name: "HelveticaNeue-Bold", size: 18)
        messageLabel.textColor = UIColor.lightGray
        messageLabel.font = UIFont(name: "HelveticaNeue-Regular", size: 17)
        emptyView.addSubview(titleLabel)
        emptyView.addSubview(messageLabel)
        titleLabel.centerYAnchor.constraint(equalTo: emptyView.centerYAnchor).isActive = true
        titleLabel.centerXAnchor.constraint(equalTo: emptyView.centerXAnchor).isActive = true
        messageLabel.topAnchor.constraint(equalTo: titleLabel.bottomAnchor, constant: 20).isActive = true
        messageLabel.leftAnchor.constraint(equalTo: emptyView.leftAnchor, constant: 20).isActive = true
        messageLabel.rightAnchor.constraint(equalTo: emptyView.rightAnchor, constant: -20).isActive = true
        titleLabel.text = title
        messageLabel.text = message
        messageLabel.numberOfLines = 0
        messageLabel.textAlignment = .center
        // The only tricky part is here:
        self.backgroundView = emptyView
        self.indicatorStyle = .default
    }
    func restore() {
        self.backgroundView = nil
        self.indicatorStyle = .default
    }
}

