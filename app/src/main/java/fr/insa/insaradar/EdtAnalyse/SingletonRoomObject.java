package fr.insa.insaradar.EdtAnalyse;

public class SingletonRoomObject {
    private static SingletonRoomObject instance;
    private Room[] rooms;

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
