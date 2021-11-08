package com.icommerce;

import com.icommerce.dom.*;
import com.icommerce.filter.ProductFilter;
import com.icommerce.repository.IProductRepository;
import com.icommerce.repository.impl.ProductRepositoryImpl;
import com.icommerce.service.IProductService;
import com.icommerce.service.impl.ProductServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProductServiceTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private ProductRepositoryImpl productRepositoryImpl;

    private IProductService productService;

    Map<Long, Product> products = new HashMap<>();

    @Before
    public void setUp() {
        productService = new ProductServiceImpl(productRepository, productRepositoryImpl);
        products.put(1L, Product.builder().id(1).name("bmw 320")
                .brands(Collections.singletonList(Brand.builder().id(1L).build()))
                .categories(Collections.singletonList(Category.builder().id(1L).build()))
                .colours(Collections.singletonList(Colour.builder().id(1L).build()))
                .build());
        products.put(2L, Product.builder().id(2).name("iphone X")
                .brands(Collections.singletonList(Brand.builder().id(2L).build()))
                .categories(Collections.singletonList(Category.builder().id(2L).build()))
                .colours(Collections.singletonList(Colour.builder().id(2L).build()))
                .build());
        products.put(3L, Product.builder().id(3).name("E200")
                .brands(Collections.singletonList(Brand.builder().id(3L).build()))
                .categories(Collections.singletonList(Category.builder().id(3L).build()))
                .colours(Collections.singletonList(Colour.builder().id(3L).build()))
                .build());
        products.put(4L, Product.builder().id(4).name("OLED C80")
                .brands(Collections.singletonList(Brand.builder().id(4L).build()))
                .categories(Collections.singletonList(Category.builder().id(4L).build()))
                .colours(Collections.singletonList(Colour.builder().id(4L).build()))
                .build());
    }

    @Test
    public void findAllActive() {
        ProductFilter filter = ProductFilter.builder().build();
        Mockito.when(productRepositoryImpl.findAllWithCondition(ProductStatus.ACTIVE, filter)).thenReturn(new ArrayList<>(products.keySet()));
        Mockito.when(productRepository.findAllByIdIn(new ArrayList<>(products.keySet()))).thenReturn(new ArrayList<>(products.values()));
        // Use mock in test
        Assert.assertEquals(new ArrayList<>(products.values()), productService.findProduct(filter));
    }

    @Test
    public void findAllWithKeyword() {
        ProductFilter filter = ProductFilter.builder().keyword("e").build();

        List<Product> result = products.values().stream().filter(product -> product.getName().toLowerCase().contains(filter.getKeyword())).collect(Collectors.toList());

        List ids = result.stream().map(Product::getId).collect(Collectors.toList());
        Mockito.when(productRepositoryImpl.findAllWithCondition(ProductStatus.ACTIVE, filter)).thenReturn(ids);
        Mockito.when(productRepository.findAllByIdIn(ids)).thenReturn(result);
        // Use mock in test
        Assert.assertEquals(3, productService.findProduct(filter).size());
    }


    @Test
    public void findByBrand() {
        ProductFilter filter = ProductFilter.builder().brands(Arrays.asList(1L, 2L)).build();

        List<Product> result = products.values().stream()
                .filter(product -> product.getBrands().stream().anyMatch(l -> filter.getBrands().contains(l.getId()))).collect(Collectors.toList());

        List ids = result.stream().map(Product::getId).collect(Collectors.toList());
        Mockito.when(productRepositoryImpl.findAllWithCondition(ProductStatus.ACTIVE, filter)).thenReturn(ids);
        Mockito.when(productRepository.findAllByIdIn(ids)).thenReturn(result);
        // Use mock in test
        Assert.assertEquals(2, productService.findProduct(filter).size());
    }

    @Test
    public void findByCategory() {
        ProductFilter filter = ProductFilter.builder().categories(Arrays.asList(3L, 5L)).build();

        List<Product> result = products.values().stream()
                .filter(product -> product.getCategories().stream().anyMatch(l -> filter.getCategories().contains(l.getId()))).collect(Collectors.toList());

        List ids = result.stream().map(Product::getId).collect(Collectors.toList());
        Mockito.when(productRepositoryImpl.findAllWithCondition(ProductStatus.ACTIVE, filter)).thenReturn(ids);
        Mockito.when(productRepository.findAllByIdIn(ids)).thenReturn(result);
        // Use mock in test
        Assert.assertEquals(1, productService.findProduct(filter).size());
    }

    @Test
    public void findByColour() {
        ProductFilter filter = ProductFilter.builder().colours(Arrays.asList(5L, 6L)).build();

        List<Product> result = products.values().stream()
                .filter(product -> product.getColours().stream().anyMatch(l -> filter.getColours().contains(l.getId()))).collect(Collectors.toList());

        List<Object> ids = result.stream().map(Product::getId).collect(Collectors.toList());
        Mockito.when(productRepositoryImpl.findAllWithCondition(ProductStatus.ACTIVE, filter)).thenReturn(ids);
        Mockito.when(productRepository.findAllByIdIn(ids.stream().map(i -> Long.valueOf(i.toString())).collect(Collectors.toList()))).thenReturn(result);
        // Use mock in test
        Assert.assertEquals(0, productService.findProduct(filter).size());
    }
}
