package com.huertohogar.repository;

import com.huertohogar.model.entity.StoreLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreLocationRepository extends JpaRepository<StoreLocation, String> {
    
    List<StoreLocation> findAllByOrderByNameAsc();
}
