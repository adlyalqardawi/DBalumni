package com.example.crudroomwildan;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.crudroomwildan.R;
import com.example.crudroomwildan.roomdb.AppDatabase;
import com.example.crudroomwildan.roomdb.Mahasiswa;

import java.util.ArrayList;

public class AdapterMahasiswa extends RecyclerView.Adapter<AdapterMahasiswa.ViewHolder> {

    //Deklarasi Variable
    private ArrayList<Mahasiswa> daftarMahasiswa;
    private AppDatabase appDatabase;
    private Context context;

    public AdapterMahasiswa(ArrayList<Mahasiswa> daftarMahasiswa, Context context) {

        //Inisialisasi data yang akan digunakan
        this.daftarMahasiswa = daftarMahasiswa;
        this.context = context;

        appDatabase = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class, "dbMahasiswa").allowMainThreadQueries().build();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        //Deklarasi View yang akan digunakan
        private TextView Nim, Nama;
        private CardView item;

        ViewHolder(View itemView) {
            super(itemView);
            Nim = itemView.findViewById(R.id.nim);
            Nama = itemView.findViewById(R.id.nama);
            item = itemView.findViewById(R.id.cvMain);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inisialisasi Layout Item untuk RecyclerView
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        //Deklarasi Variable untuk mendapatkan data dari Database melalui Array
        String getNIM = daftarMahasiswa.get(position).getNim();
        String getNama = daftarMahasiswa.get(position).getNama();

        //Menampilkan data berdasarkan posisi Item dari RecyclerView
        holder.Nim.setText(getNIM);
        holder.Nama.setText(getNama);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mahasiswa mahasiswa = appDatabase.mahasiswaDAO()
                        .selectDetailMahasiswa(daftarMahasiswa.get(position).getNim());
                context.startActivity(new Intent(context, DetailDataActivity.class).putExtra("detail", mahasiswa));

            }
        });

        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CharSequence[] menuPilihan = {"Edit", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext())
                        .setTitle("Pilih Aksi")
                        .setItems(menuPilihan, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                    /*
                                     Menjalankan Perintah Edit Data
                                     Menggunakan Bundle untuk mengambil data yang akan Diedit
                                     */
                                        onEditData(position, context);
                                        break;

                                    case 1:
                                    /*
                                     Menjalankan Perintah Delete Data
                                     Akan dibahas pada Tutorial selanjutnya
                                     */
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                        alertDialogBuilder.setMessage("Are you sure delete ?");
                                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                onDeleteData(position);
                                                Toast.makeText(context, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        alertDialogBuilder.create();
                                        alertDialogBuilder.show();
                                        break;

                                }
                            }
                        });
                dialog.create();
                dialog.show();
                return true;
            }
        });
    }

    //Mengirim Data yang akan diedit dari ArrayList berdasarkan posisi item pada RecyclerView
    private void onEditData(int position, Context context) {
        context.startActivity(new Intent(context, EditActivity.class).putExtra("data", daftarMahasiswa.get(position)));
    }

    //Menghapus Data dari Room Database yang dipilih oleh user
    private void onDeleteData(int position) {
        appDatabase.mahasiswaDAO().deleteMahasiswa(daftarMahasiswa.get(position));
        daftarMahasiswa.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, daftarMahasiswa.size());
        Toast.makeText(context, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getItemCount() {
        //Menghitung data / ukuran dari Array
        return daftarMahasiswa.size();
    }
}