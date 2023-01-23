package com.example.mmonews.mmodetail

import androidx.lifecycle.ViewModel
import com.example.mmonews.data.remote.response.ResponseApiItem
import com.example.mmonews.data.remote.response.ResponseId
import com.example.mmonews.repository.MmoRepository
import com.example.mmonews.util.Ressource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MmoDetailViewModel @Inject  constructor(
    private val repository: MmoRepository
) :ViewModel() {
    suspend fun getMmmoInfo(id:Int):Ressource<ResponseId>{
        return repository.getMmoName(id)
    }
}