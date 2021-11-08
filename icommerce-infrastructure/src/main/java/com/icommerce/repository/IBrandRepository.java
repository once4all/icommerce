package com.icommerce.repository;

import com.icommerce.dom.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBrandRepository extends JpaRepository<Brand, Long> {

    public List<Brand> findAllByActiveTrue();
}
