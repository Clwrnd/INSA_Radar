/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.insaradar.EdtAnalyse;

import fr.insa.insaradar.RoomModel;

/**
 *
 * @author cidmo
 */
public class GenerateAllRoom {

    public static Room[] getAll() {
        Room[] rooms = new Room[45];
        // Amphi:
        Room AetI = new Room("Amphi A&I E0.19 (277) VP+Visio", "amphi");
        Room arp = new Room("Amphi ARP E1.01 (112) VP+Visio", "amphi");
        Room chimie = new Room("Amphi Chimie L2.20 (112) VP", "amphi");
        Room elec = new Room("Amphi Elec L0.28 (106) VP", "amphi");
        Room phys = new Room("Amphi Physique L1.38 (106) VP", "amphi");
        Room vinci = new Room("Amphi VINCI E0.21 (96) VP", "amphi");
        Room dediet = new Room("Amphi de Dietrich L0.04 (188) VP", "amphi");
        rooms[0] = AetI;
        rooms[1] = arp;
        rooms[2] = chimie;
        rooms[3] = elec;
        rooms[4] = phys;
        rooms[5] = vinci;
        rooms[6] = dediet;

        // Bâtiment C
        Room c110 = new Room("C1.10 (44) VP", "C");
        Room c111 = new Room("C1.11 (36) VP", "C");
        Room c115 = new Room("C1.15 (41) VP", "C");
        Room c116 = new Room("C1.16 (42) VP", "C");
        Room c118 = new Room("C1.18 LIG VP (14)", "C");
        Room c119 = new Room("C1.19 LIG VP (14)", "C");
        Room c124 = new Room("C1.24 (42) VP", "C");
        rooms[7] = c110;
        rooms[8] = c111;
        rooms[9] = c115;
        rooms[10] = c116;
        rooms[11] = c118;
        rooms[12] = c119;
        rooms[13] = c124;

        Room c210 = new Room("C2.10 LI (20 PC) VP", "C");
        Room c212 = new Room("C2.12 LI (20 PC) VP", "C");
        Room c214 = new Room("C2.14 LI (20 PC) VP", "C");
        Room c216 = new Room("C2.16 (50) VP", "C");
        Room c221 = new Room("C2.21 (40) VP", "C");
        Room c223 = new Room("C2.23 (42) VP", "C");
        rooms[14] =c210;
        rooms[15] =c212;
        rooms[16] =c214;
        rooms[17] =c216;
        rooms[18] =c221;
        rooms[19] =c223;

        Room c305 = new Room("C3.05 (42) VP", "C");
        Room c310 = new Room("C3.10 (40) VP", "C");
        Room c311 = new Room("C3.11 (28)", "C");
        Room c312 = new Room("C3.12 (32+4PC) VP", "C");
        Room c314 = new Room("C3.14 (25) VP", "C");
        Room c319 = new Room("C3.19 (42) VP", "C");
        rooms[20] =c305;
        rooms[21] =c310;
        rooms[22] =c311;
        rooms[23] =c312;
        rooms[24] =c314;
        rooms[25] =c319;

        Room c411 = new Room("C4.11 (30)", "C");
        Room c412A = new Room("C4.12A FIPMECA VP (32)", "C");
        Room c412B = new Room("C4.12B (36) VP", "C");
        Room c414 = new Room("C4.14 (30) VP","C");
        Room c416 = new Room("C4.16 LI (16 PC) VP", "C");
        Room c419 = new Room("C4.19 (35) VP", "C");
        Room c421A = new Room("C4.21A (36) VP", "C");
        rooms[26] =c411;
        rooms[27] =c412A;
        rooms[28] =c412B;
        rooms[43]=c414;
        rooms[29] =c416;
        rooms[30] =c419;
        rooms[31] =c421A;
        
        // Batiment E:
        Room e001 = new Room("E0.01 Arts plastiques (39)", "E");
        Room e109 = new Room("E1.09 (37) VP", "E");
        Room e111 = new Room("E1.11 (38) VP", "E");
        Room e205 = new Room("E2.05 Labo Langues VP", "E");
        Room e305 = new Room("E3.05 Cours Langues VP", "E");
        Room e307 = new Room("E3.07 Cours Langues VP", "E");
        Room e309 = new Room("E3.09 Cours Langues VP", "E");
        Room e313 = new Room("E3.13 Labo Langues", "E");
        Room e115 = new Room("E3.15 Labo Langues VP", "E");
        Room e317 = new Room("E3.17 Conv. Langues VP", "E");
        Room e311 = new Room("E3.11 Cours Langues VP","E");
        Room oeuf = new Room("Espace A&I (L'oeuf)", "E");
        rooms[32] =e001;
        rooms[33] =e109;
        rooms[34] =e111;
        rooms[35] =e205;
        rooms[36] =e305;
        rooms[37] =e307;
        rooms[38] =e309;
        rooms[39] =e313;
        rooms[40] =e115;
        rooms[41] =e317;
        rooms[42] =oeuf;
        rooms[44]= e311;
        
        return rooms;
    }
    public static Room[] getAmphi(Room[] allRooms){
        Room[] rooms =new Room[7] ;
        int i=0;
        for (Room room: allRooms){
            if(room.isInBat("amphi")){
                rooms[i]=room;
                i++;
            }
        }
        return rooms;
    }

    public static Room[] getC(Room[] allRooms){
        Room[] rooms =new Room[26] ;
        int i=0;
        for (Room room: allRooms){
            if(room.isInBat("C")){
                rooms[i]=room;
                i++;
            }
        }
        return rooms;
    }
    public static Room[] getE(Room[] allRooms){
        Room[] rooms =new Room[12];
        int i=0;
        for (Room room: allRooms){
            if(room.isInBat("E")){
                rooms[i]=room;
                i++;
            }
        }
        return rooms;
    }

    public static Room[] SpecificRoom(String bat, Room[] rooms){
        switch (bat){
            case "Amphithéâtre": return getAmphi(rooms);
            case "Batiment C": return getC(rooms);
            case "Batiment E": return  getE(rooms);
            default: return  rooms;
        }
    }

}
