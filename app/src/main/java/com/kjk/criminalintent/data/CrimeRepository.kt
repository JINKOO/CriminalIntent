package com.kjk.criminalintent.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.kjk.criminalintent.data.database.CrimeDataBase
import com.kjk.criminalintent.data.database.migration_1_2
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

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
    )//.build()
        .addMigrations(migration_1_2) // CrimeEntity Model계층에서 suspect 칼럼 값 추가로, Room에 새 버전의 데이터베이스를 생성하기 위해.
        .build()

    private val crimeDao = database.crimeDAO()
    // LiveData를 반환하는 Dao함수들은 Room이 백그라운드 스레드에서 자동 실행하고, 메인 스레드로 데이터를 전달하기 때문에 UI를 변경할 수 있다.
    // 하지만, 변경, 삽입에서는 Room이 백그라운드 스레드로 자동 실행하지 못한다. 따라서, 백그라운드 스레드로 변경, 삽입하는 함수들을 호출해야한다. --> exctutor사용
    // 새로운 백그라운드 스레드를 사용해 블럭의 코드를 수행한다. main스레드를 방해하지 않고, 안전하게 데이터베이스 작업을 할 수 있다.
    private val executor = Executors.newSingleThreadExecutor()

    // LiveData
    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()
    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)

    fun updateCrime(crime: Crime) {
        executor.execute {
            crimeDao.updateCrime(crime)
        }
    }

    // 14장 새로운 범죄 데이터를 추가한다.
    fun addCrime(crime: Crime) {
        executor.execute {
            crimeDao.addCrime(crime)
        }
    }

    fun deleteCrime(crime: Crime) {
        executor.execute {
            crimeDao.deleteCrime(crime)
        }
    }


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