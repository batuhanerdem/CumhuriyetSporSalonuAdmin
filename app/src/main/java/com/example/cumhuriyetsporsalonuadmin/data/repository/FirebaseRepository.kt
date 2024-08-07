package com.example.cumhuriyetsporsalonuadmin.data.repository

import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.domain.mappers.DocumentConverters
import com.example.cumhuriyetsporsalonuadmin.domain.model.Admin
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.domain.model.VerifiedStatus
import com.example.cumhuriyetsporsalonuadmin.domain.model.firebase_collection.CollectionName
import com.example.cumhuriyetsporsalonuadmin.domain.model.firebase_collection.LessonField
import com.example.cumhuriyetsporsalonuadmin.domain.model.firebase_collection.UserField
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ViewModelScoped
class FirebaseRepository @Inject constructor(
    db: FirebaseFirestore
) {
    private val adminDocumentRef = db.collection(CollectionName.ADMIN.value).document("admin")
    private val userCollectionRef = db.collection(CollectionName.USER.value)
    private val lessonCollectionRef = db.collection(CollectionName.LESSON.value)

    fun adminLogin(admin: Admin): Flow<Resource<Unit>> = callbackFlow {
        try {
            val task = adminDocumentRef.get().await()
            val dbAdmin = DocumentConverters.convertDocumentToAdmin(task)
            if (admin.username == dbAdmin?.username && admin.password == dbAdmin.password) {
                trySend(Resource.Success(Unit))
            } else {
                trySend(Resource.Error(message = R.string.login_error_invalid_credentials.stringfy()))
            }
        } catch (e: Exception) {
            trySend(Resource.Error(message = e.message?.stringfy()))
        }
        awaitClose { this.cancel() }
    }

    fun updateAdmin(admin: Admin): Flow<Resource<Unit>> = callbackFlow {
        try {
            adminDocumentRef.set(admin).await()
            trySend(Resource.Success())
        } catch (e: Exception) {
            trySend(Resource.Error(message = e.message?.stringfy()))
        }
        awaitClose { this.cancel() }
    }

    fun getUnverifiedStudents(): Flow<Resource<List<Student>>> = callbackFlow {
        val listenerRegistration = userCollectionRef.whereEqualTo(
            UserField.IS_VERIFIED.key, VerifiedStatus.NOTANSWERED.asString
        ).addSnapshotListener { value, error ->
            if (error != null) {
                trySend(Resource.Error(message = error.message?.stringfy()))
                return@addSnapshotListener
            }

            val unverifiedStudents = value?.documents?.mapNotNull {
                DocumentConverters.convertDocumentToStudent(it)
            } ?: emptyList()
            trySend(Resource.Success(unverifiedStudents))
        }

        awaitClose {
            listenerRegistration.remove()
            this.cancel()
        }
    }

    fun getVerifiedStudents(): Flow<Resource<List<Student>>> = callbackFlow {
        try {
            val result = userCollectionRef.whereEqualTo(
                UserField.IS_VERIFIED.key, VerifiedStatus.VERIFIED.asString
            ).get().await()
            val studentList = DocumentConverters.convertDocumentToStudentList(result.documents)
            trySend(Resource.Success(studentList))
        } catch (e: Exception) {
            trySend(Resource.Error(message = e.message?.stringfy()))
        }
        awaitClose { this.cancel() }
    }

    fun getStudentByUid(studentUid: String): Flow<Resource<Student>> = callbackFlow {
        try {
            val result = userCollectionRef.document(studentUid).get().await()
            val student = DocumentConverters.convertDocumentToStudent(result)
            trySend(Resource.Success(student))
        } catch (e: Exception) {
            trySend(Resource.Error(message = e.message?.stringfy()))
        }
        awaitClose { this.cancel() }
    }

    fun getStudentsByLessonUid(lessonUID: String): Flow<Resource<List<Student>>> = callbackFlow {
        try {
            val result = userCollectionRef.whereEqualTo(
                UserField.IS_VERIFIED.key, VerifiedStatus.VERIFIED.asString
            ).whereArrayContains(UserField.LESSON_UIDS.key, lessonUID).get().await()
            val studentList = DocumentConverters.convertDocumentToStudentList(result.documents)
            trySend(Resource.Success(studentList))
        } catch (e: Exception) {
            trySend(Resource.Error(message = e.message?.stringfy()))
        }
        awaitClose { this.cancel() }
    }

    fun deleteStudent(studentUid: String): Flow<Resource<Unit>> = callbackFlow {
        try {
            userCollectionRef.document(studentUid).delete().await()
            trySend(Resource.Success())
        } catch (e: Exception) {
            trySend(Resource.Error(message = e.message?.stringfy()))
        }
        awaitClose { this.cancel() }
    }

    fun setStudent(student: Student): Flow<Resource<Unit>> = callbackFlow {
        try {
            userCollectionRef.document(student.uid).set(student.toHashMap()).await()
            trySend(Resource.Success())
        } catch (e: Exception) {
            trySend(Resource.Error(message = e.message?.stringfy()))
        }
        awaitClose { this.cancel() }
    }

    fun getAllLessons(): Flow<Resource<List<Lesson>>> = callbackFlow {
        try {
            val result =
                lessonCollectionRef.orderBy(LessonField.DAY.key).orderBy(LessonField.START_HOUR.key)
                    .get().await()
            val lessonList = DocumentConverters.convertDocumentToLessonList(result.documents)
            trySend(Resource.Success(lessonList))
        } catch (e: Exception) {
            trySend(Resource.Error(message = e.message?.stringfy()))
        }
        awaitClose { this.cancel() }
    }

    fun getLessonByUID(lessonUID: String): Flow<Resource<Lesson>> = callbackFlow {
        try {
            val result = lessonCollectionRef.document(lessonUID).get().await()
            val lesson = DocumentConverters.convertDocumentToLesson(result)
            trySend(Resource.Success(lesson))
        } catch (e: Exception) {
            trySend(Resource.Error(message = e.message?.stringfy()))
        }
        awaitClose { this.cancel() }
    }

    fun getLessonsByStudentUid(studentUid: String): Flow<Resource<List<Lesson>>> = callbackFlow {
        try {
            val result =
                lessonCollectionRef.whereArrayContains(LessonField.STUDENT_UIDS.key, studentUid)
                    .orderBy(LessonField.DAY.key).orderBy(LessonField.START_HOUR.key).get().await()
            val lessonList = DocumentConverters.convertDocumentToLessonList(result.documents)
            trySend(Resource.Success(lessonList))
        } catch (e: Exception) {
            trySend(Resource.Error(message = e.message?.stringfy()))
        }
        awaitClose { this.cancel() }
    }

    fun getRequestedLessons(): Flow<Resource<List<Lesson>>> = callbackFlow {
        try {
            lessonCollectionRef.whereNotEqualTo(LessonField.REQUEST_UIDS.key, emptyList<String>())
                .orderBy(LessonField.DAY.key).addSnapshotListener { event, error ->
                    if (error != null) trySend(Resource.Error(error.message?.stringfy()))
                    event?.documents ?: return@addSnapshotListener
                    val lessonList = DocumentConverters.convertDocumentToLessonList(event.documents)
                    trySend(Resource.Success(lessonList))
                }

        } catch (e: Exception) {
            trySend(Resource.Error(message = e.message?.stringfy()))
        }
        awaitClose { this.cancel() }
    }

    fun setLesson(lesson: Lesson): Flow<Resource<Unit>> = callbackFlow {
        try {
            lessonCollectionRef.document(lesson.uid).set(lesson.toFirebaseLesson()).await()
            trySend(Resource.Success())
        } catch (e: Exception) {
            trySend(Resource.Error(message = e.message?.stringfy()))
        }
        awaitClose { this.cancel() }
    }

    fun deleteLesson(lessonUid: String): Flow<Resource<Unit>> = callbackFlow {
        try {
            lessonCollectionRef.document(lessonUid).delete().await()
            trySend(Resource.Success())
        } catch (e: Exception) {
            trySend(Resource.Error(message = e.message?.stringfy()))
        }
        awaitClose { this.cancel() }
    }
}

