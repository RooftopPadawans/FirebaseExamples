package com.flknlabs.firebaseexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val db = Firebase.firestore

    companion object {
        val TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSaveHardcoded.setOnClickListener { saveHardcoded() }
    }

    private fun saveHardcoded() {
        val user = hashMapOf(
            "name" to "Ada Ava",
            "age" to 29,
            "isStudent" to true
        )

        val studentsRef: DocumentReference =
            db.collection("users")
                .document("students")



        readAStoredData("Perez", studentsRef) {
            val a = it

            /*it?.let {
                writeStorage(it) {

                }
            }*/

        }

    }

    private fun writeStorage(
        ref: DocumentReference,
        callback: (Boolean) -> Unit) {

        ref.set(mapOf("Student" to Student(
            name = "David Perez",
            age = 29,
            isStudent = false,
        )))
            .addOnSuccessListener {
                Log.d(TAG, "Snapshot added")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                callback(false)
            }
    }

    private fun readAStoredData(
        containsName: String,
        ref: DocumentReference,
        callback: (Student?) -> Unit) {

        ref.get()
            .addOnSuccessListener { result ->
                result.data?.let {
                    val auxStudent = Gson().toJson(it["Student"])
                    val student = Gson().fromJson(auxStudent, Student::class.java)

                    if (student.name.contains(containsName)) callback(student)
                    else callback(null)
                }
                callback(null)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                callback(null)
            }
    }
}

data class Student(
    val name: String,
    val age: Int,
    @SerializedName("student")
    val isStudent: Boolean
)