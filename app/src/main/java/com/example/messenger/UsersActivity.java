package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {
    private UsersViewModel viewModel;
    private RecyclerView recyclerViewUsers;
    private UsersAdapter usersAdapter;

    private ArrayList<User> usersFromFireBase = new ArrayList<>();
    public static final String EXTRA_CURRENT_ID = "current_id";
    private  String currentUserId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        initViews();
        Intent intent = getIntent();
        currentUserId = intent.getStringExtra(EXTRA_CURRENT_ID);
        observeViewModel();
        usersAdapter.setOnUserClickListener(new UsersAdapter.onUserClickListener() {
            @Override
            public void onUserClick(User user) {
                Intent intent =  ChatActivity.newIntent(UsersActivity.this, currentUserId, user.getId());
                startActivity(intent);
            }
        });


    }


    private void initViews() {
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        usersAdapter = new UsersAdapter();
        recyclerViewUsers.setAdapter(usersAdapter);
    }

    public static Intent newIntent(Context context, String currentUserId) {
        Intent intent = new Intent(context, UsersActivity.class);
        intent.putExtra(EXTRA_CURRENT_ID, currentUserId);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.users_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logOut_menu) {
            viewModel.logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void observeViewModel() {
        viewModel.getUser().observe(UsersActivity.this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser == null) {
                    Intent intent = LoginActivity.newIntent(UsersActivity.this);
                    startActivity(intent);
                    finish();
                }


            }
        });
viewModel.getAllUsers().observe(UsersActivity.this, new Observer<List<User>>() {
    @Override
    public void onChanged(List<User> users) {
        usersAdapter.setUsers(users);
    }
});

    }

}