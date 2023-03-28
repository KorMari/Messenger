package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    private String TAG = "RegistrationActivity";
    private RegistrationViewModel viewModel;

    private Button buttonSignUp;
    private EditText editTextEmailRegistration;
    private EditText editTextPasswordFromRegistration;
    private EditText editTextPersonName;
    private EditText editTextLastName;
    private EditText editTextHowOld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
        viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        observeViewModel();
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editTextEmailRegistration.getEditableText().toString();
                String password = editTextPasswordFromRegistration.getEditableText().toString();
                String name = editTextPersonName.getEditableText().toString();
                String lastName = editTextLastName.getEditableText().toString();
                int age = Integer.parseInt(editTextHowOld.getEditableText().toString());
                viewModel.signUp(email, password, name, lastName, age);
            }
        });


    }

    private void observeViewModel() {

        viewModel.getUser().observe(RegistrationActivity.this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null){
                    Intent intent = UsersActivity.newIntent(RegistrationActivity.this);
                    startActivity(intent);
                    finish();
                }

            }
        });

        viewModel.getErrorMessage().observe(RegistrationActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null){
                    Toast.makeText(RegistrationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
           }
        });
    }



    private void initViews() {
        buttonSignUp = findViewById(R.id.buttonSignUp);
        editTextEmailRegistration = findViewById(R.id.editTextEmailRegistration);
        editTextPasswordFromRegistration = findViewById(R.id.editTextPasswordFromRegistration);
        editTextPersonName = findViewById(R.id.editTextPersonName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextHowOld = findViewById(R.id.editTextHowOld);
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, RegistrationActivity.class);
        return intent;
    }
}