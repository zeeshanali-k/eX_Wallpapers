package com.techsensei.exwallpapers.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import java.io.File

fun getDestination(name: String, context: Context): Uri {
//if (SDK_INT <= 30) {
    val saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    if (!saveDir.exists()) saveDir.mkdirs()
    val destination = File(saveDir, name + System.currentTimeMillis() + ".jpg")
    return destination.toUri()
}
/*} else {
val contentVals = ContentValues()

contentVals.put(
    MediaStore.Images.ImageColumns.DISPLAY_NAME,
    name + System.currentTimeMillis() + ".jpg"
)

contentVals.put(
    MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM +
            File.separatorChar + Constants.APP_DIR_NAME
)
//setting mime type
contentVals.put(MediaStore.MediaColumns.MIME_TYPE, "image/*")
val imageSaveUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
return context.contentResolver.insert(imageSaveUri, contentVals)!!
}
*/*/