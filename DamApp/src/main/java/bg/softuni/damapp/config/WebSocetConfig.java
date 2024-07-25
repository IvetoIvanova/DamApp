//package bg.softuni.damapp.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
//@Configuration
//@EnableWebSocket
//public class WebSocetConfig implements WebSocketConfigurer {
//
//    private static final Logger logger = LoggerFactory.getLogger(WebSocetConfig.class);
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(myHandler(), "/ws").setAllowedOrigins("*");
//        logger.info("WebSocket handler registered");
//    }
//
//    @Bean
//    public WebSocketHandler myHandler() {
//        return new MyWebSocketHandler();
//    }
//}
