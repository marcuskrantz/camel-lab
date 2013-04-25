package com.mk.bean;

import com.mk.model.CitizenEntity;

/**
 * Created with IntelliJ IDEA.
 * User: marcus
 * Date: 2013-04-25
 * Time: 11:21
 * To change this template use File | Settings | File Templates.
 */
public class CitizenLookupService {

    public CitizenEntity findByAccessToken(final String accessToken) {
        return getTolvan();
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
