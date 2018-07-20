package it.polito.ai.lab03.repository.model;

import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

import java.util.Objects;

public class AreaRequest {
    private GeoJsonPolygon area;
    private long timestampBefore;
    private long timestampAfter;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AreaRequest)) return false;
        AreaRequest that = (AreaRequest) o;
        return getTimestampBefore() == that.getTimestampBefore() &&
                getTimestampAfter() == that.getTimestampAfter() &&
                Objects.equals(getArea(), that.getArea());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getArea(), getTimestampBefore(), getTimestampAfter());
    }

    @Override
    public String toString() {
        return "AreaRequest{" +
                "area=" + area +
                ", timestampBefore=" + timestampBefore +
                ", timestampAfter=" + timestampAfter +
                '}';
    }
}
