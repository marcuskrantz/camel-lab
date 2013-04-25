package com.mk.bean;

import com.mk.model.CitizenEntity;
import com.mk.services.model.TransferJob;
import com.mk.services.model.TransferParams;
import org.apache.camel.Exchange;
import org.apache.camel.component.websocket.WebsocketConstants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class CitizenLookupService {

    public List<TransferJob> findByAccessToken(final Exchange ex) {
        final Map<String, Object> map = (Map<String, Object>) ex.getIn().getBody();

        final String accessToken = (String) map.get("token");
        final String scope = (String) map.get("scope");

        final Map<String, Object> eng = (Map<String, Object>) map.get("engagement");
        final String timestamp = (String) eng.get("updateTime"); // Or "creationTime

        final TransferJob tj = new TransferJob();
        tj.setCrn(getTolvan().getCivicRegistrationNumber());
        tj.setTransferType(scope);
        tj.setStart(timestamp);
        tj.setEnd(timestamp);
        tj.setAccessToken(accessToken);

        return Collections.singletonList(tj);
    }

    public List<TransferJob> findByLoggedInCitizen(final Exchange exchange) {
        final TransferParams params = (TransferParams) exchange.getIn().getBody();
        final List<TransferJob> jobs = new ArrayList<TransferJob>();

        for (final String tt : params.getTransferTypes()) {
            final TransferJob job = new TransferJob();
            job.setStart(params.getStart());
            job.setEnd(params.getEnd());
            job.setTransferType(tt);
            job.setCrn(getTolvan().getCivicRegistrationNumber());

            jobs.add(job);
        }

        return jobs;

    }

    private CitizenEntity getTolvan() {
        final CitizenEntity cit = new CitizenEntity();
        cit.setCivicRegistrationNumber("191212121212");

        return cit;
    }
}
