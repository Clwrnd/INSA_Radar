package fr.insa.insaradar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword, editTextUsername;
    Button buttonRegister;
    ImageButton closeButton;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textViewLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth=FirebaseAuth.getInstance(); //initialisation de l'instance de FirebaseAuth
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextUsername = findViewById(R.id.username);
        buttonRegister = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        closeButton = findViewById(R.id.imageButton);
        closeButton.setOnClickListener(v -> {
            Intent intent = new Intent(Registration.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        textViewLogin = findViewById(R.id.loginNow);
        textViewLogin.setOnClickListener(v -> {
            //open the login activity
            Intent intent = new Intent(Registration.this, Login.class);
            startActivity(intent);
            finish();
        });

        buttonRegister.setOnClickListener(v -> {
            progressBar.setVisibility(v.VISIBLE);
            String email = String.valueOf(editTextEmail.getText());
            String password = String.valueOf(editTextPassword.getText());
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(Registration.this, "Veuillez saisir une adresse mail", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(Registration.this, "Veuillez saisir un mot de passe", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(String.valueOf(editTextUsername.getText()))){
                Toast.makeText(Registration.this, "Veuillez saisir un nom d'utilisateur", Toast.LENGTH_SHORT).show();
                return;
            }
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");
            String userId = reference.push().getKey();
            ReadWriteUserDetails userDetails = new ReadWriteUserDetails(userId, String.valueOf(editTextUsername.getText()), email);
            reference.child(userId).setValue(userDetails);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(v.GONE);
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(Registration.this, "Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Registration.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Registration.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });

            //registerUser(email, password);

        });
    }
}