package com.keystone.validation;

import com.keystone.entity.Status;
import com.keystone.exception.IllegalStatusTransitionException;

public class WorkOrderStatusValidator {

    public static void validateTransition(
            Status currentStatus,
            Status newStatus) {

        // No status change
        if (currentStatus == newStatus) {
            return;
        }

        // NEW -> ASSIGNED
        if (currentStatus == Status.NEW
                && newStatus == Status.ASSIGNED) {
            return;
        }

        // NEW -> CANCELLED
        if (currentStatus == Status.NEW
                && newStatus == Status.CANCELLED) {
            return;
        }

        // ASSIGNED -> IN_PROGRESS
        if (currentStatus == Status.ASSIGNED
                && newStatus == Status.IN_PROGRESS) {
            return;
        }

        // ASSIGNED -> CANCELLED
        if (currentStatus == Status.ASSIGNED
                && newStatus == Status.CANCELLED) {
            return;
        }

        // IN_PROGRESS -> ON_HOLD
        if (currentStatus == Status.IN_PROGRESS
                && newStatus == Status.ON_HOLD) {
            return;
        }

        // IN_PROGRESS -> COMPLETED
        if (currentStatus == Status.IN_PROGRESS
                && newStatus == Status.COMPLETED) {
            return;
        }

        // ON_HOLD -> IN_PROGRESS
        if (currentStatus == Status.ON_HOLD
                && newStatus == Status.IN_PROGRESS) {
            return;
        }

        // COMPLETED -> CLOSED
        if (currentStatus == Status.COMPLETED
                && newStatus == Status.CLOSED) {
            return;
        }

        // All other transitions are illegal
        throw new IllegalStatusTransitionException(
                "Invalid work order status transition: "
                        + currentStatus
                        + " -> "
                        + newStatus
        );
    }
}