package com.mk.camel;

import com.mk.bean.CitizenLookupService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

@Component
public class RouteBuilder extends SpringRouteBuilder {

    private static final Logger log = LoggerFactory.getLogger(RouteBuilder.class);

    public void configure() throws Exception {

        log.info("Building routes...");

        from("cxfrs://bean://manualServer?bindingStyle=SimpleConsumer")
        .log("Manual transfer request...")
        .wireTap("seda:/prepare-for-manual-transfer")
        .setBody(constant(Response.ok().build()));

        from("cxfrs://bean://notificationServer?bindingStyle=SimpleConsumer")
        .log("Notification request...")
        .wireTap("seda:/prepare-for-automatic-transfer")
        .setBody(constant(Response.ok().build()));

        from("seda:/prepare-for-manual-transfer")
        .bean(CitizenLookupService.class, "findByLoggedInCitizen")
        .to("log:com.mk.camel?level=DEBUG")
        .split(body())
        .to("seda:/jobqueue");

        from("seda:/prepare-for-automatic-transfer")
        .bean(CitizenLookupService.class, "findByAccessToken")
        .to("log:com.mk.camel?level=DEBUG")
        .split(body())
        .to("seda:/jobqueue");

        from("seda:/jobqueue")
        .to("seda:/eventqueue")
        .process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                log.debug("BODY: {}", exchange.getIn().getBody().getClass());
            }
        });



        from("seda:/eventqueue").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                log.debug("Event on event queue");
                exchange.getOut().setBody("Event queue: " + exchange.getIn().getBody());
            }
        }).marshal().json().to("websocket:/events");
    }
}
