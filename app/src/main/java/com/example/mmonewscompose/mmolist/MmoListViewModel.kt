package com.example.mmonews.mmolist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmonews.data.remote.models.MmoListEntry
import com.example.mmonews.repository.MmoRepository
import com.example.mmonews.util.Constants.PAGE_SIZE
import com.example.mmonews.util.Ressource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MmoListViewModel @Inject constructor(private val repository: MmoRepository):ViewModel() {


    private var currentpage=0

    var mmoList= mutableStateOf<List<MmoListEntry>>(listOf())
    var loadError= mutableStateOf("")
    var isLoading= mutableStateOf(false)
    var endReached= mutableStateOf(false)

    private var cachedMmoList= listOf<MmoListEntry>()
    private var isSearchingStarting = true
    var isSearching = mutableStateOf(false)


    init {
        loadMmoPage()
    }

    fun searchMmoList(query:String){
        val listToSearch = if(isSearchingStarting){
            mmoList.value
        }else{
            cachedMmoList
        }
        //not in the current thead car trop long
        viewModelScope.launch(Dispatchers.Default) {
            if(query.isEmpty()){
                mmoList.value = cachedMmoList
                isSearching.value=false
                isSearchingStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.name.contains(query.trim(), ignoreCase = true) ||
                        it.id.toString() == query.trim()
            }
            if(isSearchingStarting){
               cachedMmoList = mmoList.value
                isSearchingStarting=false
            }
            mmoList.value= results
            isSearching.value=true
        }
    }

    fun loadMmoPage() {
       viewModelScope.launch {
           val result = repository.getMmoList(PAGE_SIZE, currentpage * PAGE_SIZE )
           isLoading.value=true
           when(result){
               is Ressource.Succes->{
                    endReached.value = currentpage * PAGE_SIZE >= result.data!!.size
                    val mmoEntries = MmoListEntry(result.data[currentpage].id,
                        result.data[currentpage].title,
                        result.data[currentpage].thumbnail)
                   currentpage++
                   isLoading.value=false
                   loadError.value=""
                   mmoList.value += mmoEntries
               }
               is Ressource.Error-> {
                   isLoading.value=false
               }
           }
       }
    }



}