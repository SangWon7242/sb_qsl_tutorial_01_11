package com.qsl.qsl_tutorial.boundedContext.user.repository;

import com.qsl.qsl_tutorial.boundedContext.user.entity.SiteUser;

import java.util.List;

public interface UserRepositoryCustom {
  SiteUser getQslUser(Long id);

  long getQslCount();

  SiteUser getQslUserOrderByIdAscOne();

  List<SiteUser> getQslUserOrderByIdAsc();
}
