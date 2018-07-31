package it.polito.ai.lab03.repository.model;

public class PositionRepresentationTimestamp {

    private String userId;
    private String archiveId;
    private long timestamp;

    public PositionRepresentationTimestamp(Position position) {
        this.userId = position.getUserId();
        this.archiveId = position.getArchiveId();
        this.timestamp = Math.round( position.getTimestamp() / 100 ) * 100;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
