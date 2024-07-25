//package bg.softuni.damapp.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.io.IOException;
//
//public class MyWebSocketHandler extends TextWebSocketHandler {
//
//    private static final Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);
//
//    @Override
//    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
//        logger.info("Received WebSocket message: {}", message.getPayload());
//        // Handle incoming messages
//    }
//}
