package com.kjk.criminalintent.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kjk.criminalintent.data.Crime
import com.kjk.criminalintent.databinding.ListItemCrimeBinding
import com.kjk.criminalintent.databinding.ListItemCrimePoliceBinding

class CrimeAdapter(
    private val callBacks: CrimeListFragment.CallBacks
) : ListAdapter<Crime, RecyclerView.ViewHolder>(diffUtil) {

    // TODO 9장 챌린지 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "onCreateViewHolder: ")
        return when (viewType) {
            REQUIRE_POLICE -> {
                Log.d(TAG, "onCreateViewHolder: ${REQUIRE_POLICE}")
                val binding = ListItemCrimePoliceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CrimePoliceViewHolder(binding, callBacks)
            }

            else -> {
                Log.d(TAG, "onCreateViewHolder: ${REQUIRE_NO_POLICE}")
                val binding = ListItemCrimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CrimeViewHolder(binding, callBacks)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ")
        if (holder is CrimePoliceViewHolder) {
            holder.bind(getItem(position))
        } else if (holder is CrimeViewHolder) {
            holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        Log.d(TAG, "getItemViewType: ")
        return if (!getItem(position).isSolved/*requiresPolice*/) {
            REQUIRE_POLICE
        } else {
            REQUIRE_NO_POLICE
        }
    }

    companion object {
        private const val TAG = "CrimeAdapter"
        const val REQUIRE_POLICE = 0
        const val REQUIRE_NO_POLICE = 1
        
        val diffUtil = object : DiffUtil.ItemCallback<Crime>() {
            override fun areItemsTheSame(oldItem: Crime, newItem: Crime): Boolean {
                Log.d(TAG, "areItemsTheSame: ")
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Crime, newItem: Crime): Boolean {
                Log.d(TAG, "areContentsTheSame: ")
                return oldItem == newItem
            }
        }
    }
}