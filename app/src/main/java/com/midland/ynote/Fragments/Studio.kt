package com.midland.ynote.Fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.midland.ynote.Activities.LectureStudio2
import com.midland.ynote.Activities.PhotoDoc
import com.midland.ynote.Activities.SourceBitmapList
import com.midland.ynote.Adapters.GridRecyclerAdapter
import com.midland.ynote.Objects.BitMapTitle
import com.midland.ynote.Objects.PendingLecObj
import com.midland.ynote.R
import com.midland.ynote.Utilities.FilingSystem
import com.midland.ynote.Utilities.FilingSystem.Companion.flag

/**
 * A simple [Fragment] subclass.
 * Use the [Studio.newInstance] factory method to
 * create an instance of this fragment.
 */
class Studio : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private val REQUEST_PERMISSION = 1001
    private var studScroll: CoordinatorLayout? = null
    private val STORAGE_ACCESS_CODE = 99
    var lectureName = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_studio, container, false)
        val picCamBtn = v.findViewById<Button>(R.id.picCamButton)
        val docCamBtn = v.findViewById<Button>(R.id.docCamButton)
        studScroll = v.findViewById(R.id.studScroll)
        val shelfCard = v.findViewById<Button>(R.id.libraryButton)
        val docBtn = v.findViewById<Button>(R.id.docBtn)
        val camBtn = v.findViewById<Button>(R.id.camBtn)
        val picBtn = v.findViewById<Button>(R.id.picBtn)
        val pendingLY = v.findViewById<LinearLayout>(R.id.pendingLY)
        val pendingLecGrid = v.findViewById<RecyclerView>(R.id.pendingRV)

        permissions()
        pendingLecGrid.layoutManager = GridLayoutManager(context, 2)
        if (pendingLecturesObj() == null) {
            pendingLY.visibility = View.GONE
        } else {
            pendingLY.visibility = View.VISIBLE
            val grid = GridRecyclerAdapter(
                context, null,
                pendingLecturesObj(), null, "ProfileItemsList", "VicFocus"
            )
            grid.notifyDataSetChanged()
            pendingLecGrid.adapter = grid
            pendingLecGrid.visibility = View.VISIBLE
        }
        docBtn.setOnClickListener { v1: View? ->
            lectureNameDialog1("docOnly")
//            val i = Intent(context, SourceDocList::class.java)
//            i.putExtra("flag", "docOnly")
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//               lectureNameDialog1("docOnly")
//            } else {
//                startActivity(i)
//            }
        }
        picBtn.setOnClickListener { v1: View? ->
            val sharedPreferences = context?.getSharedPreferences("bitmapTitle", Context.MODE_PRIVATE)
            val bitMaps: ArrayList<BitMapTitle> = if (sharedPreferences!!.contains("bitmapTitle")) {
                val gson = Gson()
                val json = sharedPreferences.getString("bitmapTitle", "")
                val type = object : TypeToken<ArrayList<BitMapTitle>>() {}.type
                gson.fromJson(json, type)!!
            }else{
                ArrayList()
            }
            if (bitMaps.size <= 0){
                Snackbar.make(studScroll!!, "No pictorials yet.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("CREATE?") {
                        if (activity?.let { it1 ->
                                ContextCompat.checkSelfPermission(
                                    it1,
                                    Manifest.permission.CAMERA
                                )
                            } != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(
                                activity!!,
                                Manifest.permission.RECORD_AUDIO
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    activity!!,
                                    Manifest.permission.CAMERA
                                )
                                && ActivityCompat.shouldShowRequestPermissionRationale(
                                    activity!!,
                                    Manifest.permission.RECORD_AUDIO
                                )
                            ) {
                                Snackbar.make(
                                    studScroll!!,
                                    "Permission",
                                    Snackbar.LENGTH_INDEFINITE
                                )
                                    .setAction(
                                        "ENABLE"
                                    ) { v1: View? ->
                                        ActivityCompat.requestPermissions(
                                            activity!!, arrayOf(
                                                Manifest.permission.CAMERA,
                                                Manifest.permission.RECORD_AUDIO
                                            ), 7
                                        )
                                    }.show()
                            } else {
                                ActivityCompat.requestPermissions(
                                    activity!!, arrayOf(
                                        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
                                    ), 7
                                )
                            }
                        } else {
                            val intent = Intent(activity, PhotoDoc::class.java)
                            intent.putExtra("newDoc", "newDoc")
                            startActivity(intent)
                        }
                    }.show()
            }else{
                lectureNameDialog("picOnly", context)
            }
        }
        camBtn.setOnClickListener { v1: View? -> lectureNameDialog("camOnly", context) }
        docCamBtn.setOnClickListener { v1: View? ->
            lectureNameDialog1("docCamCard")
//            val i = Intent(context, SourceDocList::class.java)
//            i.putExtra("flag", "docCamCard")
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                lectureNameDialog1("docCamCard")
//            } else {
//                startActivity(i)
//            }
        }
        picCamBtn.setOnClickListener { v1: View? ->
            val sharedPreferences = context?.getSharedPreferences("bitmapTitle", Context.MODE_PRIVATE)
            val bitMaps: ArrayList<BitMapTitle> = if (sharedPreferences!!.contains("bitmapTitle")) {
                val gson = Gson()
                val json = sharedPreferences.getString("bitmapTitle", "")
                val type = object : TypeToken<ArrayList<BitMapTitle>>() {}.type
                gson.fromJson(json, type)!!
            }else{
                ArrayList()
            }
            if (bitMaps.size <= 0){
                Snackbar.make(studScroll!!, "No pictorials yet.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("CREATE?") {
                        if (activity?.let { it1 ->
                                ContextCompat.checkSelfPermission(
                                    it1,
                                    Manifest.permission.CAMERA
                                )
                            } != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(
                                activity!!,
                                Manifest.permission.RECORD_AUDIO
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    activity!!,
                                    Manifest.permission.CAMERA
                                )
                                && ActivityCompat.shouldShowRequestPermissionRationale(
                                    activity!!,
                                    Manifest.permission.RECORD_AUDIO
                                )
                            ) {
                                Snackbar.make(
                                    studScroll!!,
                                    "Permission",
                                    Snackbar.LENGTH_INDEFINITE
                                )
                                    .setAction(
                                        "ENABLE"
                                    ) { v1: View? ->
                                        ActivityCompat.requestPermissions(
                                            activity!!, arrayOf(
                                                Manifest.permission.CAMERA,
                                                Manifest.permission.RECORD_AUDIO
                                            ), 7
                                        )
                                    }.show()
                            } else {
                                ActivityCompat.requestPermissions(
                                    activity!!, arrayOf(
                                        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
                                    ), 7
                                )
                            }
                        } else {
                            val intent = Intent(activity, PhotoDoc::class.java)
                            intent.putExtra("newDoc", "newDoc")
                            startActivity(intent)
                        }
                    }.show()
            }else{
                lectureNameDialog("picCam", context)
            }
        }
        shelfCard.setOnClickListener { v1: View? ->
            val sharedPreferences = context?.getSharedPreferences("bitmapTitle", Context.MODE_PRIVATE)
            val bitMaps: ArrayList<BitMapTitle> = if (sharedPreferences!!.contains("bitmapTitle")) {
                val gson = Gson()
                val json = sharedPreferences.getString("bitmapTitle", "")
                val type = object : TypeToken<ArrayList<BitMapTitle>>() {}.type
                gson.fromJson(json, type)!!
            }else{
                ArrayList()
            }
            if (bitMaps.size <= 0){
                Snackbar.make(studScroll!!, "No pictorials yet.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("CREATE?") {
                        if (activity?.let { it1 ->
                                ContextCompat.checkSelfPermission(
                                    it1,
                                    Manifest.permission.CAMERA
                                )
                            } != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(
                                activity!!,
                                Manifest.permission.RECORD_AUDIO
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    activity!!,
                                    Manifest.permission.CAMERA
                                )
                                && ActivityCompat.shouldShowRequestPermissionRationale(
                                    activity!!,
                                    Manifest.permission.RECORD_AUDIO
                                )
                            ) {
                                Snackbar.make(
                                    studScroll!!,
                                    "Permission",
                                    Snackbar.LENGTH_INDEFINITE
                                )
                                    .setAction(
                                        "ENABLE"
                                    ) { v1: View? ->
                                        ActivityCompat.requestPermissions(
                                            activity!!, arrayOf(
                                                Manifest.permission.CAMERA,
                                                Manifest.permission.RECORD_AUDIO
                                            ), 7
                                        )
                                    }.show()
                            } else {
                                ActivityCompat.requestPermissions(
                                    activity!!, arrayOf(
                                        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
                                    ), 7
                                )
                            }
                        } else {
                            val intent = Intent(activity, PhotoDoc::class.java)
                            intent.putExtra("newDoc", "newDoc")
                            startActivity(intent)
                        }
                    }.show()
            }else{
                lectureNameDialog1("shelfCard")
//                val i = Intent(context, SourceDocList::class.java)
//                i.putExtra("flag", "shelfCard")
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                    lectureNameDialog1("shelfCard")
//                } else {
//                    startActivity(i)
//                }
            }
        }
        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == STORAGE_ACCESS_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
                val docUri = data.data.toString()
                    when (flag) {
                        "docOnly" -> {
                            Toast.makeText(
                                context,
                                "New lecture created!",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(
                                context!!.applicationContext,
                                LectureStudio2::class.java
                            )
                            intent.putExtra("selectedDoc", docUri)
                            intent.putExtra("fileName", lectureName)
                            intent.putExtra("studio", "0")
                            context!!.startActivity(intent)
                        }
                        "docCamCard" -> {
                            Toast.makeText(
                                context,
                                "New lecture created!",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent1 = Intent(
                                context!!.applicationContext,
                                LectureStudio2::class.java
                            )
                            intent1.putExtra("selectedDoc", docUri)
                            intent1.putExtra("fileName", lectureName)
                            intent1.putExtra("studio", "3")
                            context!!.startActivity(intent1)
                        }
                        "shelfCard" -> {
                            Toast.makeText(
                                context,
                                "New lecture created!",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent2 = Intent(
                                context!!.applicationContext,
                                SourceBitmapList::class.java
                            )
                            intent2.putExtra("selectedDoc", docUri)
                            intent2.putExtra("fileName", lectureName)
                            intent2.putExtra("studio", "5")
                            context!!.startActivity(intent2)
                        }
                    }

//                lectureNameDialog1(data.data.toString(), flag)
            }
        }
    }

    private fun pendingLecturesObj(): ArrayList<PendingLecObj>? {
        val preferences = context!!.getSharedPreferences("pendingLectures", Context.MODE_PRIVATE)
        val json = preferences.getString("pendingLectures", "")
        val gson = Gson()
        val type = object : TypeToken<ArrayList<PendingLecObj?>?>() {}.type

//        ArrayList<PendingLecObj> pendingLectures = new ArrayList<>();
//        if (checkAndRequestPermission()) {
//            File[] fileList = FilingSystem.Companion.getPendingLectures().listFiles();
//            assert fileList != null;
//            for (File file : fileList) {
//                String[] lecDetails = readFile(file);
//                try {
//                    PendingLecObj pendingLecObj = new PendingLecObj(lecDetails[0], lecDetails[1], lecDetails[2],
//                            lecDetails[3], file.lastModified());
//                    pendingLectures.add(pendingLecObj);
//                } catch (IndexOutOfBoundsException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(),
//                            file.getName() + " appears to be having issues, Delete", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
        return gson.fromJson(json, type)
    }

    private fun lectureNameDialog1(flag: String) {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(50, 0, 50, 100)
        val input = EditText(context)
        input.layoutParams = lp
        input.gravity = Gravity.TOP or Gravity.START
        linearLayout.addView(input, lp)
        val fileNameDialog = AlertDialog.Builder(context!!)
        fileNameDialog.setTitle("Lecture title")
        fileNameDialog.setMessage("e.g 'Critical & creative reasoning...'")
        fileNameDialog.setView(linearLayout)
        fileNameDialog.setNegativeButton(
            "cancel"
        ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        fileNameDialog.setPositiveButton("submit") { dialog: DialogInterface, _: Int ->
            if (input.text.toString().trim { it <= ' ' } == "") {
                lectureNameDialog1(flag)
            } else {
                lectureName = input.text.toString().trim()
                FilingSystem.flag = flag
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "application/*"
                startActivityForResult(intent, STORAGE_ACCESS_CODE)
            }
        }
        fileNameDialog.setOnCancelListener { dialog: DialogInterface -> dialog.dismiss() }
        fileNameDialog.show()
    }

    private fun lectureNameDialog(flag: String, c: Context?) {
        val linearLayout = LinearLayout(c)
        linearLayout.orientation = LinearLayout.VERTICAL
        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(50, 0, 50, 100)
        val input = EditText(c)
        input.layoutParams = lp
        input.gravity = Gravity.TOP or Gravity.START
        linearLayout.addView(input, lp)
        val fileNameDialog = AlertDialog.Builder(
            c!!
        )
        fileNameDialog.setTitle("Lecture title")
        fileNameDialog.setMessage("e.g 'Critical & creative reasoning...'")
        fileNameDialog.setView(linearLayout)
        fileNameDialog.setNegativeButton("cancel") { dialog: DialogInterface, which: Int -> dialog.dismiss() }
        fileNameDialog.setPositiveButton("submit") { dialog: DialogInterface?, which: Int ->
            val lectureName: String
            if (input.text.toString().trim { it <= ' ' } == "") {
                lectureNameDialog(flag, c)
            } else {
                when (flag) {
                    "camOnly" -> {
                        lectureName = input.text.toString().trim { it <= ' ' }
                        val i = Intent(context, LectureStudio2::class.java)
                        i.putExtra("camOnly", "camOnly")
                        i.putExtra("studio", "2")
                        i.putExtra("fileName", lectureName)
                        startActivity(i)
                    }
                    "picOnly" -> {
                        lectureName = input.text.toString().trim { it <= ' ' }
                        val intent2 = Intent(c, SourceBitmapList::class.java)
                        intent2.putExtra("selectedDoc", "_Nothing was selected so studio 1_")
                        intent2.putExtra("fileName", lectureName)
                        c.startActivity(intent2)
                    }
                    "picCam" -> {
                        lectureName = input.text.toString().trim { it <= ' ' }
                        val intent1 = Intent(c, SourceBitmapList::class.java)
                        intent1.putExtra("selectedDoc", "_Nothing was selected so studio 4_")
                        intent1.putExtra("fileName", lectureName)
                        c.startActivity(intent1)
                    }
                }
            }
        }
        fileNameDialog.setOnCancelListener { dialog: DialogInterface -> dialog.dismiss() }
        fileNameDialog.show()
    }

    fun permissions() {
        if (((ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
                    + ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.RECORD_AUDIO
            )
                    + ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
                    + ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.CAMERA
            ))
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                        || ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.RECORD_AUDIO
                )
                        || ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                        || ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.CAMERA
                ))
            ) {

                Snackbar.make(studScroll!!, "Permission", Snackbar.LENGTH_INDEFINITE)
                    .setAction("ENABLE") {
                        ActivityCompat.requestPermissions(
                            activity!!, arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                            ), REQUEST_PERMISSION
                        )
                    }.show()
            } else {
                ActivityCompat.requestPermissions(
                    activity!!, arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    ), REQUEST_PERMISSION
                )
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION -> {
                run {
                    if (grantResults.isNotEmpty() && (grantResults[0] + grantResults[1] + grantResults[2] + grantResults[3] == PackageManager.PERMISSION_GRANTED)) {
                        Snackbar.make(studScroll!!, "Permissions granted. Select your studio.", Snackbar.LENGTH_LONG)
                    } else {
                        Snackbar.make(
                            studScroll!!,
                            "Permission",
                            Snackbar.LENGTH_INDEFINITE
                        )
                            .setAction("ENABLE") {
                                ActivityCompat.requestPermissions(
                                    activity!!, arrayOf(
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.RECORD_AUDIO,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.CAMERA
                                    ), REQUEST_PERMISSION
                                )
                            }.show()
                    }
                }
                run {
                    Settings.System.putInt(
                        context!!.contentResolver,
                        "show_touches", 1
                    )
                }
            }
        }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Studio.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): Studio {
            val fragment = Studio()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}