package com.midhun.hawkssolutions.activityinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.midhun.hawkssolutions.activityinfo.databinding.ActivityLoginBinding;

import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String UID;
    ActivityLoginBinding binding;
    EditText email;
    EditText password;
    CardView btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            UID = currentUser.getUid();
        }

        email = binding.email;
        password=binding.password;
        btn=binding.btn;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty()){
                    email.setError("Required");
                }
                else if(password.getText().toString().isEmpty()){
                    password.setError("Required");
                }
                else{
                    Toast.makeText(LoginActivity.this, "Please Wait ", Toast.LENGTH_SHORT).show();
              login(email.getText().toString(),password.getText().toString());
                }
            }
        });
    }

    private void signUp(String email,String password){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Registration Completed", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("info");
                            HashMap<String, Object> mapList = new HashMap<String, Object>();
                            mapList.put("uid", UID);
                            reference1.child(UID).setValue(mapList);
                            finish();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    
    private void login(String email,String password){
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            signUp(email,password);
                        }
                    }
                });
    }

}