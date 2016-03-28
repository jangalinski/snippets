package io.github.jangalinski.springboot.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExampleLastnameComponent {

  @Value("${example.customer.lastname}")
  private String lastname;

  public String getLastname() {
    return lastname;
  }
}
