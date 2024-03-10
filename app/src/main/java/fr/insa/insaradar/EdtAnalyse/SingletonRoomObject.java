package fr.insa.insaradar.EdtAnalyse;

public class SingletonRoomObject {
    // SingletonClass to get different information between two Activities
    private static SingletonRoomObject instance;
    private Room[] rooms;

    public String getLastStamp() {
        return lastStamp;
    }
    public void setLastStamp(String lastStamp) {
        this.lastStamp = lastStamp;
    }
    private String lastStamp;

    public Room[] getRooms() {
        return rooms;
    }

    public void setRooms(Room[] rooms) {
        this.rooms = rooms;
    }
    private SingletonRoomObject(){}
    public static SingletonRoomObject getInstance(){
        if(instance==null){
            instance = new SingletonRoomObject();
        }
        return instance;
    }
}
