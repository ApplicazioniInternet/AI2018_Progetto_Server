package it.polito.ai.lab03.repository.model;

import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

import java.util.List;
import java.util.Objects;

public class AreaRequest {
    private GeoJsonPolygon area;
    private long timestampBefore;
    private long timestampAfter;
    private List<String> userIds;

    public GeoJsonPolygon getArea() {
        return area;
    }

    public void setArea(GeoJsonPolygon area) {
        this.area = area;
    }

    public long getTimestampBefore() {
        return timestampBefore;
    }

    public void setTimestampBefore(long timestampBefore) {
        this.timestampBefore = timestampBefore;
    }

    public long getTimestampAfter() {
        return timestampAfter;
    }

    public void setTimestampAfter(long timestampAfter) {
        this.timestampAfter = timestampAfter;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AreaRequest)) return false;
        AreaRequest that = (AreaRequest) o;
        return timestampBefore == that.timestampBefore &&
                timestampAfter == that.timestampAfter &&
                Objects.equals(area, that.area) &&
                Objects.equals(userIds, that.userIds);
    }

    @Override
    public int hashCode() {

        return Objects.hash(area, timestampBefore, timestampAfter, userIds);
    }

    @Override
    public String toString() {
        return "AreaRequest{" +
                "area=" + area +
                ", timestampBefore=" + timestampBefore +
                ", timestampAfter=" + timestampAfter +
                ", userIds=" + userIds +
                '}';
    }
}
