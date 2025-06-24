package br.com.brainyit.posystem2.interceptor;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import br.com.brainyit.posystem2.exception.ForbiddenOperationException;
import br.com.brainyit.posystem2.security.SecurityHandler;
import br.com.brainyit.posystem2.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RestCallInterceptor implements HandlerInterceptor {

    private static final String FORBIDDEN_OPERATION_EXCEPTION_MESSAGE_KEY = "forbidden.operation.exception.message";

    @Value("${security.api.protected-apis}")
    private String[] protectedApis;

    private final SecurityHandler securityHandler;

    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (Arrays.asList(protectedApis).contains(request.getRequestURI())) {
            final var requestSubscriber = request.getRequestURI().split("/")[0];
            final var user = userRepository.findByUserName(securityHandler.getUserName());
            if (user.isEmpty() || !user.get().getSubscriber().getUuid().equals(requestSubscriber)) {
                throw new ForbiddenOperationException(FORBIDDEN_OPERATION_EXCEPTION_MESSAGE_KEY);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}