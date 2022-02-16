package com.kjk.criminalintent.view

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kjk.criminalintent.data.CrimeDataSender
import com.kjk.criminalintent.databinding.ListItemCrimePoliceBinding
import java.util.*

class CrimePoliceViewHolder(
    private val binding: ListItemCrimePoliceBinding,
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

    private fun setCrimeText(crimeTitle: String) {
        binding.crimeTitle.apply { text = crimeTitle }
    }

    private fun setCrimeDate(date: Date) {
        binding.crimeDate.apply { text = date.toString() }
    }

    private fun setListener() {
        binding.callPoliceButton.setOnClickListener(this)
    }

    private fun showToast() {
        Toast.makeText(binding.root.context, "call police cause of ${dataSender.getCimeList()[adapterPosition].title}", Toast.LENGTH_SHORT).show()
    }

    override fun onClick(view: View?) {
        binding.run {
            when (view) {
                callPoliceButton -> {
                    showToast()
                }
            }
        }
    }
}