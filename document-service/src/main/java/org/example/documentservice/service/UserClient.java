package org.example.documentservice.service;

import org.example.documentservice.dto.RoomInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userClient", url = "${user-service.url}")
public class UserClient {
    @GetMapping("/users/{id}")
    RoomInfo getUserInfo(@PathVariable("id") Long userId) {
        return null;
    }
}
