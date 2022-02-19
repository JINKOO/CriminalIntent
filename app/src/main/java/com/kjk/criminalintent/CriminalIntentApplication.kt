package com.kjk.criminalintent

import android.app.Application
import com.kjk.criminalintent.data.CrimeRepository

/**
 *  Application Class
 *  어느 컴포넌트(액티비티, 서비스, 인텐트 등) 어디에서나 공유 할 수 있 전역 클래스이다.
 *  Application Class를 상속받는 클래스에서는 공동으로 관리해야하는 data들을 작성하기 적합한 곳이다.
 *  첫번째 액티비티보다 먼저 인스턴스화 된다.
 */
class CriminalIntentApplication : Application() {

    // 앱이 최초로 메모리에 로드될 때, 안드로이드 시스템이 자동으로 호출한다.
    // 한번만 초기화되는 작업을 수행할 때, 적합하다.
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}