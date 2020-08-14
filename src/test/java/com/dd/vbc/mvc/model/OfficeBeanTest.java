package com.dd.vbc.mvc.model;

import com.dd.vbc.enums.BallotItemType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class OfficeBeanTest {

    @Test
    public void testOfficeBeanSerializeDeserialize() {

        String office = "President";
        String description = "Presidential Election";
        List<String> candidates = Arrays.asList("Biden", "Trump");
        OfficeBean presidentOffice = new OfficeBean(BallotItemType.OFFICE, office, description, candidates);
        byte[] presidentialBytes = presidentOffice.serialize();
        presidentOffice.deserialize(presidentialBytes);
        Assert.assertTrue(presidentOffice.getOffice().equals(office));
        Assert.assertEquals(presidentOffice.getCandidates().get(0), candidates.get(0));

        candidates = Arrays.asList("Ossof", "Purdue");
        OfficeBean senate1Office = new OfficeBean(BallotItemType.OFFICE, "Senate1", "Senate1 Election", candidates);
        byte[] senate1Bytes = senate1Office.serialize();
        senate1Office.deserialize(senate1Bytes);
        Assert.assertEquals(senate1Office.getCandidates().get(1), candidates.get(1));

        candidates = Arrays.asList("Liebermann", "Shaffer");
        OfficeBean senate2Office = new OfficeBean(BallotItemType.OFFICE, "Senate2", "Senate2 Election", candidates);
        candidates = Arrays.asList("Johnson", "theOther");
        OfficeBean houseOffice = new OfficeBean(BallotItemType.OFFICE, "House", "House District 1", candidates);

    }
}
