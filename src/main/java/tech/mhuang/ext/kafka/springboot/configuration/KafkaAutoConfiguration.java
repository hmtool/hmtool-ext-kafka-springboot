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
import tech.mhuang.ext.springboot.context.ContextAutoConfiguration;

/**
 * kafka配置类
 *
 * @author mhuang
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(KafkaFramework.class)
@EnableConfigurationProperties(value = KafkaProperties.class)
@ConditionalOnProperty(prefix = "mhuang.kafka", name = "enable", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter({ContextAutoConfiguration.class})
@Slf4j
public class KafkaAutoConfiguration {

    private final KafkaProperties kafkaProperties;

    public KafkaAutoConfiguration(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }


    /**
     * create kafka external bean
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public IKafkaExternal kafkaExternal() {
        return new SpringKafkaExternal();
    }

    /**
     * create kafka framework
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public KafkaFramework KafkaFramework() {
        CheckAssert.check(this.kafkaProperties, "kafka properties invalid...");
        KafkaFramework framework = new KafkaFramework(this.kafkaProperties.getInfo());
        framework.executorService(this.kafkaProperties.getExecutor());
        framework.kafkaExternal(kafkaExternal());
        framework.start();
        return framework;
    }


}
