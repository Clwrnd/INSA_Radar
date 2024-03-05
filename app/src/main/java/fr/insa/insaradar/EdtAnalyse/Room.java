
package fr.insa.insaradar.EdtAnalyse;

/**
 *
 * @author cidmo
 */

/* Room à récupérer:
    -Amphi
    -Bâtiment C
    -Bâtiment E    
*/  
public class Room {
    private final String id;
    private int nbEvent;
    private Event[] availability;
    private final String batiment;

    public Room(String id,String bat) {
        this.nbEvent=0;
        this.id = id;
        this.batiment=bat;
    }

    public void incrementNbEvent(){
        this.nbEvent++;
    }
    
    public void decrementNbEvent(){
        this.nbEvent--;
    }
    /**
     * @return the availability
     */
    public Event[] getAvailability() {
        return availability;
    }

    /**
     * @param availability the availability to set
     */
    public void setAvailability(Event[] availability) {
        this.availability = availability;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the nbEvent
     */
    public int getNbEvent() {
        return nbEvent;
    }

    /**
     * @param nbEvent the nbEvent to set
     */
    public void setNbEvent(int nbEvent) {
        this.nbEvent = nbEvent;
    }
    
    public void setAvaibilityLenght(){
        this.availability = new Event[this.nbEvent];
    }


    public String getBatiment() {
        return batiment;
    }

    public boolean isInBat(String bat){
        return this.getBatiment().equals(bat);
    }

}

