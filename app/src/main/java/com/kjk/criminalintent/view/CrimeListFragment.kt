package com.kjk.criminalintent.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kjk.criminalintent.data.Crime
import com.kjk.criminalintent.data.CrimeListViewModel
import com.kjk.criminalintent.databinding.FragmentCrimeListBinding
import java.util.*

class CrimeListFragment : Fragment() {

    /**
     * hosting Activity에서 구현할 인터페이스
     */
    interface CallBacks {
        fun onCrimeSelected(crimeID: UUID)
    }

    private var callBacks: CallBacks? = null

    private var _binding: FragmentCrimeListBinding? = null
    private val binding get() = _binding!!

    private val crimeListViewModel by lazy {
        ViewModelProvider(this@CrimeListFragment).get(CrimeListViewModel::class.java)
    }

    private val crimeAdapter by lazy {
        CrimeAdapter(crimeListViewModel.crimeLiveData.value!!, callBacks!!)
    }

    // Activity는 Context의 SubClass이다.
    // Fragment가 호스팅 액티비티에 연결될 때, 호출된다.
    // 따라서, 여기서 Context 객체의 참조를 callBacks에 저장.
    // 여기서 CrimeListFragment를 호스팅하는 액티비티 인스턴스가 Context 인스턴스이다.
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callBacks = context as CallBacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)
        initLayout()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        crimeListViewModel.crimeLiveData.observe(
            viewLifecycleOwner
        ) { crimes ->
            crimes?.let {
                Log.i(TAG, "onViewCreated: Got crimes ${crimes.size}")
                updateUI()
            }
        }
    }

    // Fragment가 호스팅 액티비티에서 분리될 때 호출 된다.
    override fun onDetach() {
        super.onDetach()
        _binding = null
        callBacks = null
    }

    private fun initLayout() {
        binding.crimeRecyclerView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = CrimeAdapter(emptyList(), callBacks!!)
        }
    }

    private fun updateUI() {
        binding.crimeRecyclerView.run {
            adapter = crimeAdapter
        }
    }

    companion object {
        private const val TAG = "CrimeListFragment"
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}