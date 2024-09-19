package com.example.lab4

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject

class SharedPrefsControl (private var context: Context) {
    fun saveMetaData(key: String, title: String, description: String, tag: String) {
        val prefs = context.getSharedPreferences("Metadata", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        val metadataJson = JSONObject().apply {
            put("title", title)
            put("description", description)
            put("tag", tag)
        }

        editor.putString(key, metadataJson.toString())
        editor.apply()
    }

    fun readMetaData(key: String): Map<String, Any?>? {

        val sharedPreferences: SharedPreferences = context.getSharedPreferences("Metadata", Context.MODE_PRIVATE)

        val jsonString = sharedPreferences.getString(key, null) ?: return null
        val jsonObject = JSONObject(jsonString)
        val title = jsonObject.optString("title", null.toString())
        val description = jsonObject.optString("description", null.toString())
        val tag = jsonObject.optString("tag", null.toString())

        return mapOf(
            "title" to title,
            "description" to description,
            "tag" to tag
        )
    }

    fun findKeyByTag(searchItem: String): List<String> {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("Metadata", Context.MODE_PRIVATE)
        val allEntries = sharedPreferences.all
        val keys_list = mutableListOf<String>()

        for ((key, value) in allEntries) {
            if (value is String) {
                val jsonObject = JSONObject(value)
                val title = jsonObject.optString("title", null)
                val description = jsonObject.optString("description", null)
                val tag = jsonObject.optString("tag", null)

                if (title == searchItem && !keys_list.contains(key)) {
                    keys_list.add(key)
                }

                if (description == searchItem && !keys_list.contains(key)) {
                    keys_list.add(key)
                }

                if (tag == searchItem && !keys_list.contains(key)) {
                    keys_list.add(key)
                }
            }
        }
        return keys_list
    }

}