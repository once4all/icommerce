package com.icommerce.service.impl;

import com.icommerce.category.model.AddItemRequest;
import com.icommerce.category.model.CheckoutCartRequest;
import com.icommerce.category.model.RemoveItemRequest;
import com.icommerce.dom.*;
import com.icommerce.exception.BadRequestException;
import com.icommerce.exception.NotFoundException;
import com.icommerce.repository.IBookingRepository;
import com.icommerce.repository.ICartRepository;
import com.icommerce.repository.IProductRepository;
import com.icommerce.service.ICartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CartServiceImpl implements ICartService {
    private final ICartRepository cartRepository;
    private final IProductRepository productRepository;
    private final IBookingRepository bookingRepository;

    @Override
    public Cart getOpenCart(UserInformation user) {
        Cart openCart = cartRepository.findByStatusAndCustomer(CartStatus.OPEN, user);
        if (Objects.isNull(openCart)) {
            openCart = Cart.builder()
                    .customer(user)
                    .status(CartStatus.OPEN)
                    .cartItems(new ArrayList<>())
                    .build();
            cartRepository.save(openCart);
        }
        return openCart;
    }

    @Override
    @Transactional
    public Cart addItemToCart(AddItemRequest request, UserInformation user) {
        //Check if all information is correct
        Long productId = request.getProductId();
        Product product = productRepository.findById(productId).orElse(null);
        if (Objects.isNull(product)) {
            throw new BadRequestException("Product not found");
        }
        Long colourId = request.getColourId();
        Colour colour = null;
        if (Objects.nonNull(colourId)) {
            if (Objects.isNull(product.getColours())) {
                throw new BadRequestException("The product does not support this colour");
            } else {
                colour = product.getColours().stream().filter(c -> c.getId() == colourId).findFirst().orElse(null);
                if (Objects.isNull(colour)) {
                    throw new BadRequestException("Colour not found");
                }
            }
        }
        //Create open cart if not exist
        Cart openCart = getOpenCart(user);
        //Check if there is an item has the same info with this request, if exist simply increase it's quantity
        CartItem cartItem = null;
        if (!openCart.getCartItems().isEmpty()) {
            cartItem = openCart.getCartItems().stream().filter(i -> {
                if (product.getId() != productId) {
                    return false;
                }
                if (Objects.isNull(i.getColour()) && Objects.isNull(colourId)) {
                    return true;
                }
                return Objects.nonNull(i.getColour()) && Objects.nonNull(colourId) && i.getColour().getId() == colourId;
            }).findFirst().orElse(null);
        }
        if (Objects.isNull(cartItem)) {
            cartItem = CartItem.builder()
                    .cart(openCart)
                    .colour(colour)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();

            openCart.getCartItems().add(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        }
        cartRepository.save(openCart);
        return openCart;
    }

    @Override
    @Transactional
    public Cart removeItemFromCart(Long itemId, RemoveItemRequest request, UserInformation user) {
        //Check if all information is correct
        Cart openCart = getOpenCart(user);
        if (Objects.isNull(openCart)) {
            throw new BadRequestException();
        }
        CartItem cartItem = openCart.getCartItems().stream().filter(i -> i.getId() == itemId).findFirst().orElseThrow(NotFoundException::new);
        //If the request quantity is larger than the item quantity, throw exception
        //If it is smaller just deduct the amount
        //If it is equal delete the item
        int quantity = request.getQuantity();
        if (quantity > cartItem.getQuantity()) {
            throw new BadRequestException("Quantity is bigger than current's cart entity");
        } else if (quantity < cartItem.getQuantity()) {
            cartItem.setQuantity(cartItem.getQuantity() - quantity);
        } else {
            openCart.getCartItems().remove(cartItem);
        }
        cartRepository.save(openCart);
        return openCart;
    }

    @Override
    @Transactional
    public Booking checkoutCurrentCart(CheckoutCartRequest request, UserInformation user) {
        Cart openCart = getOpenCart(user);
        List<CartItem> items = openCart.getCartItems();
        if (Objects.isNull(items) || items.isEmpty()) {
            throw new BadRequestException("Cart has no item");
        }
        double price = items.stream().mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity()).sum();
        if (price != request.getTotalPrice()) {
            throw new BadRequestException("Price is different !");
        }
        //Set cart to completed
        openCart.setStatus(CartStatus.COMPLETED);
        cartRepository.save(openCart);
        //Create booking
        final Booking booking = Booking.builder()
                .cart(openCart)
                .customer(user)
                .createdDate(Instant.now())
                .totalPrice(price)
                .build();
        bookingRepository.save(booking);
        booking.setBookingItems(items.stream().map(i -> BookingItem.builder()
                .booking(booking)
                .colour(i.getColour())
                .product(i.getProduct())
                .quantity(i.getQuantity())
                .totalPrice(i.getProduct().getPrice() * i.getQuantity())
                .build()).collect(Collectors.toList()));
        bookingRepository.save(booking);
        return booking;
    }
}
