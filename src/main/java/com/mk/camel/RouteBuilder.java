package com.mk.camel;

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


        from("cxfrs:/manual?resourceClasses=" + ManualTransferService.class.getName() + "&bindingStyle=SimpleConsumer")
        .wireTap("seda:/test")
        .process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getOut().setBody(Response.status(202).build());
            }
        });

        from("seda:/test").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                final Object start = exchange.getIn().getHeader("start");
                final Object end = exchange.getIn().getHeader("end");

                final Set<String> transfertypes = (Set<String>) exchange.getIn().getHeader("transfertypes");

                final List<TransferJob> params = new ArrayList<TransferJob>();
                for (final String tt : transfertypes) {
                    final TransferJob job = new TransferJob();
                    job.setStart(start.toString());
                    job.setEnd(end.toString());
                    job.setTransferType(tt);
                    job.setCrn("191212121212");

                    params.add(job);
                }

                exchange.getOut().setBody(params);
            }
        })
        .to("log:com.mk.camel?level=DEBUG")
        .split(body()).to("seda:/jobqueue");


        from("seda:/jobqueue").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                log.debug("BODY: {}", exchange.getIn().getBody().getClass());
            }
        });
    }
}
