package com.flknlabs.firebaseexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAnalytics = Firebase.analytics

        btnFirstEvent.setOnClickListener {
            firebaseAnalytics.logEvent("first_event") {
                param(FirebaseAnalytics.Param.START_DATE, Date().time)
            }
        }

        btnSecondEvent.setOnClickListener {
            firebaseAnalytics.logEvent("second_event") {
                param("from_country", "México")
            }
        }

        btnThirdEvent.setOnClickListener {
            firebaseAnalytics.logEvent("third_event") {
                param("from_class", "RoofTop LevelUp 1")
            }
        }
    }
}