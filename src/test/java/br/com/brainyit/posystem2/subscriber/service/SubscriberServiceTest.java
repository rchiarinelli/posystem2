package br.com.brainyit.posystem2.subscriber.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.brainyit.posystem2.domain.Subscriber;
import br.com.brainyit.posystem2.exception.NotFoundException;
import br.com.brainyit.posystem2.repository.SubscriberRepository;

@ExtendWith(MockitoExtension.class)
class SubscriberServiceTest {

    @InjectMocks
    private SubscriberService subscriberService;

    @Mock
    private SubscriberRepository subscriberRepository;

    private String subscriberUuid;
    private Subscriber subscriber;

    @BeforeEach
    void setUp() {
        subscriberUuid = "test-subscriber-uuid";
        subscriber = Subscriber.builder()
                .id(1L)
                .uuid(subscriberUuid)
                .name("Test Subscriber")
                .email("test@example.com")
                .build();
    }

    @Test
    void test_getSubscriber_withValidUuid_subscriberIsReturned() {
        // Arrange
        when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));

        // Act
        Subscriber result = subscriberService.getSubscriber(subscriberUuid);

        // Assert
        assertThat(result, is(subscriber));
        verify(subscriberRepository).findByUuid(subscriberUuid);
    }

    @Test
    void test_getSubscriber_withInvalidUuid_notFoundExceptionIsThrown() {
        // Arrange
        when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.empty());

        // Act
        assertThrows(NotFoundException.class, () -> subscriberService.getSubscriber(subscriberUuid));
    }

}
