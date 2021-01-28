package com.example.crudroomwildan;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.crudroomwildan.roomdb.AppDatabase;
import com.example.crudroomwildan.roomdb.Mahasiswa;

import java.util.ArrayList;
import java.util.Arrays;

public class AlumniFragment extends Fragment {

    //Deklarasi Variable
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private AppDatabase database;
    private ArrayList<Mahasiswa> daftarMahasiswa;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alumni, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = view.findViewById(R.id.dataItem);

        //Inisialisasi ArrayList
        daftarMahasiswa = new ArrayList<>();

        //Inisialisasi RoomDatabase
        database = Room.databaseBuilder(view.getContext().getApplicationContext(),
                AppDatabase.class, "dbMahasiswa").allowMainThreadQueries().build();

        /*
         * Mengambil data Mahasiswa dari Database
         * lalu memasukannya ke kedalam ArrayList (daftarMahasiswa)
         */
        daftarMahasiswa.addAll(Arrays.asList(database.mahasiswaDAO().readDataMahasiswa()));

        //Agar ukuran RecyclerView tidak berubah
        recyclerView.setHasFixedSize(true);

        //Menentukan bagaimana item pada RecyclerView akan tampil
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        //Mamasang adapter pada RecyclerView
        adapter = new AdapterMahasiswa(daftarMahasiswa, getContext());
        recyclerView.setAdapter(adapter);
    }
}