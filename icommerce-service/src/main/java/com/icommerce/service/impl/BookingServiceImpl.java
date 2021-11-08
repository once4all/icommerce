package com.icommerce.service.impl;

import com.icommerce.dom.Booking;
import com.icommerce.dom.Brand;
import com.icommerce.repository.IBookingRepository;
import com.icommerce.repository.IBrandRepository;
import com.icommerce.security.SecurityUtils;
import com.icommerce.service.IBookingService;
import com.icommerce.service.IBrandService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements IBookingService {
    private final IBookingRepository bookingRepository;


    @Override
    public List<Booking> getListBooking() {
        return bookingRepository.findAllByCustomer(SecurityUtils.getUserDetails().getUserInfo());
    }
}
