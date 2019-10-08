package tech.mhuang.ext.kafka.springboot.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import tech.mhuang.core.check.CheckAssert;
import tech.mhuang.core.util.CollectionUtil;
import tech.mhuang.ext.kafka.admin.KafkaFramework;
import tech.mhuang.ext.kafka.admin.external.IKafkaExternal;
import tech.mhuang.ext.spring.bean.BaseBeanRegisitryPostProcessor;
import tech.mhuang.ext.spring.start.SpringContextHolder;
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
        KafkaFramework framework = new KafkaFramework(this.kafkaProperties.getInfo());
        framework.executorService(this.kafkaProperties.getExecutor());
        framework.kafkaExternal(kafkaExternal);
        framework.start();
        return framework;
    }
}
