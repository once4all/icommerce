package com.icommerce.service.impl;

import com.icommerce.dom.Product;
import com.icommerce.dom.ProductStatus;
import com.icommerce.filter.ProductFilter;
import com.icommerce.repository.IProductRepository;
import com.icommerce.repository.impl.ProductRepositoryImpl;
import com.icommerce.service.IProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements IProductService {
    private final IProductRepository productRepository;
    private final ProductRepositoryImpl productRepositoryImpl;

    @Override
    public List<Product> findProduct(ProductFilter filter) {
        List<Object> productIds = productRepositoryImpl.findAllWithCondition(ProductStatus.ACTIVE, filter);
        if (productIds.isEmpty()) {
            return Collections.emptyList();
        }
        return productRepository.findAllByIdIn(productIds.stream().map(i -> Long.valueOf(i.toString())).collect(Collectors.toList()));
    }
}
