package com.dd.vbc.mvc.model;

import com.dd.vbc.enums.BallotItemType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class BallotBeanTest {

    @Test
    public void TestSerializeDeserialize() {

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
        byte[] bytes = presidentBallot.serialize();
        presidentBallot.deserialize(bytes);
        Assert.assertEquals(presidentBallot.getOfficeBeanList().get(0).getCandidates().get(1), pCandidates.get(1));

        BallotBean senateBallot = new BallotBean("SENATE", "Senate Races", Arrays.asList(senate1Office, senate2Office));
        bytes = senateBallot.serialize();
        senateBallot.deserialize(bytes);
        Assert.assertEquals(senateBallot.getOfficeBeanList().get(0).getCandidates().get(1), s1Candidates.get(1));

        BallotBean houseBallot = new BallotBean("HOUSE", "House Race", Arrays.asList(houseOffice));
        bytes = houseBallot.serialize();
        houseBallot.deserialize(bytes);
        Assert.assertEquals(houseBallot.getOfficeBeanList().get(0).getCandidates().get(0), hCandidates.get(0));



    }
}
