package com.godeltech.mastery.employeeservice.security.service.impl;

import com.godeltech.mastery.employeeservice.security.service.SecurityServiceToken;
import com.godeltech.mastery.employeeservice.security.user.AuthenticatedUser;
import lombok.var;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityServiceTokenImpl implements SecurityServiceToken {
    public String getToken() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AuthenticatedUser user = (AuthenticatedUser) principal;
        return user.getToken();
    }
}
