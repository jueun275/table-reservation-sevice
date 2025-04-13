package com.example.tablereservation.domain.store;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StoreRepository extends CrudRepository<Store, Long> {
    List<Store> findAllByOrderByNameAsc();

    void deleteAllInBatch();
}
