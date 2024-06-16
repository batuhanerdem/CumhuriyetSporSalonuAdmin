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

    fun adminLogin(admin: Admin, callback: (Resource<Unit>) -> Unit) {
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
                        val user = convertDocumentToStudent(it)
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
                val result = task.result ?: return@addOnCompleteListener
                val studentList = convertDocumentToStudentList(result.documents)
                callback(Resource.Success(studentList))
            } else callback(Resource.Error(message = task.exception?.message?.stringfy()))
        }
    }

    fun getStudentsByLesson(lessonUID: String, callback: (Resource<List<Student>>) -> Unit) {
        userCollectionRef.whereArrayContains(UserField.LESSON_UIDS.key, lessonUID).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result ?: return@addOnCompleteListener
                    val studentList = convertDocumentToStudentList(result.documents)
                    callback(Resource.Success(studentList))
                } else callback(Resource.Error(message = task.exception?.message?.stringfy()))
            }
    }

    fun getLessons(callback: (Resource<List<Lesson>>) -> Unit) {
        callback(Resource.Loading())
        lessonCollectionRef.orderBy(LessonField.DAY.key).orderBy(LessonField.START_HOUR.key).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result ?: return@addOnCompleteListener
                    val lessonList = convertDocumentToLessonList(result.documents)
                    callback(Resource.Success(lessonList))
                } else callback(Resource.Error(message = task.exception?.message?.stringfy()))
            }
    }

    fun getLessonByUID(lessonUID: String, callback: (Resource<Lesson>) -> Unit) {
        callback(Resource.Loading())
        lessonCollectionRef.whereEqualTo(LessonField.UID.key, lessonUID).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) { //test
                    try {
                        val result = task.result ?: return@addOnCompleteListener
                        val lesson = convertDocumentToLesson(result.documents[0])
                            ?: return@addOnCompleteListener
                        callback(Resource.Success(lesson))
                    } catch (e: Exception) {

                    }

                } else callback(Resource.Error(message = task.exception?.message?.stringfy()))
            }

    }

    fun getLessonByStudentUid(studentUid: String, callback: (Resource<List<Lesson>>) -> Unit) {
        callback(Resource.Loading())
        lessonCollectionRef.whereArrayContains(LessonField.STUDENT_UIDS.key, studentUid).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result ?: return@addOnCompleteListener
                    val leesonList = convertDocumentToLessonList(result.documents)
                    callback(Resource.Success(leesonList))
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

    fun addStudentToLesson(
        lessonUid: String, studentList: List<Student>, callback: (Resource<Unit>) -> Unit
    ) {
        callback(Resource.Loading())
        lessonCollectionRef.document(lessonUid).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result ?: return@addOnCompleteListener
                val lesson = convertDocumentToLesson(result) ?: return@addOnCompleteListener
                val tempUidList = mutableListOf<String>()
                studentList.map {
                    tempUidList.add(it.uid)
                }
                val savedStudentList = lesson.studentUids.toMutableList()
                savedStudentList.addAll(tempUidList)
                lesson.studentUids = savedStudentList
                lessonCollectionRef.document(lessonUid).set(lesson.toFirebaseLesson())
                    .addOnCompleteListener { lessonTask ->
                        if (lessonTask.isSuccessful) {
                            studentList.map {
                                addLessonUidToStudent(it.uid, lessonUid) {
                                    callback(Resource.Success())
                                }
                            }
                        } else callback(Resource.Error(message = task.exception?.message?.stringfy()))
                    }

            } else callback(Resource.Error(message = task.exception?.message?.stringfy()))

        }
    }

    fun getStudentByUid(studentUid: String, callback: (Resource<Student>) -> Unit) {
        userCollectionRef.document(studentUid).get().addOnCompleteListener { task ->
            callback(Resource.Loading())
            if (task.isSuccessful) {
                val result = task.result
                val student = convertDocumentToStudent(result)
                callback(Resource.Success(student))
            } else callback(Resource.Error(message = task.exception?.message?.stringfy()))

        }
    }

    fun deleteLesson(uid: String, callback: (Resource<Nothing>) -> Unit) {
        callback(Resource.Loading())
        lessonCollectionRef.document(uid).delete().addOnCompleteListener { task ->
            if (task.isSuccessful) callback(Resource.Success())
            else callback(Resource.Error(message = task.exception?.message?.stringfy()))
        }
    }

    fun answerRequest(
        student: Student, isAccepted: Boolean, callback: (Resource<Nothing>) -> Unit
    ) {
        student.isVerified = isAccepted
        userCollectionRef.document(student.uid).set(student).addOnCompleteListener { task ->
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

    fun deleteEverything() {
        lessonCollectionRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result.documents.map {
                    it.reference.delete()
                }
            }

        }
        userCollectionRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result.documents.map {
                    it.reference.delete()
                }
            }

        }
    }


    private fun addLessonUidToStudent(
        studentUid: String, lessonUID: String, callback: (Resource<Unit>) -> Unit
    ) {
        userCollectionRef.document(studentUid).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result ?: return@addOnCompleteListener
                val student = convertDocumentToStudent(result)
                student?.let {
                    val lessonUids = it.lessonUids.toMutableList()
                    lessonUids.add(lessonUID)
                    it.lessonUids = lessonUids
                    userCollectionRef.document(studentUid).set(student)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) callback(Resource.Success())
                            else callback(Resource.Error(message = task.exception?.message?.stringfy()))
                        }
                }
            }

        }
    }


    private fun convertDocumentToLessonList(
        documentSnapshot: List<DocumentSnapshot>
    ): MutableList<Lesson> {
        val list = mutableListOf<Lesson>()
        for (document in documentSnapshot) {
            val lesson = convertDocumentToLesson(document)
            lesson?.let { list.add(it) }
        }
        return list
    }

    private fun convertDocumentToStudentList(
        documentSnapshot: List<DocumentSnapshot>
    ): MutableList<Student> {
        val list = mutableListOf<Student>()
        for (document in documentSnapshot) {
            val student = convertDocumentToStudent(document)
            student?.let { list.add(it) }
        }
        return list
    }

    private fun convertDocumentToStudent(doc: DocumentSnapshot): User? {
        return try {
            val uid = doc.get(UserField.UID.key) as String
            val email = doc.get(UserField.EMAIL.key) as String
            val name = doc.get(UserField.NAME.key) as String?
            val lessonUids = doc.get(UserField.LESSON_UIDS.key) as List<String>
            val isVerified = doc.get(UserField.IS_VERIFIED.key) as Boolean
            User(
                uid = uid,
                email = email,
                name = name,
                isVerified = isVerified,
                lessonUids = lessonUids
            )
        } catch (e: Exception) {
            Log.d(TAG, "convertDocumentToStudent: ${e}\n ${e.cause} message: ${e.message}")
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