package com.mk.services.model;

import java.util.Set;

public class TransferParams {
    private String start;
    private String end;
    private Set<String> transferTypes;

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

    public Set<String> getTransferTypes() {
        return transferTypes;
    }

    public void setTransferTypes(Set<String> transferTypes) {
        this.transferTypes = transferTypes;
    }

    @Override
    public String toString() {
        return "TransferParams{" +
                "start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", transferTypes=" + transferTypes +
                '}';
    }
}
