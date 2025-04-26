package com.devsuperior.bds04.repositories;

import com.devsuperior.bds04.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findAllByOrderByNameAsc();
}
