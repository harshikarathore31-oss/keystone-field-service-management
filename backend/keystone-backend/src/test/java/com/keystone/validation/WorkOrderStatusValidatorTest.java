package com.keystone.validation;

import com.keystone.entity.Status;
import com.keystone.exception.IllegalStatusTransitionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkOrderStatusValidatorTest {

    @Test
    void shouldAllowNewToInProgress() {

        assertDoesNotThrow(() ->
                WorkOrderStatusValidator.validateTransition(
                        Status.NEW,
                        Status.IN_PROGRESS));
    }

    @Test
    void shouldAllowInProgressToCompleted() {

        assertDoesNotThrow(() ->
                WorkOrderStatusValidator.validateTransition(
                        Status.IN_PROGRESS,
                        Status.COMPLETED));
    }

    @Test
    void shouldThrowExceptionForCompletedToInProgress() {

        assertThrows(
                IllegalStatusTransitionException.class,
                () -> WorkOrderStatusValidator.validateTransition(
                        Status.COMPLETED,
                        Status.IN_PROGRESS)
        );
    }

    @Test
    void shouldThrowExceptionForCancelledToNew() {

        assertThrows(
                IllegalStatusTransitionException.class,
                () -> WorkOrderStatusValidator.validateTransition(
                        Status.CANCELLED,
                        Status.NEW)
        );
    }
}