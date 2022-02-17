package com.kjk.criminalintent.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kjk.criminalintent.data.CrimeDataSender
import com.kjk.criminalintent.databinding.ListItemCrimeBinding
import com.kjk.criminalintent.databinding.ListItemCrimePoliceBinding

class CrimeAdapter(
    private val dataSender: CrimeDataSender
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // TODO 9장 챌린지 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "onCreateViewHolder: ")
        return when (viewType) {
            REQUIRE_POLICE -> {
                Log.d(TAG, "onCreateViewHolder: ${REQUIRE_POLICE}")
                val binding = ListItemCrimePoliceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CrimePoliceViewHolder(binding, dataSender)
            }

            else -> {
                Log.d(TAG, "onCreateViewHolder: ${REQUIRE_NO_POLICE}")
                val binding = ListItemCrimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CrimeViewHolder(binding, dataSender)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ")
        if (holder is CrimePoliceViewHolder) {
            holder.bind(position)
        } else if (holder is CrimeViewHolder) {
            holder.bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        Log.d(TAG, "getItemViewType: ")
        return if (dataSender.getCrimeList()[position].requiresPolice) {
            REQUIRE_POLICE
        } else {
            REQUIRE_NO_POLICE
        }
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ")
        return dataSender.getCrimeList().size
    }

    companion object {
        private const val TAG = "CrimeAdapter"
        const val REQUIRE_POLICE = 0
        const val REQUIRE_NO_POLICE = 1
    }
}