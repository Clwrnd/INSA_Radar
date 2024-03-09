/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package fr.insa.insaradar.EdtAnalyse;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import fr.insa.insaradar.MainActivity;
import fr.insa.insaradar.R;


/**
 * @author cidmo
 */
public class EdtAnalyse {
    public static Callable<Room[]> initializeFile2(Context context, boolean isInternetConnection) {
    return new Callable<Room[]>() {
        @Override
        public Room[] call() {
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String sourceUrl = "https://apps-int.insa-strasbourg.fr/ade/export.php?projectId=30&resources=5982,5987,5988,5989,5990,5992";

            File edt = null;
            Future<File> file = null;
            ExecutorService ex1 = Executors.newSingleThreadExecutor();

            if (!GeneralMethod.idAlreadyDl(context)&&isInternetConnection) {
                try {
                    Callable<File> cF = GeneralMethod.getSourceFile2(sourceUrl, context);
                    file = ex1.submit(cF);
                    edt = file.get();
                } catch (IOException | InterruptedException | ExecutionException ex) {
                }
            } else {
                edt = new File(context.getFilesDir(), "edt.ics");
            }

            if(file!=null){
                while (!file.isDone()) {
                }
                SingletonRoomObject.getInstance().setLastStamp(LocalDate.now().toString());
            }
            Room[] rooms = GenerateAllRoom.getAll();
            if (edt.exists()) {
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
            }else {
                SingletonRoomObject.getInstance().setLastStamp("None");
                return null;
            }
        }
    };
    }
}
