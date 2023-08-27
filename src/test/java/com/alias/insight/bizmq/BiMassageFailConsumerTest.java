package com.alias.insight.bizmq;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class BiMassageFailConsumerTest {

    @Resource
    private BiMassageFailConsumer biMassageFailConsumer;


    @Test
    void receiveMessage() {

    }
}