package com.example.chatme.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.chatme.Fragment.ChatFragment;
import com.example.chatme.Fragment.ContactFragment;
import com.example.chatme.Fragment.GroupFragment;
import com.example.chatme.Fragment.ProfileFragment;
import com.example.chatme.R;
import com.example.chatme.Utils.Util;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;




public class DashBoard extends AppCompatActivity {

    private ChipNavigationBar navigationBar;
    Fragment fragment = null;
    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        util = new Util();
        // Get the reference to your bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

// Set the item selected listener for the bottom navigation view
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Get the ID of the selected menu item
                int id = item.getItemId();

                // Get the reference to your fragment container view
                FragmentContainerView fragmentContainerView = findViewById(R.id.fragment_container_view);


                // Get the reference to the fragment manager
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Begin a transaction to replace the fragment in the container view
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Create an instance of the fragment to be displayed
                Fragment fragment;

                // Use a switch statement to determine which fragment to display based on the selected menu item ID
                switch (id) {

                    case R.id.chat:
                        fragment = new ChatFragment();
                        break;
                    case R.id.contacts:
                        fragment = new ContactFragment();
                        break;
                    case R.id.profile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.group:
                        fragment = new GroupFragment();
                        break;
                    default:
                        fragment = new ChatFragment();
                        break;
                }

                // Replace the current fragment in the container view with the selected fragment
                transaction.replace(fragmentContainerView.getId(), fragment);

                // Add the transaction to the back stack to enable back navigation
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                // Return true to indicate that the item has been selected
                return true;
            }
        });


    }

    @Override
    protected void onResume() {
        util.updateOnlineStatus("online");
        super.onResume();
    }

    @Override
    protected void onPause() {
        util.updateOnlineStatus(String.valueOf(System.currentTimeMillis()));
        super.onPause();
    }


}
