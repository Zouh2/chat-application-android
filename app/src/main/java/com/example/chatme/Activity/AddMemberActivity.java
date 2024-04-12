package com.example.chatme.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.chatme.Adapter.GroupContactAdapter;
import com.example.chatme.Adapter.SelectedContactAdapter;
import com.example.chatme.GroupModel;
import com.example.chatme.Interface.ContactItemInterface;
import com.example.chatme.Permissions.Permissions;
import com.example.chatme.UserModel;
import com.example.chatme.databinding.ActivityAddMemberBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddMemberActivity extends AppCompatActivity implements ContactItemInterface {

    private ActivityAddMemberBinding binding;
    private Permissions permissions;
    private DatabaseReference databaseReference;
    private GroupContactAdapter groupContactAdapter;
    private SelectedContactAdapter selectedContactAdapter;
    ArrayList<UserModel> appContacts, selectedContacts;
    String userPhoneNumber;
    FirebaseAuth firebaseAuth;
    private GroupModel groupModel;
    private FirebaseDatabase database;

    UserModel user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMemberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        groupModel = getIntent().getParcelableExtra("groupModel");


        permissions = new Permissions();
        firebaseAuth = FirebaseAuth.getInstance();
        userPhoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();
        groupContactAdapter = new GroupContactAdapter(this);
        selectedContactAdapter = new SelectedContactAdapter(this, this);

        selectedContacts = new ArrayList<>();

        binding.recyclerViewContact.setLayoutManager(new LinearLayoutManager(this));

        binding.selectedRecyclerView.setLayoutManager(new
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        binding.recyclerViewContact.setAdapter(groupContactAdapter);

        binding.selectedRecyclerView.setAdapter(selectedContactAdapter);

        selectedContactAdapter.setUserModels(selectedContacts);

        //getUserContacts();


        database = FirebaseDatabase.getInstance();
        appContacts = new ArrayList<>();


        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                selectedContacts.clear();
                for (DataSnapshot snapshots : snapshot.getChildren()) {
                    UserModel user = snapshots.getValue(UserModel.class);
                    if (!user.getuID().equals(FirebaseAuth.getInstance().getUid()))
                        selectedContacts.add(user);

                }
                binding.recyclerViewContact.setHasFixedSize(true);
                groupContactAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

  /*  private void getUserContacts() {


        if (permissions.isContactOk(this)) {
            ArrayList<UserModel> userContacts = new ArrayList<>();
            String[] projection = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
            };
            ContentResolver cr = getContentResolver();
            Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
            if (cursor != null) {
                userContacts.clear();
                try {


                    while (cursor.moveToNext()) {

                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        number = number.replaceAll("\\s", "");
                        String num = String.valueOf(number.charAt(0));

                        if (num.equals("0"))
                            number = number.replaceFirst("(?:0)+", "+92");

                        UserModel userModel = new UserModel();
                        userModel.setName(name);
                        userModel.setNumber(number);
                        userContacts.add(userModel);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            cursor.close();
            getAppContacts(userContacts);

        } else {
            permissions.requestStorage(this);
        }
    }

    private void getAppContacts(final ArrayList<UserModel> mobileContacts) {

        appContacts = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.orderByChild("number");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    appContacts.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String number = ds.child("number").getValue().toString();
                        String userId = ds.child("uID").getValue().toString();


                        for (UserModel userModel : mobileContacts) {

                            if (userModel.getNumber().equals(number) && !number.equals(userPhoneNumber)) {

                                for (GroupMemberModel memberModel : groupModel.members) {

                                    if (!userId.equals(memberModel.id)) {

                                        String image = ds.child("image").getValue().toString();
                                        String status = ds.child("status").getValue().toString();
                                        String uID = ds.child("uID").getValue().toString();

                                        String name = ds.child("name").getValue().toString();
                                        UserModel registeredUser = new UserModel();

                                        registeredUser.setName(name);
                                        registeredUser.setStatus(status);
                                        registeredUser.setImage(image);
                                        registeredUser.setuID(uID);

                                        appContacts.add(registeredUser);

                                    }
                                }


                                break;
                            }
                        }
                    }
                    groupContactAdapter.setArrayList(appContacts);


                } else
                    Toast.makeText(AddMemberActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AllConstants.CONTACTS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserContacts();
            } else {
                Toast.makeText(this, "Contact Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
*/
    @Override
    public void onContactClick(UserModel userModel, int position, boolean isSelect) {

        if (isSelect) {
            selectedContacts.remove(userModel);
            selectedContactAdapter.setUserModels(selectedContacts);
        } else {
            if (!selectedContacts.contains(userModel)) {
                selectedContacts.add(userModel);
                selectedContactAdapter.setUserModels(selectedContacts);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra("groupModel", groupModel);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }
}