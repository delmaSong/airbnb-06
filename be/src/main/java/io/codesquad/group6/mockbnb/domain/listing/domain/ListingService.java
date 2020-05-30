package io.codesquad.group6.mockbnb.domain.listing.domain;

import io.codesquad.group6.mockbnb.domain.listing.api.dto.request.ListingFilter;
import io.codesquad.group6.mockbnb.domain.listing.api.dto.response.ListingDetail;
import io.codesquad.group6.mockbnb.domain.listing.api.dto.response.ListingSummary;
import io.codesquad.group6.mockbnb.domain.listing.data.ListingDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListingService {

    private final ListingDao listingDao;

    public ListingService(ListingDao listingDao) {
        this.listingDao = listingDao;
    }

    public List<ListingSummary> getListings(ListingFilter listingFilter) {
        List<Listing> listings = listingDao.findListings(listingFilter);
        return listings.stream()
                       .map(this::mapToListingSummary)
                       .collect(Collectors.toList());
    }

    public ListingDetail getListing(long listingId, long guestId) {
        Listing listing = listingDao.findListingById(listingId, guestId);
        return mapToListingDetail(listing);
    }

    private ListingSummary mapToListingSummary(Listing listing) {
        return ListingSummary.builder()
                             .id(listing.getId())
                             .name(listing.getName())
                             .imageUrls(listing.getImageUrls())
                             .housingType(listing.getHousingType())
                             .numBedrooms(listing.getNumBedrooms())
                             .numBeds(listing.getNumBeds())
                             .rating(listing.getRating())
                             .numReviews(listing.getNumReviews())
                             .isSuperHost(listing.isSuperHost())
                             .isBookmarked(listing.isBookmarked())
                             .latitude(listing.getLatitude())
                             .longitude(listing.getLongitude())
                             .build();
    }

    private ListingDetail mapToListingDetail(Listing listing) {
        return ListingDetail.builder()
                            .imageUrls(listing.getImageUrls())
                            .isBookmarked(listing.isBookmarked())
                            .name(listing.getName())
                            .location(listing.getLocation())
                            .hostName(listing.getHostName())
                            .roomType(listing.getRoomType())
                            .capacity(listing.getCapacity())
                            .numBedrooms(listing.getNumBedrooms())
                            .numBeds(listing.getNumBeds())
                            .numBathrooms(listing.getNumBathrooms())
                            .price(listing.getPrice())
                            .rating(listing.getRating())
                            .numReviews(listing.getNumReviews())
                            .build();
    }

}