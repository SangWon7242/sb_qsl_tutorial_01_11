package com.qsl.qsl_tutorial.boundedContext.user.entity;

import com.qsl.qsl_tutorial.boundedContext.interestKeyword.entity.InterestKeyword;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SiteUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String username;

  private String password;

  @Column(unique = true)
  private String email;

  @Builder.Default
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
  private Set<InterestKeyword> interestKeywords = new HashSet<>();

  @Builder.Default
  @ManyToMany(cascade = CascadeType.ALL)
  private Set<SiteUser> followers = new HashSet<>();

  @Builder.Default
  @ManyToMany(cascade = CascadeType.ALL)
  private Set<SiteUser> followings = new HashSet<>();

  public void addInterestKeywordContent(String keywordContent) {
    interestKeywords.add(new InterestKeyword(this, keywordContent));
  }

  public void removeInterestKeywordContent(String keywordContent) {
    interestKeywords.remove(new InterestKeyword(this, keywordContent));
  }

  public void follow(SiteUser following) {
    if(this == following) return;
    if(following == null) return;
    if(this.getId() == following.getId()) return;

    // 유튜버(following)이 나(follower)를 구독자로 등록
    following.getFollowers().add(this);

    // 내(follower)가 유트버(following) 구독한다.
    getFollowings().add(following);
  }
}
