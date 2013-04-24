package com.mk.camel;

import com.mk.services.RestService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RouteBuilder extends SpringRouteBuilder {

    private static final Logger log = LoggerFactory.getLogger(RouteBuilder.class);

    @Override
    public void configure() throws Exception {

        log.info("Building routes...");

        /*
         * / = http://<server>:<port>/services/ since our CXF servlet is mapped onto /services/*
         */
        from("cxfrs:///?resourceClasses=" + RestService.class.getName()).marshal().json(JsonLibrary.Jackson).process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {

                final Object o = exchange.getIn().getBody();

                log.debug("Now processing...");
                log.debug("Body is of type: {}", o.getClass());
            }
        });


    }
}
