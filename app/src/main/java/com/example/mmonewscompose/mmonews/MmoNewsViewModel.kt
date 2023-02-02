package com.example.mmonewscompose.mmonews

import androidx.lifecycle.ViewModel
import com.example.mmonews.repository.MmoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MmoNewsViewModel @Inject constructor(private val repository:MmoRepository): ViewModel(){


    init {
        loadMmoNews()
    }

    private fun loadMmoNews() {
        TODO("Not yet implemented")
    }
}