package it.polito.ai.lab03.repository.model.archive;

import it.polito.ai.lab03.repository.model.position.PositionDownload;
import org.springframework.data.annotation.Id;

import java.util.List;

public class ArchiveDownload {
    @Id
    private String id;
    private String userId;
    private List<PositionDownload> positions;

    public ArchiveDownload(String id, String userId, List<PositionDownload> positions) {
        this.id = id;
        this.userId = userId;
        this.positions = positions;
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

    public List<PositionDownload> getPositions() {
        return positions;
    }

    public void setPositions(List<PositionDownload> positions) {
        this.positions = positions;
    }
}
