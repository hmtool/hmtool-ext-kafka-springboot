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
public class KafkaProperties {

    /**
     * default threadpool queue size
     */
    private final static Integer DEFAULT_QUEUE_CAPACITY = 20;

    /**
     * default threadpool keepalive secord
     */
    private final static Integer DEFAULT_KEEPALIVE_SECOND = 200;

    /**
     * open kafka property..
     * default is true
     */
    private boolean enable = true;

    /**
     * thread pool use consumer...
     */
    private SpringThreadPool executor = new SpringThreadPool();

    /**
     * kafka common property
     */
    private KafkaInfo info = new KafkaInfo();

    public KafkaProperties(){
        executor.setBeanName("kafkaThreadPool");
        executor.setCorePoolSize(DefaultThreadPool.DEFAULT_CORE_POOL_SIZE);
        executor.setMaxPoolSize(DefaultThreadPool.DEFAULT_MAX_POOL_SIZE);
        executor.setQueueCapacity(DEFAULT_QUEUE_CAPACITY);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setKeepAliveSeconds(DEFAULT_KEEPALIVE_SECOND);
        executor.initialize();
    }
}
