package it.polito.ai.lab03.repository.model.archive;

import java.util.List;

public class ArchiveLightList {

    private List<ArchiveLight> archiveList;

    public ArchiveLightList() {
        super();
    }

    public ArchiveLightList(List<ArchiveLight> archiveList) {
        this.archiveList = archiveList;
    }

    public List<ArchiveLight> getArchiveList() {
        return archiveList;
    }

    public void setArchiveList(List<ArchiveLight> archiveList) {
        this.archiveList = archiveList;
    }
}
