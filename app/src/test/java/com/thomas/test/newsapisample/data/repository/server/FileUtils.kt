package com.isansc.apollographqltestwithmockwebserverpoc.tools

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object FileUtils {


    fun loadJSONFromAsset(context: Context, jsonPath: String?): String? {
        var json: String? = null
        try {
            val `is` = context.assets.open(jsonPath!!)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return json
    }

    fun readJson(path: String): String {
        val url = FileUtils::class.java.classLoader!!.getResource(path) ?: throw RuntimeException("File not found: $path")

        var inputStream: InputStream? = null
        try {
            inputStream = url.openStream()
            val reader = BufferedReader(InputStreamReader(inputStream))
            var file = ""
            while (reader.ready()) {
                file += reader.readLine()
            }
            return file
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        throw RuntimeException("File not found: $path")
    }
}
