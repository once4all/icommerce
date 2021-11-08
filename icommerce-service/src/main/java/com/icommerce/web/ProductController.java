package com.icommerce.web;

import com.icommerce.category.model.ProductsGetResponse;
import com.icommerce.filter.ProductFilter;
import com.icommerce.mapper.CommonMapper;
import com.icommerce.service.IProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.openapitools.api.ProductApi;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@Transactional
public class ProductController implements ProductApi {
    private final IProductService productService;
    private final CommonMapper mapper = Mappers.getMapper(CommonMapper.class);

    @Override
    public ResponseEntity<ProductsGetResponse> findProduct(@Valid String keyword, @Valid List<Long> brands, @Valid List<Long> categories, @Valid List<Long> colours, @Valid Double priceFrom, @Valid Double priceTo) {
        ProductFilter filter = ProductFilter.builder()
                .keyword(keyword)
                .brands(brands)
                .categories(categories)
                .colours(colours)
                .priceFrom(priceFrom)
                .priceTo(priceTo)
                .build();
        return ResponseEntity.ok(new ProductsGetResponse().data(mapper.productToResponse(productService.findProduct(filter))));
    }
}
