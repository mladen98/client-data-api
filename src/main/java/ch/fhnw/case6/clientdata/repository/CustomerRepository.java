package ch.fhnw.case6.clientdata.repository;

import ch.fhnw.case6.clientdata.dto.CustomerData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CustomerRepository {

    private final JdbcTemplate jdbcTemplate;
    private final String findByCustomerReferenceSql;

    public CustomerRepository(
            JdbcTemplate jdbcTemplate,
            @Value("${clientdata.sql.findByCustomerReference}") String findByCustomerReferenceSql) {
        this.jdbcTemplate = jdbcTemplate;
        this.findByCustomerReferenceSql = findByCustomerReferenceSql;
    }

    public Optional<CustomerData> findByCustomerReference(String customerReference) {
        try {
            CustomerData customerData = jdbcTemplate.queryForObject(
                    findByCustomerReferenceSql,
                    new Object[]{customerReference},
                    (rs, rowNum) -> {
                        CustomerData data = new CustomerData();
                        data.setCustomerReference(rs.getString("customerReference"));
                        data.setDestination(rs.getString("destination"));
                        data.setRecepientPhone(rs.getString("recepientPhone"));
                        data.setEmail(rs.getString("email"));
                        data.setCountry(null);
                        data.setCustomerLookupSuccess(false);
                        return data;
                    }
            );

            return Optional.ofNullable(customerData);

        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }
}