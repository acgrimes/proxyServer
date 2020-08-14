package com.dd.vbc.mvc.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class Voter extends Serialization implements Serializable {

    private static final long serialVersionUID = 1706030007465952279L;
    private Long id;    // voter id, provided by voting agency
    private UUID vtoken;    // token defining this particular ballot
    private VotingDistrict districts;   // defines which ballot voter gets, this is only used by the server

    public Voter() {}
    public Voter(Long id, UUID vtoken, VotingDistrict districts) {
        this.id = id;
        this.vtoken = vtoken;
        this.districts = districts;
    }

    public Long getId() {
        return id;
    }

    public UUID getVtoken() {
        return vtoken;
    }

    public VotingDistrict getDistricts() {
        return districts;
    }

    public void setDistricts(VotingDistrict districts) {
        this.districts = districts;
    }

    public byte[] serialize() {
        byte[] longBytes = serializeLong(id);
        byte[] uuidBytes = serializeString(vtoken.toString());
        return concatenateBytes(longBytes, uuidBytes);
    }

    public void deserialize(byte[] bytes) {

        int index = 0;
        if(bytes!=null) {
            id = deserializeLong(Arrays.copyOfRange(bytes, index, index = index + 8));
            if (index < bytes.length) {
                vtoken = UUID.fromString(deserializeString(Arrays.copyOfRange(bytes, index, bytes.length)));
            }
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voter voter = (Voter) o;
        return id.equals(voter.id) &&
                vtoken.equals(voter.vtoken) &&
                districts.equals(voter.districts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vtoken, districts);
    }

    @Override
    public String toString() {
        return "Voter{" +
                "id='" + id + '\'' +
                ", vtoken=" + vtoken +
                ", districts=" + districts +
                '}';
    }
}
