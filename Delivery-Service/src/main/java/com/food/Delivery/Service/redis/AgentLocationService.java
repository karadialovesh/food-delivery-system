package com.food.Delivery.Service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgentLocationService {
    
    private static final Logger log = LoggerFactory.getLogger(AgentLocationService.class);

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String KEY = "delivery_agents";

    public void updateLocation(Long agentId, double lat, double lon) {
        log.debug("Updating location for Agent ID: {} to [{}, {}]", agentId, lat, lon);
        redisTemplate.opsForGeo()
                .add(KEY, new Point(lon, lat), agentId.toString());
    }

    public void remove(Long agentId) {
        redisTemplate.opsForZSet().remove(KEY, agentId.toString());
    }

    public GeoResults<RedisGeoCommands.GeoLocation<Object>> findNearby(double lat, double lon, double radiusKm) {

        Circle area = new Circle(
                new Point(lon, lat),
                new Distance(radiusKm, Metrics.KILOMETERS)
        );

        return redisTemplate.opsForGeo().radius(KEY, area);
    }
}
