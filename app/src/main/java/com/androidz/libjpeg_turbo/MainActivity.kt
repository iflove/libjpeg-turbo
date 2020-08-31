package com.androidz.libjpeg_turbo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        sample_text.text = stringFromJNI()


        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.aa)
        val result: String = getSaveLocation().toString() + "/compress1.png"
        var time = System.currentTimeMillis()
        val qu = 40
        nativeCompressBitmap(bitmap, qu, result)
        Log.e("C_TAG", "NAtive" + (System.currentTimeMillis() - time))
        time = System.currentTimeMillis()
        compressByDefault(bitmap, qu)
        Log.e("C_TAG", "Java" + (System.currentTimeMillis() - time))
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String
    external fun nativeCompressBitmap(bitmap: Bitmap?, quality: Int, destFile: String?): Int


    private fun compressByDefault(bitmap: Bitmap, quality: Int) {
        val file = File(getSaveLocation().toString() + "/compress2.png")
        if (file.exists()) {
            try {
                file.delete()
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun getSaveLocation(): String? {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
    }

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
