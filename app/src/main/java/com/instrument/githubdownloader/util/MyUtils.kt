package com.instrument.githubdownloader.util

import android.os.Environment
import com.instrument.githubdownloader.MyApp
import com.instrument.githubdownloader.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object MyUtils {


    fun assignOrIncrementIndex(filename: String): String {
        val regex = Regex("""^(.*?)(\((\d+)\))?(\.\w+)$""")
        val match = regex.matchEntire(filename)

        if (match != null) {
            val baseName = match.groupValues[1]
            val existingIndex = match.groupValues[3].toIntOrNull()
            val extension = match.groupValues[4]

            val newIndex = if (existingIndex != null) existingIndex + 1 else 1

            return "$baseName($newIndex)$extension"
        }

        return filename
    }

    fun getMemorySize(totalBytes: Long): String{
        var progressStr = ""
        val readKb = totalBytes / (1024.0)

        if(readKb<1024){
            progressStr = "${readKb.toInt()} Кб"
        }else{
            val mbRead = totalBytes / (1024.0 * 1024.0)
            progressStr = String.format("%.2f Мб", mbRead)
        }
        return progressStr
    }

    fun formatUnixTime(unixTime: Long): String {
        val date = Date(unixTime)
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(date)
    }

    fun isExistFile(fileName: String): Boolean{
        val pathFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val projectDir = File(pathFile, MyApp.globalContext.getString(R.string.app_name))
        val file = File(projectDir, fileName)
        return file.exists()
    }
}