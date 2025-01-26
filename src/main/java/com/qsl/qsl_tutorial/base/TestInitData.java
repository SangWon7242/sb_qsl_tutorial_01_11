package com.qsl.qsl_tutorial.base;

import com.qsl.qsl_tutorial.boundedContext.user.entity.SiteUser;
import com.qsl.qsl_tutorial.boundedContext.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;


// @Profile({"dev", "test"}) // 개발환경과 테스트 환경에서만 실행되라!
// 이 클래스 안에 정의 된 Bean들은 test 모드에서만 실행
// Production(운영 환경, 라이브 서버) 이 아니다.
// TestInitData : 테스트 데이터를 초기화 할 때 사용한다.

@Configuration
@Profile("test")
public class TestInitData {
  // CommandLineRunner : 앱 실행 직후 초기 데이터 세팅 및 초기화
  @Bean
  CommandLineRunner init(UserRepository userRepository) {
    return args -> {
      SiteUser u1 = SiteUser.builder()
          .username("user1")
          .password("{noop}1234")
          .email("user1@test.com")
          .build();

      SiteUser u2 = SiteUser.builder()
          .username("user2")
          .password("{noop}1234")
          .email("user2@test.com")
          .build();

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

      SiteUser u5 = SiteUser.builder()
          .username("user5")
          .password("{noop}1234")
          .email("user5@test.com")
          .build();

      SiteUser u6 = SiteUser.builder()
          .username("user6")
          .password("{noop}1234")
          .email("user6@test.com")
          .build();

      SiteUser u7 = SiteUser.builder()
          .username("user7")
          .password("{noop}1234")
          .email("user7@test.com")
          .build();

      SiteUser u8 = SiteUser.builder()
          .username("user8")
          .password("{noop}1234")
          .email("user8@test.com")
          .build();

      u1.addInterestKeywordContent("야구");
      u1.addInterestKeywordContent("농구");

      u2.addInterestKeywordContent("등산");
      u2.addInterestKeywordContent("캠핑");
      u2.addInterestKeywordContent("야구");

      u3.addInterestKeywordContent("첼로");
      u4.addInterestKeywordContent("바이올린");
      u5.addInterestKeywordContent("베이스");
      u6.addInterestKeywordContent("일렉기타");
      u7.addInterestKeywordContent("트럼펫");
      u8.addInterestKeywordContent("댄스");

      userRepository.saveAll(Arrays.asList(u1, u2, u3, u4, u5, u6, u7, u8)); // PERSIST

      u8.follow(u7); // 8번 회원이 7번 회원을 구독(팔로잉) 한다. -- 7번 회원의 팔로워는 8번 회원이다.
      u8.follow(u6);
      u8.follow(u5);
      u8.follow(u4);
      u8.follow(u3);
      u7.follow(u6);
      u7.follow(u5);
      u7.follow(u4);
      u7.follow(u3);

      userRepository.saveAll(Arrays.asList(u1, u2, u3, u4, u5, u6, u7, u8)); // MERGE
    };
  }
}
