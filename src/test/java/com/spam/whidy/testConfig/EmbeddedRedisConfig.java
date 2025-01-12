package com.spam.whidy.testConfig;

import redis.embedded.RedisServer;

import java.io.IOException;
import java.net.ServerSocket;

//@TestConfiguration
public class EmbeddedRedisConfig {

    private final RedisServer redisServer;

    public EmbeddedRedisConfig() throws Exception{
        int port = getAvailablePort();
        redisServer = new RedisServer(port);
        System.setProperty("spring.data.redis.port", String.valueOf(port));
    }

//    @PostConstruct
    public void startRedis() throws IOException {
        redisServer.start();
    }

//    @PreDestroy
    public void stopRedis() throws IOException {
        if (redisServer != null && redisServer.isActive()) {
            redisServer.stop();
        }
    }

    private int getAvailablePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }
}