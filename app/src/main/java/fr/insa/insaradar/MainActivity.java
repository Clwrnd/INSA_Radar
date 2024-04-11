package fr.insa.insaradar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import fr.insa.insaradar.EdtAnalyse.EdtAnalyse;
import fr.insa.insaradar.EdtAnalyse.GenerateAllRoom;
import fr.insa.insaradar.EdtAnalyse.Room;
import fr.insa.insaradar.EdtAnalyse.SingletonRoomObject;

public class MainActivity extends AppCompatActivity implements RecyclerViewListener {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    ImageButton imageButton;
    ImageButton refreshButton;
    ImageButton infoBut;
    TextView lastStamp;
    ArrayList<BuildingModel> buildings = new ArrayList<>();
    private Room[] rooms;
    private boolean isInternetConnection;
    private boolean isBug;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance(); //initialisation de l'instance de FirebaseAuth
        user = mAuth.getCurrentUser();
        /* A terminer plus tard :
        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this::handleAccountManagement);
        */
        infoBut = findViewById(R.id.infoBut);
        infoBut.setOnClickListener(this::showInfo);
        refreshButton= findViewById(R.id.refreshBut);
        refreshButton.setOnClickListener(this::refreshAction);
        lastStamp = findViewById(R.id.lastStamp);

        setupBuildingModels();
        RecyclerView buildingsRecyclerView = findViewById(R.id.buildingsRecyclerView);
        BuildingsAdapter adapter = new BuildingsAdapter(buildings, this, this);
        buildingsRecyclerView.setAdapter(adapter);
        buildingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        isNetworkAvailable();

        ProgressDialog mProgressDialog = ProgressDialog.show(this, "Veuillez patienter","Nous récupérons les informations des salles...", true);
        new Thread(() -> {
            isBug =false;
            ExecutorService exe = Executors.newSingleThreadExecutor();
            Future<Room[]> rm = exe.submit(EdtAnalyse.initializeFile2(MainActivity.this,isInternetConnection));
            try {
                rooms = rm.get(30, TimeUnit.SECONDS);
            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                isBug=true;
                rooms=null;
            } finally {
                exe.shutdownNow();
            }
            try {
                runOnUiThread(() -> {
                    mProgressDialog.dismiss();
                    if (rooms==null){
                        if(isBug){
                            Toast.makeText(MainActivity.this,"Erreur lors du chargement des fichiers, veuillez relancer l'application",Toast.LENGTH_LONG).show();
                        }else {
                            Toast .makeText(MainActivity.this,"Pas de connexion à internet", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast .makeText(MainActivity.this,"Mise à jour avec succès !", Toast.LENGTH_SHORT).show();

                    }
                    lastStamp.setText("Dernière mise à jour: "+SingletonRoomObject.getInstance().getLastStamp());
                });
            } catch (final Exception ex) {
                Toast.makeText(MainActivity.this,"Erreur inconnue, veuillez relancer l'application",Toast.LENGTH_LONG).show();
            }
        }).start();


    }

    private void showInfo(View view) {
        AlertDialog.Builder infoDial = new AlertDialog.Builder(this);
        infoDial.setTitle("Bienvenue sur INSA RADAR ");
        infoDial.setMessage("Vous pouvez ici trouver une salle libre à n'importe quel moment !!" +
                " \nSi vous rencontrez des problèmes à charger les données vérifiez que votre connexion tient la route et faites un refresh avec le button " +
                "en haut à gauche  !\n \n" +
                "N'hésitez pas à nous faire parvenir vos diverses remarques concernant l'application pour toujours l'améliorer.\n" +
                "Petit crédit à notre cher Louis Carbo Estaque à qui on doit le concept. " +
                "\n \n" +
                "La bise !\n" +
                "L'équipe: Rudy Virquin,Clara WURTZER et IDMONT Clément\n" +
                "Contact:  ");
        infoDial.show();

    }

    private void refreshAction(View view) {
        isNetworkAvailable();
        if(isInternetConnection) {
            ProgressDialog mProgressDialog = ProgressDialog.show(this, "Veuillez patienter", "mise à jour des données", true);
            new Thread(() -> {
                isBug = false;
                ExecutorService exe = Executors.newSingleThreadExecutor();
                Future<Room[]> rm = exe.submit(EdtAnalyse.refreshFile(MainActivity.this));
                try {
                    rooms = rm.get(30, TimeUnit.SECONDS);
                } catch (ExecutionException | InterruptedException | TimeoutException e) {
                    isBug = true;
                    rooms = null;
                } finally {
                    exe.shutdownNow();
                }
                try {
                    runOnUiThread(() -> {
                        mProgressDialog.dismiss();
                        if (rooms == null && isBug) {
                            Toast.makeText(MainActivity.this, "Erreur lors du chargement des fichiers, veuillez relancer l'application", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Mise à jour avec succès !", Toast.LENGTH_SHORT).show();

                        }
                        lastStamp.setText("Dernière mise à jour: " + SingletonRoomObject.getInstance().getLastStamp());
                    });
                } catch (final Exception ex) {
                    Toast.makeText(MainActivity.this,"Erreur inconnue, veuillez relancer l'application",Toast.LENGTH_LONG).show();
                }
            }).start();
        } else {
            Toast.makeText(MainActivity.this, "Pas de connexion à internet", Toast.LENGTH_SHORT).show();
        }

    }

    void setupBuildingModels(){
        String[] names = {"Amphithéâtre","Bâtiment C","Bâtiment E"};
        for (String name : names) {
            buildings.add(new BuildingModel(name));
        }
    }

    public void handleAccountManagement(View view) {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null) {
            user = mAuth.getCurrentUser();
            Intent intent;
            if (user != null) {
                // User is signed in, navigate to Account activity
                intent = new Intent(MainActivity.this, Account.class);
            } else {
                // User is not signed in, navigate to Registration activity
                intent = new Intent(MainActivity.this, Registration.class);
            }
            startActivity(intent);
            finish();
        } else {
            // FirebaseAuth instance is null, show an error message
            Toast.makeText(MainActivity.this, "Error initializing Firebase Auth", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClicked(int position) {
        //switch to the details activity
        if(rooms!=null){
            SingletonRoomObject.getInstance().setRooms(GenerateAllRoom.SpecificRoom(buildings.get(position).getName(),rooms));
            Intent intent = new Intent(MainActivity.this, Details.class);
            startActivity(intent);
        } else {
            Toast .makeText(MainActivity.this,"Pas de connexion à internet, veuillez relancer l'application", Toast.LENGTH_SHORT).show();
        }

    }

    public void isNetworkAvailable() {
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                isInternetConnection = true;
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                isInternetConnection=false;
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                isInternetConnection=false;

            }
        }

        ;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(ConnectivityManager.class);
        connectivityManager.requestNetwork(networkRequest, networkCallback);
    }

}