package com.mk.camel;

import com.mk.bean.CitizenLookupService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.websocket.WebsocketComponent;
import org.apache.camel.component.websocket.WebsocketConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Map;

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
        .to("seda:/eventqueue?multipleConsumers=true");

        WebsocketComponent comp = (WebsocketComponent) getContext().getComponent("websocket");
        comp.setPort(8081);

        from("seda:/eventqueue?multipleConsumers=true")
        .marshal()
        .json(JsonLibrary.Jackson)
        .setHeader(WebsocketConstants.CONNECTION_KEY, simple("apa"))
        .to("websocket:events");

        from("websocket:events").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                final Map<String,Object> headers = exchange.getIn().getHeaders();
                for (final String key: headers.keySet()) {
                    log.debug("HEADER {}: {}", key, headers.get(key));
                }
            }
        });
    }
}
