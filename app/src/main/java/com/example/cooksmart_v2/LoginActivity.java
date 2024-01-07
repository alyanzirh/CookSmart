package com.example.cooksmart_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cooksmart-testv2-default-rtdb.firebaseio.com/");

    public EditText loginEmail, loginPassword;
    Button btnLogin;
    TextView registerRedirectText;
    FirebaseAuth firebaseAuth;

    // private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        btnLogin = findViewById(R.id.btnLogIn);
        registerRedirectText = findViewById(R.id.txtRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = loginEmail.getText().toString().trim();
                String paswd = loginPassword.getText().toString();

                if (email.isEmpty() || paswd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your email and password!", Toast.LENGTH_SHORT).show();
                } else {
                    // Use Firebase Authentication for login
                    firebaseAuth.signInWithEmailAndPassword(email, paswd)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success
                                        if (email.equals("nutritionist@cooksmart.com") && paswd.equals("nutritionist@cooksmart")) {
                                        // if (email.equals("mamak@gmail.com") && paswd.equals("alimamak123")) {
                                            Log.d("Admin Executed", "TRUE");
                                            Intent I = new Intent(LoginActivity.this, AdminActivity.class);
                                            startActivity(I);
                                            Log.d("Admin Page", "TRUE");
                                        } else {
                                            Log.d("User Executed", "TRUE");
                                            startActivity(new Intent(LoginActivity.this, UserActivity.class));
                                        }
                                        finish();
                                        Toast.makeText(LoginActivity.this, "Successfully Signed In.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginActivity.this, "Incorrect username or password!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

                /* else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // check if email exist in the firebase database
                            // String encodedEmail = encodeEmail(emailID);
                            // String getUname = snapshot.child(usernameTxt).child("fullname").getValue(String.class);
                            if(getUname != null && getUname.equalsIgnoreCase(usernameTxt)){
                                // email exist, get pwd
                                String  = snapshot.child(usernameTxt).child("signupPassword").getValue(String.class);

                                if(getPwd != null && getPwd.equalsIgnoreCase(paswd)){
                                    Toast.makeText(LoginActivity.this, "Successfully Signed In.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, UserActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Incorrect Username!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                        private String encodeEmail(String email) {
                            return email.replace('.', ',').replace('@', '_');
                        }
                    });
                }
            }
        });

        registerRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(I);
            }
        });

        /* authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(LoginActivity.this, "Successfully Sign In", Toast.LENGTH_SHORT).show();
                    Intent I = new Intent(LoginActivity.this, UserActivity.class);
                    startActivity(I);
                } /* else {
                    Toast.makeText(LoginActivity.this, "Please enter sign in details!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        registerRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(I);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = loginEmail.getText().toString();
                String userPassword = loginPassword.getText().toString();
                if (userEmail.isEmpty()) {
                    loginEmail.setError("Please enter your email!");
                    loginEmail.requestFocus();
                } else if (userPassword.isEmpty()) {
                    loginPassword.setError("Please enter your password!");
                    loginPassword.requestFocus();
                } else if (userEmail.isEmpty() && userPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your email and password!", Toast.LENGTH_SHORT).show();
                } else if (!(userEmail.isEmpty() && userPassword.isEmpty())) {
                    firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(LoginActivity.this, UserActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }); */

            }
        });

        registerRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(I);
            }
        });

    }

    private boolean isAdmin(String email, String password) {
        // Replace this with your admin email and password check logic
        boolean isAdmin = email.equals("hello@cooksmart.com") && password.equals("admin@cooksmart");
        Log.d("LoginActivity", "Is Admin: " + isAdmin);
        return isAdmin;
    }

    /* @Override
    protected void onStart() {
        /* super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);

    } */
}