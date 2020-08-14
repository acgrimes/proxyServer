package com.dd.vbc.service;

import com.dd.vbc.mvc.model.Voter;
import com.dd.vbc.network.ElectionResponse;

import java.io.File;
import java.nio.file.Files;

public class BallotRequestService {

    private static final String filePath = "src/main/resources/ballots/";
    private static final String fileName = "Ballot1.ballot";

    public ElectionResponse getBallotResponse(Voter voter) {

        File ballotFile = new File(filePath+fileName);
        byte[] bytes = null;
        try {
            String ballot = filePath+fileName;
            bytes = Files.readAllBytes(ballotFile.toPath());
        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
        ElectionResponse electionResponse = new ElectionResponse();
        electionResponse.deserialize(bytes);
        return electionResponse;
    }


}
