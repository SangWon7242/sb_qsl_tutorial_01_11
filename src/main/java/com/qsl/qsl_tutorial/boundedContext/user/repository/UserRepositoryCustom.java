package com.qsl.qsl_tutorial.boundedContext.user.repository;

import com.qsl.qsl_tutorial.boundedContext.user.entity.SiteUser;

public interface UserRepositoryCustom {
  SiteUser getQslUser(Long id);

  long getQslCount();
}
