package com.icommerce.security;

import com.icommerce.dom.UserInformation;
import com.icommerce.repository.IUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        try {
            final UserInformation login = userRepository.findUserByUsername(username);
            if (login != null) {
                return new JwtUserDetails(login).loginId(login.getId());
            }
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
        }
        throw new UsernameNotFoundException(username + " could not be found.");
    }
}
