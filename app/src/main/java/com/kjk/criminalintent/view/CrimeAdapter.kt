package com.kjk.criminalintent.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kjk.criminalintent.data.CrimeDataSender
import com.kjk.criminalintent.databinding.ListItemCrimeBinding

class CrimeAdapter(
    private val dataSender: CrimeDataSender
) : RecyclerView.Adapter<CrimeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeViewHolder {
        val binding = ListItemCrimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CrimeViewHolder(binding, dataSender)
    }

    override fun onBindViewHolder(holder: CrimeViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return dataSender.getCimeList().size
    }
}