package io.codesquad.group6.mockbnb.data.listing;

import io.codesquad.group6.mockbnb.domain.listing.Listing;
import io.codesquad.group6.mockbnb.domain.listing.ListingFilter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ListingDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ListingDao(DataSource dataSource) {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Listing> findListings(ListingFilter listingFilter) {
        String sql = "SELECT l.id, l.name, l.housing_type, l.capacity, l.num_bathrooms, l.num_bedrooms, l.num_beds, " +
                         "l.price, l.cleaning_fee, l.num_reviews, l.rating, l.latitude, l.longitude, " +
                         "CONCAT_WS(', ', l.neighborhood, l.city, l.state, l.country) AS l_location, " +
                         "EXISTS(SELECT bm.id " +
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
                                 "AND ((b.checkin BETWEEN :checkin AND :checkout) " +
                                 "OR (b.checkout BETWEEN :checkin AND :checkout) " +
                                 "OR (b.checkin < :checkin AND :checkout < checkout))) " +
                     "LIMIT :limit OFFSET :offset";
        SqlParameterSource sqlParameterSource = listingFilter.toSqlParameterSource();
        return namedParameterJdbcTemplate.query(sql, sqlParameterSource, ListingMapper.instance());
    }

    public Listing findListingById(long listingId, long guestId) {
        String sql = "SELECT l.id, l.name, l.housing_type, l.capacity, l.num_bathrooms, l.num_bedrooms, l.num_beds, " +
                         "l.price, l.cleaning_fee, l.num_reviews, l.rating, l.latitude, l.longitude, " +
                         "CONCAT_WS(', ', l.neighborhood, l.city, l.state, l.country) AS l_location, " +
                         "(SELECT GROUP_CONCAT(i.image_url) " +
                         "FROM image i " +
                         "WHERE i.listing = l.id) AS l_image_urls, " +
                         "EXISTS(SELECT b.id " +
                             "FROM bookmark b " +
                             "WHERE b.guest = :g_id " +
                                 "AND b.listing = l.id) AS l_is_bookmarked, " +
                         "h.id, h.name, h.is_superhost " +
                     "FROM listing l " +
                         "JOIN host h ON l.host = h.id " +
                     "WHERE l.id = :l_id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("l_id", listingId)
                                                                           .addValue("g_id", guestId);
        return namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, ListingMapper.instance());
    }

}
