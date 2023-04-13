package com.midland.ynote.Utilities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.midland.ynote.Fragments.Documents
import com.midland.ynote.Objects.Schools
import com.midland.ynote.Objects.SourceDocObject
import com.ortiz.touchview.TouchImageView
import java.io.*
import java.util.concurrent.Executors

class FilingSystem {

    companion object Companion {

        //doc upload
        var knowledgeBase = ""
        var schoolName = ""
        var dept = ""
        var flag = ""
        var mainField = ""
        var subField = ""
        var semester = ""
        var institution = ""
        var unitCode = ""
        var docDetail = ""

        var schools = java.util.ArrayList<Schools>()

        var pictures = ""
        var narrations = ""
        var descriptions = ""

        var allTags = ArrayList<String>()
        var selectedSubFields = ArrayList<String>()
        var selectedMainFields = ArrayList<String>()



        fun loadHistory(con: Context, fileName: String): ArrayList<SourceDocObject> {
            val sharedPreferences = con.getSharedPreferences(fileName, Context.MODE_PRIVATE)
            var historyDocs: ArrayList<SourceDocObject> = ArrayList()
            if (sharedPreferences.contains(fileName)) {
                val gson = Gson()
                val json = sharedPreferences.getString(fileName, "")
                val type = object : TypeToken<ArrayList<SourceDocObject>>() {}.type
                historyDocs.addAll(gson.fromJson(json, type)!!)
            }
            return historyDocs
        }

        //    //GETTING ALL IMAGES FROM EXTERNAL STORAGE
        //    private ArrayList<SourceDocObject> getFileObjects() {
        //        Uri uri = MediaStore.Files.getContentUri("external");
        //        String[] projection = {MediaStore.Files.FileColumns.DATA};
        //        Cursor c = null;
        //        SortedSet<String> dirList = new TreeSet<>();
        //        ArrayList<SourceDocObject> sourceDocObjects = new ArrayList<>();
        //        String[] directories = null;
        //        String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        //
        //        //USED AS ARGUMENTS FOR CURSOR
        //        String pdf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
        //        String doc = MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc");
        //        String docx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx");
        //        String xls = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xls");
        //        String xlsx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xlsx");
        //        String ppt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("ppt");
        //        String pptx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pptx");
        //        String txt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt");
        //        String rtx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtx");
        //        String rtf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtf");
        //        String html = MimeTypeMap.getSingleton().getMimeTypeFromExtension("html");
        //
        //        //Where
        //        String where = MediaStore.Files.FileColumns.MIME_TYPE + "=?"
        //                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
        //                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
        //                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
        //                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
        //                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
        //                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
        //                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
        //                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?" + " OR "
        //                + MediaStore.Files.FileColumns.MIME_TYPE + "=?" + " OR "
        //                + MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        //
        //        //args
        //        String[] args = new String[]{pdf};
        //
        //
        //        if (uri != null) {
        //            c = getContext().getContentResolver().query(uri, projection, where, args, orderBy);
        //        }
        //
        //        if ((c != null) && (c.moveToFirst())) {
        //            do {
        //                String tempDir = c.getString(0);
        //                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
        //                try {
        //                    dirList.add(tempDir);
        //                } catch (Exception e) {
        //                    e.printStackTrace();
        //                }
        //            }
        //            while (c.moveToNext());
        //            directories = new String[dirList.size()];
        //            dirList.toArray(directories);
        //        }
        //
        //        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM, yyyy");
        //        for (int i = 0; i < dirList.size(); i++) {
        //            File imageDir = new File(directories[i]);
        //            File[] docList = imageDir.listFiles();
        //            if (imageDir == null)
        //                continue;
        //
        //            for (File docPath : docList) {
        //                try {
        //                    if (docPath.isDirectory()) {
        //                        docList = docPath.listFiles();
        //                    }
        //
        //                    File docFile = docPath.getAbsoluteFile();
        //                    Uri docUri = Uri.fromFile(docFile);
        //                    int fileSize = Integer.parseInt(String.valueOf(docFile.length()/1024));
        //                    SourceDocObject sourceDocObject = new SourceDocObject(docFile.getName(), Uri.fromFile(docFile),
        //                            String.valueOf(fileSize), dateFormat.format(docFile.lastModified()), docUri);
        //                    if (sourceDocObject.getName().endsWith("pdf") || sourceDocObject.getName().endsWith("PDF")
        //                            || sourceDocObject.getName().endsWith("doc") || sourceDocObject.getName().endsWith("DOC")
        //                            || sourceDocObject.getName().endsWith("docx") || sourceDocObject.getName().endsWith("DOCX")
        //                            || sourceDocObject.getName().endsWith("ppt") || sourceDocObject.getName().endsWith("PPT")
        //                            || sourceDocObject.getName().endsWith("yNote")
        //                            || sourceDocObject.getName().endsWith("pptx") || sourceDocObject.getName().endsWith("PPTX")){
        //
        //                        sourceDocObjects.add(sourceDocObject);
        //                    }
        //
        //                } catch (Exception e) {
        //                    e.printStackTrace();
        //                }
        //            }
        //        }
        //
        //        return sourceDocObjects;
        //    }
        fun saveHistory(myObjects: ArrayList<SourceDocObject>, c: Context) {
            val preferences = c.getSharedPreferences("documentHistory", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            val gson = Gson()
            val json = gson.toJson(myObjects)
            editor.putString("documentHistory", json)
            editor.apply()
        }



        fun getRealPathFromURI(uri: Uri, context: Context): String? {
            val returnCursor = context.contentResolver.query(uri, null, null, null, null)
            val nameIndex =  returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
            returnCursor.moveToFirst()
            val name = returnCursor.getString(nameIndex)
            val size = returnCursor.getLong(sizeIndex).toString()
            val file = File(context.filesDir, name)
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val outputStream = FileOutputStream(file)
                var read = 0
                val maxBufferSize = 1 * 1024 * 1024
                val bytesAvailable: Int = inputStream?.available() ?: 0
                //int bufferSize = 1024;
                val bufferSize = Math.min(bytesAvailable, maxBufferSize)
                val buffers = ByteArray(bufferSize)
                while (inputStream?.read(buffers).also {
                        if (it != null) {
                            read = it
                        }
                    } != -1) {
                    outputStream.write(buffers, 0, read)
                }
                Log.e("File Size", "Size " + file.length())
                inputStream?.close()
                outputStream.close()
                Log.e("File Path", "Path " + file.path)

            } catch (e: java.lang.Exception) {
                Log.e("Exception", e.message!!)
            }
            return file.path
        }
        fun downloadImage(touchIV: TouchImageView, imageURL: String){
            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())
            var image: Bitmap? = null
            executor.execute {
                try {
                    val `in` = java.net.URL(imageURL).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    // Only for making changes in UI
                    handler.post {
                        touchIV.setImageBitmap(image)
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }

        fun isExternalStorageReadWritable(): Boolean {
            return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                Log.i("State", "writable")
                true
            } else {
                false
            }
        }

        fun checkPermission(permission: String?, c: Context?): Boolean {
            val check = ContextCompat.checkSelfPermission(c!!, permission!!)
            return check == PackageManager.PERMISSION_GRANTED
        }

        var stamp = String()
        var file = File(
                Environment
                        .getExternalStorageDirectory()
                        .toString() + File.separator + "yNoteStudios/"
        )

        val subFile1 = File(file, "DabLectures")
        val completedLectures = File(subFile1, "Completed Lectures")
        val pendingLectures = File(subFile1, "Pending Lectures")
        val insidePendingLec = File(completedLectures, "")

        val subFile2 = File(file, "DabMedia")
        val dubImages = File(subFile2, "DabImages")
        val dubDocuments = File(subFile2, "DabDocuments")
        val insideDubDocs = File(dubDocuments, "")
        val dubAnthems = File(subFile2, "DabAnthems-ENCRYPT-license.mayo")
        val dubVideos = File(subFile2, "DabVideos")

        val subFile3 = File(file, "Manifest")
        val uploadedLecturesManifest = File(subFile3, "ul.manifest")
        val uploadedVideosManifest = File(subFile3, "uv.manifest")
        val uploadedAnthemsManifest = File(subFile3, "ua.manifest")
        val mainFeedManifest = File(subFile3, "mf.manifest")
        val shelfManifest = File(subFile3, "s.manifest")

        fun checkAndRequestPermission(a: Activity): Boolean {
            val readPermission = ContextCompat.checkSelfPermission(a, Manifest.permission.READ_EXTERNAL_STORAGE)
            val writePermission = ContextCompat.checkSelfPermission(a, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val neededPermission: MutableList<String> = java.util.ArrayList()
            if (readPermission != PackageManager.PERMISSION_GRANTED) {
                neededPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            if (writePermission != PackageManager.PERMISSION_GRANTED) {
                neededPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (neededPermission.isNotEmpty()) {
                ActivityCompat.requestPermissions(a, neededPermission.toTypedArray(), Documents.REQUEST_MULTI_PERMISSION_ID)
                return false
            }
            return true
        }


        fun createRelevantFolders() {
            if (!file.exists()) {
                file.mkdir()

                subFile1.mkdir()
                completedLectures.mkdir()
                pendingLectures.mkdir()
//                photoDocuments.mkdir()

                subFile2.mkdir()
                dubImages.mkdir()
                dubDocuments.mkdir()
                dubAnthems.mkdir()
                dubVideos.mkdir()

                subFile3.mkdir()


                try {
                    val fos = FileOutputStream(shelfManifest)
                    val fos1 = FileOutputStream(uploadedLecturesManifest)
                    val fos2 = FileOutputStream(mainFeedManifest)
                    val fos3 = FileOutputStream(uploadedAnthemsManifest)
                    val fos4 = FileOutputStream(uploadedVideosManifest)

                    fos.write("".toByteArray())
                    fos.close()
                    fos1.close()
                    fos2.close()
                    fos3.close()
                    fos4.close()
                }catch (e: IOException){
                    e.printStackTrace()
                }


            }
        }


        fun writeUploadedVideosManifest(c: Context, uri: Uri?) {
            if (isExternalStorageReadWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, c.applicationContext)) {
                try {
                    val br = BufferedReader(FileReader(uploadedVideosManifest))
                    val manifestContent: String = br.readLine()
                    val fos = FileOutputStream(uploadedVideosManifest)
                    fos.write((manifestContent + uri.toString() + "_-_").toByteArray())
                    fos.close()
                    Toast.makeText(c.applicationContext, "Lecture manifest saved", Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(c.applicationContext, "Something happened. You might lose your points.", Toast.LENGTH_LONG).show()
            }
        }

        fun writeUploadedAnthemsManifest(c: Context, uri: Uri?) {
            if (isExternalStorageReadWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, c.applicationContext)) {
                try {
                    val br = BufferedReader(FileReader(uploadedAnthemsManifest))
                    val manifestContent: String = br.readLine()
                    val fos = FileOutputStream(uploadedAnthemsManifest)
                    fos.write((manifestContent + uri.toString() + "_-_").toByteArray())
                    fos.close()
                    Toast.makeText(c.applicationContext, "Lecture manifest saved", Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(c.applicationContext, "Something happened. You might lose your points.", Toast.LENGTH_LONG).show()
            }
        }

        fun writeUploadedShelfManifest(c: Context, uri: Uri?) {
            if (isExternalStorageReadWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, c.applicationContext)) {
                try {
                    val br = BufferedReader(FileReader(shelfManifest))
                    val manifestContent: String = br.readLine()
                    val fos = FileOutputStream(shelfManifest)
                    fos.write((manifestContent + uri.toString() + "_-_").toByteArray())
                    fos.close()
                    Toast.makeText(c.applicationContext, "Lecture manifest saved", Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(c.applicationContext, "Something happened. You might lose your points.", Toast.LENGTH_LONG).show()
            }
        }

    }
}

