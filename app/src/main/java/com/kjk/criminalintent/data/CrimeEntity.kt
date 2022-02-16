package com.kjk.criminalintent.data

import java.util.*

data class CrimeEntity(
    // UUID :: Universally Unique Identifier, 128bit의 고유값.
    val id: UUID = UUID.randomUUID(), // 범죄 고유 ID
    var title: String = "",  // 제목
    var date: Date = Date(), // 발생 일자
    var isSolved: Boolean = false, // 해결 여부
    var requiresPolice: Boolean = false // 심각한 범죄여부
)