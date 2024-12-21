package com.okta.developer.jugtours.repository;

import com.okta.developer.jugtours.model.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

  List<RestaurantEntity> findAllByUserId(String id);

  List<RestaurantEntity> findAllByName(String name);

}
