package it.polito.ai.lab03.repository.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "positions")
public class Position {

    @Id
    private String id;

    private long timestamp;
    private String userId;
    private String archiveId;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Position() {
        this.location = new GeoJsonPoint(0, 0);
    }

    public Position(GeoJsonPoint point, long timestamp) {
        this.timestamp = timestamp;
        this.location = point;
    }

    public Position(String userId, String archiveId, GeoJsonPoint location) {
        this.userId = userId;
        this.archiveId = archiveId;
        this.location = location;
    }

    public Position(String userId, String archiveId, long timestamp) {
        this.userId = userId;
        this.archiveId = archiveId;
        this.timestamp = timestamp;
    }

    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return location.getY();
    }

    public double getLongitude() {
        return location.getX();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public GeoJsonPoint getLocation() {
        return location;
    }

    public void setLocation(GeoJsonPoint location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return timestamp == position.timestamp &&
                Objects.equals(id, position.id) &&
                Objects.equals(userId, position.userId) &&
                Objects.equals(archiveId, position.archiveId) &&
                Objects.equals(location, position.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, userId, archiveId, location);
    }

    @Override
    public String toString() {
        return "Position{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", userId='" + userId + '\'' +
                ", archiveId='" + archiveId + '\'' +
                ", location=" + location +
                '}';
    }
}
