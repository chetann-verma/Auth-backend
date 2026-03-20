package com.auth.controller;

import com.auth.dto.FeedItemResponse;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedController {

    @GetMapping("/feed")
    public List<FeedItemResponse> feed(Authentication authentication) {
        // Feed is populated on the frontend side for now.
        return List.of();
    }
}

