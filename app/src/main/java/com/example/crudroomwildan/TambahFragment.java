package com.example.crudroomwildan;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.crudroomwildan.roomdb.AppDatabase;
import com.example.crudroomwildan.roomdb.Mahasiswa;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TambahFragment extends Fragment {


    //Deklarasi Variable
    private TextInputEditText NIM, Nama, Jurusan, Alamat, tanggalLahir;
    private AppDatabase database;
    private Button bSimpan, bLihatData;
    private RadioButton lakiLaki, perempuan;
    private String myJenisKelamin;
    DatePickerDialog tgllahir;
    SimpleDateFormat tglformat;
    Context context;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tambah, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        v = view;
        NIM = v.findViewById(R.id.nim);
        Nama = v.findViewById(R.id.nama);
        Jurusan = v.findViewById(R.id.jurusan);
        Alamat = v.findViewById(R.id.alamat);
        tanggalLahir = v.findViewById(R.id.tanggal_lahir);
        tglformat = new SimpleDateFormat("dd-MM-yyyy");
        tanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDataDialog();
            }
        });

        lakiLaki = v.findViewById(R.id.laki_laki);
        perempuan = v.findViewById(R.id.perempuan);
        bSimpan = v.findViewById(R.id.save);


        bSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Mengecek Data NIM dan Nama
                if (NIM.getText().toString().isEmpty() || Nama.getText().toString().isEmpty()) {
                    Toast.makeText(v.getContext(), "NIM atau Nama Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                } else {
                    //Membuat Instance/Objek Dari Class Entity Mahasisiwaa
                    Mahasiswa data = new Mahasiswa();

                    //Memasukan data yang diinputkan user pada database
                    data.setNim(NIM.getText().toString());
                    data.setNama(Nama.getText().toString());
                    data.setJurusan(Jurusan.getText().toString());
                    data.setAlamat(Alamat.getText().toString());
                    data.setTanggalLahir(tanggalLahir.getText().toString());
                    data.setJenisKelamin(myJenisKelamin);
                    insertData(data);

                    //Mengembalikan EditText menjadi seperti semula\
                    NIM.setText("");
                    Nama.setText("");
                    Jurusan.setText("");
                    Alamat.setText("");
                    tanggalLahir.setText("");
                }


            }
        });

        //Menentukan Jenis Kelamin pada Data Mahasiswa Baru
        if (lakiLaki.isChecked()) {
            myJenisKelamin = "Laki-Laki";
        } else if (perempuan.isChecked()) {
            myJenisKelamin = "Perempuan";
        }

        //Inisialisasi dan memanggil Room Database
        database = Room.databaseBuilder(
                v.getContext().getApplicationContext(),
                AppDatabase.class,
                "dbMahasiswa") //Nama File Database yang akan disimpan
                .build();


    }

    private void showDataDialog() {
        Calendar kalender = Calendar.getInstance();
        tgllahir = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newData = Calendar.getInstance();
                newData.set(year, month, dayOfMonth);
                tanggalLahir.setText(tglformat.format(newData.getTime()));
            }
        }, kalender.get((Calendar.YEAR)), kalender.get(Calendar.MONTH), kalender.get(Calendar.DAY_OF_MONTH));
        tgllahir.show();

    }


    //Menjalankan method Insert Data
    @SuppressLint("StaticFieldLeak")
    private void insertData(final Mahasiswa mahasiswa) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                //Menjalankan proses insert data
                return database.mahasiswaDAO().insertMahasiswa(mahasiswa);
            }

            @Override
            protected void onPostExecute(Long status) {
                //Menandakan bahwa data berhasil disimpan
                Toast.makeText(v.getContext(), "Status Row " + status, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

}