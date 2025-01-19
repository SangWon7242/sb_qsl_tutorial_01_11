package com.qsl.qsl_tutorial.boundedContext.user.repository;

import com.qsl.qsl_tutorial.boundedContext.user.entity.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {
  SiteUser getQslUser(Long id);

  long getQslCount();

  SiteUser getQslUserOrderByIdAscOne();

  List<SiteUser> getQslUserOrderByIdAsc();

  List<SiteUser> searchQsl(String kw);

  Page<SiteUser> searchQsl(String kw, Pageable pageable);

  List<SiteUser> getQslUserByInterestKeyword(String keyword);
}
