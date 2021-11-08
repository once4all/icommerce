package com.icommerce.service.impl;

import com.icommerce.dom.UserInformation;
import com.icommerce.exception.UserNotFoundException;
import com.icommerce.repository.IUserRepository;
import com.icommerce.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;

    @Override
    public UserInformation findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    @Transactional
    public void updateUserWithoutCheckingPermission(UserInformation user) {
        Optional<UserInformation> userDom = userRepository.findById(user.getId());
        if (!userDom.isPresent()) {
            throw new UserNotFoundException("User with id " + user.getId() + " not exist to be updated");
        }
        user.setPassword(userDom.get().getPassword());
        userRepository.save(user);
    }
}
