package com.qsl.qsl_tutorial;

import com.qsl.qsl_tutorial.boundedContext.user.entity.SiteUser;
import com.qsl.qsl_tutorial.boundedContext.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;

@SpringBootTest
@Transactional // 각 테스트케이스에 전부 @Transactional을 붙인 것과 같은 효과
// @Test + @Transactional 조합은 자동으로 롤백을 유발시킨다.
@ActiveProfiles("test")
class QslTutorialApplicationTests {
	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("회원 생성")
	void t1() {
		SiteUser u3 = SiteUser.builder()
				.username("user3")
				.password("{noop}1234")
				.email("user3@test.com")
				.build();

		SiteUser u4 = SiteUser.builder()
				.username("user4")
				.password("{noop}1234")
				.email("user4@test.com")
				.build();

		userRepository.saveAll(Arrays.asList(u3, u4));
	}

	@Test
	@DisplayName("1번 회원을 Qsl로 가져오기")
	void t2() {
		// SELECT * FROM site_user WHERE id = 1;
		SiteUser u1 = userRepository.getQslUser(1L);

		assertThat(u1.getId()).isEqualTo(1L);
		assertThat(u1.getUsername()).isEqualTo("user1");
		assertThat(u1.getPassword()).isEqualTo("{noop}1234");
		assertThat(u1.getEmail()).isEqualTo("user1@test.com");
	}

	@Test
	@DisplayName("2번 회원을 Qsl로 가져오기")
	void t3() {
		// SELECT * FROM site_user WHERE id = 2;
		SiteUser u2 = userRepository.getQslUser(2L);

		assertThat(u2.getId()).isEqualTo(2L);
		assertThat(u2.getUsername()).isEqualTo("user2");
		assertThat(u2.getPassword()).isEqualTo("{noop}1234");
		assertThat(u2.getEmail()).isEqualTo("user2@test.com");
	}

	@Test
	@DisplayName("모든 회원 수")
	void t4() {
		long count = userRepository.getQslCount();
		
		assertThat(count).isGreaterThan(0); // 회원 수가 0보다 큰지 확인
	}

	@Test
	@DisplayName("가장 오래된 회원 1명")
	void t5() {
		SiteUser u1 = userRepository.getQslUserOrderByIdAscOne();

		assertThat(u1.getId()).isEqualTo(1L);
		assertThat(u1.getUsername()).isEqualTo("user1");
		assertThat(u1.getPassword()).isEqualTo("{noop}1234");
		assertThat(u1.getEmail()).isEqualTo("user1@test.com");
	}

	@Test
	@DisplayName("전체 회원, 오래된 순")
	void t6() {
		List<SiteUser> users = userRepository.getQslUserOrderByIdAsc();

		SiteUser u1 = users.get(0);

		assertThat(u1.getId()).isEqualTo(1L);
		assertThat(u1.getUsername()).isEqualTo("user1");
		assertThat(u1.getPassword()).isEqualTo("{noop}1234");
		assertThat(u1.getEmail()).isEqualTo("user1@test.com");

		SiteUser u2 = users.get(1);

		assertThat(u2.getId()).isEqualTo(2L);
		assertThat(u2.getUsername()).isEqualTo("user2");
		assertThat(u2.getPassword()).isEqualTo("{noop}1234");
		assertThat(u2.getEmail()).isEqualTo("user2@test.com");
	}

	@Test
	@DisplayName("검색, List 리턴, 검색 대상 : username, email")
	void t7() {
		// 검색 : username
		// username이 user1인 회원 검색
		List<SiteUser> users = userRepository.searchQsl("user1");
		assertThat(users.size()).isEqualTo(1);

		SiteUser u = users.get(0);

		assertThat(u.getId()).isEqualTo(1L);
		assertThat(u.getUsername()).isEqualTo("user1");
		assertThat(u.getPassword()).isEqualTo("{noop}1234");
		assertThat(u.getEmail()).isEqualTo("user1@test.com");

		// 검색 : username
		// username이 user2인 회원 검색
		users = userRepository.searchQsl("user2");
		assertThat(users.size()).isEqualTo(1);

		u = users.get(0);
		assertThat(u.getId()).isEqualTo(2L);
		assertThat(u.getUsername()).isEqualTo("user2");
		assertThat(u.getPassword()).isEqualTo("{noop}1234");
		assertThat(u.getEmail()).isEqualTo("user2@test.com");
	}

	@Test
	@DisplayName("검색, Page 리턴, id ASC, pageSize = 1, page = 0")
	void t8() {
		long totalCount = userRepository.count();
		int pageSize = 1; // 한 페이지에 보여줄 아이템 개수
		int totalPages = (int)Math.ceil(totalCount / (double)pageSize);
		int page = 1; // 현재 페이지 -> 2번 째 페이지를 의미
		String kw = "user";

		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.asc("id")); // id 기준 오름차순
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts)); // 한 페이지당 몇 개까지 보여질 것인가
		Page<SiteUser> usersPage = userRepository.searchQsl(kw, pageable);

		assertThat(usersPage.getTotalPages()).isEqualTo(totalPages);
		assertThat(usersPage.getNumber()).isEqualTo(page);
		assertThat(usersPage.getSize()).isEqualTo(pageSize);

		List<SiteUser> users = usersPage.get().toList();
		assertThat(users.size()).isEqualTo(pageSize);

		SiteUser u = users.get(0);
		assertThat(u.getId()).isEqualTo(2L);
		assertThat(u.getUsername()).isEqualTo("user2");
		assertThat(u.getPassword()).isEqualTo("{noop}1234");
		assertThat(u.getEmail()).isEqualTo("user2@test.com");


		// 검색어 : user1
		// 한 페이지에 나올 수 있는 아이템 개수 : 1개
		// 정렬 : id 정순
		// 내용 가져오는 쿼리
		/*
		SELECT *
		FROM site_user
		WHERE username LIKE '%user%'
		OR email LIKE '%user%'
		ORDER BY id ASC
		LIMIT 1, 1;
		*/

		// 쿼리가 두번 실행됨
		// 전체 개수를 계산하는 SQL
		/*
		SELECT COUNT(*)
		FROM site_user
		WHERE username LIKE '%user%'
		OR email LIKE '%user%'
		*/
	}

	@Test
	@DisplayName("검색, Page 리턴, id DESC, pageSize = 1, page = 0")
	void t9() {
		long totalCount = userRepository.count();
		int pageSize = 1; // 한 페이지에 보여줄 아이템 개수
		int totalPages = (int)Math.ceil(totalCount / (double)pageSize); // 전체 페이지
		int page = 1; // 현재 페이지 -> 2번 째 페이지를 의미
		String kw = "user";

		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("id")); // id 기준 내림차순
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts)); // 한 페이지당 몇 개까지 보여질 것인가
		Page<SiteUser> usersPage = userRepository.searchQsl(kw, pageable);

		assertThat(usersPage.getTotalPages()).isEqualTo(totalPages);
		assertThat(usersPage.getNumber()).isEqualTo(page);
		assertThat(usersPage.getSize()).isEqualTo(pageSize);

		List<SiteUser> users = usersPage.get().toList();
		assertThat(users.size()).isEqualTo(pageSize);

		// page 값이 1 == 2번째 페이지를 의미
		// 2번째 페이지는 1번 회원이 나와야 함
		SiteUser u = users.get(0);

		assertThat(u.getId()).isEqualTo(1L);
		assertThat(u.getUsername()).isEqualTo("user1");
		assertThat(u.getPassword()).isEqualTo("{noop}1234");
		assertThat(u.getEmail()).isEqualTo("user1@test.com");
	}

	@Test
	@DisplayName("회원에게 관심사를 등록할 수 있다.")
	@Rollback(false)
	void t10() {
		SiteUser u2 = userRepository.getQslUser(2L);
		u2.addInterestKeywordContent("테니스");
		u2.addInterestKeywordContent("오버워치");
		u2.addInterestKeywordContent("헬스");
		u2.addInterestKeywordContent("런닝");
		u2.addInterestKeywordContent("런닝");
		
		userRepository.save(u2);

		// 문제 힌트
		// 중복 등록은 무시
		// 엔티티 클래스 : InterestKeyword(interest_keyword 테이블)
		// 중간테이블도 생성되어야 함, 힌트 : @ManyToMany
		// interest_keyword 테이블에 테니스, 오버워치, 헬스, 런닝에 해당하는 row 생성
	}
}

