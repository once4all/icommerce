package com.icommerce.service;

import com.icommerce.dom.UserInformation;
import com.icommerce.dom.UserRole;

public interface LoginService {

    UserInformation getUserDisplayName();

    UserInformation getCurrentLogin();

    UserRole getCurrentUserRoles();
}
