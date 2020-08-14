package com.dd.vbc.network;

import com.dd.vbc.enums.BallotItemType;
import com.dd.vbc.enums.Response;
import com.dd.vbc.mvc.model.BallotBean;
import com.dd.vbc.mvc.model.OfficeBean;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class ElectionResponseTest {

    @Test
    public void responseEnumOnlyTest() {

        ElectionResponse electionResponse = new ElectionResponse(Response.Authentication, null);
        byte[] byteArr = ElectionResponse.serialize(electionResponse);
        ElectionResponse result = ElectionResponse.deserialize(byteArr);

        System.out.println("Response Enum: "+result.getResponse().name());
        Assert.assertEquals(electionResponse, result);

    }

    @Test
    public void responseEnumAndBallotBeanListTest() {

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

        byte[] erByteArray = ElectionResponse.serialize(electionResponse);
        ElectionResponse result = ElectionResponse.deserialize(erByteArray);

        System.out.println("Office - Senate2 1st Candidate: "+result.getBallotBeanList().get(1).getOfficeBeanList().get(0));
        Assert.assertEquals(result, electionResponse);

    }
}
