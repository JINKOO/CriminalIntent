package com.kjk.criminalintent.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.kjk.criminalintent.data.database.CrimeDataBase
import java.lang.IllegalStateException
import java.util.*

// Repository Pattern
// 데이터베이스 액세스를 위해서, 구글의 앱 아키텍쳐 권장하는 레포지토리 패턴을 따른다.
// Repository Pattern에서의 Repository Class이다.
// 데이터 저장소를 구현, 단일 또는 여러 소스로부터 '데이터 액세스'하는 로직을 캡슐화
// (로컬 데이터베이스, 원격 서버로 부터 특정 데이터 셋을 가져오거나, 저장하는 방법을 결정)
// UI 코드(View)에서는 Repository에 모든 data를 요청.
// 이때, View는 데이터 저장 방식, 가져오는 방식에 대해서는 관여하지 않는다.
// 따라서 이런일은 Repository에서 구현해야한다.
// SingleTon Pattern이다.
class CrimeRepository private constructor(context: Context) {

    private val database: CrimeDataBase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDataBase::class.java,
        DATABASE_NAME
    ).build()

    private val crimeDao = database.crimeDAO()

    // LiveData
    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()
    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)

    companion object {
        private const val DATABASE_NAME = "crime-database"
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException(
                "CrimeRepository must be initialized"
            )
        }
    }
}