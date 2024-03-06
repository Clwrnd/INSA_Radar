package fr.insa.insaradar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import fr.insa.insaradar.EdtAnalyse.EdtAnalyse;
import fr.insa.insaradar.EdtAnalyse.GenerateAllRoom;
import fr.insa.insaradar.EdtAnalyse.Room;
import fr.insa.insaradar.EdtAnalyse.SingletonRoomObject;

public class MainActivity extends AppCompatActivity implements RecyclerViewListener {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    ImageButton imageButton;
    ArrayList<BuildingModel> buildings = new ArrayList<>();
    private RecyclerView buildingsRecyclerView;
    private Room[] rooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance(); //initialisation de l'instance de FirebaseAuth
        user = mAuth.getCurrentUser();
        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this::handleAccountManagement);

        setupBuildingModels();
        buildingsRecyclerView = findViewById(R.id.buildingsRecyclerView);
        BuildingsAdapter adapter = new BuildingsAdapter(buildings, this, this);
        buildingsRecyclerView.setAdapter(adapter);
        buildingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ProgressDialog mProgressDialog = ProgressDialog.show(this, "Veuillez patienter","Chargement des données", true);
        new Thread() {
            @Override
            public void run() {
                rooms = EdtAnalyse.initializeFile(MainActivity.this);
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressDialog.dismiss();
                        }
                    });
                } catch (final Exception ex) {
                }
            }
        }.start();
    }
    void setupBuildingModels(){
        String names[] = {"Amphithéâtre","Batiment C","Batiment E"};
        for (String name : names) {
            buildings.add(new BuildingModel(name));
        }
    }

    private List<BuildingModel> getBuildings() {
        return buildings;
    }

    public void handleAccountManagement(View view) {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null) {
            user = mAuth.getCurrentUser();
            if (user != null) {
                // User is signed in, navigate to Account activity
                Intent intent = new Intent(MainActivity.this, Account.class);
                startActivity(intent);
                finish();
            } else {
                // User is not signed in, navigate to Registration activity
                Intent intent = new Intent(MainActivity.this, Registration.class);
                startActivity(intent);
                finish();
            }
        } else {
            // FirebaseAuth instance is null, show an error message
            Toast.makeText(MainActivity.this, "Error initializing Firebase Auth", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClicked(int position) {
        //switch to the details activity
        SingletonRoomObject.getInstance().setRooms(GenerateAllRoom.SpecificRoom(buildings.get(position).getName(),rooms));
        Intent intent = new Intent(MainActivity.this, Details.class);
        startActivity(intent);
    }

}