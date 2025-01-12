package com.qsl.qsl_tutorial.boundedContext.user.repository;

import com.qsl.qsl_tutorial.boundedContext.user.entity.QSiteUser;
import com.qsl.qsl_tutorial.boundedContext.user.entity.SiteUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public SiteUser getQslUser(Long id) {
    /*
    SELECT *
    FROM site_user
    WHERE id = 1
    */

    QSiteUser siteUser = QSiteUser.siteUser;

    /*
    jpaQueryFactory
    .select(siteUser) // SELECT *
    .from(siteUser); // FROM site_user
    */

    return jpaQueryFactory
        .selectFrom(siteUser) // SELECT * FROM site_user
        .where(siteUser.id.eq(id)) // WHERE id = 1
        .fetchOne(); // 단일 결과를 반환
  }
}
