package com.kjk.criminalintent.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.util.*

/**
 *  해당 Fragment에서 DatePickerDialog 인스턴스를 생성한다.
 */
class DatePickerFragment : DialogFragment() {

    interface CallBacks {
        fun onDateSelected(date: Date)
    }

    // 오늘 날짜로 초기화 되는 DatePickerDialog인스턴스를 생성한다.
    // DialogFragment를 보여주려고, Hosting activity가 이 함수를 호출한다.
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d(TAG, "onCreateDialog: ")

        val dateListener = DatePickerDialog.OnDateSetListener { _:DatePicker, year: Int, month: Int, day: Int ->
            val resultDate = GregorianCalendar(year, month, day).time
            // targetFragment 여기서는 CrimeFragment를 가진다.
            targetFragment?.let {
                (it as CallBacks).onDateSelected(resultDate)
            }
        }

        val date = arguments?.getSerializable(ARG_DATE) as Date
        val calendar = Calendar.getInstance()
        calendar.time = date

        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }

    companion object {
        private const val TAG = "DatePickerFragment"
        const val ARG_DATE = "date"

        // 생성자를 통한 호출이 아닌, 다음과 같이 newInstance를 사용한다.
        fun newInstance(date: Date): DatePickerFragment {
            Log.d(TAG, "newInstance: ")
            val args = Bundle().apply { 
                putSerializable(ARG_DATE, date)
            }
            
            return DatePickerFragment().apply { 
                arguments = args
            }
        }
    }
}