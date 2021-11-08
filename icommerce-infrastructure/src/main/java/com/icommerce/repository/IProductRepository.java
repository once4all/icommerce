package com.icommerce.repository;

import com.icommerce.dom.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface IProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByIdIn(List<Long> id);
}
