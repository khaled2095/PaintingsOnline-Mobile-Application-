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

class ArtInCatViewController: UIViewController , UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var tableview1: UITableView!
    
    var ListOfnames = [String]()
     var ListOfPrices = [String]()
    var ListOfArtists = [String]()
   var ListOfurl = [String]()
    var ListOfdescription = [String]()
    var ListOfID = [String]()
     var Images = [UIImage]()
    //Our web service url
    
    
    @objc func sortArray() {
        var URL1 = UserDefaults.standard.string(forKey: "URL") as! String
        var Category = UserDefaults.standard.string(forKey: "Selected")
        URL1.append(Category!)
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                if (gitData.count != self.ListOfnames.count ){
                for data in gitData {
                    self.ListOfnames.append((data.painting_name as! String))
                    self.ListOfPrices.append((data.painting_price as! String))
                    self.ListOfArtists.append((data.painting_artist as! String))
                    self.ListOfurl.append((data.painting_url as! String))
                    self.ListOfdescription.append((data.painting_description as! String))
                    self.ListOfID.append((data.painting_id as! String))
                }
                
                
                for str in self.ListOfurl{
                    self.Images.append(UIImage(named:"empty-image")!)
                }
                
                for (index, str) in self.ListOfurl.enumerated() {
                    Alamofire.request(str).responseImage { response in
                        if let image = response.result.value {
                            self.Images[index] = image
                            DispatchQueue.main.async(execute: {let offset = self.tableview1.contentOffset
                                self.reloadTableOnMain(with: offset)})
                        }
                    }
                }
                }
                
                DispatchQueue.main.async(execute: {let offset = self.tableview1.contentOffset
                    self.reloadTableOnMain(with: offset)})
            } catch let err {
                print("Err", err)
            }
            }.resume()
        
        tableview1.reloadData()
        tableview1.refreshControl?.endRefreshing()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let refreshControl = UIRefreshControl()
        refreshControl.addTarget(self, action:  #selector(sortArray), for: .valueChanged)
        self.tableview1.refreshControl = refreshControl
        
        var URL1 = UserDefaults.standard.string(forKey: "URL") as! String
        var Category = UserDefaults.standard.string(forKey: "Selected")
        URL1.append(Category!)
        guard let gitUrl = URL(string: URL1 ) else { return }
        URLSession.shared.dataTask(with: gitUrl) { (data, response
            , error) in
            guard let data = data else { return }
            do {
                let decoder = JSONDecoder()
                let gitData = try decoder.decode(Array<MyGitHub>.self, from: data)
                for data in gitData {
                    self.ListOfnames.append((data.painting_name as! String))
                    self.ListOfPrices.append((data.painting_price as! String))
                     self.ListOfArtists.append((data.painting_artist as! String))
                     self.ListOfurl.append((data.painting_url as! String))
 self.ListOfdescription.append((data.painting_description as! String))
                    self.ListOfID.append((data.painting_id as! String))
                }
                
                
                for str in self.ListOfurl{
                    self.Images.append(UIImage(named:"empty-image")!)
                }
                
                for (index, str) in self.ListOfurl.enumerated() {
                    Alamofire.request(str).responseImage { response in
                        if let image = response.result.value {
                            self.Images[index] = image
                            DispatchQueue.main.async(execute: {let offset = self.tableview1.contentOffset
                                self.reloadTableOnMain(with: offset)})
                        }
                    }
                }
                
                
                DispatchQueue.main.async(execute: {let offset = self.tableview1.contentOffset
                    self.reloadTableOnMain(with: offset)})
            } catch let err {
                print("Err", err)
            }
            }.resume()
    }

    
    func reloadTableOnMain(with offset: CGPoint = CGPoint.zero){
        
        DispatchQueue.main.async { [weak self] () in
            
            self?.tableview1.reloadData()
            self?.tableview1.layoutIfNeeded()
            self?.tableview1.contentOffset = offset
        }
    }
    
    
    func reload(tableView: UITableView) {
        
        let contentOffset = tableView.contentOffset
        tableView.reloadData()
        tableView.layoutIfNeeded()
        tableView.setContentOffset(contentOffset, animated: false)
        
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
        return  ListOfnames.count;
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier:"CustomCell", for: indexPath as IndexPath) as! Category1TableViewCell
        
        cell.Title.text = ListOfnames[indexPath.row]
        cell.PriceB.setTitle(ListOfPrices[indexPath.row] + "$", for: .normal)
        cell.Artist.text = ListOfArtists[indexPath.row]
        cell.thisUrl = ListOfurl[indexPath.row]
        if (Images.indexExists(indexPath.row)){
            cell.CImage.image = Images[indexPath.row]
        }
        else {
            cell.CImage.image = UIImage(named: "empty-image")
        }
        cell.Description.text = ListOfdescription[indexPath.row]
        cell.ID = ListOfID[indexPath.row]
        return cell
    }

    
}



struct MyGitHub: Codable {
    let painting_name: String?
    let painting_price: String?
    let painting_url: String?
    let painting_description: String?
    let painting_artist: String?
    let painting_id : String?
    let painting_Data : String?
    let username : String?
    let name : String?
    let Email : String?
    let Address : String?
    let usertype : String?
    let Response : String?
    let Name : String?
    let URL : String?
    let purchase_id : String?
    let paintings_id : String?
    let status : String?
    let Artist: String?
    let PaintingUrl : String?
    let Price : String?
    let verified : String?
    let Quantity : String?
    let Buyer : String?
    let Size : String?

    
    private enum CodingKeys: String, CodingKey {
        case painting_name
        case painting_price
        case painting_url
        case painting_description
        case painting_artist
        case painting_id
        case painting_Data
        case username
        case name
        case Email
        case Address
        case usertype
        case Response
        case Name
        case URL
        case purchase_id
        case paintings_id
        case status
        case Artist
        case PaintingUrl
        case Price
        case verified
        case Quantity
        case Buyer
        case Size
    }
}
