package fr.insa.insaradar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This class represents the Account activity in the application.
 * It handles user authentication and profile display.
 */
public class Account extends AppCompatActivity {
    FirebaseAuth mAuth; // Firebase Authentication instance
    Button buttonLogout; // Logout button
    TextView textViewEmail, textViewUsername; // TextViews for email and username
    FirebaseUser user; // Current logged in user
    ImageButton imageButton;


    /**
     * This method is called when the activity is starting.
     * It initializes the activity view and Firebase Authentication instance.
     * It also sets up the logout button and user profile display.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mAuth=FirebaseAuth.getInstance(); //initialisation de l'instance de FirebaseAuth
        buttonLogout = findViewById(R.id.logout);
        textViewEmail = findViewById(R.id.email);
        textViewUsername = findViewById(R.id.username);
        imageButton = findViewById(R.id.imageButton);
        user = mAuth.getCurrentUser();
        if(user==null){
            Intent intent = new Intent(Account.this, Login.class);
            startActivity(intent);
            finish();
        }else{
            textViewEmail.setText(user.getEmail());
            showUserProfil(user);
        }
        buttonLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(Account.this, Login.class);
            startActivity(intent);
            finish();
        });
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Account.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    /**
     * This method retrieves and displays the user profile from Firebase Database.
     * @param user The current logged in user.
     */
    private void showUserProfil(FirebaseUser user) {
        DatabaseReference referenceProfil = FirebaseDatabase.getInstance().getReference("Registered Users");
                referenceProfil.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    /**
                     * This method will be called with a snapshot of the data at this location.
                     * It will be called each time that data changes.
                     * @param snapshot The data snapshot of the user profile.
                     */
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                        if(readUserDetails!=null){
                            //textViewUsername.setText(readUserDetails.getUsername());
                        }
                    }

                    /**
                     * This method will be triggered in the event that this listener either failed at the server,
                     * or is removed as a result of the security and Firebase rules.
                     * @param error The database error, if any.
                     */
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
                );
    }
}