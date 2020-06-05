//
//  SearchViewController.swift
//  Airbnb
//
//  Created by delma on 2020/05/19.
//  Copyright © 2020 jinie. All rights reserved.
//

import UIKit
import Floaty
import Alamofire

class SearchViewController: UIViewController {
    
    @IBOutlet weak var filteringDescriptionLabel: SearchTextField!
    @IBOutlet weak var accommodationSearchCollectionView: AccommodationSearchCollectionView!
    @IBOutlet weak var floatingButton: Floaty!
    
    private var accommodationListViewModel: AccommodationListViewModel!
    private var accommodationSearchDataSource: AccommodationSearchCollectionViewDataSource!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        collectionViewConfigure()
        requestAccommodationList()
    }
    
    @IBAction func showCalendarViewController(_ sender: Any) {
        guard let calendarViewController = storyboard?.instantiateViewController(withIdentifier: "CalendarViewController") as? CalendarViewController else { return }
        calendarViewController.delegate = self
        show(calendarViewController)
    }
    
    @IBAction func showGuestViewController(_ sender: FilterButton) {
        guard let guestViewController = storyboard?.instantiateViewController(withIdentifier: "GuestViewController") as? GuestViewController else { return }
        guestViewController.delegate = self
        show(guestViewController)
    }
    
    @IBAction func showPriceRangeViewController(_ sender: FilterButton) {
        guard let priceRangeViewController = storyboard?.instantiateViewController(withIdentifier: "PriceRangeViewController") as? PriceRangeViewController else { return }
        show(priceRangeViewController)
    }
    
    @IBAction func showMapViewController(_ sender: Floaty) {
        guard let mapViewController = storyboard?.instantiateViewController(withIdentifier: "MapViewController") as? MapViewController else { return }
        mapViewController.modalPresentationStyle = .fullScreen
        present(mapViewController, animated: true, completion: nil)
    }
    
    private func show(_ viewController: UIViewController) {
        viewController.modalPresentationStyle = .overCurrentContext
        viewController.modalTransitionStyle = .crossDissolve
        present(viewController, animated: true, completion: nil)
    }
    
    private func collectionViewConfigure() {
        accommodationSearchCollectionView.register(UINib(nibName: "AccommodationSearchCollectionViewCell", bundle: nil), forCellWithReuseIdentifier: "AccommodationSearchCollectionViewCell")
        let layout = UICollectionViewFlowLayout()
        layout.itemSize = CGSize(width: self.view.frame.width - 40, height: self.view.frame.height / 3)
        layout.scrollDirection = .vertical
        accommodationSearchCollectionView.collectionViewLayout = layout
        accommodationSearchCollectionView.showsVerticalScrollIndicator = false
    }
    
    private func requestAccommodationList() {
        let request = AccommodationRequests.list.request
        AccommodationUseCase(request: request, networkDispatcher: AF).perform(dataType: [Accommodation].self) { accommodationList in
            self.accommodationListViewModel = AccommodationListViewModel(accommodation: accommodationList as! [Accommodation], handler: { accommodations in
                DispatchQueue.main.async {
                    self.accommodationSearchDataSource = AccommodationSearchCollectionViewDataSource(accommodations: accommodations)
                    self.accommodationSearchCollectionView.dataSource = self.accommodationSearchDataSource
                    self.accommodationSearchCollectionView.reloadData()
                }
            })
        }
    }
    
    func changeFilteringDescription(_ text: String) {
        filteringDescriptionLabel.text = text
    }
}

extension SearchViewController: SendDataDelegate {
    func send(text: String) {
        changeFilteringDescription(text)
    }
}
