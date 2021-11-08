package com.icommerce.dto;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class ICommerceUser extends User {
    private String name;
    private Set<String> roles;

    public ICommerceUser(String userId) {
        this(userId, (Collection) Lists.newArrayList());
    }

    public ICommerceUser(String userId, Collection<String> roles) {
        super(userId, "{noop}N/A", (Collection) CollectionUtils.emptyIfNull(roles).stream().map(SimpleGrantedAuthority::new));
        this.roles = ImmutableSet.copyOf((Collection) CollectionUtils.emptyIfNull(this.getAuthorities()).stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
    }

}
