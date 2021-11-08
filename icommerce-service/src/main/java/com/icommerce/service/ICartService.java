package com.icommerce.service;

import com.icommerce.category.model.AddItemRequest;
import com.icommerce.category.model.CheckoutCartRequest;
import com.icommerce.category.model.RemoveItemRequest;
import com.icommerce.dom.Booking;
import com.icommerce.dom.Cart;
import com.icommerce.dom.UserInformation;

public interface ICartService {

    Cart getOpenCart(UserInformation user);

    Cart addItemToCart(AddItemRequest request, UserInformation user);

    Cart removeItemFromCart(Long itemId, RemoveItemRequest request, UserInformation user);

    Booking checkoutCurrentCart(CheckoutCartRequest request, UserInformation user);
}
