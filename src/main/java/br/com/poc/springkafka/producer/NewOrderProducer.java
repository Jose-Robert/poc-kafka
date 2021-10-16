package br.com.poc.springkafka.producer;

import java.util.UUID;

import org.apache.kafka.common.errors.ProducerFencedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NewOrderProducer {

	@Value("${order.topic}")
	private String newOrderTopic;

	private final ProducerFactory<?, ?> kafkaProducerFactory;

	private final KafkaTemplate<String, String> kafkaTemplate;

	public NewOrderProducer(final KafkaTemplate<String, String> kafkaTemplate,
			ProducerFactory<?, ?> kafkaProducerFactory) {
		this.kafkaTemplate = kafkaTemplate;
		this.kafkaProducerFactory = kafkaProducerFactory;
	}

	public void process(@Payload String newOrder) {
		try {
			String transactionIdPrefix = kafkaProducerFactory.getTransactionIdPrefix();
			if (transactionIdPrefix != null) {
				kafkaTemplate.executeInTransaction(
						kt -> kafkaTemplate.send(newOrderTopic, transactionIdPrefix, newOrder));
				log.info("Payload Producer: {}", newOrder);
			} else {
				String key = UUID.randomUUID().toString();
				kafkaTemplate.send(newOrderTopic, key, newOrder)
						.addCallback(success -> {
							if (success != null) {
								log.info("Payload Success: " + success.getProducerRecord().value());
							}
						}, failure -> log.info("Payload Failure: " + failure.getMessage()));
			}
		} catch (ProducerFencedException e) {
			throw new ProducerFencedException(e.getCause().toString());
		}

	}
}