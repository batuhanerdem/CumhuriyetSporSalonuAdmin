package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.use_case.DeleteLessonUseCase
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val deleteLessonUseCase: DeleteLessonUseCase
) : BaseViewModel<LessonActionBus>() {
    var lessonList = mutableListOf<Lesson>() // list-private set
    var deletedLessonName = ""

    fun getClasses() {
        firebaseRepository.getAllLessons { result ->
            when (result) {
                is Resource.Loading -> setLoading(true)
                is Resource.Error -> sendAction(LessonActionBus.ShowError(result.message))
                is Resource.Success -> {
                    result.data?.let {
                        lessonList = it.toMutableList()
                        setLoading(false)
                        sendAction(LessonActionBus.ClassesLoaded)
                    }
                }
            }

        }
    }

    fun deleteLesson(lessonUid: String) {
        deleteLessonUseCase.execute(lessonUid) { result ->
            when (result) {
                is Resource.Error -> {
                    setLoading(false)
                    sendAction(LessonActionBus.ShowError(result.message))
                }

                is Resource.Loading -> setLoading(true)
                is Resource.Success -> {
                    setLoading(false)
                    updateDeletedLessonName(lessonUid)
                    lessonList.removeIf {
                        it.uid == lessonUid
                    }
                    sendAction(LessonActionBus.LessonDeleted)
                }
            }
        }
    }

    private fun updateDeletedLessonName(lessonUid: String) {
        lessonList.map {
            if (it.uid == lessonUid) deletedLessonName = it.name //?
        }
    }

}