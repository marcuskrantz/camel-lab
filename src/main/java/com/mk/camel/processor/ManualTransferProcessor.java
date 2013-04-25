package com.mk.camel.processor;

import com.mk.services.model.TransferJob;
import com.mk.services.model.TransferParams;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.ArrayList;
import java.util.List;

public class ManualTransferProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        final TransferParams params = (TransferParams) exchange.getIn().getBody();


        final List<TransferJob> jobs = new ArrayList<TransferJob>();
        for (final String tt : params.getTransferTypes()) {
            final TransferJob job = new TransferJob();
            job.setStart(params.getStart());
            job.setEnd(params.getEnd());
            job.setTransferType(tt);
            job.setCrn("191212121212");

            jobs.add(job);
        }

        exchange.getOut().setBody(jobs);
    }
}
