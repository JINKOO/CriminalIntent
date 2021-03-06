package com.kjk.criminalintent.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kjk.criminalintent.R
import com.kjk.criminalintent.data.Crime
import com.kjk.criminalintent.data.CrimeListViewModel
import com.kjk.criminalintent.databinding.FragmentCrimeListBinding
import java.util.*

class CrimeListFragment :
    Fragment(),
    View.OnClickListener {

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

    private lateinit var crimeAdapter: CrimeAdapter

    // Activity는 Context의 SubClass이다.
    // Fragment가 호스팅 액티비티에 연결될 때, 호출된다.
    // 따라서, 여기서 Context 객체의 참조를 callBacks에 저장.
    // 여기서 CrimeListFragment를 호스팅하는 액티비티 인스턴스가 Context 인스턴스이다.
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach: ")
        callBacks = context as CallBacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        // onCreateOptionsMenu() 콜백을 호출해야함을 알려준다.
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ")
        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)
        initLayout()
        initListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")

        crimeListViewModel.crimeLiveData.observe(
            viewLifecycleOwner
        ) { crimes ->
            crimes?.let {
                Log.i(TAG, "onViewCreated: Got crimes ${crimes.size}")
                if (crimes.isEmpty()) {
                    Log.d(TAG, "onViewCreated: if()")
                    binding.noDataConstraintLayout.visibility = View.VISIBLE
                } else {
                    Log.d(TAG, "onViewCreated: else")
                    binding.noDataConstraintLayout.visibility = View.GONE
                    crimeAdapter.submitList(crimes)
                }
            }
        }
    }

    private fun initLayout() {
        binding.crimeRecyclerView.run {
            layoutManager = LinearLayoutManager(context)
            crimeAdapter = CrimeAdapter(callBacks!!)
            adapter = crimeAdapter
        }
    }

    private fun initListener() {
        binding.run {
            createDataButton.setOnClickListener(this@CrimeListFragment)
        }
    }

    override fun onClick(view: View?) {
        when(view) {
            binding.createDataButton -> {
                createNewCrime()
            }
        }
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
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    // Fragment가 호스팅 액티비티에서 분리될 때 호출 된다.
    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach: ")
        _binding = null
        callBacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        Log.d(TAG, "onCreateOptionsMenu: ")
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.new_crime -> {
                createNewCrime()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun createNewCrime() {
        val crime = Crime()
        crimeListViewModel.addCrime(crime)
        callBacks?.onCrimeSelected(crime.id)
    }

    companion object {
        private const val TAG = "CrimeListFragment"
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}