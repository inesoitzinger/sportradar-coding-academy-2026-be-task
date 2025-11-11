package com.sportradar.inesoitzinger.exceptions;

/**
 * Thrown when a business rule of the domain model is violated.
 *
 * This is not a technical exception.
 * This signals that the client requested an operation which is not allowed
 * according to the domain invariants (for example: same team plays against itself,
 * match scheduled in the past, team belongs to a different sport, etc).
 */
public class DomainRuleViolation extends RuntimeException {
    public DomainRuleViolation(String message) {
        super(message);
    }
}
