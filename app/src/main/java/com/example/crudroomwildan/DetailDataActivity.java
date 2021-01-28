package com.example.crudroomwildan;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.crudroomwildan.roomdb.Mahasiswa;

public class DetailDataActivity extends AppCompatActivity {

    EditText getNIM,  getName, getJurusan, getAlamat, getTanggalLahir, getJenisKelamin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data);

        getNIM = findViewById(R.id.myNIM);
        getName = findViewById(R.id.myName);
        getJurusan = findViewById(R.id.myJurusan);
        getAlamat = findViewById(R.id.myAlamat);
        getTanggalLahir = findViewById(R.id.myTanggalLahir);
        getJenisKelamin = findViewById(R.id.myJenisKelamin);

        getDetailData();
    }

    //Mendapatkan data yang akan ditampilkan secara detail
    private void getDetailData(){
        Mahasiswa mahasiswa = (Mahasiswa)getIntent().getSerializableExtra("detail");

        //Menampilkan data Mahasiswa pada activity
        if(mahasiswa != null){
            getNIM.setText(mahasiswa.getNim());
            getName.setText(mahasiswa.getNama());
            getJurusan.setText(mahasiswa.getJurusan());
            getAlamat.setText(mahasiswa.getAlamat());
            getTanggalLahir.setText(mahasiswa.getTanggalLahir());
            getJenisKelamin.setText(mahasiswa.getJenisKelamin());
        }
    }
}