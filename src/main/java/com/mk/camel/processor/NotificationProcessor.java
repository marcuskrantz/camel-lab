package com.mk.camel.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class NotificationProcessor implements Processor {

    private static final Logger log = LoggerFactory.getLogger(NotificationProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        final Map<String, Object> rawBody = (Map<String, Object>) exchange.getIn().getBody();

        final String accessToken = (String) rawBody.get("token");
        log.debug("Token: {}", accessToken);
    }
}
