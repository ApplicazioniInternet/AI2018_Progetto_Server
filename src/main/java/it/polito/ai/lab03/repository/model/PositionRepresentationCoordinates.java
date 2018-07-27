package it.polito.ai.lab03.repository.model;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "positionsReprCoord")
public class PositionRepresentationCoordinates extends Position {

    private String positionId;

    public PositionRepresentationCoordinates(Position position) {
        super(
                position.getUserId(),
                position.getArchiveId(),
                new GeoJsonPoint(
                        (double) Math.round( position.getLongitude() * 100 ) / 100,
                        (double) Math.round( position.getLatitude() * 100 ) / 100
                )
        );
        this.positionId = position.getId();
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PositionRepresentationCoordinates)) return false;
        PositionRepresentationCoordinates that = (PositionRepresentationCoordinates) o;
        return Objects.equals(getLatitude(), that.getLatitude()) &&
                Objects.equals(getLongitude(), that.getLongitude());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), positionId);
    }
}
