package tech.mhuang.ext.kafka.springboot.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.mhuang.core.check.CheckAssert;
import tech.mhuang.ext.kafka.admin.KafkaFramework;
import tech.mhuang.ext.kafka.admin.external.IKafkaExternal;
import tech.mhuang.ext.spring.start.SpringContextHolder;
import tech.mhuang.ext.springboot.context.SpringBootExtAutoConfiguration;

/**
 * kafka配置类
 *
 * @author mhuang
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(KafkaFramework.class)
@EnableConfigurationProperties(value = {KafkaProperties.class,KafkaThreadPool.class})
@ConditionalOnProperty(prefix = "mhuang.kafka", name = "enable", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter({SpringBootExtAutoConfiguration.class})
@Slf4j
public class KafkaAutoConfiguration {

    private final KafkaProperties kafkaProperties;
    private final KafkaThreadPool kafkaThreadPool;

    public KafkaAutoConfiguration(KafkaProperties kafkaProperties,
                                  KafkaThreadPool kafkaThreadPool) {
        this.kafkaProperties = kafkaProperties;
        this.kafkaThreadPool = kafkaThreadPool;
    }

    @Bean
    @ConditionalOnMissingBean
    public IKafkaExternal springKafkaExternal(){
        return new SpringKafkaExternal();
    }

    @Bean
    @ConditionalOnMissingBean
    public KafkaFramework kafkaFramework(IKafkaExternal kafkaExternal,SpringContextHolder springContextHolder) {
        CheckAssert.check(springContextHolder, "SpringContextHolder不存在、请设置mhuang.holder.enable=true");
        CheckAssert.check(this.kafkaProperties, "KafkaProperties不存在、请设置mhuang.kafka.enable=true");
        KafkaFramework framework = new KafkaFramework(this.kafkaProperties);
        kafkaThreadPool.initialize();
        framework.executorService(this.kafkaThreadPool);
        framework.kafkaExternal(kafkaExternal);
        framework.start();
        return framework;
    }
}
