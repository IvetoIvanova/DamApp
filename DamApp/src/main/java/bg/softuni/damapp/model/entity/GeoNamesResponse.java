package bg.softuni.damapp.model.entity;

import java.util.List;

public class GeoNamesResponse {
    private List<GeoName> geonames;

    public List<GeoName> getGeonames() {
        return geonames;
    }

    public void setGeonames(List<GeoName> geonames) {
        this.geonames = geonames;
    }
}
