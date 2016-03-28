package io.github.jangalinski.springboot.properties;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import static org.slf4j.LoggerFactory.getLogger;

@SpringBootApplication
@EnableConfigurationProperties(ExampleProperties.class)
public class SpringBootPropertiesExampleApplication implements CommandLineRunner {

  public static void main(String... args) {
    SpringApplication.run(SpringBootPropertiesExampleApplication.class, args);
  }

  private final Logger logger = getLogger(this.getClass());

  @Value("${example.customer.age}")
  private long age;

  @Value("${example.customer.duration:10}")
  private long duration;

  @Autowired
  private ExampleProperties properties;

  @Autowired
  private ExampleLastnameComponent component;

  @Override
  public void run(String... args) throws Exception {
    logger.info("example.customer.age=25: {}", age);
    logger.info("example.customer.firstname=Kermit: {}", properties.getCustomer().getFirstname());
    logger.info("example.customer.lastname=Frog: {}", component.getLastname());
    logger.info("example.customer.duration=10 (default): {}", duration);
  }
}
