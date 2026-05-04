package com.example.smartroute.data.repo

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.smartroute.data.datastore.appDataStore
import com.example.smartroute.data.model.RecentRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecentRoutesRepository(private val context: Context) {

    private object Keys {
        val RECENT_ROUTES_JSON = stringPreferencesKey("recent_routes_json")
        val LAST_APK_UPDATE_TIME = longPreferencesKey("last_apk_update_time")
    }

    // Clears recent routes whenever a new APK is installed/updated
    suspend fun clearRecentRoutesIfApkUpdated() {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val currentApkUpdateTime = packageInfo.lastUpdateTime

        val prefs = context.appDataStore.data.first()
        val savedApkUpdateTime = prefs[Keys.LAST_APK_UPDATE_TIME]

        if (savedApkUpdateTime == null || savedApkUpdateTime != currentApkUpdateTime) {
            context.appDataStore.edit { preferences ->
                preferences[Keys.RECENT_ROUTES_JSON] = "[]"
                preferences[Keys.LAST_APK_UPDATE_TIME] = currentApkUpdateTime
            }
        }
    }

    fun recentRoutesFlow(): Flow<List<RecentRoute>> {
        return context.appDataStore.data.map { preferences ->
            val json = preferences[Keys.RECENT_ROUTES_JSON] ?: "[]"
            parseRoutes(json)
        }
    }

    suspend fun addRecentRoute(
        routeName: String,
        start: String,
        destination: String,
        durationMin: Int
    ) {
        val existing = recentRoutesFlow().first().toMutableList()

        val dateString = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            .format(Date())

        existing.add(
            0,
            RecentRoute(
                routeName = routeName,
                start = start,
                destination = destination,
                date = dateString,
                durationMin = durationMin
            )
        )

        val updated = existing.take(5)

        context.appDataStore.edit { preferences ->
            preferences[Keys.RECENT_ROUTES_JSON] = toJson(updated)
        }
    }

    private fun parseRoutes(json: String): List<RecentRoute> {
        val array = JSONArray(json)
        val result = mutableListOf<RecentRoute>()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            result.add(
                RecentRoute(
                    routeName = obj.getString("routeName"),
                    start = obj.getString("start"),
                    destination = obj.getString("destination"),
                    date = obj.getString("date"),
                    durationMin = obj.getInt("durationMin")
                )
            )
        }

        return result
    }

    private fun toJson(routes: List<RecentRoute>): String {
        val array = JSONArray()

        routes.forEach { route ->
            val obj = JSONObject()
            obj.put("routeName", route.routeName)
            obj.put("start", route.start)
            obj.put("destination", route.destination)
            obj.put("date", route.date)
            obj.put("durationMin", route.durationMin)
            array.put(obj)
        }

        return array.toString()
    }
}
