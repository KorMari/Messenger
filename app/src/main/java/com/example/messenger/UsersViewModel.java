package com.example.messenger;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersViewModel extends AndroidViewModel {

    private FirebaseAuth auth;
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    private MutableLiveData<List<User>> allUsers = new MutableLiveData<>();
    private FirebaseDatabase database;
    private DatabaseReference userReference;


    public UsersViewModel(@NonNull Application application) {
        super(application);
        auth = FirebaseAuth.getInstance();


        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                user.setValue(firebaseAuth.getCurrentUser());

            }
        });
        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("Users");
        getUsersFromFirebase();
    }

    public void getUsersFromFirebase() {
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser == null){
                    return;
                }
                List<User> usersFromFB = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User userFromFB = dataSnapshot.getValue(User.class);
                    if (userFromFB == null){
                        return;
                    }
                    if(!userFromFB.getId().equals(currentUser.getUid())){
                        usersFromFB.add(userFromFB);
                    }

                }
                allUsers.setValue(usersFromFB);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void logout() {
        auth.signOut();
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }
}
