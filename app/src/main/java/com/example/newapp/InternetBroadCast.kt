package com.example.newapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class InternetBroadCast : BroadcastReceiver() {
    var isInternetAvaible : MutableLiveData<Boolean> = MutableLiveData()
    override fun onReceive(context: Context?, intent: Intent?) {
        if (isNetworkAvailable(context!!)) {
           isInternetAvaible.postValue(true)
        }
        else
        {
            isInternetAvaible.postValue(false)
        }
    }

    companion object
    {
        fun isNetworkAvailable(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }
}