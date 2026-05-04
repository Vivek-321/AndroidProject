package com.example.smartroute.data.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.appDataStore by preferencesDataStore(name = "smartroute_prefs")
