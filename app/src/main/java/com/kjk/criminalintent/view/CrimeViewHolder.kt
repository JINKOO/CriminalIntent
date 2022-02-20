package com.kjk.criminalintent.view

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kjk.criminalintent.data.Crime
import com.kjk.criminalintent.databinding.ListItemCrimeBinding
import com.kjk.criminalintent.extension.Date.dateFormatLong
import java.util.*

class CrimeViewHolder(
    private val binding: ListItemCrimeBinding,
    private val crimes: List<Crime>,
    private val callBacks: CrimeListFragment.CallBacks
//    private val dataSender: CrimeDataSender
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

//    interface CallBacks {
//        fun onCrimeSelected(crimeID: UUID)
//    }
//    private val callBacks: CallBacks? = null

    private lateinit var crime: Crime

    init {
        setListener()
    }

    fun bind(position: Int) {
        crime = crimes[position]
        setCrimeText(crime.title)
        setCrimeDate(crime.date)
        setCrimeIsSolved(crime.isSolved)
    }

    private fun setListener() {
        binding.root.setOnClickListener(this)
    }

    private fun setCrimeText(crimeTitle: String) {
        binding.crimeTitle.apply { text = crimeTitle }
    }

    private fun setCrimeDate(date: Date) {
        binding.crimeDate.apply { text = date.dateFormatLong() /*dataSender.getDateFormatString(date)*/ }
    }

    private fun setCrimeIsSolved(isSolved: Boolean) {
        binding.crimeSolvedImageView.apply {
            visibility = if (isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    override fun onClick(view: View?) {
        showToast()
        // 여기서 호스팅 액티비티의 fragment manager를 가져와서 처리해도 되지만, 프래그먼트 독립성에 어긋나기 때문에
        // 호스팅 액티비티에서 프래그먼트 이동을 정의한다.
        callBacks.onCrimeSelected(crime.id)
    }

    private fun showToast() {
        Toast.makeText(binding.root.context, "${crimes[adapterPosition].title} pressed", Toast.LENGTH_SHORT).show()
    }
}