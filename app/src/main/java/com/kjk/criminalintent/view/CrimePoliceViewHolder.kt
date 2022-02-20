package com.kjk.criminalintent.view

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kjk.criminalintent.data.Crime
import com.kjk.criminalintent.databinding.ListItemCrimePoliceBinding
import com.kjk.criminalintent.extension.Date.dateFormatLong
import com.kjk.criminalintent.extension.Date.toLocalDateTime
import java.util.*

class CrimePoliceViewHolder(
    private val binding: ListItemCrimePoliceBinding,
    private val crimes: List<Crime>,
    private val callBacks: CrimeListFragment.CallBacks
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var crime: Crime

    init {
        setListener()
    }

    fun bind(position: Int) {
        crime = crimes[position]
        setCrimeText(crime.title)
        setCrimeDate(crime.date)
    }

    private fun setCrimeText(crimeTitle: String) {
        binding.crimeTitle.apply { text = crimeTitle }
    }

    private fun setCrimeDate(date: Date) {
        binding.crimeDate.apply { text = date.toLocalDateTime().dateFormatLong() /*dataSender.getLocalDateFormatString(date)*/ }
    }

    private fun setListener() {
        binding.callPoliceButton.setOnClickListener(this)
    }

    private fun showToast() {
        Toast.makeText(binding.root.context, "call police cause of ${crimes[adapterPosition]}", Toast.LENGTH_SHORT).show()
        callBacks.onCrimeSelected(crime.id)
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