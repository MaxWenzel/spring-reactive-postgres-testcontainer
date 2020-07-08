package org.devzone.addresses.repository;

import org.devzone.addresses.model.Address;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostalCodeRepository extends ReactiveCrudRepository<Address, Integer> {
}