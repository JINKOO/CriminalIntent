package com.kjk.criminalintent.view

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kjk.criminalintent.data.CrimeDataSender
import com.kjk.criminalintent.data.CrimeEntity
import com.kjk.criminalintent.databinding.ListItemCrimeBinding
import java.util.*

class CrimeViewHolder(
    private val binding: ListItemCrimeBinding,
    private val dataSender: CrimeDataSender
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    init {
        setListener()
    }

    fun bind(position: Int) {
        val crime = dataSender.getCimeList()[position]
        setCrimeText(crime.title)
        setCrimeDate(crime.date)
    }

    private fun setListener() {
        binding.root.setOnClickListener(this)
    }

    private fun setCrimeText(crimeTitle: String) {
        binding.crimeTitle.apply { text = crimeTitle }
    }

    private fun setCrimeDate(date: Date) {
        binding.crimeDate.apply { text = date.toString() }
    }

    override fun onClick(view: View?) {
        showToast()
    }

    private fun showToast() {
        Toast.makeText(binding.root.context, "${dataSender.getCimeList()[adapterPosition].title} pressed", Toast.LENGTH_SHORT).show()
    }
}