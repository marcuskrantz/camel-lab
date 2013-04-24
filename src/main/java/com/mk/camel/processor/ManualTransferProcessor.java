package com.mk.camel.processor;

import com.mk.services.model.TransferJob;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.ArrayList;
import java.util.List;

public class ManualTransferProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        final Object start = exchange.getIn().getHeader("start");
        final Object end = exchange.getIn().getHeader("end");

        final String transfertypes = (String) exchange.getIn().getHeader("transfertypes");
        final String[] transfers = transfertypes.split(",");

        final List<TransferJob> params = new ArrayList<TransferJob>();
        for (final String tt : transfers) {
            final TransferJob job = new TransferJob();
            job.setStart(start.toString());
            job.setEnd(end.toString());
            job.setTransferType(tt);
            job.setCrn("191212121212");

            params.add(job);
        }

        exchange.getOut().setBody(params);
    }
}
