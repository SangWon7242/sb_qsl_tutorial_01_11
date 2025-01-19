package com.qsl.qsl_tutorial.boundedContext.interestKeyword;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class InterestKeyword {
  @Id
  private String content;
}
