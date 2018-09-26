package com.ishchenko.artem.tools;

import java.util.List;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class LeafSpaceContainer {
    private String spaceName;
    private List<String> leafsName;

    public LeafSpaceContainer(String spaceName, List<String> leafsName) {
        this.spaceName = spaceName;
        this.leafsName = leafsName;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public List<String> getLeafsName() {
        return leafsName;
    }

    public void setLeafsName(List<String> leafsName) {
        this.leafsName = leafsName;
    }
}