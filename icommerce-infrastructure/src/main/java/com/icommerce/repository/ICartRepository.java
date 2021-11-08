package com.icommerce.repository;

import com.icommerce.dom.Brand;
import com.icommerce.dom.Cart;
import com.icommerce.dom.CartStatus;
import com.icommerce.dom.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Long> {

    Cart findByStatusAndCustomer(CartStatus status, UserInformation customer);
}
