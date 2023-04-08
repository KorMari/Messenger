package com.example.messenger;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationViewModel extends AndroidViewModel {

    private FirebaseDatabase database;
    private DatabaseReference userReference;
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private FirebaseAuth auth;

    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();

    public RegistrationViewModel(@NonNull Application application) {
        super(application);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("Users");
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    user.setValue(firebaseAuth.getCurrentUser());
                }
            }
        });
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }


    public void signUp(
            String email,
            String password,
            String name,
            String lastName,
            int age
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser firebaseUser = authResult.getUser();
                        User userForFB = new User(firebaseUser.getUid(), name,lastName,age,true);
                        userReference.child(userForFB.getId()).setValue(userForFB);
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
