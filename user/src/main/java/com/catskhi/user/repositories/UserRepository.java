package com.catskhi.user.repositories;

import com.catskhi.user.domain.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, String> {

}
