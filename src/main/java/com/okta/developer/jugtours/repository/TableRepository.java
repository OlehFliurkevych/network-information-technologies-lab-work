package com.okta.developer.jugtours.repository;

import com.okta.developer.jugtours.model.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long> {

  List<TableEntity> findAllByRestaurantId(Long restaurantId);

}
