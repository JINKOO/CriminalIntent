package com.kjk.criminalintent.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.kjk.criminalintent.data.Crime
import com.kjk.criminalintent.data.CrimeDetailViewModel
import com.kjk.criminalintent.databinding.FragmentCrimeBinding
import java.util.*

// Controller (Model과 View와 상호 작용한다.)
// 특정 범죄의 상세내역 조회, 수정 기능 제공
class CrimeFragment :
    Fragment(),
    DatePickerFragment.CallBacks {

    private var _binding: FragmentCrimeBinding? = null

    private val binding
        get() = _binding!!

    private val crimeDetailViewModel: CrimeDetailViewModel by lazy {
        ViewModelProvider(this@CrimeFragment).get(CrimeDetailViewModel::class.java)
    }

    private lateinit var crime: Crime

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach(): ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(): ")
        crime = Crime()

        val crimeID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        Log.d(TAG, "onCreate: ${crimeID}")

        // 궁극적으로는 database로 부터 data를 로드해야 한다.
        crimeDetailViewModel.loadCrime(crimeID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView(): ")
        _binding = FragmentCrimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")

        crimeDetailViewModel.crimeLiveData.observe(viewLifecycleOwner) { crime ->
            crime?.let {
                this.crime = crime
                updateUI()
            }
        }

        // Fragment Result API를 사용해서 DatePickerFragment로 부터 변경된 날짜를 받아온다.
        // setTargetFragment가 deprecated됨.
        setFragmentResultListener(REQUEST_TIME) { requestKey, bundle ->
            val result = bundle.getSerializable("result_time") as Date
            crime.date = result
            updateUI()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated: ")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // 현재 여기서는 구현하지 않아도 된다.
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, end: Int, count: Int) {
                crime.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                // 현재 여기서는 구현하지 않아도 된다.
            }
        }

        binding.apply {
            crimeTitleEditText.addTextChangedListener(titleWatcher)
            crimeSolvedCheckBox.setOnCheckedChangeListener { compoundButton, isChecked ->
                crime.isSolved = isChecked
            }

            // 날짜 수정을 위한 버튼 클릭이벤트,
            // 범죄가 일어난 날짜 버튼을 클릭하면, DatePicker가 출력되도록한다.
            crimeDateButton.setOnClickListener {
                DatePickerFragment.newInstance(crime.date).apply {
                    // null이 아닌 FragmentManager를 호출한다.
                    // 다른 모든 fragment처럼 dialog fragment 인스턴스 역시
                    // hosting activity가 관리한다.
                    setTargetFragment(this@CrimeFragment, 0)
                    show(this@CrimeFragment.parentFragmentManager, DIALOG_DATE)
                }
            }

            // 시간 수정을 위한 이벤트
            crimeTimeButton.setOnClickListener {
                TimePickerFragment.newInstance(crime.date).apply {
                    show(this@CrimeFragment.parentFragmentManager, DIALOG_TIME)
                }
            }
        }
    }

    private fun updateUI() {
        binding.run {
            crimeTitleEditText.setText(crime.title)
            crimeDateButton.text = crime.date.toString()
            crimeTimeButton.text = crime.date.toString()
            crimeSolvedCheckBox.run {
                isChecked = crime.isSolved
                jumpDrawablesToCurrentState()
            }
        }
    }

    override fun onDateSelected(date: Date) {
        crime.date = date
        updateUI()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
        crimeDetailViewModel.saveCrime(crime)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: ")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach: ")
    }

    companion object {
        private const val TAG = "CrimeFragment"
        private const val ARG_CRIME_ID = "crime_id"
        private const val DIALOG_DATE = "DialogDate"
        private const val DIALOG_TIME = "DialogTime"
        const val REQUEST_DATE = "request_date"
        const val REQUEST_TIME = "request_time"
        fun newInstance(crimeID: UUID): CrimeFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeID)
            }
            return CrimeFragment().apply {
                arguments = args
            }
        }
    }
}