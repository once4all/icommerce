package com.icommerce.web;

import com.icommerce.category.model.BrandsGetResponse;
import com.icommerce.category.model.CategoriesGetResponse;
import com.icommerce.category.model.ColoursGetResponse;
import com.icommerce.mapper.CommonMapper;
import com.icommerce.service.IBrandService;
import com.icommerce.service.ICategoryService;
import com.icommerce.service.IColourService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.openapitools.api.CommonApi;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@Transactional
public class CommonController implements CommonApi {
    private final IBrandService brandService;
    private final IColourService colourService;
    private final ICategoryService categoryService;
    private final CommonMapper mapper = Mappers.getMapper(CommonMapper.class);

    @Override
    public ResponseEntity<BrandsGetResponse> getBrands() {
        return ResponseEntity.ok().body(new BrandsGetResponse().data(mapper.brandsToResponse(brandService.getAllBrands())));
    }

    @Override
    public ResponseEntity<ColoursGetResponse> getColours() {
        return ResponseEntity.ok().body(new ColoursGetResponse().data(mapper.coloursToResponse(colourService.getAllColours())));
    }

    @Override
    public ResponseEntity<CategoriesGetResponse> getCategories() {
        return ResponseEntity.ok().body(new CategoriesGetResponse().data(mapper.categoriesToResponse(categoryService.getAllCategories())));
    }
}
