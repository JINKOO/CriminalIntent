package com.kjk.criminalintent.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kjk.criminalintent.data.CrimeEntity
import com.kjk.criminalintent.databinding.FragmentCrimeBinding

// Controller (Model과 View와 상호 작용한다.)
// 특정 범죄의 상세내역 조회, 수정 기능 제공
class CrimeFragment : Fragment() {

    private var _binding: FragmentCrimeBinding? = null

    private val binding
        get() = _binding!!

    private lateinit var crime: CrimeEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crime = CrimeEntity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView()")
        _binding = FragmentCrimeBinding.inflate(inflater, container, false)

        binding.crimeDateButton.apply {
            text = crime.date.toString()
            isEnabled = false
        }

        return binding.root
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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "CrimeFragment"
    }
}