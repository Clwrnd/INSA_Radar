/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.insaradar.EdtAnalyse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * @author cidmo
 * Model of an effective course
 */
public class Event {

    private final LocalDateTime startPoint;
    private final LocalDateTime endPoint;

    public Event(LocalDateTime startPoint, LocalDateTime endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    /**
     * @return the startPoint at the right Timeline
     */
    public LocalDateTime getStartPoint() {
        return LocalDateTime.ofInstant(startPoint.toInstant(ZoneOffset.UTC), ZoneId.of("Europe/Paris"));
    }

    /**
     * @return the endPoint at the right Timeline
     */
    public LocalDateTime getEndPoint() {
        return LocalDateTime.ofInstant(endPoint.toInstant(ZoneOffset.UTC), ZoneId.of("Europe/Paris"));
    }


}
