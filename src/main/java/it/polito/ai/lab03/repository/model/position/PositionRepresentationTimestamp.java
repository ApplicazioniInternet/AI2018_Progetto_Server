package it.polito.ai.lab03.repository.model.position;

import java.util.Objects;

public class PositionRepresentationTimestamp
        implements Comparable<PositionRepresentationTimestamp> {

    private String userId;
    private String archiveId;
    private long tstp;

    public PositionRepresentationTimestamp(Position position) {
        this.userId = position.getUserId();
        this.archiveId = position.getArchiveId();
        this.tstp = Math.round( position.getTimestamp() / 100 ) * 100;
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

    public long getTstp() {
        return tstp;
    }

    public void setTstp(long tstp) {
        this.tstp = tstp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PositionRepresentationTimestamp)) return false;
        PositionRepresentationTimestamp that = (PositionRepresentationTimestamp) o;
        return tstp == that.tstp &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(archiveId, that.archiveId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, archiveId, tstp);
    }

    public int compareTo (PositionRepresentationTimestamp that){
        if ( !(Objects.equals(userId, that.userId)) ||
                !(Objects.equals(archiveId, that.archiveId)))
            return 1;

        return (int) (this.getTstp() - that.getTstp());
    }
}
