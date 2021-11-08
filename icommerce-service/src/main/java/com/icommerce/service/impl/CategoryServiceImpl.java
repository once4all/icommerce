package com.icommerce.service.impl;

import com.icommerce.dom.Category;
import com.icommerce.repository.ICategoryRepository;
import com.icommerce.service.ICategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements ICategoryService {
    private final ICategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAllByActiveTrue();
    }
}
