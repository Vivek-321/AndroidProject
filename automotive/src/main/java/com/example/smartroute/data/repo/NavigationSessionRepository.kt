package com.example.smartroute.data.repo

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.smartroute.data.datastore.appDataStore
import com.example.smartroute.data.model.NavigationSession
import kotlinx.coroutines.flow.first
import org.json.JSONObject

class NavigationSessionRepository(private val context: Context) {

    private object Keys {
        val NAV_SESSION_JSON = stringPreferencesKey("nav_session_json")
    }

    suspend fun saveSession(session: NavigationSession) {
        val obj = JSONObject()
        obj.put("routeName", session.routeName)
        obj.put("start", session.start)
        obj.put("destination", session.destination)
        obj.put("currentStepIndex", session.currentStepIndex)
        obj.put("isNavigating", session.isNavigating)
        obj.put("isCompleted", session.isCompleted)

        context.appDataStore.edit { prefs ->
            prefs[Keys.NAV_SESSION_JSON] = obj.toString()
        }
    }

    suspend fun getSession(): NavigationSession? {
        val prefs = context.appDataStore.data.first()
        val json = prefs[Keys.NAV_SESSION_JSON] ?: return null

        val obj = JSONObject(json)
        return NavigationSession(
            routeName = obj.getString("routeName"),
            start = obj.getString("start"),
            destination = obj.getString("destination"),
            currentStepIndex = obj.getInt("currentStepIndex"),
            isNavigating = obj.getBoolean("isNavigating"),
            isCompleted = obj.getBoolean("isCompleted")
        )
    }

    suspend fun clearSession() {
        context.appDataStore.edit { prefs ->
            prefs.remove(Keys.NAV_SESSION_JSON)
        }
    }
}
