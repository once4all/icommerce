package com.icommerce.service;

import com.icommerce.dom.UserInformation;

public interface IUserService {

    UserInformation findByUsername(String username);

    void updateUserWithoutCheckingPermission(UserInformation user);
}
