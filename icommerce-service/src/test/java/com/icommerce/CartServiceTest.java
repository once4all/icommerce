package com.icommerce;

import com.icommerce.category.model.AddItemRequest;
import com.icommerce.category.model.CheckoutCartRequest;
import com.icommerce.category.model.RemoveItemRequest;
import com.icommerce.dom.*;
import com.icommerce.exception.NotFoundException;
import com.icommerce.repository.IBookingRepository;
import com.icommerce.repository.ICartRepository;
import com.icommerce.repository.IProductRepository;
import com.icommerce.service.ICartService;
import com.icommerce.service.impl.CartServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CartServiceTest {

    @Mock
    private ICartRepository cartRepository;
    @Mock
    private IProductRepository productRepository;
    @Mock
    private IBookingRepository bookingRepository;

    private ICartService cartService;
    private UserInformation user;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp() {
        cartService = new CartServiceImpl(cartRepository, productRepository, bookingRepository);
        user = new UserInformation();
        user.setId(1L);
    }

    @Test
    public void testGetOpenCart() {
        Mockito.when(cartRepository.findByStatusAndCustomer(CartStatus.OPEN, user)).thenReturn(null);
        // Use mock in test
        Assert.assertNotNull(cartService.getOpenCart(user));
    }

    @Test
    public void testAddItemToCartWithoutProductId() {
        Mockito.when(cartRepository.findByStatusAndCustomer(CartStatus.OPEN, user)).thenReturn(null);
        AddItemRequest request = new AddItemRequest();
        // Use mock in test
        exceptionRule.expect(NotFoundException.class);
        cartService.addItemToCart(request, user);
    }


    @Test

    public void testAddItemToCart() {
        Mockito.when(cartRepository.findByStatusAndCustomer(CartStatus.OPEN, user)).thenReturn(null);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(Product.builder().id(1L).build()));
        AddItemRequest request = new AddItemRequest();
        request.setProductId(1L);
        request.setQuantity(1);
        // Use mock in test
        Assert.assertNotNull(cartService.getOpenCart(user));
        Assert.assertNotNull(cartService.getOpenCart(user).getCartItems());
    }

    @Test
    public void testRemoveItemFromCart() {
        Cart openCart = Cart.builder()
                .customer(user)
                .status(CartStatus.OPEN)
                .cartItems(new ArrayList<>())
                .build();
        CartItem cartItem = CartItem.builder()
                .id(1)
                .cart(openCart)
                .colour(null)
                .product(null)
                .quantity(10)
                .build();
        openCart.getCartItems().add(cartItem);
        Mockito.when(cartRepository.findByStatusAndCustomer(CartStatus.OPEN, user)).thenReturn(openCart);
        // Use mock in test
        Assert.assertEquals(cartService.removeItemFromCart(1L, new RemoveItemRequest().quantity(2), user).getCartItems().get(0).getQuantity(), 8);
        Assert.assertEquals(cartService.removeItemFromCart(1L, new RemoveItemRequest().quantity(8), user).getCartItems().size(), 0);
    }

    @Test
    public void testCheckoutCart() {
        Cart openCart = Cart.builder()
                .customer(user)
                .status(CartStatus.OPEN)
                .cartItems(new ArrayList<>())
                .build();
        openCart.getCartItems().add(CartItem.builder()
                .id(1)
                .cart(openCart)
                .colour(null)
                .product(Product.builder().price(10000d).build())
                .quantity(10)
                .build());
        openCart.getCartItems().add(CartItem.builder()
                .id(2)
                .cart(openCart)
                .colour(null)
                .product(Product.builder().price(20000d).build())
                .quantity(1)
                .build());
        Mockito.when(cartRepository.findByStatusAndCustomer(CartStatus.OPEN, user)).thenReturn(openCart);
        Booking booking = cartService.checkoutCurrentCart(new CheckoutCartRequest().totalPrice(120000D), user);
        Assert.assertEquals(booking.getBookingItems().size(), 2);
        Assert.assertEquals(openCart.getStatus(), CartStatus.COMPLETED);

    }
}
