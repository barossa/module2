package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends RepresentationModel<UserDto> implements UserDetails {
    @JsonView(View.Base.class)
    private int id;

    @JsonView(View.Base.class)
    private String username;

    @JsonView(View.Full.class)
    private String password;

    @JsonView(View.Full.class)
    private Set<String> roles;

    @Override
    @JsonView(View.Full.class)
    public Collection<GrantedAuthority> getAuthorities() {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    @JsonView(View.Full.class)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonView(View.Full.class)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonView(View.Full.class)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonView(View.Full.class)
    public boolean isEnabled() {
        return true;
    }
}
