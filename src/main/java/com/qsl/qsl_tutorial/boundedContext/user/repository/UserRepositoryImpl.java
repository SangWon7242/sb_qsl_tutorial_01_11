package com.qsl.qsl_tutorial.boundedContext.user.repository;

import com.qsl.qsl_tutorial.boundedContext.user.entity.SiteUser;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.qsl.qsl_tutorial.boundedContext.user.entity.QSiteUser.siteUser;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public SiteUser getQslUser(Long id) {
    /*
    SELECT *
    FROM site_user
    WHERE id = 1;
    */

    return jpaQueryFactory
        .selectFrom(siteUser) // SELECT * FROM site_user
        .where(siteUser.id.eq(id)) // WHERE id = 1
        .fetchOne(); // 단일 결과를 반환
  }

  @Override
  public long getQslCount() {
    // SELECT COUNT(*) FROM site_user
    return jpaQueryFactory
        .select(siteUser.count())
        .from(siteUser)
        .fetchOne();
  }

  @Override
  public SiteUser getQslUserOrderByIdAscOne() {
    /*
    SELECT *
    FROM site_user
    ORDER BY id ASC
    LIMIT 1;
    */

    return jpaQueryFactory
        .selectFrom(siteUser) // SELECT * FROM site_user
        .orderBy(siteUser.id.asc()) // ORDER BY id ASC
        .limit(1) // LIMIT 1
        .fetchOne(); // 단일 결과 하나를 반영
  }

  @Override
  public List<SiteUser> getQslUserOrderByIdAsc() {
    return jpaQueryFactory
        .selectFrom(siteUser) // SELECT * FROM site_user
        .orderBy(siteUser.id.asc()) // ORDER BY id ASC
        .fetch();
  }

  @Override
  public List<SiteUser> searchQsl(String kw) {
    return jpaQueryFactory
        .selectFrom(siteUser) // SELECT * FROM site_user
        .where(
            siteUser.username.contains(kw)
                .or(siteUser.email.contains(kw))
        ) // WHERE username = 'kw' // WHERE email = 'kw'
        .fetch();
  }

  @Override
  public Page<SiteUser> searchQsl(String kw, Pageable pageable) {
    // BooleanExpression : 검색 조건을 처리하는 객체
    // 검색 조건 (예: username, email 필드에서 keyword 포함 여부)
    // containsIgnoreCase : 대소문자를 구분하지 않는 검색을 수행
    BooleanExpression predicate = siteUser.username.containsIgnoreCase(kw)
        .or(siteUser.email.containsIgnoreCase(kw));

    // QueryDSL로 데이터 조회
    // QueryResults : 쿼리 실행 결과와 함께 페이징을 위한 추가 정보 포함
    QueryResults<SiteUser> queryResults = jpaQueryFactory
        .selectFrom(siteUser) // SELECT * FROM site_user
        .where(predicate) // WHERE username LIKE '%user%' OR email LIKE '%user%'
        .orderBy(siteUser.id.asc()) // ORDER BY id ASC
        .offset(pageable.getOffset()) // LIMIT {1}, ? // 페이지 시작 위치
        .limit(pageable.getPageSize()) // LIMIT ?, {1} // 페이지 크기
        .fetchResults(); // 데이터와 총 데이터 수를 가져옴

    // 결과와 totalCount를 기반으로 Page 객체 생성
    List<SiteUser> users = queryResults.getResults();
    long total = queryResults.getTotal(); // 총 데이터 수
    
    // PageImpl : 페이징 된 데이터와 메타데이터(전체 개수, 페이지 정보 등)을 포함
    return new PageImpl<>(users, pageable, total);
  }
}
