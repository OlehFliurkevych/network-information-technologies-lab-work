package com.okta.developer.jugtours.repository;

import com.okta.developer.jugtours.model.BookEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Repository
public interface BookEventRepository extends JpaRepository<BookEventEntity, Long> {

  List<BookEventEntity> findAllByUserId(String userId);

  List<BookEventEntity> findAllByTableIdAndDate(Long tableId, Date date);

  List<BookEventEntity> findAllByTableId(Long tableId);
}
