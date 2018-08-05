package it.polito.ai.lab03.repository.model.position;


import java.util.Objects;

public class PositionRepresentationCoordinates
        implements Comparable<PositionRepresentationCoordinates> {

    private String userId;
    private String archiveId;
    private double lng;
    private double lat;

    public PositionRepresentationCoordinates(Position position) {
        this.userId = position.getUserId();
        this.archiveId = position.getArchiveId();
        this.lng = (double) Math.round( position.getLongitude() * 100 ) / 100;
        this.lat = (double) Math.round( position.getLatitude() * 100 ) / 100;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PositionRepresentationCoordinates)) return false;
        PositionRepresentationCoordinates that = (PositionRepresentationCoordinates) o;
        return Double.compare(that.lng, lng) == 0 &&
                Double.compare(that.lat, lat) == 0 &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(archiveId, that.archiveId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, archiveId, lng, lat);
    }

    public int compareTo(PositionRepresentationCoordinates that){
        if ( !(Objects.equals(userId, that.userId)) ||
                !(Objects.equals(archiveId, that.archiveId)))
            return 1;

        if (this.getLng() == that.getLng()) {
            if (this.getLat() > that.getLat())
                return 1;
            else
                return -1;
        } else {
            if (this.getLng() > that.getLng())
                return 1;
            else
                return -1;
        }
    }
}
