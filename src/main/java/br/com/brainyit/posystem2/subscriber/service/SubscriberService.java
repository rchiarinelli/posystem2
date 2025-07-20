package br.com.brainyit.posystem2.subscriber.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.posystem2.domain.Subscriber;
import br.com.brainyit.posystem2.exception.NotFoundException;
import br.com.brainyit.posystem2.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;

    @Transactional(readOnly = true)
    public Subscriber getSubscriber(final String uuid) {
        return subscriberRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("Subscriber not found"));
    }

}
