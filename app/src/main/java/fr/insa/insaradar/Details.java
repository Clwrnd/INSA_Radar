package fr.insa.insaradar;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Details extends AppCompatActivity implements RecyclerViewListener{
    private RecyclerView roomsRecyclerView;
    private Button timePickerButton;
    private ImageButton imageButton;
    private Spinner stageSpinner;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    int hour, minute;
    ArrayList<RoomModel> rooms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mAuth=FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        imageButton = findViewById(R.id.accountButton);
        imageButton.setOnClickListener(this::handleAccountManagement);

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
        //TODO: ajouter des disponibilités et des descriptions
        String availabilities[] = {"Disponible jusqu'à 12h", "Disponible jusqu'à 13h", "Disponible jusqu'à 14h", "Disponible jusqu'à 15h", "Disponible jusqu'à 16h", "Disponible jusqu'à 17h", "Disponible jusqu'à 18h", "Disponible jusqu'à 19h", "Disponible jusqu'à 20h", "Disponible jusqu'à 21h"};
        for (int i = 0; i < names.length; i++) {
            rooms.get(i).setAvailability(availabilities[i]);
        }
        String descriptions[] = {"36 places", "24 places", "12 places", "36 places", "24 places", "12 places", "36 places", "24 places", "12 places", "36 places"};
        for (int i = 0; i < names.length; i++) {
            rooms.get(i).setDescription(descriptions[i]);
        }
    }

    private List<RoomModel> getRooms() {
        return rooms;
    }

    @Override
    public void onItemClicked(int position) {
        //on récupère la salle sélectionnée
        RoomModel room = rooms.get(position);
        //on disable les autres salles
        for (RoomModel r : rooms) {
            r.setVisible(false);
        }
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
    public void handleAccountManagement(View view) {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null) {
            user = mAuth.getCurrentUser();
            if (user != null) {
                // User is signed in, navigate to Account activity
                Intent intent = new Intent(Details.this, Account.class);
                startActivity(intent);
                finish();
            } else {
                // User is not signed in, navigate to Registration activity
                Intent intent = new Intent(Details.this, Registration.class);
                startActivity(intent);
                finish();
            }
        } else {
            // FirebaseAuth instance is null, show an error message
            Toast.makeText(Details.this, "Error initializing Firebase Auth", Toast.LENGTH_SHORT).show();
        }
    }
}