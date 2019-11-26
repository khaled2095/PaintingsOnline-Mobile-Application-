//
//  CollectionOrderViewController.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 5/9/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit
import MaterialComponents.MaterialCards

class CollectionOrderViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource{
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 5
    }
    

    @IBOutlet weak var CollectionView: UICollectionView!
    let card = MDCCard()
    override func viewDidLoad() {
        super.viewDidLoad()
        let imageView = UIImageView()
        card.addSubview(imageView)
        // Do any additional setup after loading the view.
        CollectionView.register(MDCCardCollectionCell.self, forCellWithReuseIdentifier: "Cell")
        
    }
    

    
 
    
    
    func collectionView(_ collectionView: UICollectionView,
                        cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "Cell",
                                                      for: indexPath) as! MDCCardCollectionCell
        // If you wanted to have the card show the selected state when tapped
        // then you need to turn isSelectable to true, otherwise the default is false.
        cell.isSelectable = true
        cell.cornerRadius = 8
        cell.setShadowElevation(ShadowElevation(rawValue: 6), for: .selected)
        cell.setShadowColor(UIColor.black, for: .highlighted)
        cell.setImage(UIImage(named: "empty-image"), for: .normal)
        return cell
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
