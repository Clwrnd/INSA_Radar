/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package fr.insa.insaradar.EdtAnalyse;

import android.content.Context;

import java.io.File;
import java.time.LocalDate;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * @author cidmo
 * Summarizing the methods in order to get an Array with all the rooms,
 * with help of the Callable interface
 * to deal with bugs and exceptions.
 */

public class EdtAnalyse {
    public static Callable<Room[]> initializeFile2(Context context, boolean isInternetConnection) {
        return () -> {
            String sourceUrl = "https://apps-int.insa-strasbourg.fr/ade/export.php?projectId=30&resources=5982,5987,5988,5989,5990,5992";

            File edt;
            Future<File> file = null;
            ExecutorService ex1 = Executors.newSingleThreadExecutor();

            if (!GeneralMethod.idAlreadyDl(context) && isInternetConnection) {
                Callable<File> cF = GeneralMethod.getSourceFile2(sourceUrl, context);
                file = ex1.submit(cF);
                edt = file.get();
            } else {
                edt = new File(context.getFilesDir(), "edt.ics");
            }

            if (file != null) {
                while (!file.isDone()) {
                }
                SingletonRoomObject.getInstance().setLastStamp(LocalDate.now().toString());
            }
            Room[] rooms = GenerateAllRoom.getAll();
            if (edt.exists()) {
                GeneralMethod.countEachEvent(edt, rooms);
                GeneralMethod.assignEvent(edt, rooms);
                return rooms;
            } else {
                SingletonRoomObject.getInstance().setLastStamp("None");
                return null;
            }
        };
    }

    public static Callable<Room[]> refreshFile(Context context) {
        return () -> {
            String sourceUrl = "https://apps-int.insa-strasbourg.fr/ade/export.php?projectId=30&resources=5982,5987,5988,5989,5990,5992";

            ExecutorService ex1 = Executors.newSingleThreadExecutor();
            Callable<File> cF = GeneralMethod.getSourceFile2(sourceUrl, context);
            Future<File> file = ex1.submit(cF);
            File edt = file.get();

            while (!file.isDone()) {
            }
            SingletonRoomObject.getInstance().setLastStamp(LocalDate.now().toString());

            Room[] rooms = GenerateAllRoom.getAll();
            if (edt.exists()) {
                GeneralMethod.countEachEvent(edt, rooms);
                GeneralMethod.assignEvent(edt, rooms);
                return rooms;
            } else {
                SingletonRoomObject.getInstance().setLastStamp("None");
                return null;
            }
        };
    }


}
