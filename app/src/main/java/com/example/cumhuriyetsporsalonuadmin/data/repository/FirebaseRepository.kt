package com.example.cumhuriyetsporsalonuadmin.data.repository

import android.util.Log
import com.example.cumhuriyetsporsalonuadmin.domain.model.Admin
import com.example.cumhuriyetsporsalonuadmin.domain.model.FirebaseLesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.domain.model.User
import com.example.cumhuriyetsporsalonuadmin.domain.model.firebase_collection.AdminField
import com.example.cumhuriyetsporsalonuadmin.domain.model.firebase_collection.CollectionName
import com.example.cumhuriyetsporsalonuadmin.domain.model.firebase_collection.LessonField
import com.example.cumhuriyetsporsalonuadmin.domain.model.firebase_collection.UserField
import com.example.cumhuriyetsporsalonuadmin.domain.model.firebase_exception.LoginError
import com.example.cumhuriyetsporsalonuadmin.utils.NullValidator
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
import com.example.cumhuriyetsporsalonuadmin.utils.TAG
import com.google.firebase.firestore.DocumentSnapshot
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
    private val testCollectionRef = db.collection("test")

    fun adminLogin(admin: Admin, callback: (Resource<Nothing>) -> Unit) {
        adminDocumentRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result ?: return@addOnCompleteListener
                val username = result.get(AdminField.USERNAME.key) as String?
                val password = result.get(AdminField.PASSWORD.key) as String?
                if (admin.username == username && admin.password == password) callback(Resource.Success())
                else callback(Resource.Error(message = LoginError.InvalidCredentialsException.errorMessage))

            } else callback(Resource.Error(message = task.exception?.message?.stringfy()))
        }

    }

    fun getUnverifiedUsers(callback: (studentList: (List<Student>)) -> Unit) {
        userCollectionRef.addSnapshotListener { value, error ->
            val myList = mutableListOf<User>()
            value?.let { value ->
                val allDocuments = value.documents as MutableList<DocumentSnapshot>
                allDocuments.map {
                    val isVerified = it.get(UserField.IS_VERIFIED.key) as Boolean?
                    val isNotNull = NullValidator.validate(isVerified)
                    if (!isNotNull) return@map
                    if (!isVerified!!) {
                        val user = convertDocumentToUser(it)
                        user?.let {
                            myList.add(it)
                        }
                    }
                }
                callback(myList)
            }

        }
    }

    fun getAllStudents(callback: (Resource<List<Student>>) -> Unit) {
        callback(Resource.Loading())
        userCollectionRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val studentList = mutableListOf<Student>()
                val result = task.result ?: return@addOnCompleteListener
                addUserToStudentList(result.documents, studentList)
                callback(Resource.Success(studentList))
            } else callback(Resource.Error(message = task.exception?.message?.stringfy()))
        }
    }

    fun getStudentsByLesson(lessonUID: String, callback: (Resource<List<Student>>) -> Unit) {
        userCollectionRef.whereEqualTo(LessonField.UID.key, lessonUID).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val studentList = mutableListOf<User>()
                    val result = task.result ?: return@addOnCompleteListener
                    addUserToStudentList(result.documents, studentList)
                    callback(Resource.Success(studentList))
                } else callback(Resource.Error(message = task.exception?.message?.stringfy()))
            }
    }

    fun getLessons(callback: (Resource<List<Lesson>>) -> Unit) {
        callback(Resource.Loading())
        lessonCollectionRef.orderBy(LessonField.DAY.key).orderBy(LessonField.START_HOUR.key).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val lessonList = mutableListOf<Lesson>()
                    val result = task.result ?: return@addOnCompleteListener
                    addLessonToList(result.documents, lessonList)
                    callback(Resource.Success(lessonList))
                } else callback(Resource.Error(message = task.exception?.message?.stringfy()))
            }
    }

    fun getLessonByUID(lessonUID: String, callback: (Resource<Lesson>) -> Unit) {
        callback(Resource.Loading())
        lessonCollectionRef.whereEqualTo(LessonField.UID.key, lessonUID).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result ?: return@addOnCompleteListener
                    val lesson =
                        convertDocumentToLesson(result.documents[0]) ?: return@addOnCompleteListener
                    callback(Resource.Success(lesson))
                } else callback(Resource.Error(message = task.exception?.message?.stringfy()))
            }

    }

    fun setLessons(lesson: FirebaseLesson, callback: (Resource<Nothing>) -> Unit) {
        callback(Resource.Loading())
        lessonCollectionRef.document(lesson.uid).set(lesson).addOnCompleteListener { task ->
            if (task.isSuccessful) callback(Resource.Success())
            else callback(Resource.Error(message = task.exception?.message?.stringfy()))
        }
    }

    fun deleteLesson(uid: String, callback: (Resource<Nothing>) -> Unit) {
        callback(Resource.Loading())
        lessonCollectionRef.document(uid).delete().addOnCompleteListener { task ->
            if (task.isSuccessful) callback(Resource.Success())
            else callback(Resource.Error(message = task.exception?.message?.stringfy()))
        }
    }

    fun answerRequest(user: User, isAccepted: Boolean, callback: (Resource<Nothing>) -> Unit) {
        user.isVerified = isAccepted
        userCollectionRef.document(user.uid).set(user).addOnCompleteListener { task ->
            if (task.isSuccessful) callback(Resource.Success())
            else callback(Resource.Error(message = task.exception?.message?.stringfy()))
        }
    }

    fun deleteAllLessons() {
        lessonCollectionRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (doc in task.result.documents) {
                    doc.reference.delete()
                }
            }
        }
    }

    fun updateAdmin(admin: Admin, callback: (Resource<Nothing>) -> Unit) {
        adminDocumentRef.set(admin).addOnCompleteListener { task ->
            if (task.isSuccessful) callback(Resource.Success())
            else callback(Resource.Error(message = task.exception?.message?.stringfy()))
        }
    }


    private fun addLessonToList(
        documentSnapshot: List<DocumentSnapshot>, list: MutableList<Lesson>
    ) {
        for (document in documentSnapshot) {
            val lesson = convertDocumentToLesson(document)
            lesson?.let { list.add(it) }
        }
    }

    private fun addUserToStudentList(
        documentSnapshot: List<DocumentSnapshot>, list: MutableList<User>
    ) {
        for (document in documentSnapshot) {
            val student = convertDocumentToUser(document)
            student?.let { list.add(it) }
        }
    }

    private fun convertDocumentToUser(doc: DocumentSnapshot): User? {
        return try {
            val uid = doc.get(UserField.UID.key) as String
            val email = doc.get(UserField.EMAIL.key) as String
            val name = doc.get(UserField.NAME.key) as String?
            val isVerified = doc.get(UserField.IS_VERIFIED.key) as Boolean
            User(uid = uid, email = email, name = name, isVerified = isVerified)
        } catch (e: Exception) {
            Log.d(TAG, "convertDocumentToUser: ${e}\n ${e.cause} ")
            null
        }
    }

    private fun convertDocumentToLesson(doc: DocumentSnapshot): Lesson? {
        return try {
            val uid = doc.get(LessonField.UID.key) as String
            val name = doc.get(LessonField.NAME.key) as String
            val day = doc.get(LessonField.DAY.key) as Long
            val startHour = doc.get(LessonField.START_HOUR.key) as String
            val endHour = doc.get(LessonField.END_HOUR.key) as String
            val studentUids = doc.get(LessonField.STUDENT_UIDS.key) as List<String>
            val firebaseLesson =
                FirebaseLesson(uid, name, day.toInt(), startHour, endHour, studentUids)
            val lesson = firebaseLesson.toLesson()
            lesson
        } catch (e: Exception) {
            Log.d(TAG, "convertDocumentToLesson: $e ")
            null
        }
    }

}