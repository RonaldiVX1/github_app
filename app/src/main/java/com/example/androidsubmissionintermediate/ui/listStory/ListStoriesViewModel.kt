package com.example.androidsubmissionintermediate.ui.listStory


import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidsubmissionintermediate.data.StoryRepository
import com.example.androidsubmissionintermediate.data.response.story.Story
import com.example.androidsubmissionintermediate.di.Injection


class ListStoriesViewModel(storyRepository: StoryRepository) : ViewModel() {

    val story: LiveData<PagingData<Story>> =
        storyRepository.getStories().cachedIn(viewModelScope)
}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListStoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListStoriesViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
