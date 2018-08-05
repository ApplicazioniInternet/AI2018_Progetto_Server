package it.polito.ai.lab03.repository.model.position;

import java.util.TreeSet;

public class PositionRepresentationDownload {

    public TreeSet<PositionRepresentationCoordinates> reprCoordList;
    public TreeSet<PositionRepresentationTimestamp> reprTimeList;

    public PositionRepresentationDownload(TreeSet<PositionRepresentationCoordinates> reprCoordList,
                                          TreeSet<PositionRepresentationTimestamp> reprTimeList) {
        this.reprCoordList = reprCoordList;
        this.reprTimeList = reprTimeList;
    }

    public TreeSet<PositionRepresentationCoordinates> getReprCoordList() {
        return reprCoordList;
    }

    public void setReprCoordList(TreeSet<PositionRepresentationCoordinates> reprCoordList) {
        this.reprCoordList = reprCoordList;
    }

    public TreeSet<PositionRepresentationTimestamp> getReprTimeList() {
        return reprTimeList;
    }

    public void setReprTimeList(TreeSet<PositionRepresentationTimestamp> reprTimeList) {
        this.reprTimeList = reprTimeList;
    }
}
