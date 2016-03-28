package io.github.jangalinski.springboot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("example")
public class ExampleProperties {

  private final Customer customer = new Customer();

  public Customer getCustomer() {
    return customer;
  }

  public class Customer {
    private String firstname;

    public String getFirstname() {
      return firstname;
    }

    public void setFirstname(String firstname) {
      this.firstname = firstname;
    }
  }
}
