package com.example.crudroomwildan.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MahasiswaDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMahasiswa(Mahasiswa mahasiswa);

    @Query("SELECT * FROM tMahasiswa")
    Mahasiswa[] readDataMahasiswa();

    @Update
    int updateMahasiswa(Mahasiswa mahasiswa);

    @Delete
    void deleteMahasiswa(Mahasiswa mahasiswa);

    @Query("SELECT * FROM tMahasiswa WHERE nim_mahasiswa = :nim LIMIT 1")
    Mahasiswa selectDetailMahasiswa(String nim);

}
