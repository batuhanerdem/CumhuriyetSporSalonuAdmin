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
import javax.inject.Inject

@ViewModelScoped
class FirebaseRepository @Inject constructor(
    db: FirebaseFirestore
) {
    private val adminDocumentRef = db.collection(CollectionName.ADMIN.value).document("admin")
    private val userCollectionRef = db.collection(CollectionName.USER.value)
    private val lessonCollectionRef = db.collection(CollectionName.LESSON.value)


    fun adminLogin(admin: Admin, callback: (Resource<Unit>) -> Unit) {
        callback(Resource.Loading())
        adminDocumentRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result ?: return@addOnCompleteListener
                val dbAdmin = DocumentConverters.convertDocumentToAdmin(result)
                dbAdmin ?: return@addOnCompleteListener
                if (admin.username == dbAdmin.username && admin.password == dbAdmin.password) callback(
                    Resource.Success()
                ) else callback(Resource.Error(message = R.string.login_error_invalid_credentials.stringfy()))
            } else callback(Resource.Error(message = task.exception?.message?.stringfy()))
        }
    }

    fun updateAdmin(admin: Admin, callback: (Resource<Nothing>) -> Unit) {
        adminDocumentRef.set(admin).addOnCompleteListener { task ->
            if (task.isSuccessful) callback(Resource.Success())
            else callback(Resource.Error(message = task.exception?.message?.stringfy()))
        }
    }

    fun getUnverifiedStudents(callback: (Resource<List<Student>>) -> Unit) {
        callback(Resource.Loading())
        userCollectionRef.whereEqualTo(
            UserField.IS_VERIFIED.key, VerifiedStatus.NOTANSWERED.asString
        ).addSnapshotListener { value, error ->
            if (error != null) {
                callback(Resource.Error(message = error.message?.stringfy()))
                return@addSnapshotListener
            }

            val unverifiedStudents =
                value?.documents?.mapNotNull { DocumentConverters.convertDocumentToStudent(it) }
                    ?: emptyList()
            callback(Resource.Success(unverifiedStudents))
        }
    }


    fun getVerifiedStudents(callback: (Resource<List<Student>>) -> Unit) {
        callback(Resource.Loading())
        userCollectionRef.whereEqualTo(
            UserField.IS_VERIFIED.key, VerifiedStatus.VERIFIED.asString
        ).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result ?: return@addOnCompleteListener
                val studentList = DocumentConverters.convertDocumentToStudentList(result.documents)
                callback(Resource.Success(studentList))
            } else callback(Resource.Error(message = task.exception?.message?.stringfy()))
        }
    }

    fun getStudentByUid(studentUid: String, callback: (Resource<Student>) -> Unit) {
        userCollectionRef.document(studentUid).get().addOnCompleteListener { task ->
            callback(Resource.Loading())
            if (task.isSuccessful) {
                val result = task.result
                val student = DocumentConverters.convertDocumentToStudent(result)
                callback(Resource.Success(student))
            } else callback(Resource.Error(message = task.exception?.message?.stringfy()))

        }
    }

    fun getStudentsByLessonUid(lessonUID: String, callback: (Resource<List<Student>>) -> Unit) {
        callback(Resource.Loading())
        userCollectionRef.whereEqualTo(
            UserField.IS_VERIFIED.key, VerifiedStatus.VERIFIED.asString
        ).whereArrayContains(UserField.LESSON_UIDS.key, lessonUID).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result ?: return@addOnCompleteListener
                    val studentList =
                        DocumentConverters.convertDocumentToStudentList(result.documents)
                    callback(Resource.Success(studentList))
                } else callback(Resource.Error(message = task.exception?.message?.stringfy()))
            }
    }

    fun deleteStudent(studentUid: String, callback: (Resource<Nothing>) -> Unit) {
        callback(Resource.Loading())
        userCollectionRef.document(studentUid).delete().addOnCompleteListener { task ->
            if (task.isSuccessful) callback(Resource.Success())
            else callback(Resource.Error(message = task.exception?.message?.stringfy()))
        }
    }

    fun setStudent(student: Student, callback: (Resource<Nothing>) -> Unit) {
        callback(Resource.Loading())
        userCollectionRef.document(student.uid).set(student.toHashMap())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) callback(Resource.Success())
                else callback(Resource.Error(message = task.exception?.message?.stringfy()))
            }
    }

    fun getAllLessons(callback: (Resource<List<Lesson>>) -> Unit) {
        callback(Resource.Loading())
        lessonCollectionRef.orderBy(LessonField.DAY.key).orderBy(LessonField.START_HOUR.key).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result ?: return@addOnCompleteListener
                    val lessonList =
                        DocumentConverters.convertDocumentToLessonList(result.documents)
                    callback(Resource.Success(lessonList))
                } else callback(Resource.Error(message = task.exception?.message?.stringfy()))
            }
    }

    fun getLessonByUID(lessonUID: String, callback: (Resource<Lesson>) -> Unit) {
        callback(Resource.Loading())
        lessonCollectionRef.document(lessonUID).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result ?: return@addOnCompleteListener
                val lesson = DocumentConverters.convertDocumentToLesson(result)
                    ?: return@addOnCompleteListener
                callback(Resource.Success(lesson))
            } else callback(Resource.Error(message = task.exception?.message?.stringfy()))
        }

    }

    fun getLessonsByStudentUid(studentUid: String, callback: (Resource<List<Lesson>>) -> Unit) {
        callback(Resource.Loading())
        lessonCollectionRef.whereArrayContains(LessonField.STUDENT_UIDS.key, studentUid)
            .orderBy(LessonField.DAY.key).orderBy(LessonField.START_HOUR.key).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result ?: return@addOnCompleteListener
                    val lessonList =
                        DocumentConverters.convertDocumentToLessonList(result.documents)
                    callback(Resource.Success(lessonList))
                } else callback(Resource.Error(message = task.exception?.message?.stringfy()))
            }

    }

    fun setLesson(lesson: Lesson, callback: (Resource<Nothing>) -> Unit) {
        callback(Resource.Loading())
        lessonCollectionRef.document(lesson.uid).set(lesson.toFirebaseLesson())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) callback(Resource.Success())
                else callback(Resource.Error(message = task.exception?.message?.stringfy()))
            }
    }

    fun deleteLesson(lessonUid: String, callback: (Resource<Nothing>) -> Unit) {
        callback(Resource.Loading())
        lessonCollectionRef.document(lessonUid).delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(Resource.Success())
            } else callback(Resource.Error(message = task.exception?.message?.stringfy()))
        }
    }
}