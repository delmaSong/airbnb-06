//
//  PriceRangeViewController.swift
//  Airbnb
//
//  Created by jinie on 2020/06/01.
//  Copyright © 2020 jinie. All rights reserved.
//

import UIKit

class PriceRangeViewController: UIViewController {
    
    @IBOutlet weak var contentView: UIView!
    @IBOutlet weak var headerView: PopupHeaderView!
    @IBOutlet weak var averageLabel: UILabel!
    @IBOutlet weak var footerView: PopupFooterView!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        configureView()
    }
    
    private func configureView() {
        contentView.layer.cornerRadius = 12.0
        contentView.layer.masksToBounds = true
        headerView.titleLabel.text = "가격"
    }
}
