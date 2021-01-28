package com.example.crudroomwildan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.crudroomwildan.roomdb.AppDatabase;
import com.example.crudroomwildan.roomdb.Mahasiswa;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class EditActivity extends AppCompatActivity {

    //Deklarasi Variable
    private TextInputEditText Nama, Jurusan, Alamat, tanggalLahir;
    private AppDatabase database;
    private Button bSimpan;
    private RadioButton lakiLaki, perempuan;
    private RadioGroup jenisKelamin;
    private String myJenisKelamin;
    private Mahasiswa mahasiswa;
    DatePickerDialog tgllahir;
    SimpleDateFormat tglformat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Nama = findViewById(R.id.nama);
        Jurusan = findViewById(R.id.jurusan);
        Alamat = findViewById(R.id.alamat);
        tanggalLahir = findViewById(R.id.tanggal_lahir);
        tglformat = new SimpleDateFormat("dd-MM-yyyy");
        tanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDataDialog();
            }
        });
        jenisKelamin = findViewById(R.id.jenis_kelamin);
        lakiLaki = findViewById(R.id.laki_laki);
        perempuan = findViewById(R.id.perempuan);
        bSimpan = findViewById(R.id.save);

        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "dbMahasiswa").build();

        //Menampilkan data mahasiswa yang akan di edit
        getDataMahasiswa();

        bSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mahasiswa.setNama(Nama.getText().toString());
                mahasiswa.setJurusan(Jurusan.getText().toString());
                mahasiswa.setTanggalLahir(tanggalLahir.getText().toString());
                mahasiswa.setJenisKelamin(myJenisKelamin);
                updateData(mahasiswa);
            }
        });
    }

    private void showDataDialog() {
        Calendar kalender = Calendar.getInstance();
        tgllahir = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newData = Calendar.getInstance();
                newData.set(year, month, dayOfMonth);
                tanggalLahir.setText(tglformat.format(newData.getTime()));
            }
        }, kalender.get((Calendar.YEAR)),kalender.get(Calendar.MONTH),kalender.get(Calendar.DAY_OF_MONTH));
        tgllahir.show();
    }

    //Method untuk menapilkan data mahasiswa yang akan di edit
    private void getDataMahasiswa(){
        //Mendapatkan data dari Intent
        mahasiswa = (Mahasiswa)getIntent().getSerializableExtra("data");

        Nama.setText(mahasiswa.getNama());
        Jurusan.setText(mahasiswa.getJurusan());
        tanggalLahir.setText(mahasiswa.getTanggalLahir());
        switch (mahasiswa.getJenisKelamin()){
            case "Laki-Laki":
                lakiLaki.setChecked(true);
                myJenisKelamin = "Laki-Laki";
                break;

            case "Perempuan":
                perempuan.setChecked(true);
                myJenisKelamin = "Perempuan";
                break;
        }
    }

    //Menjalankan method Update Data
    @SuppressLint("StaticFieldLeak")
    private void updateData(final Mahasiswa mahasiswa){
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                //Menjalankan proses insert data
                return database.mahasiswaDAO().updateMahasiswa(mahasiswa);
            }

            @Override
            protected void onPostExecute(Integer status) {
                //Menandakan bahwa data berhasil disimpan
                Toast.makeText(EditActivity.this, "Data Berhasil Diubah", Toast.LENGTH_SHORT).show();
                finish();
            }
        }.execute();
    }

}