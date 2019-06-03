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


class HomeViewController: UIViewController , UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate, UISearchBarDelegate, UISearchDisplayDelegate  {

  
    
    @IBOutlet weak var Items: UIBarButtonItem!
    @IBOutlet weak var Searxhtxt: UITextField!
    @IBOutlet weak var TableView2: UITableView!
    var ListofPaintings = [CartItem]()
    var ListOfPAintings = [[String]]()
    var Images = [UIImage]()

    
    override func viewWillAppear(_ animated: Bool) {
         var Count = 0
        if (isKeyPresentInUserDefaults(key: "ListofPaintings")){
            Count =  (UserDefaults.standard.array(forKey: "ListofPaintings")?.count)!
        }
        Items.addBadge(number: Count)
    }
    
    
    func UpdateCartNotify()
    {
        var Count = 0
        if (isKeyPresentInUserDefaults(key: "ListofPaintings")){
            Count =  (UserDefaults.standard.array(forKey: "ListofPaintings")?.count)!
        }
        Items.addBadge(number: Count)
        
    }
    
    func isKeyPresentInUserDefaults(key: String) -> Bool {
        return UserDefaults.standard.object(forKey: key) != nil
    }
    
    @objc func sortArray() {
        let refreshControl = UIRefreshControl.self
        var URL1 = "https://jrnan.info/Painting/ShowPaintings.php?Search="
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                if (gitData.count != self.ListOfPAintings.count ){
                    self.stopTheDamnRequests()
                    self.ListOfPAintings.removeAll()
                    self.Images.removeAll()
                for data in gitData {
                    self.ListOfPAintings.append([data.painting_name as! String,data.painting_price as! String,data.painting_artist as! String,data.painting_url as! String,data.Quantity as! String,data.painting_description as! String,data.painting_id as! String,data.Size as! String])
                }
                for cart in self.ListOfPAintings{
                    self.Images.append(UIImage(named:"empty-image")!)
                }
                for (index, str) in self.ListOfPAintings.enumerated() {
                    Alamofire.request(str[3]).responseImage { response in
                        if let image = response.result.value {
                            self.Images[index] = image
                            DispatchQueue.main.async { let offset = self.TableView2.contentOffset
                                self.reloadTableOnMain(with: offset)  }
                        }
                    }
                }
                }
                DispatchQueue.main.async { let offset = self.TableView2.contentOffset
                    self.reloadTableOnMain(with: offset)
                    self.UpdateCartNotify()
                }
            } catch let err {
                print("Err", err)
            }
            }.resume()
        TableView2.reloadData()
        TableView2.refreshControl?.endRefreshing()
    }
    
    
    override func viewDidLoad() {
        TableView2.backgroundColor = .clear
        TableView2.tableFooterView = UIView()
        super.viewDidLoad()
        let refreshControl = UIRefreshControl()
        refreshControl.addTarget(self, action:  #selector(sortArray), for: .valueChanged)
        self.TableView2.refreshControl = refreshControl
        self.Searxhtxt.delegate = self as! UITextFieldDelegate
        self.Searxhtxt.delegate = self
        var URL1 = "https://jrnan.info/Painting/ShowPaintings.php?Search="
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    self.ListOfPAintings.append([data.painting_name as! String,data.painting_price as! String,data.painting_artist as! String,data.painting_url as! String,data.Quantity as! String,data.painting_description as! String,data.painting_id as! String,data.Size as! String])
                }
                for cart in self.ListOfPAintings{
                    self.Images.append(UIImage(named:"empty-image")!)
                }
                for (index, str) in self.ListOfPAintings.enumerated() {
                    Alamofire.request(str[3]).responseImage { response in
                        if let image = response.result.value {
                            self.Images[index] = image
                            DispatchQueue.main.async { let offset = self.TableView2.contentOffset
                                self.reloadTableOnMain(with: offset)  }
                        }
                    }
                }
                DispatchQueue.main.async { let offset = self.TableView2.contentOffset
                    self.reloadTableOnMain(with: offset)  }
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }
    
    func textFieldDidEndEditing(_ textField: UITextField){
       SearchMe()
    }
    
   
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        // don't force `endEditing` if you want to be asked for resigning
        // also return real flow value, not strict, like: true / false
        return textField.endEditing(false)
    }
    
    override func touchesBegan(_ touches: Set<UITouch>,
                               with event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    @IBAction func LoadRooms(_ sender: Any) {
        self.ListOfPAintings.removeAll()
       // reload(tableView: TableView2)
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
      self.ListOfPAintings.removeAll()
        stopTheDamnRequests()
        var URL1 = "https://jrnan.info/Painting/ShowPaintings.php?Search="
        URL1.append(Searxhtxt.text!)
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    self.ListOfPAintings.append([data.painting_name as! String,data.painting_price as! String,data.painting_artist as! String,data.painting_url as! String,data.Quantity as! String,data.painting_description as! String,data.painting_id as! String,data.Size as! String])
                }
                
                for cart in self.ListOfPAintings{
                    self.Images.append(UIImage(named:"empty-image")!)
                }
                
                for (index, str) in self.ListOfPAintings.enumerated() {
                    Alamofire.request(str[3]).responseImage { response in
                        if let image = response.result.value {
                            self.Images[index] = image
                            DispatchQueue.main.async {
                                let offset = self.TableView2.contentOffset
                                self.reloadTableOnMain(with: offset) }
                        }
                    }
                }
                DispatchQueue.main.async { let offset = self.TableView2.contentOffset
                    self.reloadTableOnMain(with: offset) }
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
    
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destination.
     // Pass the selected object to the new view controller.
     }
     */
    

    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return  ListOfPAintings.count;
    }
    
    func UpdateCart() {
        var Count =  UserDefaults.standard.array(forKey: "ListofPaintings")?.count
        Items.addBadge(number: Count!)
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier:"CustomCell", for: indexPath as IndexPath) as! ArtTableViewCell
        cell.Title.text = ListOfPAintings[indexPath.row][0]
        cell.thisUrl = ListOfPAintings[indexPath.row][3]
        cell.Artist.text = ListOfPAintings[indexPath.row][2]
        if (Images.indexExists(indexPath.row)){
        cell.CImage.image = Images[indexPath.row]
        }
        else {
        cell.CImage.image = UIImage(named: "empty-image")
        }
        cell.Description.text = ListOfPAintings[indexPath.row][5]
        cell.ID = ListOfPAintings[indexPath.row][6]
        cell.Size.text = "Size: " + ListOfPAintings[indexPath.row][7]
        cell.Daddy = self as! HomeViewController
        cell.MaxQuantity = Int(ListOfPAintings[indexPath.row][4])!
        if (Int(ListOfPAintings[indexPath.row][4])! < 1) {
            cell.PriceB.setTitle("Out Of Stock", for: .normal)
            cell.PriceB.isEnabled = false
        }
        else{
           cell.PriceB.setTitle(ListOfPAintings[indexPath.row][1] + "$", for: .normal)
            cell.PriceB.isEnabled = true
        }
        return cell
    }
    
    func reload(tableView: UITableView) {
        let contentOffset = tableView.contentOffset
        tableView.reloadData()
        tableView.layoutIfNeeded()
        tableView.setContentOffset(contentOffset, animated: false)
    }
    
 
    
}


extension Array {
    func indexExists(_ index: Int) -> Bool {
        return self.indices.contains(index)
    }
}


