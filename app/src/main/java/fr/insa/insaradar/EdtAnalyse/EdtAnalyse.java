/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package fr.insa.insaradar.EdtAnalyse;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;


/**
 *
 * @author cidmo
 */
public class EdtAnalyse {
    
    public static Room[]  initializeFile(Context context) {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String finalDir = "edtFull.txt";
        String sourceUrl = "https://apps-int.insa-strasbourg.fr/ade/export.php?projectId=30&resources=5982,5987,5988,5989,5990,5992";
        File edt = null;
        try {
            edt = GeneralMethod.getSourceFile(sourceUrl, finalDir,context);
        } catch (IOException ex) {
            System.out.println("Erreur");
        }

        Room[] rooms = GenerateAllRoom.getAll();
        try {
            GeneralMethod.countEachEvent(edt, rooms);
            GeneralMethod.assignEvent(edt, rooms);
        } catch (UnsupportedEncodingException ex) {
            System.out.println("Erreur");
        } catch (IOException ex) {
            System.out.println("Erreur");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    return rooms;
    }
    
}
