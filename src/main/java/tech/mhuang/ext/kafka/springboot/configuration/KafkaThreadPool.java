package tech.mhuang.ext.kafka.springboot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import tech.mhuang.ext.spring.pool.SpringThreadPool;

/**
 * kafka 线程池
 *
 * @author mhuang
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "mhuang.kafka.pool")
public class KafkaThreadPool extends SpringThreadPool {

    private final String DEFAULT_NAME = "kafkaThreadPool";

    public KafkaThreadPool() {
        super();
        setBeanName(DEFAULT_NAME);
    }
}
