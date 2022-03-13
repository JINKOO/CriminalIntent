package com.kjk.criminalintent.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kjk.criminalintent.data.Crime

// 데이터베이스 클래스
@Database(entities = [Crime::class], version = 2)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDataBase : RoomDatabase() {
    abstract fun crimeDAO(): CrimeDAO
}

// suspect 칼럼 속성이 추가되었기 때문에, Room이 새 버전의 데이터베이스를 Migration해야한다
// 생성된 Migration객체는 데이터베이스를 생성할 때, Room에 제공해야한다.
val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE Crime ADD COLUMN suspect TEXT NOT NULL DEFAULT ''"
        )
    }
}