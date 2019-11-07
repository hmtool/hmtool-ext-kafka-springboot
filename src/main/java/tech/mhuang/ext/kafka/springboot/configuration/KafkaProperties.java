package tech.mhuang.ext.kafka.springboot.configuration;

import tech.mhuang.core.pool.DefaultThreadPool;
import tech.mhuang.ext.kafka.admin.bean.KafkaInfo;
import tech.mhuang.ext.spring.pool.SpringThreadPool;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * kafka配置信息
 *
 * @author mhuang
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "mhuang.kafka")
@Data
public class KafkaProperties extends KafkaInfo{

    /**
     * open kafka property..
     * default is true
     */
    private boolean enable = true;
}
