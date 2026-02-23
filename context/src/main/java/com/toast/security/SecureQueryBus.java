/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.security;

import com.toast.shared.app.AuditableQuery;
import com.toast.shared.app.Query;
import com.toast.shared.app.QueryBus;
import com.toast.shared.app.Response;

import java.time.Instant;

public final class SecureQueryBus {
    private final QueryBus queryBus;
    private final SecurityContext securityContext;
    
    public SecureQueryBus(QueryBus queryBus, SecurityContext securityContext) {
        this.queryBus = queryBus;
        this.securityContext = securityContext;
    }
    
    public <R extends Response> R ask(Query query) {
        SecureQuery secureQuery = createSecureQuery(query);
        System.out.println("Asking secure query: " + secureQuery);
        System.out.println(secureQuery.trace());
        // Here you can add additional security checks or logging if needed
        return queryBus.ask(secureQuery.query());
    }
    
    public <R extends Response, ID> R ask(AuditableQuery<ID> query) {
        AuditableQuery<ID> signedQuery = signQuery(query);
        SecureQuery secureQuery = createSecureQuery(signedQuery);
        System.out.println("Asking secure auditable query: " + secureQuery);
        System.out.println(secureQuery.trace());
        // Here you can add additional security checks or logging if needed
        return queryBus.ask(secureQuery.query());
    }
    
    private <ID> AuditableQuery<ID> signQuery(AuditableQuery<ID> query) {
        Principal principal = securityContext.getPrincipal();
        return query.sign(principal.getIdentifier());
    }
    
    private SecureQuery createSecureQuery(Query query) {
        Principal principal = securityContext.getPrincipal();
        return new SecureQuery(query, principal, Instant.now());
    }
}
