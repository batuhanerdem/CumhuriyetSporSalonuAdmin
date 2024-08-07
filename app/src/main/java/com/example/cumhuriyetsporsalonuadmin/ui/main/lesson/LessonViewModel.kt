package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson

import androidx.lifecycle.viewModelScope
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.use_case.DeleteLessonUseCase
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val deleteLessonUseCase: DeleteLessonUseCase
) : BaseViewModel<LessonActionBus>() {
    var lessonList = mutableListOf<Lesson>() // list-private set
    var deletedLessonName = ""

    fun getClasses() {
        setLoading(true)
        firebaseRepository.getAllLessons().onEach { result ->
            setLoading(false)
            when (result) {
                is Resource.Error -> sendAction(LessonActionBus.ShowError(result.message))
                is Resource.Success -> {
                    result.data?.let {
                        lessonList = it.toMutableList()
                        sendAction(LessonActionBus.ClassesLoaded)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun deleteLesson(lessonUid: String) {
        setLoading(true)
        deleteLessonUseCase.execute(lessonUid).onEach { result ->
            setLoading(false)
            when (result) {
                is Resource.Error -> {
                    sendAction(LessonActionBus.ShowError(result.message))
                }

                is Resource.Success -> {
                    updateDeletedLessonName(lessonUid)
                    lessonList.removeIf {
                        it.uid == lessonUid
                    }
                    sendAction(LessonActionBus.LessonDeleted)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun updateDeletedLessonName(lessonUid: String) {
        lessonList.map {
            if (it.uid == lessonUid) deletedLessonName = it.name //?
        }
    }

}