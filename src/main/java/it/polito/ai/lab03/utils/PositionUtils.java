package it.polito.ai.lab03.utils;

import it.polito.ai.lab03.repository.model.Position;

class PositionUtils {
    // Uso formula di haversine
    static double getDistanceBetween(Position lastPosition, Position postedPosition) {
        return Haversine.distance(
                lastPosition.getLatitude(),
                lastPosition.getLongitude(),
                postedPosition.getLatitude(),
                postedPosition.getLongitude()
        );
    }
}
