package org.devzone.addresses.service;

import org.devzone.addresses.model.Address;
import org.devzone.addresses.repository.PostalCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);

    private PostalCodeRepository postalCodeRepository;

    public AddressService(PostalCodeRepository postalCodeRepository) {
        this.postalCodeRepository = postalCodeRepository;
    }

    public Mono<Address> saveAddress(Address address) {
        return this.postalCodeRepository. save(address);
    }

    public Mono<Long> getTotalCount() {
        return this.postalCodeRepository.count();
    }

    public Flux<Address> findAddressByPostalCode(String postalCode) {
        logger.debug("Try to find address for postalCode {}", postalCode);
        return postalCodeRepository.findAll();
    }

    public Flux<Address> findAll() {
        logger.debug("Try to find all addresses");
        return postalCodeRepository.findAll();
    }
}