package it.polito.ai.lab03.repository.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Document(collection = "archives")
public class Archive {

    @Id
    private String id;
    private String userId;
    private List<String> positionIds;

    public Archive(String userId, List<String> positionIds) {
        this.userId = userId;
        this.positionIds = positionIds;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getPositionIds() {
        return positionIds;
    }

    public void setPositionIds(List<String> positionIds) {
        this.positionIds = positionIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Archive)) return false;
        Archive archive = (Archive) o;
        return Objects.equals(id, archive.id) &&
                Objects.equals(userId, archive.userId) &&
                Objects.equals(positionIds, archive.positionIds);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId, positionIds);
    }
}
