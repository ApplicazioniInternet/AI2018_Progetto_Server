package it.polito.ai.lab03.repository.model.archive;

import org.springframework.data.annotation.Transient;

public class ArchiveLight {
    private String archiveId;
    @Transient private boolean bought;

    public ArchiveLight() {
        super();
    }

    public ArchiveLight(String archiveId) {
        this.archiveId = archiveId;
    }

    public ArchiveLight(String archiveId, boolean bought) {
        this.archiveId = archiveId;
        this.bought = bought;
    }

    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }
}
