/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.insaradar.EdtAnalyse;

/**
 * @author cidmo
 * Return the final form of what will be display on the UI.
 */
public class FreeRoom {
    private final Room freeRoom;
    private final Event nextCousr;

    public FreeRoom(Room freeRoom, Event nextCousr) {
        this.freeRoom = freeRoom;
        this.nextCousr = nextCousr;
    }

    /**
     * @return the freeRoom
     */
    public Room getFreeRoom() {
        return freeRoom;
    }

    /**
     * @return the nextCousr
     */
    public Event getNextCousr() {
        return nextCousr;
    }

}
