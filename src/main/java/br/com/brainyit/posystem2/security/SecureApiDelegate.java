package br.com.brainyit.posystem2.security;

import br.com.brainyit.posystem2.domain.Subscriber;
import br.com.brainyit.posystem2.domain.User;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class SecureApiDelegate {

    private final UserRepository userRepository;

    private final SecurityHandler securityHandler;

    public String getSubscriberUuid() {
        return userRepository.findByUserName(securityHandler.getUserName())
                .map(User::getSubscriber)
                .map(Subscriber::getUuid)
                .orElseThrow(() -> new BusinessException("User not found"));
    }

    public SecurityHandler getSecurityHandler() {
        return securityHandler;
    }
}
