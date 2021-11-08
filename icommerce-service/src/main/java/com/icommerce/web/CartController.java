package com.icommerce.web;

import com.icommerce.category.model.*;
import com.icommerce.mapper.CommonMapper;
import com.icommerce.security.SecurityUtils;
import com.icommerce.service.ICartService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.openapitools.api.CartApi;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@Transactional
public class CartController implements CartApi {
    private final ICartService cartService;
    private final CommonMapper mapper = Mappers.getMapper(CommonMapper.class);

    @Override
    public ResponseEntity<CartGetResponse> getCurrentCart() {
        return ResponseEntity.ok(new CartGetResponse().data(mapper.cartToResponse(cartService.getOpenCart(SecurityUtils.getUserDetails().getUserInfo()))));
    }

    @Override
    public ResponseEntity<CartGetResponse> addItemToCurrentCart(@Valid AddItemRequest addItemRequest) {
        return ResponseEntity.ok(new CartGetResponse().data(mapper.cartToResponse(cartService.addItemToCart(addItemRequest, SecurityUtils.getUserDetails().getUserInfo()))));
    }

    @Override
    public ResponseEntity<CartGetResponse> removeItemFromCurrentCart(Long itemId, @Valid RemoveItemRequest removeItemRequest) {
        return ResponseEntity.ok(new CartGetResponse().data(mapper.cartToResponse(cartService.removeItemFromCart(itemId, removeItemRequest, SecurityUtils.getUserDetails().getUserInfo()))));
    }

    @Override
    public ResponseEntity<BookingDTO> checkoutCurrentCart(@Valid CheckoutCartRequest checkoutCartRequest) {
        return ResponseEntity.ok(mapper.bookingToResponse(cartService.checkoutCurrentCart(checkoutCartRequest, SecurityUtils.getUserDetails().getUserInfo())));
    }


}
