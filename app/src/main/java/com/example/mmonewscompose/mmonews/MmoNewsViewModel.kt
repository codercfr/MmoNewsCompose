package com.example.mmonewscompose.mmonews

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmonews.data.remote.models.MmoListEntry
import com.example.mmonews.repository.MmoRepository
import com.example.mmonews.util.Constants.PAGE_SIZE
import com.example.mmonews.util.Ressource
import com.example.mmonewscompose.remote.models.NewsListEntry
import com.example.mmonewscompose.remote.response.news.NewsApi
import com.example.mmonewscompose.remote.response.news.NewsApiItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MmoNewsViewModel @Inject constructor(private val repository:MmoRepository): ViewModel(){

    private var currentpage=0
    var mmoList= mutableStateOf<List<NewsListEntry>>(listOf())
    var loadError= mutableStateOf("")
    var isLoading= mutableStateOf(false)
    var endReached= mutableStateOf(false)

    init {
        loadMmoNews()
    }

    fun loadMmoNews() {

      viewModelScope.launch {
          val result= repository.getMmoNews(PAGE_SIZE,currentpage* PAGE_SIZE)

          when(result){
              is Ressource.Succes->{
                  endReached.value = currentpage * PAGE_SIZE >= result.data!!.size
                  val mmoNewsEntry=NewsListEntry(
                      result.data[currentpage].id,
                      result.data[currentpage].title,
                      result.data[currentpage].main_image,
                      result.data[currentpage].article_content,
                  )
                  currentpage++
                  isLoading.value=false
                  loadError.value=""
                  mmoList.value += mmoNewsEntry

              }
              is Ressource.Error-> {
                  isLoading.value=false
              }
          }
      }
    }
}