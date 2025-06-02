package com.example.demo.ss;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * <p>An implementation of {@link UserDetailsService} that simply return a static
 * user.</p>
 * <b>Intentionally not a {@code @Component} because you will get a warning from
 * spring like below.</b>
 * 
 * <p>
 * <i>Global AuthenticationManager configured with an AuthenticationProvider
 * bean.
 * UserDetailsService beans will not be used by Spring Security for
 * automatically configuring username/password...</i>
 * </p>
 */
public class XUserDetailService implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.error("In loadUserByUsername...");

        List<GrantedAuthority> au = new ArrayList<>();
        au.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        UserDetails ud = new User("henry", "henry", au);

        return ud;
    }

}
