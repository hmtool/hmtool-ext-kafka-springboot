package tech.mhuang.ext.kafka.springboot.configuration;

import tech.mhuang.ext.kafka.admin.external.IKafkaConsumer;
import tech.mhuang.ext.kafka.admin.external.IKafkaExternal;
import tech.mhuang.ext.kafka.admin.external.IKafkaProducer;
import tech.mhuang.ext.kafka.consumer.process.DefaultKafkaConsumer;
import tech.mhuang.ext.kafka.producer.process.DefaultKafkaProducer;
import tech.mhuang.ext.spring.reflect.SpringReflectInvoke;
import tech.mhuang.ext.spring.start.SpringContextHolder;

/**
 * spring 实现kafka动态创建对应bean
 *
 * @author mhuang
 * @since 1.0.0
 */
public class SpringKafkaExternal implements IKafkaExternal {

    /**
     * 创建生产者
     *
     * @param key 产生的key
     * @return
     */
    @Override
    public IKafkaProducer createProducer(String key) {
        return SpringContextHolder.registerBean(key, DefaultKafkaProducer.class);
    }

    /**
     * 创建消费者
     *
     * @param key 产生的key
     * @return
     */
    @Override
    public IKafkaConsumer createConsumer(String key) {
        IKafkaConsumer kafkaConsumer = SpringContextHolder.registerBean(key, DefaultKafkaConsumer.class);
        kafkaConsumer.invoke(new SpringReflectInvoke());
        return kafkaConsumer;
    }
}
