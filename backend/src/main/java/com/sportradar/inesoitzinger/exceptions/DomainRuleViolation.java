package com.sportradar.inesoitzinger.exceptions;

public class DomainRuleViolation extends RuntimeException {
    public DomainRuleViolation(String msg) {
        super(msg);
    }
}
