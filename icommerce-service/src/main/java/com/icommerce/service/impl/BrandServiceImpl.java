package com.icommerce.service.impl;

import com.icommerce.dom.Brand;
import com.icommerce.repository.IBrandRepository;
import com.icommerce.service.IBrandService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BrandServiceImpl implements IBrandService {
    private final IBrandRepository brandRepository;

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAllByActiveTrue();
    }
}
