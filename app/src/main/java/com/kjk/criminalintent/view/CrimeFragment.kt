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
import androidx.lifecycle.ViewModelProvider
import com.kjk.criminalintent.data.Crime
import com.kjk.criminalintent.data.CrimeDetailViewModel
import com.kjk.criminalintent.databinding.FragmentCrimeBinding
import java.util.*

// Controller (Model과 View와 상호 작용한다.)
// 특정 범죄의 상세내역 조회, 수정 기능 제공
class CrimeFragment : Fragment() {

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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView(): ")
        _binding = FragmentCrimeBinding.inflate(inflater, container, false)

        binding.crimeDateButton.apply {
            text = crime.date.toString()
            isEnabled = false
        }

        return binding.root
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
        }
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