package com.mk.bean;

import com.mk.model.CitizenEntity;
import com.mk.services.model.TransferJob;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: marcus
 * Date: 2013-04-25
 * Time: 11:21
 * To change this template use File | Settings | File Templates.
 */
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

    public CitizenEntity findByLoggedInCitizen() {
        return getTolvan();
    }

    private CitizenEntity getTolvan() {
        final CitizenEntity cit = new CitizenEntity();
        cit.setCivicRegistrationNumber("191212121212");

        return cit;
    }
}
