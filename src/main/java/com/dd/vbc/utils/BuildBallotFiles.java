package com.dd.vbc.utils;

import com.dd.vbc.enums.BallotItemType;
import com.dd.vbc.enums.Response;
import com.dd.vbc.mvc.model.BallotBean;
import com.dd.vbc.mvc.model.OfficeBean;
import com.dd.vbc.network.ElectionResponse;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class BuildBallotFiles {

    private final static String filePath = "src/main/resources/ballots/";
    private final static String fileName = "Ballot1.ballot";

    private byte[] buildBallotBeanResponse() {

        String title = "General Election";
        String descriptiion = "This Election includes Presidential, Federal Senate and Federal House";
        List<String> pCandidates = Arrays.asList("Biden", "Trump");
        OfficeBean presidentOffice = new OfficeBean(BallotItemType.OFFICE, "President", "Presidential Election", pCandidates);
        List<String> s1Candidates = Arrays.asList("Ossof", "Purdue");
        OfficeBean senate1Office = new OfficeBean(BallotItemType.OFFICE, "Senate1", "Senate 1 Election", s1Candidates);
        List<String> s2Candidates = Arrays.asList("Liebermann", "Shaffer");
        OfficeBean senate2Office = new OfficeBean(BallotItemType.OFFICE, "Senate2", "Senate2 Election", s2Candidates);
        List<String> hCandidates = Arrays.asList("Johnson", "theOther");
        OfficeBean houseOffice = new OfficeBean(BallotItemType.OFFICE, "House", "House District 1 Election", hCandidates);

        BallotBean presidentBallot = new BallotBean("President", "Office of the President", Arrays.asList(presidentOffice));
        BallotBean senateBallot = new BallotBean("SENATE", "Senate Races", Arrays.asList(senate1Office, senate2Office));
        BallotBean houseBallot = new BallotBean("HOUSE", "House Race", Arrays.asList(houseOffice));
        List<BallotBean> ballotBeanList = Arrays.asList(presidentBallot, senateBallot, houseBallot);

        ElectionResponse electionResponse = new ElectionResponse(Response.Ballot, ballotBeanList);
        byte[] bytes = electionResponse.serialize();
        return bytes;
    }

    public void writeBallotToFile() {

            byte[] result = buildBallotBeanResponse();
            writeBytesToFile(result, filePath+fileName);

    }

    private void writeBytesToFile(byte[] bFile, String fileDest) {

        try {
            Files.write(Paths.get(fileDest), bFile, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] readBytesFromFile(String fileName) {
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(Paths.get(filePath+fileName));
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return bytes;
    }

    public ElectionResponse readBallotFromFile(String fileName) {

        ElectionResponse electionResponse = new ElectionResponse();
        try {
            byte[] byteStream = readBytesFromFile(fileName);
            ElectionResponse electionResponse1 = new ElectionResponse();
            electionResponse.deserialize(byteStream);
        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
        return electionResponse;
    }

    public static final void main(String[] args) {
        BuildBallotFiles files = new BuildBallotFiles();
        files.writeBallotToFile();
        ElectionResponse electionResponse = files.readBallotFromFile(fileName);
        System.out.println("Sending response type: "+electionResponse.getResponse().name());
        System.out.println("Office - Senate2 1st Candidate: "+electionResponse.getBallotBeanList().get(1).getOfficeBeanList().get(0));
    }
}
