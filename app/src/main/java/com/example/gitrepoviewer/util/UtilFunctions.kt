package com.example.gitrepoviewer.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class UtilFunctions @Inject constructor(){
    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        fun formatDate(dateString: String): String {
            val formatter = DateTimeFormatter.ISO_DATE_TIME
            val instant = Instant.from(formatter.parse(dateString))
            val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-d, hh:mm:ss a")
            return dateTimeFormatter.format(instant.atZone(ZoneId.systemDefault()))
        }
    }

}