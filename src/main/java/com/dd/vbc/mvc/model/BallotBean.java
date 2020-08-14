package com.dd.vbc.mvc.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BallotBean extends Serialization implements Serializable {

    private static final long serialVersionUID = 8138630345352550141L;
    private String title;
    private String description;
    private List<OfficeBean> officeBeanList;

    public BallotBean() {}
    public BallotBean(String title, String description, List<OfficeBean> officeBeanList) {
        this.title = title;
        this.description = description;
        this.officeBeanList = officeBeanList;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<OfficeBean> getOfficeBeanList() {
        return officeBeanList;
    }

    public byte[] serialize() {

        byte[] titleBytes = serializeString(title);
        byte[] titleByteLength = serializeInt(titleBytes.length);
        byte[] descriptionBytes = serializeString(description);
        byte[] descriptionByteLength = serializeInt(descriptionBytes.length);
        List<byte[]> officeBeanListBytes = officeBeanList.stream().
                map(officeBean -> {
                    byte[] officeBeanBytes = officeBean.serialize();
                    byte[] officeBeanByteLength = serializeInt(officeBeanBytes.length);
                    byte[] byteLengthConcat = concatenateBytes(officeBeanByteLength, officeBeanBytes);
                    return byteLengthConcat;
                }).collect(Collectors.toList());
        byte[] officeBeanByteArray = concatenateBytes(officeBeanListBytes.toArray(new byte[officeBeanListBytes.size()][]));
        return concatenateBytes(titleByteLength, titleBytes, descriptionByteLength, descriptionBytes, officeBeanByteArray);

    }

    public void deserialize(byte[] bytes) {

        int index = 0;
        int titleLength = deserializeInt(Arrays.copyOfRange(bytes, index, index=index+4));
        title = deserializeString(Arrays.copyOfRange(bytes, index, index=index+titleLength));
        int descriptionLength = deserializeInt(Arrays.copyOfRange(bytes, index, index=index+4));
        description = deserializeString(Arrays.copyOfRange(bytes, index, index=index+descriptionLength));
        List<OfficeBean> officeBeanList = new ArrayList<>();
        while(index<bytes.length) {
            int officeBeanLength = deserializeInt(Arrays.copyOfRange(bytes, index, index=index+4));
            OfficeBean officeBean = new OfficeBean();
            officeBean.deserialize(Arrays.copyOfRange(bytes, index, index=index+officeBeanLength));
            officeBeanList.add(officeBean);
        }
        this.officeBeanList = officeBeanList;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BallotBean that = (BallotBean) o;

        return new EqualsBuilder()
                .append(title, that.title)
                .append(description, that.description)
                .append(officeBeanList, that.officeBeanList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(title)
                .append(description)
                .append(officeBeanList)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "BallotBean{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", officeBeanList=" + officeBeanList +
                '}';
    }
}
