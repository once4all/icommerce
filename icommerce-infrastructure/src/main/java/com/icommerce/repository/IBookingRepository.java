package com.icommerce.repository;

import com.icommerce.dom.Booking;
import com.icommerce.dom.Brand;
import com.icommerce.dom.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByCustomer(UserInformation customer);
}
