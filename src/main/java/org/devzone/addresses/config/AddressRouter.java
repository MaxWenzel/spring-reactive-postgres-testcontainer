package org.devzone.addresses.config;

import org.devzone.addresses.model.Address;
import org.devzone.addresses.service.AddressService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class AddressRouter {

    @Bean
    public RouterFunction<ServerResponse> routes(AddressService addressService) {
        return route(
                        GET("/postalcodes/{postalcode}"),
                        request -> ok().contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                                .body(addressService.findAddressByPostalCode(request.pathVariable("postalcode")), Address.class)
                )
                .andRoute(
                        GET("/postalcodes"),
                        request -> ok().contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                                .body(addressService.findAll(), Address.class)
                );
    }
}