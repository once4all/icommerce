package com.icommerce.web;

import com.icommerce.category.model.BookingGetResponse;
import com.icommerce.mapper.CommonMapper;
import com.icommerce.service.IBookingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.openapitools.api.BookingApi;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@Transactional
public class BookingController implements BookingApi {
    private final IBookingService bookingService;
    private final CommonMapper mapper = Mappers.getMapper(CommonMapper.class);

    @Override
    public ResponseEntity<BookingGetResponse> getListBooking() {
        return ResponseEntity.ok(new BookingGetResponse().data(mapper.bookingToResponse(bookingService.getListBooking())));
    }
}
