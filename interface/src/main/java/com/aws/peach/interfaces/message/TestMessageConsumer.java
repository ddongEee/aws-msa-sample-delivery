package com.aws.peach.interfaces.message;

import com.aws.peach.domain.support.Message;
import com.aws.peach.domain.support.MessageConsumer;
import com.aws.peach.domain.test.TestMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestMessageConsumer implements MessageConsumer<TestMessage> {

    @Override
    public void consume(Message<TestMessage> message) {
        log.info("### Received message id: {}, key: {}, payload: {}",
                message.getMessageId().getValue(), message.getMessageKey(), message.getPayload());
    }
}
