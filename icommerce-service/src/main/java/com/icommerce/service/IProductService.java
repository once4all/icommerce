package com.icommerce.service;

import com.icommerce.dom.Product;
import com.icommerce.filter.ProductFilter;

import java.util.List;

public interface IProductService {

    List<Product> findProduct(ProductFilter filter);
}
