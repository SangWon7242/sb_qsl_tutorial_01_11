package com.qsl.qsl_tutorial.boundedContext.user.repository;

import com.qsl.qsl_tutorial.boundedContext.user.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
}
