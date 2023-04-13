package com.midland.ynote.Utilities

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.midland.ynote.Activities.PhotoDoc
import com.midland.ynote.Activities.PhotoDoc.Companion.loadPictorials1
import com.midland.ynote.Objects.BitMapTitle
import com.midland.ynote.Objects.PictorialObject
import java.io.File
import kotlin.collections.set

class Publisher {

    companion object Comp {
        private var uploadTask: StorageTask<UploadTask.TaskSnapshot>? = null
        private var ref: DatabaseReference? = null
        private var publishedDocs: DatabaseReference? = null


        private val descrs = ArrayList<String>()
        private val descrsPos = ArrayList<Int>()

        fun generateSearchKeyWord(inputText: String): List<String> {
            var inputString = inputText.toLowerCase()
            val keywords = mutableListOf<String>()
            val words = inputString.split(" ")
//            val appendString = StringBuilder()
            //Empty for each word
            for (word in words) {
//                appendString.append(inputString)
//                for (charPosition in inputString.indices) {
//                    appendString.append(inputString[charPosition].toString())
//                    keywords.add(appendString.toString())
//                }
                keywords.add(inputString)
                inputString = inputString.replace(word, "")
            }
            return keywords
        }

        fun publishPictorial(
            con: Context,
            fileName: String,
            user: FirebaseUser,
            progressBar: ProgressBar,
            stopUpload: ImageButton,
            area: String,
            pos: Int,
            bitMapTitle: BitMapTitle
        ) {

            if (uploadTask != null && uploadTask!!.isInProgress) {
                Toast.makeText(con, "Give me a sec!", Toast.LENGTH_SHORT).show()
            }
//            if (uploadTask1 != null && uploadTask1!!.isInProgress) {
//                Toast.makeText(con, "Give me a sec!", Toast.LENGTH_SHORT).show()
//            }

            val sharedPreferences = con.getSharedPreferences(fileName, Context.MODE_PRIVATE)
            if (sharedPreferences.contains(fileName)) {
                PhotoDoc.pictorials1 = ArrayList()
                PhotoDoc.pictorials1 = PhotoDoc.loadPictorials(con, fileName)
                progressBar.visibility = View.VISIBLE
                progressBar.bringToFront()
                uploadDoc(
                    con,
                    user,
                    PhotoDoc.pictorials1,
                    progressBar,
                    stopUpload,
                    fileName,
                    area,
                    pos,
                    bitMapTitle
                )
            } else {
                Toast.makeText(con, "No bitmaps found.", Toast.LENGTH_SHORT).show()
            }
        }


        private fun uploadDoc(
            con: Context,
            user: FirebaseUser,
            pictorials: ArrayList<PictorialObject>,
            uploadProgress: ProgressBar,
            stopUpload: ImageButton,
            name: String,
            area: String,
            pos: Int,
            bitMapTitle: BitMapTitle
        ) {
            uploading(pictorials, uploadProgress, stopUpload, con, name, pos, bitMapTitle)

        }

        private fun uploading(
            pictorials: ArrayList<PictorialObject>,
            uploadProgress: ProgressBar,
            stopUpload: ImageButton,
            con: Context,
            fileName: String,
            pos: Int,
            bitMapTitle: BitMapTitle
        ) {
//            FilingSystem.descriptions = ""
//            FilingSystem.narrations = ""

            var userID = FirebaseAuth.getInstance().uid
            val bitmapsDatabaseRef = FirebaseFirestore.getInstance()
                .collection("Pictorials").document(fileName)
            val narrationStorageRef = FirebaseStorage.getInstance().getReference("Narrations")
            val bitmapStorageRef = FirebaseStorage.getInstance().getReference("Bitmaps")

            val relevance = loadPictorials1(con, "bitmapTitle")[pos].relevance

                if (pictorials.size >= 4) {
//                    for (pic in pictorials){

                    stopUpload.setOnClickListener {
//                        if (uploadTask1!!.isInProgress){
//                            uploadTask1!!.cancel()
//                        }
                        if (uploadTask!!.isInProgress) {
                            uploadTask!!.cancel()
                        }
                        Toast.makeText(con, "Upload halted!", Toast.LENGTH_SHORT).show()
                    }

                    val fileReference1 = bitmapStorageRef.child("Bitmaps")
                    val pcs = StringBuilder()
                    val picLocale = ArrayList<String>()
                    val narLocale = ArrayList<String>()
                    val picDoneList = ArrayList<String>()
                    val narDoneList = ArrayList<String>()
                    val pics = ArrayList<String>()
                    val nars = ArrayList<String>()

                    var i = 0;
                    do{
                        descrs.add(pictorials[i].picDescription)
                        var uT: StorageTask<UploadTask.TaskSnapshot>? = null
                        val count = i
                        val count1 = i
                        picLocale.add(pictorials[i].picture)
                        narLocale.add(pictorials[i].narrationPath)
                        pics.add("PendingImage")
                        nars.add("PendingNarration")
                        picDoneList.add("Uploading")
                        narDoneList.add("Uploading1")
                        val time = System.currentTimeMillis().toString()
                        val finalRef = fileReference1.child(time)

                        uT = finalRef.putFile(Uri.parse(picLocale[count]))
                            .addOnSuccessListener {
                                uT!!.continueWithTask {
                                    finalRef.downloadUrl
                                }.addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            //String userID = FirebaseAuth.getInstance().getUid();
                                            picDoneList.removeAt(count)
                                            picDoneList.add(count, "Done");

                                            var downloadLink = it.result.toString()
                                            pics.removeAt(count)
                                            pics.add(count, downloadLink)

                                            uT = null
                                            downloadLink = ""

                                            uploadProgress.progress = 0
                                            if (File(pictorials[count].narrationPath).exists()
                                                && pictorials[count].narrationPath != null
                                            ) {
                                                val time1 = System.currentTimeMillis().toString()
                                                val fileReference = narrationStorageRef.child("Narrations").child(time1)
                                                uploadTask =
                                                    fileReference.putFile(Uri.fromFile(File(narLocale[count1])))
                                                        .addOnSuccessListener {
                                                            val uriTask: Task<Uri> =
                                                                uploadTask!!.continueWithTask { task ->
                                                                    if (!task.isSuccessful) {
                                                                        throw task.exception!!
                                                                    }
                                                                    fileReference.downloadUrl
                                                                }.addOnCompleteListener { task ->
                                                                    if (task.isSuccessful) {
                                                                        narDoneList.removeAt(count1)
                                                                        narDoneList.add(count1, "Done1");

                                                                        Toast.makeText(con, narDoneList.toString(), Toast.LENGTH_SHORT).show()

                                                                        val downloadLink1 = task.result
                                                                        nars.removeAt(count1)
                                                                        nars.add(count1, downloadLink1.toString())


                                                                        if (!(picDoneList.contains("Uploading")) && !(narDoneList.contains("Uploading1"))) {
                                                                            Toast.makeText(con, "Equality", Toast.LENGTH_SHORT).show()
                                                                            writeDB(
                                                                                con,
                                                                                fileName,
                                                                                pics,
                                                                                nars,
                                                                                descrs,
                                                                                bitmapsDatabaseRef,
                                                                                relevance,
                                                                                uploadProgress
                                                                            )

                                                                        }

                                                                        uploadProgress.progress = 0
                                                                        i++
                                                                        //Call the write when condition is met
                                                                    } else {
                                                                        Toast.makeText(con, "Something's up!.", Toast.LENGTH_SHORT).show()
                                                                    }
                                                                }
                                                            uploadProgress.progress = 0
                                                            //                            final String uploadID = shelfDatabaseRef.push().getKey(); //CREATES NEW UNIQUE ID
                                                        }
                                                        .addOnFailureListener { e ->
                                                            e.message?.let {
                                                                Log.e(
                                                                    "DocUploadError",
                                                                    it
                                                                )
                                                            }
                                                            Toast.makeText(
                                                                con,
                                                                "Something's preventing your narration upload.",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                        .addOnProgressListener { taskSnapshot ->
                                                            val progress =
                                                                100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                                                            uploadProgress.progress = progress.toInt()
                                                            //TO BE PLACED INSIDE A PROGRESS BAR LATER IN THE PROJECT
                                                            //PROGRESS BAR SHALL BE SET BACK TO ZERO ON SUCCESS
                                                        }
                                                        .addOnCompleteListener {

                                                        }
                                            } else {
                                                narDoneList.removeAt(count1)
                                                narDoneList.add(count1, "Done1");
                                                nars.removeAt(count1)
                                                nars.add(count1, "null")

                                                if (!(picDoneList.contains("Uploading")) && !(narDoneList.contains("Uploading1"))) {
                                                    Toast.makeText(con, "Equality", Toast.LENGTH_SHORT).show()
                                                    writeDB(
                                                        con,
                                                        fileName,
                                                        pics,
                                                        nars,
                                                        descrs,
                                                        bitmapsDatabaseRef,
                                                        relevance,
                                                        uploadProgress
                                                    )

                                                }


                                            }
                                        }
                                    uploadProgress.progress = 0
                                    }
                                }

                        i++
                    }while (i < pictorials.size)

//                    for (pic in pictorials) {
//                        descrs.add(pic.picDescription)
//                        var uploadTask1: StorageTask<UploadTask.TaskSnapshot>? = null
//
//
//                        uploadTask1 = fileReference1.putFile(Uri.parse(pic.picture))
//                            .addOnSuccessListener {
//                                uploadTask1!!.continueWithTask { task ->
////                                    if (!task.isSuccessful) {
////                                        throw task.exception!!
////                                    }
//                                    fileReference1.downloadUrl
//                                }.addOnCompleteListener { task ->
//                                    if (task.isSuccessful) {
//                                        //String userID = FirebaseAuth.getInstance().getUid();
//                                        pcs.append(task.result.toString() + "_-_")
//                                        Toast.makeText(con, pcs, Toast.LENGTH_SHORT).show()
//                                        pics.add(task.result.toString())
//                                        picsPos.add(pictorials.indexOf(pic))
//                                        Toast.makeText(con, "picPos $picsPos", Toast.LENGTH_SHORT)
//                                            .show()
//                                        uploadProgress.progress = 0
//                                        if (File(pic.narrationPath).exists()
//                                            && pic.narrationPath != null
//                                        ) {
//                                            val fileReference =
//                                                narrationStorageRef.child("Narrations")
//                                            uploadTask =
//                                                fileReference.putFile(Uri.fromFile(File(pic.narrationPath)))
//                                                    .addOnSuccessListener {
//                                                        val uriTask: Task<Uri> =
//                                                            uploadTask!!.continueWithTask { task ->
//                                                                if (!task.isSuccessful) {
//                                                                    throw task.exception!!
//                                                                }
//                                                                fileReference.downloadUrl
//                                                            }.addOnCompleteListener { task ->
//                                                                if (task.isSuccessful) {
//                                                                    val downloadLink1 = task.result
//                                                                    nars.add(downloadLink1.toString())
//                                                                    narsPos.add(
//                                                                        pictorials.indexOf(
//                                                                            pic
//                                                                        )
//                                                                    )
//                                                                    Toast.makeText(
//                                                                        con,
//                                                                        "narPos $narsPos",
//                                                                        Toast.LENGTH_SHORT
//                                                                    ).show()
//
//                                                                    if (pics.size == pictorials.size && nars.size == pictorials.size) {
//                                                                        Toast.makeText(
//                                                                            con,
//                                                                            "Equality",
//                                                                            Toast.LENGTH_SHORT
//                                                                        ).show()
//                                                                        val finishTask =
//                                                                            Tasks.whenAll(task)
//                                                                        finishTask.addOnCompleteListener {
//                                                                            val pcsArr =
//                                                                                ArrayList<String>()
//                                                                            pcsArr.addAll(
//                                                                                pcs.toString()
//                                                                                    .split("_-_")
//                                                                            )
//                                                                            pcsArr.remove("")
//                                                                            for (pcsA in pcsArr) {
//                                                                                Toast.makeText(
//                                                                                    con,
//                                                                                    pcsA,
//                                                                                    Toast.LENGTH_SHORT
//                                                                                ).show()
//                                                                            }
//                                                                            writeDB(
//                                                                                con,
//                                                                                fileName,
//                                                                                pcsArr,
//                                                                                picsPos,
//                                                                                nars,
//                                                                                narsPos,
//                                                                                descrs,
//                                                                                bitmapsDatabaseRef,
//                                                                                relevance,
//                                                                                uploadProgress
//                                                                            )
//                                                                        }
//
//                                                                    }
//
//                                                                    uploadProgress.progress = 0
//                                                                    //Call the write when condition is met
//                                                                } else {
//                                                                    Toast.makeText(
//                                                                        con,
//                                                                        "Something's up!.",
//                                                                        Toast.LENGTH_SHORT
//                                                                    )
//                                                                        .show()
//                                                                }
//                                                            }
//                                                        uploadProgress.progress = 0
//                                                        //                            final String uploadID = shelfDatabaseRef.push().getKey(); //CREATES NEW UNIQUE ID
//                                                    }
//                                                    .addOnFailureListener { e ->
//                                                        e.message?.let {
//                                                            Log.e(
//                                                                "DocUploadError",
//                                                                it
//                                                            )
//                                                        }
//                                                        Toast.makeText(
//                                                            con,
//                                                            "Something's preventing your narration upload.",
//                                                            Toast.LENGTH_SHORT
//                                                        ).show()
//                                                    }
//                                                    .addOnProgressListener { taskSnapshot ->
//                                                        val progress =
//                                                            100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
//                                                        uploadProgress.progress = progress.toInt()
//                                                        //TO BE PLACED INSIDE A PROGRESS BAR LATER IN THE PROJECT
//                                                        //PROGRESS BAR SHALL BE SET BACK TO ZERO ON SUCCESS
//                                                    }
//                                                    .addOnCompleteListener {
//
//                                                    }
//                                        } else {
//                                            nars.add("null")
//                                            narsPos.add(pictorials.indexOf(pic))
//                                            Toast.makeText(
//                                                con,
//                                                "narPos $narsPos",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//
//                                            if (pics.size == pictorials.size && nars.size == pictorials.size) {
//                                                Toast.makeText(con, "Equality", Toast.LENGTH_SHORT)
//                                                    .show()
//                                                val finishTask = Tasks.whenAll(task)
//                                                finishTask.addOnCompleteListener {
//                                                    val pcsArr = ArrayList<String>()
//                                                    pcsArr.addAll(pcs.toString().split("_-_"))
//                                                    writeDB(
//                                                        con,
//                                                        fileName,
//                                                        pcsArr,
//                                                        picsPos,
//                                                        nars,
//                                                        narsPos,
//                                                        descrs,
//                                                        bitmapsDatabaseRef,
//                                                        relevance,
//                                                        uploadProgress
//                                                    )
//                                                }
//
//                                            }
//
//                                        }
//                                    }
//                                }
//                                uploadProgress.progress = 0
//                                //                            final String uploadID = shelfDatabaseRef.push().getKey(); //CREATES NEW UNIQUE ID
//
//
//                            }
//                            .addOnFailureListener { e ->
//                                e.message?.let { Log.e("DocUploadError", it) }
//                                pics.add("null")
//
////                        writeDB(pictorials, pic, con, fileName,
////                            pics, nars, descrs, pos, bitmapsDatabaseRef, uploadTask!!)
//                                Toast.makeText(
//                                    con,
//                                    "Pic #${pic.picDescription} upload failed!",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                            .addOnProgressListener { taskSnapshot ->
//                                val progress =
//                                    100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
//                                uploadProgress.progress = progress.toInt()
//                                //TO BE PLACED INSIDE A PROGRESS BAR LATER IN THE PROJECT
//                                //PROGRESS BAR SHALL BE SET BACK TO ZERO ON SUCCESS
//                            }
//
//
//                    }



                } else {
                    Toast.makeText(con, "Can't post less than 4 slides", Toast.LENGTH_SHORT).show()
                }
//                for (pic in pictorials) {
//                    descrs.add(pic.picDescription)
//
//                    val fileReference1 = bitmapStorageRef.child(pic.picture)
//                    uploadTask1 = fileReference1.putFile(Uri.parse(pic.picture))
//                        .addOnSuccessListener {
//                            uploadTask1!!.continueWithTask { task ->
//                                if (!task.isSuccessful) {
//                                    throw task.exception!!
//                                }
//                                fileReference1.downloadUrl
//                            }.addOnCompleteListener { task ->
//                                if (task.isSuccessful) {
//                                    //String userID = FirebaseAuth.getInstance().getUid();
//                                    val downloadLink = task.result.toString()
//                                    pics.add(downloadLink)
//                                    Toast.makeText(con, pics.size.toString(), Toast.LENGTH_SHORT).show()
////                                pictures = if (pictures == "") {
////                                    downloadLink
////                                } else {
////                                    pictures + "_-_" + downloadLink
////                                }
//
////                                descriptions = if (descriptions == "") {
////                                    pic.picDescription
////                                } else {
////                                    descriptions + "_-_" + pic.picDescription
////                                }
//
//
////                                writeDB(pictorials, pic, con, fileName,
////                                    pics, nars,
////                                    descrs, pos, bitmapsDatabaseRef,
////                                    uploadTask1!!)
//
//                                    uploadProgress.progress = 0
//                                }
//                            }
//                            uploadProgress.progress = 0
//                            //                            final String uploadID = shelfDatabaseRef.push().getKey(); //CREATES NEW UNIQUE ID
//
//                            uploadProgress.visibility = View.GONE
//
//                        }
//                        .addOnFailureListener { e ->
//                            e.message?.let { Log.e("DocUploadError", it) }
////                        pictures = if (pictures == "") {
////                            "null"
////                        } else {
////                            pictures + "_-_" + "null"
////                        }
//                            pics.add("null")
//
////                        writeDB(pictorials, pic, con, fileName,
////                            pics, nars, descrs, pos, bitmapsDatabaseRef, uploadTask!!)
//                            Toast.makeText(
//                                con,
//                                "${pic.picDescription} nar upload failed.",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                        .addOnProgressListener { taskSnapshot ->
//                            val progress =
//                                100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
//                            uploadProgress.progress = progress.toInt()
//                            //TO BE PLACED INSIDE A PROGRESS BAR LATER IN THE PROJECT
//                            //PROGRESS BAR SHALL BE SET BACK TO ZERO ON SUCCESS
//                        }
//
//                    if (File(pic.narrationPath).exists()
//                        && pic.narrationPath != null
//                    ) {
//                        val fileReference = narrationStorageRef.child(pic.narrationPath)
//                        uploadTask = fileReference.putFile(Uri.parse(pic.narrationPath))
//                            .addOnSuccessListener {
//                                val uriTask: Task<Uri> =
//                                    uploadTask!!.continueWithTask { task ->
//                                        if (!task.isSuccessful) {
//                                            throw task.exception!!
//                                        }
//                                        fileReference.downloadUrl
//                                    }.addOnCompleteListener { task ->
//                                        if (task.isSuccessful) {
//                                            val downloadLink = task.result
//                                            nars.add(downloadLink.toString())
////                                        if (FilingSystem.narrations == "") {
////                                            FilingSystem.narrations =
////                                                downloadLink.toString()
////                                        } else {
////                                            FilingSystem.narrations =
////                                                FilingSystem.narrations + "_-_" + downloadLink.toString()
////                                        }
////                                        val selectedDoc = SelectedDoc(metaData + "_-_"
////                                                + user.getUid(), downloadLink.toString())
//
//                                            Toast.makeText(
//                                                con,
//                                                "${pic.picDescription} Nar Upload successful",
//                                                Toast.LENGTH_SHORT
//                                            )
//                                                .show()
//                                            uploadProgress.progress = 0
//                                            //Call the write when condition is met
//                                        } else {
//                                            Toast.makeText(
//                                                con,
//                                                "Something's up!.",
//                                                Toast.LENGTH_SHORT
//                                            )
//                                                .show()
//                                        }
//                                    }
//                                uploadProgress.progress = 0
//                                //                            final String uploadID = shelfDatabaseRef.push().getKey(); //CREATES NEW UNIQUE ID
//                            }
//                            .addOnFailureListener { e ->
//                                e.message?.let { Log.e("DocUploadError", it) }
//                                Toast.makeText(
//                                    con,
//                                    "Something's preventing your narration upload.",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                            .addOnProgressListener { taskSnapshot ->
//                                val progress =
//                                    100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
//                                uploadProgress.progress = progress.toInt()
//                                //TO BE PLACED INSIDE A PROGRESS BAR LATER IN THE PROJECT
//                                //PROGRESS BAR SHALL BE SET BACK TO ZERO ON SUCCESS
//                            }
//                            .addOnCompleteListener {
//                                uploadProgress.visibility = View.GONE
//
//                            }
//                    } else {
//                        nars.add("null")
////                    if (FilingSystem.narrations == "") {
////                        FilingSystem.narrations = "null"
////                    } else {
////                        FilingSystem.narrations = FilingSystem.narrations + "_-_" + "null"
////                    }
//                    }
//
//                    uploadTask1!!.addOnCompleteListener {
//                        uploadTask!!.addOnCompleteListener {
//                            Toast.makeText(con, "1.) Photos T, Narrations T", Toast.LENGTH_SHORT)
//                                .show()
//                            writeDB(
//                                pictorials, pic, con, fileName,
//                                pics, nars,
//                                descrs, pos, bitmapsDatabaseRef,
//                                relevance
//                            )
//
//                        }
//
//
//                    }
//
//
//                }

        }


        private fun writeDB(
            con: Context,
            fileName: String,
            picturesArrayList: ArrayList<String>,
            narrationsArrayList: ArrayList<String>,
            descriptionsArrayList: ArrayList<String>,
            bitmapsDatabaseRef: DocumentReference,
            relevance: String,
            uploadProgress: ProgressBar
        ) {
            if (!(uploadTask!!.isInProgress)) {

                val user = FirebaseAuth.getInstance().currentUser
                val map = HashMap<String, Any>()


                map["title"] = fileName
                map["uid"] = user!!.uid
                map["displayName"] = user.displayName!!
                map["thumbnail"] = picturesArrayList[0]

                map["pictures"] = picturesArrayList
                map["descriptions"] = descriptionsArrayList
                map["narrations"] = narrationsArrayList

                map["relevance"] = relevance

                map["viewCount"] = 0
                map["commentsCount"] = 0
                map["saveCount"] = 0
                map["ratersCount"] = 0
                map["ratings"] = 0

                map["slideCount"] = picturesArrayList.size.toString()
                map["status"] = "active"

                bitmapsDatabaseRef
                    .set(map).addOnSuccessListener {
                        Toast.makeText(con, "$fileName\npublished!", Toast.LENGTH_LONG).show()
                        uploadProgress.visibility = View.GONE
                        picturesArrayList.clear()
                        narrationsArrayList.clear()
                        descriptionsArrayList.clear()

                    }.addOnFailureListener {
                        Toast.makeText(con, it.message, Toast.LENGTH_LONG).show()

                        picturesArrayList.clear()
                        narrationsArrayList.clear()
                        descriptionsArrayList.clear()
                    }

            }
        }

    }
}