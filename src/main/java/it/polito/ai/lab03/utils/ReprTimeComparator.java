package it.polito.ai.lab03.utils;

import it.polito.ai.lab03.repository.model.PositionRepresentationTimestamp;

import java.util.Comparator;

public class ReprTimeComparator
        implements Comparator<PositionRepresentationTimestamp> {

    public int compare (PositionRepresentationTimestamp a, PositionRepresentationTimestamp b){
        return (int) (a.getTimestamp() - b.getTimestamp());
    }
}
