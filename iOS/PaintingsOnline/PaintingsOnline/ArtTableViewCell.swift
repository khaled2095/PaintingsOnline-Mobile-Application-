//
//  ArtTableViewCell.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 30/3/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

//
//  CategoryTableViewCell.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 30/3/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit
import AlamofireImage
import Alamofire


class ArtTableViewCell: UITableViewCell {
    
    
    @IBOutlet weak var CImage: UIImageView!
    @IBOutlet weak var Title: UILabel!
    @IBOutlet weak var Artist: UILabel!
    @IBOutlet weak var Price: UILabel!
    @IBOutlet weak var Description: UILabel!
    
    

    @IBOutlet weak var PriceB: UIButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
       contentView.frame = contentView.frame.inset(by: UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 10))
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
}


extension UIImageView {
    func dowloadFromServer(url: URL, contentMode mode: UIView.ContentMode = .scaleAspectFit) {
        contentMode = mode
        URLSession.shared.dataTask(with: url) { data, response, error in
            guard
                let httpURLResponse = response as? HTTPURLResponse, httpURLResponse.statusCode == 200,
                let mimeType = response?.mimeType, mimeType.hasPrefix("image"),
                let data = data, error == nil,
                let image = UIImage(data: data)
                else { return }
            DispatchQueue.main.async() {
                self.image = image
            }
            }.resume()
    }
    func dowloadFromServer(link: String, contentMode mode: UIView.ContentMode = .scaleAspectFit) {
        guard let url = URL(string: link) else { return }
        dowloadFromServer(url: url, contentMode: mode)
    }
}
