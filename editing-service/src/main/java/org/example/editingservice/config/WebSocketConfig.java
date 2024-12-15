package org.example.editingservice.config;

import org.example.editingservice.handler.EditingWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.Map;

@Configuration
public class WebSocketConfig {
    private final EditingWebSocketHandler editingWebSocketHandler;

    public WebSocketConfig(EditingWebSocketHandler editingWebSocketHandler) {
        this.editingWebSocketHandler = editingWebSocketHandler;
    }

    @Bean
    public SimpleUrlHandlerMapping webSocketMapping() {
        // 将 /ws/edit 路径映射到EditingWebSocketHandler
        Map<String, Object> urlMap = Map.of("/ws/edit", editingWebSocketHandler);
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(10);
        mapping.setUrlMap(urlMap);
        return mapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
