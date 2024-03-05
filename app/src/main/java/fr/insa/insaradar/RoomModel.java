package fr.insa.insaradar;

public class RoomModel {
    private String name, availability, description;
    boolean visible;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public RoomModel(String name) {
        this.name = name;
        this.visible = false;
    }

    public RoomModel(String name, String availability) {
        this.name = name;
        this.availability = "Disponible jusqu'à: "+availability;
        this.visible = false;
    }

    public void collapse() {
        this.visible = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
