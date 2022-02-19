package com.kjk.criminalintent.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kjk.criminalintent.data.Crime

// 데이터베이스 클래스
@Database(entities = [Crime::class], version = 1)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDataBase : RoomDatabase() {
    abstract fun crimeDAO(): CrimeDAO
}