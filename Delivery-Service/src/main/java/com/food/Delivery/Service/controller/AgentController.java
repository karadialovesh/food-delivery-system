package com.food.Delivery.Service.controller;

import com.food.Delivery.Service.redis.AgentLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agents")
@RequiredArgsConstructor
public class AgentController {

    private final AgentLocationService service;

    @PostMapping("/location")
    public String update(@RequestParam Long agentId,
                         @RequestParam double lat,
                         @RequestParam double lon) {

        service.updateLocation(agentId, lat, lon);
        return "updated";
    }

    @DeleteMapping("/{agentId}")
    public String deactivate(@PathVariable Long agentId) {
        service.remove(agentId);
        return "offline";
    }
}