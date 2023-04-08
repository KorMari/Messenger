package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    public static final String EXTRA_CURRENT_USER_ID = "current_id";
    public static final String EXTRA_OTHER_USER_ID = "other_id";
    private TextView textViewTitleUser;
    private View viewOnlineStatus;
    private EditText editTextMessage;
    private ImageView imageViewSendMessage;
    private MessagesAdapter messagesAdapter;
    private RecyclerView recyclerViewMessages;
    private ChatViewModel viewModel;
    private ChatViewModelFactory viewModelFactory;

    private String currentUserId;
    private String otherUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
        Intent intent = getIntent();
        currentUserId = intent.getStringExtra(EXTRA_CURRENT_USER_ID);
        otherUserId = intent.getStringExtra(EXTRA_OTHER_USER_ID);
        viewModelFactory = new ChatViewModelFactory(currentUserId, otherUserId);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ChatViewModel.class);

        messagesAdapter = new MessagesAdapter(currentUserId);
        recyclerViewMessages.setAdapter(messagesAdapter);
        observeViewModel();
        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message(editTextMessage.getText().toString().trim(), currentUserId, otherUserId);
                viewModel.sendMessage(message);
            }
        });

//        List <Message> messages = new ArrayList<>();
//        for (int i = 0; i<10; i++){
//            Message message = new Message("text " + i, currentUserId, otherUserId) ;
//            messages.add(message);
//        }
//        messagesAdapter.setMessages(messages);



    }

    private void observeViewModel() {
        viewModel.getOtherUser().observe(ChatActivity.this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                String userInfoForTitle = String.format("%s %s", user.getName(), user.getLastName());
                textViewTitleUser.setText(userInfoForTitle);
            }
        });
        viewModel.getError().observe(ChatActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Toast.makeText(ChatActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }

            }
        });

        viewModel.getMessageSend().observe(ChatActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    editTextMessage.setText("");
                }
            }
        });

        viewModel.getMessages().observe(ChatActivity.this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messagesAdapter.setMessages(messages);
            }
        });

    }

    private void initViews() {
        textViewTitleUser = findViewById(R.id.textViewTitleUser);
        viewOnlineStatus = findViewById(R.id.viewOnlineStatus);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage);

    }

    public static Intent newIntent(Context context, String currentUserId, String otherUserId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        intent.putExtra(EXTRA_OTHER_USER_ID, otherUserId);

        return intent;
    }
}