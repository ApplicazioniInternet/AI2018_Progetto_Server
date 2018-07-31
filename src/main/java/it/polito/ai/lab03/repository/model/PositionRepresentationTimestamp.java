package it.polito.ai.lab03.repository.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "positionsReprTime")
public class PositionRepresentationTimestamp extends Position {

    private String userId;
    private String archiveId;
    private long timestamp;

    public PositionRepresentationTimestamp(Position position) {
        this.userId = position.getUserId();
        this.archiveId = position.getArchiveId();
        this.timestamp = Math.round( position.getTimestamp() / 100 ) * 100;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getArchiveId() {
        return archiveId;
    }

    @Override
    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
