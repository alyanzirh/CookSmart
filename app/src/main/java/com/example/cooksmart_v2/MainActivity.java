package com.example.cooksmart_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cooksmart-testv2-default-rtdb.firebaseio.com/");

    public EditText fullname, signupEmail, dietType, targetCalorie, signupPassword, confirmPwd;
    Button btnSignUp;
    TextView loginRedirectText;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        fullname = findViewById(R.id.fullname);
        signupEmail = findViewById(R.id.edEmail);
        dietType = findViewById(R.id.dietType);
        // height = findViewById(R.id.height);
        targetCalorie = findViewById(R.id.weight);
        signupPassword = findViewById(R.id.edPassword);
        confirmPwd = findViewById(R.id.confirmPwd);
        btnSignUp = findViewById(R.id.btnSignUp);
        loginRedirectText = findViewById(R.id.txtSignIn);

        ImageView infoIcon = findViewById(R.id.infoIcon);
        String dietInfo = getString(R.string.diet_info);

        infoIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Toast.makeText(MainActivity.this, dietInfo, Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        // v1 without Firebase Auth
        /* btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get data from EditText into String variables
                String userKey = databaseReference.child("users").push().getKey();
                String fullnameTxt = fullname.getText().toString();
                String emailID = signupEmail.getText().toString();
                String dietTypeTxt = dietType.getText().toString();
                String heightTxt = height.getText().toString();
                String weightTxt = weight.getText().toString();
                String paswd = signupPassword.getText().toString();
                String cPwd = confirmPwd.getText().toString();

                // Check if user fill all the fields
                if (emailID.isEmpty()) {
                    signupEmail.setError("Please enter your email!");
                    signupEmail.requestFocus();
                } else if (fullnameTxt.isEmpty()) {
                    fullname.setError("Please enter your full name!");
                    fullname.requestFocus();
                } else if (dietTypeTxt.isEmpty()) {
                    dietType.setError("Please enter your preferred diet type!");
                    dietType.requestFocus();
                } else if (heightTxt.isEmpty()) {
                    height.setError("Please enter your height!");
                    height.requestFocus();
                } else if (weightTxt.isEmpty()) {
                    weight.setError("Please enter your weight!");
                    weight.requestFocus();
                } else if (paswd.isEmpty()) {
                    signupPassword.setError("Please enter your password!");
                    signupPassword.requestFocus();
                }

                // Check if pwd matched
                else if (!paswd.equals(cPwd)) {
                    Toast.makeText(MainActivity.this, "Password do NOT match!", Toast.LENGTH_SHORT).show();
                } else if (userKey != null) {
                    databaseReference.child("users").child(userKey).child("signupEmail").setValue(emailID);
                    databaseReference.child("users").child(userKey).child("fullname").setValue(fullnameTxt);
                    databaseReference.child("users").child(userKey).child("dietType").setValue(dietTypeTxt);
                    databaseReference.child("users").child(userKey).child("height").setValue(heightTxt);
                    databaseReference.child("users").child(userKey).child("weight").setValue(weightTxt);
                    databaseReference.child("users").child(userKey).child("signupPassword").setValue(paswd);

                    Toast.makeText(MainActivity.this, "Successfully Signed Up.", Toast.LENGTH_SHORT).show();
                    finish();
                }

                /* else if (!(emailID.isEmpty() && paswd.isEmpty())) {
                    firebaseAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(MainActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this.getApplicationContext(),
                                        "Fail to Sign Up: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(MainActivity.this, UserActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }); */

        // v2 with Firebase Auth
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullnameTxt = fullname.getText().toString();
                String emailID = signupEmail.getText().toString();
                String dietTypeTxt = dietType.getText().toString();
                // String heightTxt = height.getText().toString();
                String targetCalorieTxt = targetCalorie.getText().toString();
                String paswd = signupPassword.getText().toString();
                String cPwd = confirmPwd.getText().toString();

                // Validate email
                if (!isValidEmail(emailID)) {
                    signupEmail.setError("Please enter a valid email address");
                    signupEmail.requestFocus();
                    return;
                }

                // Check if user fill all the fields
                if (emailID.isEmpty() || fullnameTxt.isEmpty() || dietTypeTxt.isEmpty() ||
                        targetCalorieTxt.isEmpty() || paswd.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if passwords match
                if (!paswd.equals(cPwd)) {
                    Toast.makeText(MainActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Use Firebase Authentication for user registration
                firebaseAuth.createUserWithEmailAndPassword(emailID, paswd)
                        .addOnCompleteListener(MainActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // User created successfully, now store additional data in Realtime Database
                                String userId = firebaseAuth.getCurrentUser().getUid();
                                DatabaseReference userRef = databaseReference.child("users").child(userId);

                                // Store additional user data
                                userRef.child("fullname").setValue(fullnameTxt);
                                userRef.child("dietType").setValue(dietTypeTxt);
                                // userRef.child("height").setValue(heightTxt);
                                userRef.child("targetCals").setValue(targetCalorieTxt);
                                userRef.child("signupPassword").setValue(paswd);
                                userRef.child("signupEmail").setValue(emailID);

                                Toast.makeText(MainActivity.this, "Successfully Signed Up.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                // Handle unsuccessful sign-up
                                Toast.makeText(MainActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Intent I = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(I); */
                finish();
            }
        });
    }

    private boolean isValidEmail(CharSequence target){
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
