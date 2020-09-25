package me.vukas.benchmarking.benchmarks.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

  @Value("${redisi.redis1.host}")
  private String host1;
  @Value("${redisi.redis1.port}")
  private int port1;
  @Value("${redisi.redis1.password}")
  private String password1;
  @Value("${redisi.redis1.database}")
  private int database1;

  @Value("${redisi.redis2.host}")
  private String host2;
  @Value("${redisi.redis2.port}")
  private int port2;
  @Value("${redisi.redis2.password}")
  private String password2;
  @Value("${redisi.redis2.database}")
  private int database2;

  @Bean
  RedisConnectionFactory redisCacheConnectionFactory1() {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host1, port1);
    configuration.setPassword(RedisPassword.of(password1));
    configuration.setDatabase(database1);

    LettuceConnectionFactory connectionFactory =
        new LettuceConnectionFactory(
            configuration, LettucePoolingClientConfiguration.defaultConfiguration());
    connectionFactory.setShareNativeConnection(true);
    return connectionFactory;
  }

  @Bean
  RedisConnectionFactory redisCacheConnectionFactory2() {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host2, port2);
    configuration.setPassword(RedisPassword.of(password2));
    configuration.setDatabase(database2);

    LettuceConnectionFactory connectionFactory =
        new LettuceConnectionFactory(
            configuration, LettucePoolingClientConfiguration.defaultConfiguration());
    connectionFactory.setShareNativeConnection(true);
    return connectionFactory;
  }

  @Bean
  public RedisTemplate<Integer, byte[]> byteTemplate1() {
    RedisTemplate<Integer, byte[]> byteTemplate = new RedisTemplate<>();
    byteTemplate.setConnectionFactory(redisCacheConnectionFactory1());
    byteTemplate.afterPropertiesSet();
    return byteTemplate;
  }

  @Bean
  public RedisTemplate<Integer, byte[]> byteTemplate2() {
    RedisTemplate<Integer, byte[]> byteTemplate = new RedisTemplate<>();
    byteTemplate.setConnectionFactory(redisCacheConnectionFactory2());
    byteTemplate.afterPropertiesSet();
    return byteTemplate;
  }

}
