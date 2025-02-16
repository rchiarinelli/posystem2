package br.com.brainyit.posystem2.po;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.brainyit.posystem2.domain.POStatus;
import br.com.brainyit.posystem2.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
class ProductionOrderTest {

    private ProductionOrder productionOrder;

    @BeforeEach
    void setUp() {
        productionOrder = new ProductionOrder();
    }

    @Test
    void test_moveToNextPhase_fromPlanning_toExecuting() {
        // given
        productionOrder.setStatus(POStatus.PLANNING);

        // when
        productionOrder.moveToNextPhase();

        // then
        assertThat(productionOrder.getStatus(), is(POStatus.EXECUTING));
    }

    @Test
    void test_moveToNextPhase_fromExecuting_toDone() {
        // given
        productionOrder.setStatus(POStatus.EXECUTING);

        // when
        productionOrder.moveToNextPhase();

        // then
        assertThat(productionOrder.getStatus(), is(POStatus.DONE));
    }

    @Test
    void test_moveToNextPhase_fromDone_throwsException() {
        // given
        productionOrder.setStatus(POStatus.DONE);

        // when/then
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> productionOrder.moveToNextPhase());
        
        assertThat(exception.getMessage(), is("Cannot move production order from DONE status"));
    }

    @Test
    void test_moveToNextPhase_fromCancelled_throwsException() {
        // given
        productionOrder.setStatus(POStatus.CANCELLED);

        // when/then
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> productionOrder.moveToNextPhase());
        
        assertThat(exception.getMessage(), is("Cannot move production order from CANCELLED status"));
    }

    @Test
    void test_cancel_fromPlanning() {
        // given
        productionOrder.setStatus(POStatus.PLANNING);
        productionOrder.setDescription("Test PO");
        // when
        productionOrder.cancel("Canceled by a test.");

        // then
        assertThat(productionOrder.getStatus(), is(POStatus.CANCELLED));
        assertThat(productionOrder.getDescription(), is("Test PO\nCanceled by a test."));
        assertThat(productionOrder.getActualDeliveryDate(), is(LocalDate.now()));
    }

    @Test
    void test_cancel_fromExecuting() {
        // given
        productionOrder.setStatus(POStatus.EXECUTING);
        productionOrder.setDescription("Test PO");
        // when
        productionOrder.cancel("Canceled by a test.");

        // then
        assertThat(productionOrder.getStatus(), is(POStatus.CANCELLED));
        assertThat(productionOrder.getDescription(), is("Test PO\nCanceled by a test."));
        assertThat(productionOrder.getActualDeliveryDate(), is(LocalDate.now()));
    }

    @Test
    void test_cancel_fromDone_throwsException() {
        // given
        productionOrder.setStatus(POStatus.DONE);

        // when/then
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> productionOrder.cancel("Canceled by a test."));
        
        assertThat(exception.getMessage(), is("Cannot cancel production order in DONE status"));
    }

    @Test
    void test_cancel_noDescription_throwsException() {
        // given
        productionOrder.setStatus(POStatus.PLANNING);

        // when/then
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> productionOrder.cancel(""));
        
        assertThat(exception.getMessage(), is("Description cannot be empty"));
    }
    
}


