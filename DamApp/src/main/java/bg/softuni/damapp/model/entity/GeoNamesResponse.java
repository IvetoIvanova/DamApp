package bg.softuni.damapp.model.entity;

import java.util.ArrayList;
import java.util.List;

public class GeoNamesResponse {
    private List<GeoName> geonames;

    public List<GeoName> getGeonames() {
//        return geonames;
        return geonames != null ? geonames : new ArrayList<>();
    }

    public void setGeonames(List<GeoName> geonames) {
        this.geonames = geonames;
    }
}
