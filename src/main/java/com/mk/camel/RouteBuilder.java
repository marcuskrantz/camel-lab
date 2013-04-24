package com.mk.camel;

import com.mk.camel.processor.ManualTransferProcessor;
import com.mk.camel.processor.NotificationProcessor;
import com.mk.services.ManualTransferService;
import com.mk.services.NotificationService;
import com.mk.services.model.TransferJob;
import com.mk.services.model.TransferParams;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class RouteBuilder extends SpringRouteBuilder {

    private static final Logger log = LoggerFactory.getLogger(RouteBuilder.class);

    @Override
    public void configure() throws Exception {

        log.info("Building routes...");

        /*
         * / = http://<server>:<port>/services/ since our CXF servlet is mapped onto /services/*
         */


        //from("cxfrs:/manual?resourceClasses=" + ManualTransferService.class.getName() + "&bindingStyle=SimpleConsumer")
        from("cxfrs://bean://manualServer?bindingStyle=SimpleConsumer")
        .log("Manual transfer request...")
        .wireTap("seda:/prepare-for-manual-transfer")
        .setBody(constant(Response.ok().build()));

        from("cxfrs://bean://notificationServer?bindingStyle=SimpleConsumer")
        .log("Notification request...")
        .wireTap("seda:/prepare-for-automatic-transfer")
        .setBody(constant(Response.ok().build()));

        from("seda:/prepare-for-manual-transfer")
        .process(new ManualTransferProcessor())
        .to("log:com.mk.camel?level=DEBUG")
        .split(body())
        .to("seda:/jobqueue");

        from("seda:/prepare-for-automatic-transfer")
        .convertBodyTo(String.class)
        .process(new NotificationProcessor())
        .to("log:com.mk.camel?level=DEBUG")
        .split(body())
        .to("seda:/jobqueue");

        from("seda:/jobqueue").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                log.debug("BODY: {}", exchange.getIn().getBody().getClass());
            }
        });

        from("seda:/eventqueue").to("websocket:/events");
    }
}
