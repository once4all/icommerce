package com.icommerce.repository;

import com.icommerce.dom.Colour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IColourRepository extends JpaRepository<Colour, Long> {

    public List<Colour> findAllByActiveTrue();
}
