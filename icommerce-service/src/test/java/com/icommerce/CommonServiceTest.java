package com.icommerce;

import com.icommerce.repository.IBrandRepository;
import com.icommerce.repository.ICategoryRepository;
import com.icommerce.repository.IColourRepository;
import com.icommerce.service.IBrandService;
import com.icommerce.service.ICategoryService;
import com.icommerce.service.IColourService;
import com.icommerce.service.impl.BrandServiceImpl;
import com.icommerce.service.impl.CategoryServiceImpl;
import com.icommerce.service.impl.ColourServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class CommonServiceTest {

    @Mock
    private IBrandRepository brandRepository;
    @Mock
    private ICategoryRepository categoryRepository;
    @Mock
    private IColourRepository colourRepository;

    private IBrandService brandService;
    private ICategoryService categoryService;
    private IColourService colourService;

    @Before
    public void setUp() {
        brandService = new BrandServiceImpl(brandRepository);
        categoryService = new CategoryServiceImpl(categoryRepository);
        colourService = new ColourServiceImpl(colourRepository);
    }

    @Test
    public void getListBrands() {
        Mockito.when(brandRepository.findAllByActiveTrue()).thenReturn(Collections.emptyList());

        // Use mock in test
        Assert.assertEquals(Collections.emptyList(), brandService.getAllBrands());
    }

    @Test
    public void getListCategories() {
        Mockito.when(categoryRepository.findAllByActiveTrue()).thenReturn(Collections.emptyList());

        // Use mock in test
        Assert.assertEquals(Collections.emptyList(), categoryService.getAllCategories());
    }

    @Test
    public void getListColours() {
        Mockito.when(colourRepository.findAllByActiveTrue()).thenReturn(Collections.emptyList());

        // Use mock in test
        Assert.assertEquals(Collections.emptyList(), colourService.getAllColours());
    }
}
