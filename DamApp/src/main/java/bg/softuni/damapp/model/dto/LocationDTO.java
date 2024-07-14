package bg.softuni.damapp.model.dto;

import jakarta.validation.constraints.NotBlank;

public class LocationDTO {
    @NotBlank
    private String name;

    public LocationDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
