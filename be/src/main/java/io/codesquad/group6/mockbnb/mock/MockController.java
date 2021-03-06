package io.codesquad.group6.mockbnb.mock;

import io.codesquad.group6.mockbnb.domain.booking.api.dto.request.BookingRequest;
import io.codesquad.group6.mockbnb.domain.listing.api.dto.request.BookmarkRequest;
import io.codesquad.group6.mockbnb.domain.booking.api.dto.response.BookingDetail;
import io.codesquad.group6.mockbnb.domain.booking.api.dto.response.BookingResponse;
import io.codesquad.group6.mockbnb.domain.booking.api.dto.response.BookingSummary;
import io.codesquad.group6.mockbnb.domain.listing.api.dto.response.ListingDetail;
import io.codesquad.group6.mockbnb.domain.listing.api.dto.response.ListingSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class MockController {

    @GetMapping("/mock/listings")
    public ResponseEntity<List<ListingSummary>> getListings(
            @RequestParam(required = false, defaultValue = "2020-05-22") LocalDate checkin,
            @RequestParam(required = false, defaultValue = "2020-05-23") LocalDate checkout,
            @RequestParam(name = "num-guests", required = false, defaultValue = "1") int numGuests,
            @RequestParam(name = "min-price", required = false, defaultValue = "0") int minPrice,
            @RequestParam(name = "max-price", required = false, defaultValue = "10000") int maxPrice,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(name = "min-latitude", required = false, defaultValue = "37.7") double minLatitude,
            @RequestParam(name = "max-latitude", required = false, defaultValue = "37.85") double maxLatitude,
            @RequestParam(name = "min-longitude", required = false, defaultValue = "-122.55") double minLongitude,
            @RequestParam(name = "max-longitude", required = false, defaultValue = "-122.35") double maxLongitude) {
        List<ListingSummary> listings = new ArrayList<>();
        listings.add(ListingSummary.builder()
                                   .id(1)
                                   .name("listing1")
                                   .imageUrl("https://a0.muscache.com/im/pictures/dd850460-46df-4422-a98b-86991f8de674.jpg?aki_policy=large")
                                   .imageUrl("https://a0.muscache.com/im/pictures/38165109-1f28-429f-b9fd-99d0932c154e.jpg?aki_policy=large")
                                   .imageUrl("https://a0.muscache.com/im/pictures/df7f56e6-71c0-4402-96de-27604ee4d460.jpg?aki_policy=large")
                                   .housingType("Entire home/apt")
                                   .numBedrooms(2)
                                   .numBeds(3)
                                   .rating(4.85)
                                   .numReviews(210)
                                   .isSuperHost(true)
                                   .isBookmarked(true)
                                   .latitude(37.76931)
                                   .longitude(-122.43386)
                                   .build());
        listings.add(ListingSummary.builder()
                                   .id(2)
                                   .name("listing2")
                                   .imageUrl("https://a0.muscache.com/im/pictures/2b5214b8-2e0a-4fcb-8004-0f4b95bdcaa4.jpg?aki_policy=large")
                                   .imageUrl("https://a0.muscache.com/im/pictures/100627966/2ccec29c_original.jpg?aki_policy=large")
                                   .imageUrl("https://a0.muscache.com/im/pictures/92182afd-7553-42a5-8b41-98d60a600062.jpg?aki_policy=large")
                                   .housingType("Private room")
                                   .numBedrooms(1)
                                   .numBeds(2)
                                   .rating(4.93)
                                   .numReviews(52)
                                   .isSuperHost(false)
                                   .isBookmarked(false)
                                   .latitude(37.75402)
                                   .longitude(-122.45805)
                                   .build());
        listings.add(ListingSummary.builder()
                                   .id(3)
                                   .name("listing3")
                                   .imageUrl("https://a0.muscache.com/im/pictures/1fa21b5c-b533-49f9-9569-a6808af8bc29.jpg?aki_policy=large")
                                   .imageUrl("https://a0.muscache.com/im/pictures/108111917/046dd104_original.jpg?aki_policy=large")
                                   .imageUrl("https://a0.muscache.com/im/pictures/b2142aa3-f9db-4c32-8eaa-3bf0c6b548c1.jpg?aki_policy=large")
                                   .housingType("House")
                                   .numBedrooms(3)
                                   .numBeds(6)
                                   .rating(4.77)
                                   .numReviews(33)
                                   .isSuperHost(false)
                                   .isBookmarked(true)
                                   .latitude(37.74511)
                                   .longitude(-122.42102)
                                   .build());
        return ResponseEntity.ok(listings);
    }

    @PatchMapping("/mock/listings/{listing-id}")
    public void bookmarkListing(@PathVariable(name = "listing-id") long listingId,
                                @RequestBody BookmarkRequest bookmarkRequest,
                                HttpServletResponse response) {
        response.setStatus(202);
    }

    @GetMapping("/mock/listings/{listing-id}")
    public ResponseEntity<ListingDetail> getListingDetail(@PathVariable(name = "listing-id") long listingId) {
        log.debug("listingId: {}", listingId);
        return ResponseEntity.ok(ListingDetail.builder()
                                              .imageUrl("https://a0.muscache.com/im/pictures/dd850460-46df-4422-a98b-86991f8de674.jpg?aki_policy=large")
                                              .imageUrl("https://a0.muscache.com/im/pictures/38165109-1f28-429f-b9fd-99d0932c154e.jpg?aki_policy=large")
                                              .imageUrl("https://a0.muscache.com/im/pictures/df7f56e6-71c0-4402-96de-27604ee4d460.jpg?aki_policy=large")
                                              .isBookmarked(true)
                                              .name("listing1")
                                              .location("Korea Town, Los Angeles, California, United States")
                                              .hostName("David")
                                              .housingType("Entire home/apt")
                                              .capacity(4)
                                              .numBedrooms(2)
                                              .numBeds(4)
                                              .numBathrooms(2)
                                              .price(123.45)
                                              .rating(4.95)
                                              .numReviews(123)
                                              .build());
    }

    @PostMapping("/mock/bookings")
    public ResponseEntity<BookingResponse> book(@RequestBody BookingRequest bookingRequest) {
        log.debug("bookingRequest: {}",  bookingRequest);
        return ResponseEntity.ok(BookingResponse.builder()
                                                .bookingId(1)
                                                .build());
    }

    @GetMapping("/mock/bookings")
    public ResponseEntity<List<BookingSummary>> getBookings() {
        List<BookingSummary> bookings = new ArrayList<>();
        bookings.add(BookingSummary.builder()
                                   .id(1)
                                   .imageUrl("https://a0.muscache.com/im/pictures/dd850460-46df-4422-a98b-86991f8de674.jpg?aki_policy=large")
                                   .listingName("listing1")
                                   .checkin(LocalDate.parse("2020-05-22"))
                                   .checkout(LocalDate.parse("2020-05-23"))
                                   .numNights(1)
                                   .numGuests(1)
                                   .totalPrice(123.45)
                                   .build());
        bookings.add(BookingSummary.builder()
                                   .id(2)
                                   .imageUrl("https://a0.muscache.com/im/pictures/38165109-1f28-429f-b9fd-99d0932c154e.jpg?aki_policy=large")
                                   .listingName("listing2")
                                   .checkin(LocalDate.parse("2020-06-01"))
                                   .checkout(LocalDate.parse("2020-06-15"))
                                   .numNights(14)
                                   .numGuests(2)
                                   .totalPrice(1234.56)
                                   .build());
        bookings.add(BookingSummary.builder()
                                   .id(3)
                                   .imageUrl("https://a0.muscache.com/im/pictures/df7f56e6-71c0-4402-96de-27604ee4d460.jpg?aki_policy=large")
                                   .listingName("listing3")
                                   .checkin(LocalDate.parse("2020-12-24"))
                                   .checkout(LocalDate.parse("2020-12-31"))
                                   .numNights(7)
                                   .numGuests(3)
                                   .totalPrice(567.89)
                                   .build());
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/mock/bookings/{booking-id}")
    public ResponseEntity<BookingDetail> getBookingDetail(@PathVariable(name = "booking-id") long bookingId) {
        log.debug("bookingId: {}", bookingId);
        return ResponseEntity.ok(BookingDetail.builder()
                                              .imageUrl("https://a0.muscache.com/im/pictures/dd850460-46df-4422-a98b-86991f8de674.jpg?aki_policy=large")
                                              .housingType("Entire home/apt")
                                              .housingPrice(123.45)
                                              .rating(4.56)
                                              .numReviews(123)
                                              .checkin(LocalDate.parse("2020-05-22"))
                                              .checkout(LocalDate.parse("2020-05-25"))
                                              .numGuests(2)
                                              .numNights(3)
                                              .totalHousingPrice(123.45 * 3)
                                              .cleaningFee(25.00)
                                              .totalPrice(123.45 * 3 + 25.00)
                                              .build());
    }

    @DeleteMapping("/mock/bookings/{booking-id}")
    public void cancelBooking(@PathVariable(name = "booking-id") long bookingId, HttpServletResponse response) {
        log.debug("bookingId: {}", bookingId);
        response.setStatus(202);
    }

}
