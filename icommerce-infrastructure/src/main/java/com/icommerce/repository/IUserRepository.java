package com.icommerce.repository;

import com.icommerce.dom.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<UserInformation, Long> {
    UserInformation findUserByUsername(String username);
}
