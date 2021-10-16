package br.com.poc.springkafka.rest;

import br.com.poc.springkafka.producer.NewOrderProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/orders")
public class NewOrderController {

    @Autowired
    private NewOrderProducer newOrderProducer;

    @PostMapping
    public String send(@RequestBody String newOrder) {
        newOrderProducer.process(newOrder);
        return "Payload Send...";
    }
}
