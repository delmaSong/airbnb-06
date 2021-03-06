package io.codesquad.group6.mockbnb.domain.listing.data;

import io.codesquad.group6.mockbnb.domain.listing.api.dto.request.ListingFilter;
import io.codesquad.group6.mockbnb.domain.listing.api.dto.response.PriceGraphData;
import io.codesquad.group6.mockbnb.domain.listing.domain.Listing;
import io.codesquad.group6.mockbnb.domain.listing.exception.InvalidBookmarkRequestException;
import io.codesquad.group6.mockbnb.domain.listing.exception.ListingNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

@Repository
@Slf4j
public class ListingDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ListingDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Listing> findListings(ListingFilter listingFilter) {
        String sql = "SELECT l.id, l.name, l.housing_type, l.capacity, l.num_bathrooms, l.num_bedrooms, l.num_beds, " +
                         "l.price, l.cleaning_fee, l.num_reviews, l.rating, l.latitude, l.longitude, " +
                         "CONCAT_WS(', ', l.neighborhood, l.city, l.state, l.country) AS l_location, " +
                         "EXISTS(SELECT bm.listing " +
                             "FROM bookmark bm " +
                             "WHERE bm.guest = :g_id " +
                                 "AND bm.listing = l.id) AS l_is_bookmarked, " +
                         "(SELECT GROUP_CONCAT(i.image_url) " +
                             "FROM image i " +
                             "WHERE i.listing = l.id) AS l_image_urls, " +
                         "h.id, h.name, h.is_superhost " +
                     "FROM listing l " +
                         "JOIN host h ON l.host = h.id " +
                     "WHERE l.capacity >= :num_guests " +
                         "AND l.price BETWEEN :min_price AND :max_price " +
                         "AND l.latitude BETWEEN :min_latitude AND :max_latitude " +
                         "AND l.longitude BETWEEN :min_longitude AND :max_longitude " +
                         "AND NOT EXISTS(SELECT b.id " +
                             "FROM booking b " +
                             "WHERE b.listing = l.id " +
                                 "AND ((:checkin <= b.checkin AND b.checkin < :checkout) " +
                                     "OR (:checkin < b.checkout AND b.checkout <= :checkout) " +
                                     "OR (b.checkin < :checkin AND :checkout < checkout))) " +
                         "AND l.name LIKE :query " +
                     "LIMIT :limit OFFSET :offset";
        SqlParameterSource sqlParameterSource = listingFilter.toSqlParameterSource();
        return namedParameterJdbcTemplate.query(sql, sqlParameterSource, ListingMapper.getInstance());
    }

    public Listing findListingById(long listingId, long guestId) {
        String sql = "SELECT l.id, l.name, l.housing_type, l.capacity, l.num_bathrooms, l.num_bedrooms, l.num_beds, " +
                         "l.price, l.cleaning_fee, l.num_reviews, l.rating, l.latitude, l.longitude, " +
                         "CONCAT_WS(', ', l.neighborhood, l.city, l.state, l.country) AS l_location, " +
                         "EXISTS(SELECT b.listing " +
                             "FROM bookmark b " +
                             "WHERE b.guest = ? " +
                                 "AND b.listing = l.id) AS l_is_bookmarked, " +
                         "(SELECT GROUP_CONCAT(i.image_url) " +
                             "FROM image i " +
                             "WHERE i.listing = l.id) AS l_image_urls, " +
                         "h.id, h.name, h.is_superhost " +
                     "FROM listing l " +
                         "JOIN host h ON l.host = h.id " +
                     "WHERE l.id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, ListingMapper.getInstance(), guestId, listingId);
        } catch (EmptyResultDataAccessException e) {
            throw new ListingNotFoundException("No listing by the provided ID exists.");
        }
    }

    public PriceGraphData findPriceGraphData(LocalDate checkin, LocalDate checkout, int numGuests) {
        String sql = "SELECT GROUP_CONCAT(l.price) AS l_prices, " +
                         "ROUND(AVG(IF(l.price > 1000, 1000, l.price)), 2) AS l_price_avg " +
                     "FROM listing l " +
                     "WHERE l.capacity >= :num_guests " +
                         "AND NOT EXISTS(SELECT b.id " +
                             "FROM booking b " +
                             "WHERE b.listing = l.id " +
                                 "AND ((:checkin <= b.checkin AND b.checkin < :checkout) " +
                                 "OR (:checkin < b.checkout AND b.checkout <= :checkout) " +
                                 "OR (b.checkin < :checkin AND :checkout < checkout)))";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("checkin", checkin)
                                                                           .addValue("checkout", checkout)
                                                                           .addValue("num_guests", numGuests);
        return namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, PriceGraphDataMapper.instance());
    }

    public void toggleBookmark(long listingId, long guestId) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("toggle_bookmark");
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("l_id", listingId)
                                                                           .addValue("g_id", guestId);
        try {
            simpleJdbcCall.execute(sqlParameterSource);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidBookmarkRequestException("You cannot (un)bookmark a listing that does not exist.");
        }
    }

    public List<Listing> findBookmarkedListings(long guestId) {
        String sql = "SELECT l.id, l.name, l.housing_type, l.capacity, l.num_bathrooms, l.num_bedrooms, l.num_beds, " +
                         "l.price, l.cleaning_fee, l.num_reviews, l.rating, l.latitude, l.longitude, " +
                         "CONCAT_WS(', ', l.neighborhood, l.city, l.state, l.country) AS l_location, " +
                         "TRUE AS l_is_bookmarked ," +
                         "(SELECT GROUP_CONCAT(i.image_url) " +
                             "FROM image i " +
                             "WHERE i.listing = l.id) AS l_image_urls, " +
                         "h.id, h.name, h.is_superhost " +
                     "FROM listing l " +
                         "JOIN host h ON l.host = h.id " +
                         "JOIN bookmark b ON l.id = b.listing " +
                     "WHERE b.guest = ?";
        return jdbcTemplate.query(sql, ListingMapper.getInstance(), guestId);
    }

}
