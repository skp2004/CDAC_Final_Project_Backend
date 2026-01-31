package com.rideongo.bms_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "rideongo-ums-service")
public interface UmsClient {

    @GetMapping("/users/me/id")
    Long getLoggedInUserId(
        @RequestHeader("Authorization") String authHeader
    );
}
