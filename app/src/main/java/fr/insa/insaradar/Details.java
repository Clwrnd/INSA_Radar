package fr.insa.insaradar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Locale;

import fr.insa.insaradar.EdtAnalyse.FreeRoom;
import fr.insa.insaradar.EdtAnalyse.GeneralMethod;
import fr.insa.insaradar.EdtAnalyse.Room;
import fr.insa.insaradar.EdtAnalyse.SingletonRoomObject;

public class Details extends AppCompatActivity implements RecyclerViewListener {
    private RecyclerView roomsRecyclerView;
    private Button timePickerButton;
    private Button datePickerButton;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    int hour, minute, year, month, day;
    ArrayList<RoomModel> rooms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        /*
        A terminer plus tard
        ImageButton imageButton = findViewById(R.id.accountButton);
        imageButton.setOnClickListener(this::handleAccountManagement);
        */

        roomsRecyclerView = findViewById(R.id.roomsRecyclerView);
        timePickerButton = findViewById(R.id.timePickerButton);
        timePickerButton.setOnClickListener(this::popTimePicker);

        datePickerButton = findViewById(R.id.datePickerButton);
        datePickerButton.setOnClickListener(this::popDatePicker);
        hour = LocalTime.now().getHour();
        minute = LocalTime.now().getMinute();
        month = LocalDate.now().getMonthValue();
        day = LocalDate.now().getDayOfMonth();
        year = LocalDate.now().getYear();


        timePickerButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
        datePickerButton.setText(String.format(Locale.getDefault(), "%02d/%02d", day, month));

        setupRoomModels(SingletonRoomObject.getInstance().getRooms(), LocalDate.now(), LocalTime.now());
        roomsRecyclerView = findViewById(R.id.roomsRecyclerView);
        RoomsAdapter adapter = new RoomsAdapter(rooms, this, this);
        roomsRecyclerView.setAdapter(adapter);
        roomsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void setupRoomModels(Room[] rms, LocalDate date, LocalTime time) {
        FreeRoom[] freeRooms = GeneralMethod.nextCourseInDay(LocalDateTime.of(date, time), GeneralMethod.isAvailableAt(LocalDateTime.of(date, time), rms));
        for (FreeRoom freeRoom : freeRooms) {
            if (freeRoom.getNextCousr() != null) {
                rooms.add(new RoomModel(freeRoom.getFreeRoom().getId(), freeRoom.getNextCousr().getStartPoint().toLocalTime().toString()));
            } else {
                rooms.add(new RoomModel(freeRoom.getFreeRoom().getId(), "Fin de journée"));
            }
        }
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
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (view1, hourOfDay, minute) -> {
            hour = hourOfDay;
            Details.this.minute = minute;
            timePickerButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, Details.this.minute));
            rooms.clear();
            setupRoomModels(SingletonRoomObject.getInstance().getRooms(), LocalDate.of(year, month, day), LocalTime.of(hour, Details.this.minute));
            roomsRecyclerView.getAdapter().notifyDataSetChanged();
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Sélectionner l'heure");
        timePickerDialog.show();
    }

    public void popDatePicker(View view) {
        DatePickerDialog.OnDateSetListener onDateSetListener = (view1, year, month, dayOfMonth) -> {
            Details.this.year = year;
            Details.this.month = month + 1;
            Details.this.day = dayOfMonth;
            datePickerButton.setText(String.format(Locale.getDefault(), "%02d/%02d", Details.this.day, Details.this.month));
            rooms.clear();
            setupRoomModels(SingletonRoomObject.getInstance().getRooms(), LocalDate.of(Details.this.year, Details.this.month, day), LocalTime.of(hour, minute));
            roomsRecyclerView.getAdapter().notifyDataSetChanged();
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener, year, month-1, day);
        datePickerDialog.setTitle("Sélectionner la date");
        datePickerDialog.show();
    }


    public void handleAccountManagement(View view) {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null) {
            user = mAuth.getCurrentUser();
            Intent intent;
            if (user != null) {
                // User is signed in, navigate to Account activity
                intent = new Intent(Details.this, Account.class);
            } else {
                // User is not signed in, navigate to Registration activity
                intent = new Intent(Details.this, Registration.class);
            }
            startActivity(intent);
            finish();
        } else {
            // FirebaseAuth instance is null, show an error message
            Toast.makeText(Details.this, "Error initializing Firebase Auth", Toast.LENGTH_SHORT).show();
        }
    }
}