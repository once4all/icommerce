package com.icommerce.service.impl;

import com.icommerce.dom.Colour;
import com.icommerce.repository.IColourRepository;
import com.icommerce.service.IColourService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ColourServiceImpl implements IColourService {
    private final IColourRepository colourRepository;

    @Override
    public List<Colour> getAllColours() {
        return colourRepository.findAllByActiveTrue();
    }
}
