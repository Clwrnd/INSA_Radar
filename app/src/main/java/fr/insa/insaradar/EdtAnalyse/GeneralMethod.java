/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.insaradar.EdtAnalyse;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author cidmo
 */
public class GeneralMethod {
    public static File getSourceFile(String sourceUrl, String finalDir, Context context) throws IOException {
        new Thread(new Runnable() {
            public void run() {
                try (BufferedInputStream bis = new BufferedInputStream(new URL(sourceUrl).openStream()); FileOutputStream outFile = context.openFileOutput("edt.ics",Context.MODE_PRIVATE)) {
                    byte data[] = new byte[1024];
                    int byteContent;
                    while ((byteContent = bis.read(data, 0, 1024)) != -1) {
                        outFile.write(data, 0, byteContent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        File file = new File (context.getFilesDir(),"edt.ics");
        return file;
    }

    public static String to_BASIC_ISO_DATE_TIME(String raw) {
        String replace = raw.replace("T", "");
        String replace1 = replace.replace("Z", "");
        return replace1;
    }

    public static void countEachEvent(File toCount, Room... rooms) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        try (BufferedReader lire = new BufferedReader(new InputStreamReader(new FileInputStream(toCount), "UTF-8"))) {
            if (toCount != null) {
                String ligne = lire.readLine();
                while (ligne != null) {
                    if (ligne.equals("BEGIN:VEVENT")) {
                        ligne = lire.readLine();
                        while (!ligne.equals("END:VEVENT")) {
                            ligne = lire.readLine();
                            String[] beacon = ligne.split(":");
                            if (beacon[0].equals("LOCATION")) {
                                String ligneBreakCase = lire.readLine();
                                while (ligneBreakCase.startsWith(" ")) {
                                    beacon[1] = beacon[1] + ligneBreakCase.substring(1);
                                    ligneBreakCase = lire.readLine();
                                }
                                String[] multipleRoomCase = beacon[1].split(",");
                                for (Room room : rooms) {
                                    for (String rString : multipleRoomCase) {
                                        if (rString.equals(room.getId()) || rString.equals(room.getId() + "\\")) {
                                            room.incrementNbEvent();
                                        }
                                    }

                                }
                            }

                        }
                    }

                    ligne = lire.readLine();
                }
                setAvaibilitysLenght(rooms);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void assignEvent(File edt, Room... rooms) throws Exception {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        try (BufferedReader lire = new BufferedReader(new InputStreamReader(new FileInputStream(edt), "UTF-8"))) {
            if (edt != null) {
                String ligne = lire.readLine();
                while (ligne != null) {
                    if (ligne.equals("BEGIN:VEVENT")) {
                        LocalDateTime begining = null;
                        LocalDateTime ending = null;
                        ligne = lire.readLine();
                        while (!ligne.equals("END:VEVENT")) {
                            ligne = lire.readLine();
                            String[] beacon0 = ligne.split(":");
                            if (beacon0[0].equals("DTSTART")) {
                                begining = LocalDateTime.parse(to_BASIC_ISO_DATE_TIME(beacon0[1]), myFormatObj);
                            }
                            if (beacon0[0].equals("DTEND")) {
                                ending = LocalDateTime.parse(to_BASIC_ISO_DATE_TIME(beacon0[1]), myFormatObj);
                            }
                            if (beacon0[0].equals("LOCATION")) {
                                String ligneBreakCase = lire.readLine();
                                while (ligneBreakCase.startsWith(" ")) {
                                    beacon0[1] = beacon0[1] + ligneBreakCase.substring(1);
                                    ligneBreakCase = lire.readLine();
                                }
                                String[] multipleRoomCase = beacon0[1].split(",");
                                for (Room room : rooms) {
                                    for (String rString : multipleRoomCase) {
                                        if (rString.equals(room.getId()) || rString.equals(room.getId() + "\\")) {
                                            room.getAvailability()[room.getNbEvent() - 1] = new Event(begining, ending);
                                            room.decrementNbEvent();
                                            break;
                                        }
                                    }

                                }
                            }

                        }
                    }

                    ligne = lire.readLine();
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void setAvaibilitysLenght(Room... rooms) {
        for (Room room : rooms) {
            room.setAvaibilityLenght();
        }
    }

    public static Room[] isAvailableAt(LocalDateTime toDetermine, Room[] rooms) {
        List<Room> availableRooms = new ArrayList<>(Arrays.asList(rooms));
        for (Room room : rooms) {
            for (Event ev : room.getAvailability()) {
                if (toDetermine.isAfter(ev.getStartPoint()) && toDetermine.isBefore(ev.getEndPoint())) {
                    availableRooms.remove(room);
                    break;
                }
            }
        }
        Room[] availableRoomsToArray = new Room[availableRooms.size()];
        availableRooms.toArray(availableRoomsToArray);
        return availableRoomsToArray;

    }

    public static ArrayList<Event> inDay(LocalDateTime dateTime, Room room) {
        ArrayList<Event> toReturn = new ArrayList<>();
        LocalDate date = dateTime.toLocalDate();
        for (Event ev : room.getAvailability()) {
            if (ev.getStartPoint().toLocalDate().equals(date) && ev.getStartPoint().isAfter(dateTime)) {
                toReturn.add(ev);
            }
        }
        return toReturn;
    }

    public static FreeRoom[] nextCourseInDay(LocalDateTime date, Room... rooms) {
        FreeRoom[] fr = new FreeRoom[rooms.length];
        int i = 0;
        for (Room room : rooms) {
            ArrayList<Event> inday = inDay(date, room);
            if (!inday.isEmpty()) {
                Event min = inday.get(0);
                for (Event ev : inday) {
                    if (ChronoUnit.SECONDS.between(date, ev.getStartPoint()) < ChronoUnit.SECONDS.between(date, min.getStartPoint())) {
                        min = ev;
                    }
                }
                fr[i] = new FreeRoom(room, min);
                i++;
            } else{
                fr[i] = new FreeRoom(room,null);
                i++;
            }

        }
        return fr;
    }

}
