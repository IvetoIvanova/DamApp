package bg.softuni.damapp.interceptor;

import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.service.MessageService;
import bg.softuni.damapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class UserInterceptor implements HandlerInterceptor {

    private final UserService userService;
    private final MessageService messageService;

    public UserInterceptor(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        if (modelAndView != null) {
            String requestUri = request.getRequestURI();
            if ("/logout".equals(requestUri)) {
                return;
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
                String username = authentication.getName();
                UserDTO userByEmail = userService.findByEmail(username);
                int unreadMessageCount = messageService.getUnreadMessageCount(userByEmail.getId());
                modelAndView.addObject("unreadMessageCount", unreadMessageCount);
            }
        }
    }
}
