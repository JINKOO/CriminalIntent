package com.kjk.criminalintent.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.*

@Entity
// Entity : 해당 Annotation은 클래스 수준에 적용된다.
// CrimeEntity Class가 데이터베이스의 테이블 구조 정의.
// 테이블에서 각 칼럼은 CrimeEntity instance를 나타낸다.
data class Crime(
    // UUID :: Universally Unique Identifier, 128bit의 고유값.
    @PrimaryKey val id: UUID = UUID.randomUUID(), // 범죄 고유 ID
    var title: String = "",  // 제목
    var date: Date = Date(), // 발생 일자
    var isSolved: Boolean = false // 해결 여부
//    var requiresPolice: Boolean = false // 심각한 범죄여부
)