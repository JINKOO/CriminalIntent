package com.kjk.criminalintent.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kjk.criminalintent.R
import com.kjk.criminalintent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}