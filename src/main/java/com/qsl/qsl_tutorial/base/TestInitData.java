package com.qsl.qsl_tutorial.base;

import com.qsl.qsl_tutorial.boundedContext.user.entity.SiteUser;
import com.qsl.qsl_tutorial.boundedContext.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


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

      userRepository.saveAll(Arrays.asList(u1, u2));

      u1.addInterestKeywordContent("야구");
      u1.addInterestKeywordContent("농구");

      u2.addInterestKeywordContent("등산");
      u2.addInterestKeywordContent("캠핑");
      u2.addInterestKeywordContent("야구");

      userRepository.saveAll(Arrays.asList(u1, u2));
    };
  }
}
