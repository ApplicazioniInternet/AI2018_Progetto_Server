package it.polito.ai.lab03.utils;

import it.polito.ai.lab03.repository.model.PositionRepresentationCoordinates;

import java.util.Comparator;

public class ReprCoordComparator
        implements Comparator<PositionRepresentationCoordinates> {

    // compara per longitudine. se uguale per latitudine
    public int compare (PositionRepresentationCoordinates a, PositionRepresentationCoordinates b){
        if (a.getLongitude() == b.getLongitude()) {
            if (a.getLatitude() > b.getLatitude())
                return 1;
            else
                return -1;
        } else {
            if (a.getLongitude() > b.getLongitude())
                return 1;
            else
                return -1;
        }
    }

}
