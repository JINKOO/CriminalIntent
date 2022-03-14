package com.kjk.criminalintent.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.kjk.criminalintent.R
import com.kjk.criminalintent.data.Crime
import com.kjk.criminalintent.data.CrimeDetailViewModel
import com.kjk.criminalintent.databinding.FragmentCrimeBinding
import com.kjk.criminalintent.extension.Date.FORTMAT_LONGLONG
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

    private lateinit var getResultSuspect: ActivityResultLauncher<Intent>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach(): ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(): ")
        setHasOptionsMenu(true)
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

        getResultSuspect = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val contactUri = result.data?.data ?: return@registerForActivityResult

                // 쿼리에서 값으로 반환할 필드를 지정한다.
                // TODO 15장 챌린지 :: 또 다른 암시적 인텐트
                val queryFields = arrayOf(
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
                )

                // 쿼리를 수행한다. contactUri는 콘텐츠 제공자의 테이블을 나타낸다.
                val cursor = requireActivity().contentResolver
                    .query(contactUri, queryFields, null, null, null)

                cursor?.use {
                    // 쿼리 결과 데이터가 있는지 확인한다.
                    if (it.count == 0) {
                        return@registerForActivityResult
                    }

                    // 첫번째 데이터 행의 첫번째 속상 값을 가져 온다.
                    // 이 값이 용의자의 이름이다.
                    it.moveToFirst()
                    val suspectName = it.getString(0)
                    val suspectPhone = it.getString(1)
                    Log.d(TAG, "size: ${cursor.count}, name: ${suspectName}, phoneNum : ${suspectPhone}")
                    crime.suspect = suspectName
                    crimeDetailViewModel.saveCrime(crime)
                    binding.selectSuspectButton.text = suspectName
                }
            }
        }

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

            // 범죄 보고서 전송을 위한 이벤트
            sendCriminalReportButton.setOnClickListener {
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT,  // 메세지의 데이터
                        getCrimeReport())
                    putExtra(
                        Intent.EXTRA_SUBJECT, // 메세지의 제목
                        getString(R.string.crime_report_subject)
                    )
                }.also { intent ->  // also 사용하는 경우 : 객체의 속성을 전혀 사용하지 않거나, 변경하지 않는 경우에 사용한다.
                    //startActivity(intent)
                    // 사용자에게 어떤 액티비티를 사용할 것인지, 선택권을 줄 수 있도록 한다.
                    // 암시적 인텐트에 응답하는 액티비티들을 보여줄 선택기를 생성하는 코드
                    val chooserIntent = Intent.createChooser(intent, getString(R.string.send_report))
                    startActivity(chooserIntent)
                }
            }

            // 연락처에서 용의자 선택
            selectSuspectButton.apply {
                val pickContactIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)

                setOnClickListener {
                    getResultSuspect.launch(pickContactIntent)
                }

                // 일부 사용자 장치에는 연락처 앱이 없을 수 있다.
                // 이때는 버그(안드로이드 OS가 일치하는 액티비티를 찾을 수 없으므로, 앱이 중단)
                // 연락처 앱이 없는 장치의 방어코드
                val packageManager: PackageManager = requireActivity().packageManager
                val resolvedActivity: ResolveInfo? = packageManager.resolveActivity(
                    pickContactIntent,
                    PackageManager.MATCH_DEFAULT_ONLY // CATEGORY_DEFAULT가 메니페스트의 인텐트 필터에 정의된 액티비티 찾는다.
                )

                // 찾은 액티비티가 없다면(여기서는 연락처 앱) '용의자 선택' 버튼 비활성화
                if (resolvedActivity == null) {
                    isEnabled = false
                }
            }

            // 용의자에게 바로 전화 걸기
            callSuspectImageButton.apply {

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

    // 15장 범죄 보고서 전송(암시적 Intent)
    private fun getCrimeReport(): String {
        val solvedString = if (crime.isSolved) {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }

        val dateString = DateFormat.format(FORTMAT_LONGLONG, crime.date).toString()

        val suspect = if (crime.suspect.isBlank()) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect, crime.suspect)
        }

        return getString(R.string.crime_report,
            crime.title, dateString, solvedString, suspect)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.delete_crime -> {
                // database에서 삭제
                crimeDetailViewModel.deleteCrime(crime)
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
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
        const val REQUEST_SUSPECT = "request_suspect"
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