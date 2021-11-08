

package com.icommerce.service.impl;

import com.icommerce.dom.UserInformation;
import com.icommerce.dom.UserRole;
import com.icommerce.repository.IUserRepository;
import com.icommerce.security.SecurityUtils;
import com.icommerce.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service that handle business logics relate to user/login/role.
 */
@Service
@Transactional(rollbackFor = Throwable.class)
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {


    private final IUserRepository userRepository;


    public UserInformation getUserDisplayName() {
        return userRepository.findUserByUsername(SecurityUtils.getUserDetails().getUsername());
    }

    public UserInformation getCurrentLogin() {
        return userRepository.findById(SecurityUtils.getLoginId()).get();
    }

    public UserRole getCurrentUserRoles() {
        return getCurrentLogin().getRole();

    }

}
