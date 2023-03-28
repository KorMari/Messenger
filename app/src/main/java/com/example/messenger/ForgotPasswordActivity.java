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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    public static final String EXTRA_EMAIL = "email";
    private ForgotPasswordViewModel viewModel;

    private EditText editTextEmailForgotPassword;
    private Button buttonResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initViews();
        viewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
        observeViewModel();
        Intent intent = getIntent();
        String emailFromIntent = intent.getStringExtra(EXTRA_EMAIL);
        if (emailFromIntent != null) {

            editTextEmailForgotPassword.setText(emailFromIntent);
        }
        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.resetPassword(getEmail());
            }
        });
    }

    private void observeViewModel (){
        viewModel.getErrorMessage().observe(ForgotPasswordActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Toast.makeText(ForgotPasswordActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getSuccess().observe(ForgotPasswordActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    Toast.makeText(ForgotPasswordActivity.this, R.string.reset_link, Toast.LENGTH_SHORT).show();
                   
                }
            }
        });
    }
    private String getEmail() {
        return editTextEmailForgotPassword.getText().toString();
    }

    private void initViews() {
        editTextEmailForgotPassword = findViewById(R.id.editTextEmailForgotPassword);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);
    }

    public static Intent newIntent(Context context, String email) {
        Intent intent = new Intent(context, ForgotPasswordActivity.class);
        intent.putExtra(EXTRA_EMAIL, email);
        return intent;
    }
}