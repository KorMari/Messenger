package com.example.messenger;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPasswordViewModel extends AndroidViewModel {

    private FirebaseAuth auth;
    private MutableLiveData<Boolean> success = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public MutableLiveData<Boolean> getSuccess() {
        return success;
    }

    public ForgotPasswordViewModel(@NonNull Application application) {
        super(application);
        auth = FirebaseAuth.getInstance();

    }

    public void resetPassword (String email){
        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        success.setValue(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               errorMessage.setValue(e.getMessage());
            }
        });
    }

}
