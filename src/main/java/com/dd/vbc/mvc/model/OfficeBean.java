package com.dd.vbc.mvc.model;

import com.dd.vbc.enums.BallotItemType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OfficeBean extends Serialization implements Serializable {

    private static final long serialVersionUID = 1361547055992820763L;
    private BallotItemType ballotItemType;

    /*
    This parameter can represent an elected office, a question with a Yes/No answer or an amendment with a Yes/No
    approve/disapprove answer. The interpretation of this variable depends on the state of variable ballotItemType.
     */
    private String office;
    private String description;
    private List<String> candidates;

    public OfficeBean() {}
    public OfficeBean(BallotItemType ballotItemType, String office, String description, List<String> candidates) {
        this.ballotItemType = ballotItemType;
        this.office = office;
        this.description = description;
        this.candidates = candidates;
    }

    public BallotItemType getBallotItemType() {
        return ballotItemType;
    }

    public String getOffice() {
        return office;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getCandidates() {
        return candidates;
    }

    public byte[] serialize() {
        byte[] bitBytes = ballotItemType.serialize();
        byte[] officeBytes = serializeString(office);
        byte[] officeByteLength = serializeInt(officeBytes.length);
        byte[] descriptionBytes = serializeString(description);
        byte[] descriptionByteLength = serializeInt(descriptionBytes.length);
        List<byte[]> candidateListBytes = candidates.stream().
                map(candidate -> {
                    byte[] candidateBytes = serializeString(candidate);
                    byte[] candidateByteLength = serializeInt(candidateBytes.length);
                    byte[] byteLengthConcat = concatenateBytes(candidateByteLength, candidateBytes);
                    return byteLengthConcat;
                }).collect(Collectors.toList());
        byte[] candidateByteArray = concatenateBytes(candidateListBytes.toArray(new byte[candidateListBytes.size()][]));
        return concatenateBytes(bitBytes, officeByteLength, officeBytes, descriptionByteLength, descriptionBytes, candidateByteArray);
    }

    public void deserialize(byte[] bytes) {

        int index = 0;
        ballotItemType = BallotItemType.deserialize(Arrays.copyOfRange(bytes, 0, index=4));
        int officeLength = deserializeInt(Arrays.copyOfRange(bytes, index, index=index+4));
        office = deserializeString(Arrays.copyOfRange(bytes, index, index=index+officeLength));
        int descriptionLength = deserializeInt(Arrays.copyOfRange(bytes, index, index=index+4));
        description = deserializeString(Arrays.copyOfRange(bytes, index, index=index+descriptionLength));
        List<String> candidates = new ArrayList<>();
        while(index<bytes.length) {
            int candidateLength = deserializeInt(Arrays.copyOfRange(bytes, index, index=index+4));
            String candidate = deserializeString(Arrays.copyOfRange(bytes, index, index=index+candidateLength));
            candidates.add(candidate);
        }
        this.candidates = candidates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        OfficeBean that = (OfficeBean) o;

        return new EqualsBuilder()
                .append(ballotItemType, that.ballotItemType)
                .append(office, that.office)
                .append(description, that.description)
                .append(candidates, that.candidates)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(ballotItemType)
                .append(office)
                .append(description)
                .append(candidates)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "OfficeBean{" +
                "ballotItemType=" + ballotItemType +
                ", office='" + office + '\'' +
                ", description='" + description + '\'' +
                ", candidates=" + candidates +
                '}';
    }
}
