package com.cxy.entity.common;

import com.cxy.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author CHEN LEIJIN
 * @description TODO
 * @date 2019/9/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AuthenticationUser extends org.springframework.security.core.userdetails.User {

    User user;

    public AuthenticationUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public AuthenticationUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
