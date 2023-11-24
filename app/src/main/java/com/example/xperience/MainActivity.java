package com.example.xperience;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    AlertDialog dialog;
    SearchView searchView;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        dialog = builder.create();

        dataList = new ArrayList<>();
        adapter = new MyAdapter(MainActivity.this, dataList);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
        recyclerView.setAdapter(adapter);

        searchView = findViewById(R.id.buscar);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Android");

        setupDatabaseListener();
    }

    private void setupDatabaseListener() {
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    Log.d("MainActivity", "onDataChange: datos actualizados");
                    dataList.clear();
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                        if (dataClass != null) {
                            dataClass.setKey(itemSnapshot.getKey());
                            dataList.add(dataClass);
                            adapter.notifyDataSetChanged();
                            Log.d("MainActivity", "onDataChange: notifyDataSetChanged llamado");
                        } else {
                            Log.e("MainActivity", "dataClass is null for key: " + itemSnapshot.getKey());
                        }
                    }
                    dialog.dismiss();
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e("MainActivity", "Error in onDataChange", e);
                    dialog.dismiss();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity", "Database error: " + error.getMessage());
                dialog.dismiss();
            }
        });
    }

    public void searchList(String text) {
        ArrayList<DataClass> searchList = new ArrayList<>();
        for (DataClass dataClass : dataList) {
            if (dataClass.getTitulo().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }
}
