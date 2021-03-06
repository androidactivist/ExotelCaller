package com.example.android.exotelcaller.util

import android.annotation.TargetApi
import android.os.Build
import android.telecom.Call
import android.util.Log
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

@TargetApi(Build.VERSION_CODES.M)
object CallManager {

    private const val TAG = "CallManager"

    private val subject = BehaviorSubject.create<GsmCall>()

    private var currentCall: Call? = null

    fun updates(): Observable<GsmCall> = subject

    fun updateCall(call: Call?) {
        currentCall = call
        call?.let {
            subject.onNext(it.toGsmCall())
        }
    }

    fun cancelCall() {
        currentCall?.let {
            when (it.state) {
                Call.STATE_RINGING -> rejectCall()
                else -> disconnectCall()
            }
        }
    }

    fun acceptCall() {
        Log.i(TAG, "acceptCall")
        currentCall?.let {
            it.answer(it.details.videoState)
        }
    }

    private fun rejectCall() {
        Log.i(TAG, "rejectCall")
        currentCall?.reject(false, "")
    }

    private fun disconnectCall() {
        Log.i(TAG, "disconnectCall")
        currentCall?.disconnect()
    }


    private fun makeCall() {
        Log.i(TAG, "makeCall")
    }
}