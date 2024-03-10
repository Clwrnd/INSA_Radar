package fr.insa.insaradar;

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
    TextView lastStamp;
    ArrayList<BuildingModel> buildings = new ArrayList<>();
    private RecyclerView buildingsRecyclerView;
    private Room[] rooms;
    private boolean isInternetConnection;
    private boolean isBug;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance(); //initialisation de l'instance de FirebaseAuth
        user = mAuth.getCurrentUser();
        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this::handleAccountManagement);
        refreshButton= findViewById(R.id.refreshBut);
        refreshButton.setOnClickListener(this::refreshAction);
        lastStamp = findViewById(R.id.lastStamp);

        setupBuildingModels();
        buildingsRecyclerView = findViewById(R.id.buildingsRecyclerView);
        BuildingsAdapter adapter = new BuildingsAdapter(buildings, this, this);
        buildingsRecyclerView.setAdapter(adapter);
        buildingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        isNetworkAvailable();

        ProgressDialog mProgressDialog = ProgressDialog.show(this, "Veuillez patienter","Chargement des données", true);
        new Thread() {
            @Override
            public void run() {
                isBug =false;
                ExecutorService exe = Executors.newSingleThreadExecutor();
                Future<Room[]> rm = exe.submit(EdtAnalyse.initializeFile2(MainActivity.this,isInternetConnection));
                try {
                    rooms = rm.get(30, TimeUnit.SECONDS);
                } catch (ExecutionException | InterruptedException e) {
                    isBug=true;
                    rooms=null;
                } catch (TimeoutException e) {
                    isBug=true;
                    rooms=null;
                }finally {
                    exe.shutdownNow();
                }
                //  rooms = EdtAnalyse.initializeFile(MainActivity.this,isInternetConnection);
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressDialog.dismiss();
                            if (rooms==null){
                                if(isBug){
                                    Toast.makeText(MainActivity.this,"Error while loading files, please try to refresh the app",Toast.LENGTH_LONG).show();
                                }else {
                                    Toast .makeText(MainActivity.this,"No Internet Connection\nNo Existing Version", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast .makeText(MainActivity.this,"Data succesfully received", Toast.LENGTH_SHORT).show();

                            }
                            lastStamp.setText("Last Stamp: "+SingletonRoomObject.getInstance().getLastStamp());
                        }
                    });
                } catch (final Exception ex) {
                }
            }
        }.start();


    }

    private void refreshAction(View view) {
        isNetworkAvailable();
        if(isInternetConnection) {
            ProgressDialog mProgressDialog = ProgressDialog.show(this, "Veuillez patienter", "Chargement des données", true);
            new Thread() {
                @Override
                public void run() {
                    isBug = false;
                    ExecutorService exe = Executors.newSingleThreadExecutor();
                    Future<Room[]> rm = exe.submit(EdtAnalyse.initializeFile2(MainActivity.this, isInternetConnection));
                    try {
                        rooms = rm.get(30, TimeUnit.SECONDS);
                    } catch (ExecutionException | InterruptedException e) {
                        isBug = true;
                        rooms = null;
                    } catch (TimeoutException e) {
                        isBug = true;
                        rooms = null;
                    } finally {
                        exe.shutdownNow();
                    }
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgressDialog.dismiss();
                                if (rooms == null && isBug) {
                                    Toast.makeText(MainActivity.this, "Error while loading files, please try to refresh the app", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Data succesfully received", Toast.LENGTH_SHORT).show();

                                }
                                lastStamp.setText("Last Stamp: " + SingletonRoomObject.getInstance().getLastStamp());
                            }
                        });
                    } catch (final Exception ex) {
                    }
                }
            }.start();
        } else {
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

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
        if(rooms!=null){
            SingletonRoomObject.getInstance().setRooms(GenerateAllRoom.SpecificRoom(buildings.get(position).getName(),rooms));
            Intent intent = new Intent(MainActivity.this, Details.class);
            startActivity(intent);
        } else {
            Toast .makeText(MainActivity.this,"No Existing Version. Try to refresh the app with Internet Connection", Toast.LENGTH_SHORT).show();
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