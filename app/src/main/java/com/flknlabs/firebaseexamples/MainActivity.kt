package com.flknlabs.firebaseexamples

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private val database = Firebase.database

    companion object {
        val TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSaveHardcoded.setOnClickListener { readFromRealtime() }
    }

    // Saving with Firestore
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

    // Saving with Realtime DB
    private fun saveRealTime() {
        /*val myRef = database.getReference("message")
        myRef.setValue("Hello, World!")*/

        /*val student = Student(
            name = "David Perez",
            age = 29,
            isStudent = false,
        )
        val newRef = database.getReference("student")
        newRef.setValue(student)*/

        val teachers = listOf<Teacher>(
            Teacher("Martin del Campo", 32, true),
            Teacher("Marcelina Gutierrez", 30,false)
        )
        val newRef2 = database.getReference("teachers")
        newRef2.setValue(teachers)
    }

    private fun readFromRealtime() {
        //val myRef = database.getReference("message")
        //val newRef = database.getReference("student")
        val newRef2 = database.getReference("teachers")

        newRef2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.value
                Log.d(TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }
}

data class Student(
    val id: Int = Utils.generateRandomID(),
    val name: String,
    val age: Int,
    @SerializedName("student")
    val isStudent: Boolean
)

data class Teacher(
    val name: String,
    val age: Int,
    @SerializedName("active")
    val isActive: Boolean
)

object Utils {
    fun generateRandomID(): Int { return Random().nextInt(100000) }
}
