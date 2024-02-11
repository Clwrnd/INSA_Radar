package fr.insa.insaradar;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Details extends AppCompatActivity implements RecyclerViewListener{
    private RecyclerView roomsRecyclerView;
    private Button timePickerButton;
    private Spinner stageSpinner;
    int hour, minute;
    ArrayList<RoomModel> rooms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        roomsRecyclerView = findViewById(R.id.roomsRecyclerView);
        timePickerButton = findViewById(R.id.datePickerButton);
        timePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popTimePicker(v);
            }
        });

    stageSpinner = findViewById(R.id.stageSpinner);

    setupRoomModels();
    roomsRecyclerView = findViewById(R.id.roomsRecyclerView);
    RoomsAdapter adapter = new RoomsAdapter(rooms, this, this);
    roomsRecyclerView.setAdapter(adapter);
    roomsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
}



    private void setupRoomModels() {
        String names[] = {"Salle 1", "Salle 2", "Salle 3", "Salle 4", "Salle 5", "Salle 6", "Salle 7", "Salle 8", "Salle 9", "Salle 10"};
        for (String name : names) {
            rooms.add(new RoomModel(name));
        }
    }

    private List<RoomModel> getRooms() {
        return rooms;
    }

    @Override
    public void onItemClicked(int position) {
        //on récupère la salle sélectionnée
        RoomModel room = rooms.get(position);
        Toast.makeText(this, "Salle sélectionnée : " + room.getName(), Toast.LENGTH_SHORT).show();
        //TODO: on élargit la carte pour afficher un bouton de réservation
        room.setVisible(true);

    }
    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                Details.this.minute = minute;
                timePickerButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Sélectionner l'heure");
        timePickerDialog.show();
    }
}