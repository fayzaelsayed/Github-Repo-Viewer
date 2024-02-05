package com.example.gitrepoviewer.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class UtilFunctions @Inject constructor(){
        fun isInternetWorking(): Boolean {
            return try {
                val url = URL("https://www.google.com")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.connect()
                connection.responseCode == 200
            } catch (e: IOException) {
                false
            }
        }

    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        fun formatDate(dateString: String): String {
            val formatter = DateTimeFormatter.ISO_DATE_TIME
            val instant = Instant.from(formatter.parse(dateString))
            val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-d, hh:mm:ss a")
            return dateTimeFormatter.format(instant.atZone(ZoneId.systemDefault()))
        }

        private val dataArray: Array<Array<Int>> = Array(10) { Array(10) { 0 } }

        fun setDataValue(row: Int, column: Int, value: Int) {
            dataArray[row][column] = value
        }

        fun getDataValue(row: Int, column: Int): Int {
            return dataArray[row][column]
        }
    }

}