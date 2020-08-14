package com.dd.vbc.mvc.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class ElectionTransaction extends Serialization implements Serializable {

    private static final long serialVersionUID = -1464301026352768625L;
    private Voter voter;
    private Election election;
    private Ballot ballot;

    public ElectionTransaction() {}
    public ElectionTransaction(Voter voter, Election election, Ballot ballot) {
        this.voter = voter;
        this.election = election;
        this.ballot = ballot;
    }

    public byte[] serialize() {
        byte[] voterBytes = voter.serialize();
        byte[] electionBytes = election.serialize();
        byte[] ballotBytes = ballot.serialize();
        byte[] bytes = concatenateBytes(voterBytes, electionBytes, ballotBytes);
        byte[] etLength = serializeInt(bytes.length);
        return concatenateBytes(etLength, bytes);
    }

    public void deserialize(byte[] bytes) {

        int ind = 0;
        if(bytes!=null) {
            int etLength = deserializeInt(Arrays.copyOfRange(bytes, ind, ind = ind + 4));
            int voterLength = deserializeInt(Arrays.copyOfRange(bytes, ind, ind = ind + 4));
            Voter voter = new Voter();
            voter.deserialize(Arrays.copyOfRange(bytes, ind, ind = ind + voterLength));
            int electionLength = deserializeInt(Arrays.copyOfRange(bytes, ind, ind = ind + 4));
            Election election = new Election();
            election.deserialize(Arrays.copyOfRange(bytes, ind, ind = ind + electionLength));
            int ballotLength = deserializeInt(Arrays.copyOfRange(bytes, ind, ind = ind + 4));
            Ballot ballot = new Ballot();
            ballot.deserialize(Arrays.copyOfRange(bytes, ind, ind + ballotLength));
        }
    }

    public Voter getVoter() {
        return voter;
    }

    public Election getElection() {
        return election;
    }

    public Ballot getBallot() {
        return ballot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectionTransaction that = (ElectionTransaction) o;
        return voter.equals(that.voter) &&
                election.equals(that.election) &&
                ballot.equals(that.ballot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voter, election, ballot);
    }

    @Override
    public String toString() {
        return "ElectionTransaction{" +
                "voter=" + voter +
                ", election=" + election +
                ", ballot=" + ballot +
                '}';
    }
}
