package com.kjk.criminalintent.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kjk.criminalintent.data.Crime
import java.util.*

// Database 테이블의 data에 access하려면, DAO를 생성.
// 데이터베이스 작업을 수행하는 함수들을 포함하는 interface이다.
/*
   LiveData
   jetpack의 life-extension 라이브러리에 있는 데이터 홀더 class이다.
   Room에서 LiveData를 사용한다.

   LiveData의 목적
   1. 앱의 서로 다른 부분 간에서 데이터 전달을 쉽게 만드는 것이다.
        예를 들어, 범죄 데이터를 보여주는 CrimeListFragment로 CrimeRepository에서 data를 전달하는 경우.
   2. thread간에도 data를 전달 가능하다. 즉, 백그라운드 스레드 -> 메인 스레드로 data전달 가능하다.

   Room DAO 쿼리에서 LiveData를 return하도록 구현하면,
   Room이 백그라운드 스레드에서 쿼리 작업을 자동으로 실행한 후, 그 결과를 LiveData로 반환한다.
   쿼리가 완료되면, 데이터가 메인 스레드로 전달된다.
   따라서 Activity, Fragment에서는 LiveData를 관찰(Observe)하도록 구성하면 된다.
   LiveData가 준비되면, Observing 하고 있는 Activity, Fragment로 통보된다.

 */
@Dao
interface CrimeDAO {
    @Query("SELECT * FROM Crime")
    fun getCrimes(): LiveData<List<Crime>>

    @Query("SELECT * FROM Crime WHERE id=(:id)")
    fun getCrime(id: UUID): LiveData<Crime?>

    @Update
    fun updateCrime(crime: Crime)

    @Insert
    fun addCrime(crime: Crime)

    @Delete
    fun deleteCrime(crime: Crime)
}