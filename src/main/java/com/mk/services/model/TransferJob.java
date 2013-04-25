package com.mk.services.model;

public class TransferJob {

    private String start;
    private String end;
    private String transferType;
    private String accessToken;

    private String crn;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return new StringBuilder("TransferJob: { ")
                .append("crn : ")
                .append(getCrn())
                .append(", start : ")
                .append(getStart())
                .append(", end : ")
                .append(getEnd())
                .append(", scope : ")
                .append(getTransferType())
                .append(" } ").toString();
    }
}
