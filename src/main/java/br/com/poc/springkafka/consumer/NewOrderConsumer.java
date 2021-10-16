package br.com.poc.springkafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NewOrderConsumer {

    @KafkaListener(topics = "${order.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumer(ConsumerRecord<String, String> consumerRecord) {
        log.info("key: " + consumerRecord.key());
        log.info("Headers: " + consumerRecord.headers());
        log.info("Partion: " + consumerRecord.partition());
        log.info("Offset: " + consumerRecord.offset());
        log.info("Topic: " + consumerRecord.topic());
        log.info("Payload Consumer: " + consumerRecord.value());

        // Commmit manual, que também será síncrono
//        ack.acknowledge();
    }
}
