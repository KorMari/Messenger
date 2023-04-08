package com.example.messenger;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {

    private MutableLiveData<User> otherUser = new MutableLiveData<>();
    private MutableLiveData<List<Message>> messages = new MutableLiveData<>();
    private MutableLiveData<Boolean> messageSend = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    private FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
    private DatabaseReference usersReference = firebaseDB.getReference("Users");
    private DatabaseReference messagesReference = firebaseDB.getReference("Messages");


    private String currentUserId;
    private String otherUserId;


    public ChatViewModel(String currentUserId, String otherUserId) {

        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;


        usersReference.child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                otherUser.setValue(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messagesReference
                .child(currentUserId)
                .child(otherUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Message> messageList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Message message = dataSnapshot.getValue(Message.class);
                            messageList.add(message);
                            Log.d("ChatViewModel", "message from dataBase");
                        }
                        messages.setValue(messageList);
                        Log.d("ChatViewModel", "setValue in List");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError e) {

                    }
                });

    }


    public LiveData<User> getOtherUser() {
        return otherUser;
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public LiveData<Boolean> getMessageSend() {
        return messageSend;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void sendMessage(Message message) {
        messagesReference
                .child(message.getSenderId())
//                .child(message.getReceiverId())
                .push()
                .setValue(messages)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("ChatViewModel", "messagesReference get Sender in dataBase");
//                        messagesReference
//                                .child(message.getReceiverId())
//                                .child(message.getSenderId())
//                                .push()
//                                .setValue(messages)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        Log.d("ChatViewModel", "messagesSend is true");
//
//                                        messageSend.setValue(true);
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        error.setValue(e.getMessage());
//                                    }
//                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        error.setValue(e.getMessage());
                    }
                });
    }


}
