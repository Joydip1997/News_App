package com.example.newapp.UI.Activities

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newapp.Data.DB.ArticleRoomDatabase
import com.example.newapp.Data.Repository.Repository
import com.example.newapp.InternetBroadCast
import com.example.newapp.R
import com.example.newapp.ViewModel.NewsViewModel
import com.example.newapp.ViewModel.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var alertDialog: AlertDialog
    private lateinit var viewModelFactory: ViewModelFactory
    lateinit var newsViewModel : NewsViewModel
    private lateinit var mNetworkReceiver : InternetBroadCast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.setupWithNavController(newNavHostFragment.findNavController())
        val repository = Repository(ArticleRoomDatabase(this))
        viewModelFactory = ViewModelFactory(repository)
        newsViewModel = ViewModelProvider(this, viewModelFactory).get(NewsViewModel::class.java)

        mNetworkReceiver = InternetBroadCast()
        registerNetworkBroadcastForNougat()
        noInternetDialog()
        checkInternet()
    }

    private fun checkInternet() {
        mNetworkReceiver.isInternetAvaible.observe(this, Observer {
            if (it) {
                alertDialog.dismiss()
                val locale: String = getResources().getConfiguration().locale.getCountry()
                newsViewModel.getBreakingNews(locale.toLowerCase())
            } else {
                alertDialog.show()
            }
        })
    }





    private fun noInternetDialog()
    {
       alertDialog = MaterialAlertDialogBuilder(this)
            .setTitle("You are disconnected")
            .setMessage("Please enable mobile data or Wifi")
            .setCancelable(false)
            .setNegativeButton("Saved News") { dialog, which ->
                newNavHostFragment.findNavController().navigate(R.id.savedNewsFragment)
            }
            .setPositiveButton("Retry") { dialog, which ->
                MainScope().launch {
                    delay(500L)
                    checkInternet()
                }
            }
            .show()
    }

    private fun registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(
                mNetworkReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(
                mNetworkReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

    protected fun unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterNetworkChanges()
    }
}