package com.kjk.criminalintent.view

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

class CrimeListFragment : Fragment() {

    private var _binding: FragmentCrimeListBinding? = null
    private val binding get() = _binding!!

    private val crimeListViewModel by lazy {
        ViewModelProvider(this@CrimeListFragment).get(CrimeListViewModel::class.java)
    }

    private val crimeAdapter by lazy {
        CrimeAdapter(crimeListViewModel.crimeLiveData.value!!)
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

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }

    private fun initLayout() {
        binding.crimeRecyclerView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = CrimeAdapter(emptyList())
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