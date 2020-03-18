package com.example.watchlistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.watchlistapp.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private Button buttonSignUp;
    private EditText editTextEmail,editTextPassword,editTextName;
    private TextView logIn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        buttonSignUp = (Button) findViewById(R.id.signUpSubmit);
        editTextEmail = (EditText) findViewById(R.id.signUpEMail);
        editTextName = (EditText) findViewById(R.id.signUpName);
        editTextPassword = (EditText) findViewById(R.id.signUpPassword);
        logIn =(TextView) findViewById(R.id.signUpToLogIn);
        databaseUser = FirebaseDatabase.getInstance().getReference("users");
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpUser();
            }

        });
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this,MainActivity.class));
            }
        });
    }

    private void signUpUser(){
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Email is empty",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(password.length()<6) {
            Toast.makeText(this, "Password is too short", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name area is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String id = databaseUser.push().getKey();
                            User user = new User(email,name,"");
                            databaseUser.child(id).setValue(user);
                            Intent intent = new Intent(SignUp.this, Home.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("email",email);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else {
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
