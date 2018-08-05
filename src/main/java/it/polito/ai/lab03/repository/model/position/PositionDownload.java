package it.polito.ai.lab03.repository.model.position;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

public class PositionDownload {

    private String id;

    private long tstp;
    private double lat;
    private double lng;

    public PositionDownload(String id, GeoJsonPoint point, long timestamp) {
        this.id = id;
        this.lng = point.getX();
        this.lat = point.getY();
        this.tstp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTstp() {
        return tstp;
    }

    public void setTstp(long tstp) {
        this.tstp = tstp;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
