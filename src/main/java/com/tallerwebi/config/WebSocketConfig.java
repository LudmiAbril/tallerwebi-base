package com.tallerwebi.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Prefijos para destinos del lado del cliente
        registry.setApplicationDestinationPrefixes("/app");
        // Prefijos para el broker de mensajes
        registry.enableSimpleBroker("/topic", "/queue");
        // Prefijo para las rutas de usuario
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registro del endpoint WebSocket
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }
}