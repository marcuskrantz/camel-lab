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

        final String rawBody = (String) exchange.getIn().getBody();
        log.debug("Raw body {}", rawBody);

        final ObjectMapper mapper = new ObjectMapper();

        final Map<String, Object> map = mapper.readValue(rawBody, Map.class);
        log.debug("got map");

        final String accessToken = (String) map.get("token");
        log.debug("Token: {}", accessToken);






    }
}
