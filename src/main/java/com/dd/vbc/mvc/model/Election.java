package com.dd.vbc.mvc.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class Election extends Serialization implements Serializable {

    private static final long serialVersionUID = -3740370420901713205L;
    private Date electionDate;
    private String description;

    public Election() {}
    public Election(Date electionDate, String description) {
        this.electionDate = electionDate;
        this.description = description;
    }

    public Date getElectionDate() {
        return electionDate;
    }

    public String getDescription() {
        return description;
    }

    public byte[] serialize() {
        long longDate = electionDate.getTime();
        byte[] date = serializeLong(longDate);
        byte[] desc = serializeString(description);
        byte[] descLength = serializeInt(desc.length);
        byte[] bytes = concatenateBytes(date, descLength, desc);
        byte[] electionLength = serializeInt(bytes.length);
        return concatenateBytes(electionLength, bytes);
    }

    public void deserialize(byte[] bytes) {
        if(bytes!=null) {
            int electionLength = deserializeInt(Arrays.copyOfRange(bytes, 0, 4));
            electionDate = new Date(deserializeLong(Arrays.copyOfRange(bytes, 4, 12)));
            int descLength = deserializeInt(Arrays.copyOfRange(bytes, 12, 16));
            description = deserializeString(Arrays.copyOfRange(bytes, 16, descLength + 16));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Election election = (Election) o;
        return electionDate.equals(election.electionDate) &&
                description.equals(election.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(electionDate, description);
    }

    @Override
    public String toString() {
        return "Election{" +
                "electionDate=" + electionDate +
                ", description='" + description + '\'' +
                '}';
    }
}
