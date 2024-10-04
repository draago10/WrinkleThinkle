package com.example.wrinklethinkle.viewmodel
import android.content.Context
import androidx.lifecycle.ViewModel
import java.io.IOException


class FlowerViewModel : ViewModel() {

    fun loadFlowerJson(context: Context, fileName: String): String? {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }
}