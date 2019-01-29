package com.example.android.exotelcaller.service


import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.telecom.Call
import android.telecom.InCallService
import android.util.Log
import com.example.android.exotelcaller.ui.CallActivity
import com.example.android.exotelcaller.util.CallManager

class CallService : InCallService() {
    companion object {
        private const val TAG = "CallService"
    }

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        Log.i(TAG, "onCallAdded: $call")
        call.registerCallback(callCallback)
        startActivity(Intent(this, CallActivity::class.java))
        CallManager.updateCall(call)
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        Log.i(TAG, "onCallRemoved: $call")
        call.unregisterCallback(callCallback)
        CallManager.updateCall(null)
    }

    private val callCallback = object : Call.Callback() {
        override fun onStateChanged(call: Call, state: Int) {
            Log.i(TAG, "Call.Callback onStateChanged: $call, state: $state")
            CallManager.updateCall(call)
        }
    }
}
