package com.example.cumhuriyetsporsalonuadmin.data.repository

import com.example.cumhuriyetsporsalonuadmin.domain.model.Admin
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.User
import com.example.cumhuriyetsporsalonuadmin.domain.model.firebase_collection.AdminField
import com.example.cumhuriyetsporsalonuadmin.domain.model.firebase_collection.CollectionName
import com.example.cumhuriyetsporsalonuadmin.domain.model.firebase_collection.LessonField
import com.example.cumhuriyetsporsalonuadmin.domain.model.firebase_collection.UserField
import com.example.cumhuriyetsporsalonuadmin.domain.model.firebase_exception.LoginError
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
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

    fun getUnverifiedUsers(callback: (userList: (List<User>)) -> Unit) {
        userCollectionRef.addSnapshotListener { value, error ->
            val myList = mutableListOf<User>()
            value?.let { value ->
                val allDocuments = value.documents as MutableList<DocumentSnapshot>
                allDocuments.map {
                    val isVerifed = it.get(UserField.IS_VERIFIED.key) as Boolean
                    if (!isVerifed) {
                        val user = convertDocumentToUser(it)
                        myList.add(user)
                    }
                }
                callback(myList)
            }

        }
    }

    fun getLessons(callback: (Resource<List<Lesson>>) -> Unit) {
        callback(Resource.Loading())
        lessonCollectionRef.get().addOnCompleteListener { task ->
            val lessonList = mutableListOf<Lesson>()
            if (task.isSuccessful) {
                val result = task.result
                result ?: return@addOnCompleteListener
                for (document in result.documents) {
                    val lesson = convertDocumentToLesson(document)
                    lessonList.add(lesson)
                }
                callback(Resource.Success(lessonList))
            } else callback(Resource.Error(message = task.exception?.message?.stringfy()))
        }
    }

    fun setLessons(lesson: Lesson, callback: (Resource<Nothing>) -> Unit) {
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

    fun updateAdmin(admin: Admin, callback: (Resource<Nothing>) -> Unit) {
        adminDocumentRef.set(admin).addOnCompleteListener { task ->
            if (task.isSuccessful) callback(Resource.Success())
            else callback(Resource.Error(message = task.exception?.message?.stringfy()))
        }
    }

    private fun convertDocumentToUser(doc: DocumentSnapshot): User {
        val uid = doc.get(UserField.UID.key) as String
        val email = doc.get(UserField.EMAIL.key) as String
        val name = doc.get(UserField.NAME.key) as String?
        val isVerified = doc.get(UserField.IS_VERIFIED.key) as Boolean
        return User(uid = uid, email = email, name = name, isVerified = isVerified)
    }

    private fun convertDocumentToLesson(doc: DocumentSnapshot): Lesson {
        val uid = doc.get(LessonField.UID.key) as String
        val name = doc.get(LessonField.NAME.key) as String
        val day = doc.get(LessonField.DAY.key) as String
        val timeBegin = doc.get(LessonField.TIME_BEGIN.key) as String
        val timeEndTest = doc.get(LessonField.TIME_END.key) as String
        val studentUids = doc.get(LessonField.STUDENT_UIDS.key) as List<String>
        return Lesson(uid, name, day, timeBegin, timeEndTest, studentUids)
    }

}