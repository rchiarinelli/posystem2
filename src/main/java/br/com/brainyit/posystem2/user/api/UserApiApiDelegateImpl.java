package br.com.brainyit.posystem2.user.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.brainyit.posystem2.api.UserApiApiDelegate;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.model.AboutMeModel;
import br.com.brainyit.posystem2.model.SubscriberModel;
import br.com.brainyit.posystem2.security.SecureApiDelegate;
import br.com.brainyit.posystem2.security.SecurityHandler;
import br.com.brainyit.posystem2.user.repository.UserRepository;

@Service
public class UserApiApiDelegateImpl extends SecureApiDelegate implements UserApiApiDelegate {

    private final UserRepository userRepository;

    public UserApiApiDelegateImpl(UserRepository userRepository, SecurityHandler securityHandler) {
        super(userRepository, securityHandler);
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<AboutMeModel> aboutMeGet() throws Exception {
        var user = userRepository.findByUserName(getSecurityHandler().getUserName())
                .orElseThrow(() -> new BusinessException("User not found"));

        final var aboutMeModel = new AboutMeModel()
                .createdAt(user.getCreatedAt().toString())
                .updatedAt(user.getUpdatedAt().toString())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .subscriber(new SubscriberModel().uuid(user.getSubscriber().getUuid())
                        .name(user.getSubscriber().getName()).email(user.getSubscriber().getEmail()));

        return ResponseEntity.ok(aboutMeModel);
    }
}
