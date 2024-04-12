package com.example.chatme.Fragment;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatme.Adapter.ContactAdapter;
import com.example.chatme.Constants.AllConstants;
import com.example.chatme.Permissions.Permissions;
import com.example.chatme.UserModel;
import com.example.chatme.databinding.FragmentContactBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Objects;

public class ContactFragment extends Fragment implements SearchView.OnQueryTextListener{
    private FragmentContactBinding binding;
    private FirebaseDatabase database;
    private ArrayList<UserModel> arrayList;
    private ContactAdapter usersAdapter;
    private ContactAdapter contactAdapter;
    private UserModel user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentContactBinding.inflate(inflater, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Contacts");


        binding.contactSearchView.setOnQueryTextListener(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = FirebaseDatabase.getInstance();
        arrayList = new ArrayList<>();

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(UserModel.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        usersAdapter = new ContactAdapter(requireContext(), arrayList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerViewContact.setLayoutManager(layoutManager);
        binding.recyclerViewContact.setHasFixedSize(true);
        binding.recyclerViewContact.setAdapter(usersAdapter);
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot snapshots : snapshot.getChildren()) {
                    UserModel user = snapshots.getValue(UserModel.class);
                    if (!user.getuID().equals(FirebaseAuth.getInstance().getUid()))
                        arrayList.add(user);

                }
                binding.recyclerViewContact.setHasFixedSize(true);
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        String currentUid = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentUid).setValue("Online");
    }

    @Override

    public void onPause() {
        super.onPause();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Offline");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (contactAdapter != null)
            contactAdapter.getFilter().filter(newText);
        return false;
    }
}