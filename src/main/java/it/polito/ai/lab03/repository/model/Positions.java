package it.polito.ai.lab03.repository.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class Positions {

    @JsonProperty("positions")
    private List<Position> positions;

    public Positions(List<Position> positions) {
        this.positions = positions;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }
}