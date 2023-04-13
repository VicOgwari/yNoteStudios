package com.midland.ynote.Utilities

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.midland.ynote.R
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.midland.ynote.Activities.PhotoDoc
import com.midland.ynote.Activities.SchoolDepartmentDocuments
import com.midland.ynote.Objects.Schools
import java.lang.Boolean
import kotlin.Any
import kotlin.Int
import kotlin.String

class UserPowers {

    companion object Companion {

        fun isFollowing(
            userID: String,
            button: Button?,
            firebaseUser: FirebaseUser,
            context: Context,
            thisSchool: SchoolDepartmentDocuments?
        ) {
            if (userID == firebaseUser.uid) {
                Toast.makeText(context, "Can't follow self!", Toast.LENGTH_SHORT).show()
            } else {
                val thisUser = FirebaseFirestore.getInstance()
                    .collection("Users").document(firebaseUser.uid)

                val thisCoach = FirebaseFirestore.getInstance()
                    .collection("Users").document(userID)



                thisCoach.collection("squad")
                    .whereArrayContains("userIDs", firebaseUser.uid).get().addOnSuccessListener {
                    if (!it.isEmpty) {
                        if (button != null) {
                            button.text = "Unfollow"
                            button.visibility = View.VISIBLE
                        } else {
                            Toast.makeText(context, "Already following.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        if (button != null) {
                            button.text = "Follow"
                            button.visibility = View.VISIBLE
                        } else {
                            val map1: MutableMap<String, Any> = HashMap()
                            map1["coachesCount"] = FieldValue.increment(1)
                            map1["userIDs"] = FieldValue.arrayUnion(userID)

                            val map2: MutableMap<String, Any> = HashMap()
                            map2["studentsCount"] = FieldValue.increment(1)
                            map2["userIDs"] = FieldValue.arrayUnion(firebaseUser.uid)

                            val coachesList = thisUser.collection("squad")
                            val studentsList = thisCoach.collection("squad")
                            coachesList
                                .whereLessThan("coachesCount", 50000).limit(1)
                                .get()
                                .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                                    if (!queryDocumentSnapshots.isEmpty) {
                                        for (qds in queryDocumentSnapshots) {
                                            coachesList.document("coaches")
                                                .update(map1)
                                                .addOnSuccessListener {
                                                    thisUser.update(
                                                        "coaches",
                                                        FieldValue.increment(1)
                                                    )
//                                                        thisSchool?.showSnackBar("Done!")
                                                }
                                                .addOnFailureListener { }
                                        }
                                    } else {
                                        coachesList
                                            .document("coaches").set(map1)
                                            .addOnSuccessListener {
                                                thisUser.update(
                                                    "coaches",
                                                    FieldValue.increment(1)
                                                )
                                                thisSchool?.showSnackBar("Done!")
                                                button?.text = "Unfollow"
                                            }
                                            .addOnFailureListener {
                                                thisSchool?.showSnackBar("Something went wrong..")
                                            }
                                    }
                                }
                                .addOnFailureListener {
                                    thisSchool?.showSnackBar("Something went wrong1..")
                                }

                            studentsList
                                .whereLessThan("studentsCount", 50000).limit(1)
                                .get()
                                .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                                    if (!queryDocumentSnapshots.isEmpty) {
                                        for (qds in queryDocumentSnapshots) {
                                            studentsList.document("students")
                                                .update(map2)
                                                .addOnSuccessListener {
                                                    thisCoach.update(
                                                        "students",
                                                        FieldValue.increment(1)
                                                    )
                                                    thisSchool?.showSnackBar("Done!")
                                                    button?.text = "Unfollow"
                                                }
                                                .addOnFailureListener {
                                                    thisSchool?.showSnackBar("Something went wrong2..")
                                                }
                                        }
                                    } else {
                                        studentsList
                                            .document("students").set(map2)
                                            .addOnSuccessListener {
                                                thisCoach.update(
                                                    "students",
                                                    FieldValue.increment(1)
                                                )
                                                thisSchool?.showSnackBar("Done!")
                                                button?.text = "Unfollow"
                                            }
                                            .addOnFailureListener {
                                                thisSchool?.showSnackBar("Something went wrong3..")
                                            }
                                    }
                                }
                                .addOnFailureListener {
                                    thisSchool?.showSnackBar("Something went wrong4..")
                                }

//                            thisUser.get().addOnSuccessListener { it1 ->
//                                if (it1.exists()) {
//                                    userMap["userID"] = it1.getString("userID")!!
//                                }
//                            }
//                            thisCoach.get().addOnSuccessListener { it2 ->
//                                if (it2.exists()) {
//                                    coachMap["userID"] = it2.getString("userID")!!
//                                }
//                            }


//                            coach.set(coachMap).addOnSuccessListener {
//                                thisCoach.update("students", FieldValue.increment(1))
//                                        .addOnSuccessListener {
//                                            student.set(userMap).addOnSuccessListener {
//                                                thisUser.update("coaches", FieldValue.increment(1))
//                                                        .addOnSuccessListener {
//                                                            Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show()
//                                                        }
//                                            }
//
//                                        }
//                            }

                        }
                    }
                }
            }
        }

        fun enrol(
            flag: String,
            school: Schools?,
            firebaseUser: FirebaseUser,
            subFields: List<String>,
            context: Context
        ) {

            if (flag == "Documents") {
                val reference = FirebaseFirestore.getInstance()
                    .collection("Enrolments")
                    .document("User ID")
                    .collection(firebaseUser.uid)
                    .document(school!!.schoolName)


                reference.get().addOnSuccessListener {
                    if (it.exists()) {
                        Toast.makeText(context, "Already enrolled.", Toast.LENGTH_SHORT).show()
                        val enrolled = AlertDialog.Builder(context)
                            .setTitle("Already enrolled!")
                            .setMessage("Do you wish to discard your enrollment?")
                            .setPositiveButton("Yup!") { dialog: DialogInterface?, which: Int ->
                                dialog!!.dismiss()
                                Toast.makeText(context, "Done!.", Toast.LENGTH_SHORT).show()

                            }
                            .setNegativeButton("Nope!") { dialog: DialogInterface, which: Int ->
                                dialog.dismiss()
                            }
                            .create()

                        enrolled.show()
                    } else {
                        val map = HashMap<String, Any>()
                        map["school"] = school.schoolName
                        map["subFields"] = subFields
                        map["uId"] = firebaseUser.uid

                        reference.set(map).addOnSuccessListener {
                            val enrolCount = FirebaseFirestore.getInstance().collection("Count")
                                .document("Enrollment count")

                            enrolCount.update(school.schoolName, FieldValue.increment(1))
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Changes applied.", Toast.LENGTH_SHORT)
                                        .show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Update failed \n${it.message}!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            val schools = PhotoDoc.loadSchools(context, "schools")
//                            school.enrolled = Boolean.TRUE
                            for (s in schools) {
                                if (s.schoolName.equals(school.schoolName)) {
                                    s.enrolled = Boolean.TRUE
                                }
                            }

                            PhotoDoc.saveSchools(context, "schools", schools)

                        }
                        //Save the school object
                    }
                }


            }

//            else
//                if (flag == "Lectures"){
//                    val reference: Query = FirebaseDatabase.getInstance().reference
//                            .child("Users").child(firebaseUser.uid).child("Lectures enrollments").orderByChild("id").equalTo(school)
//
//                    reference.addListenerForSingleValueEvent(object : ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            if (snapshot.childrenCount > 0) {
//
//                                Toast.makeText(context, "Already enrolled.", Toast.LENGTH_SHORT).show()
//
//                            } else {
//
//                                FirebaseDatabase.getInstance().reference.child("Lectures")
//                                        .child(school).child("Enrolled users")
//                                        .child(firebaseUser.uid).setValue(true)
//                                FirebaseDatabase.getInstance().reference.child("Users")
//                                        .child(firebaseUser.uid)
//                                        .child("Lectures enrollments")
//                                        .child(school).setValue(true)
//                                Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show()
//
//                            }
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {}
//                    })
//                }
        }

        fun setUpSchools(): ArrayList<Schools>? {
            val schools = ArrayList<Schools>()
            val school1 = Schools(
                "Agriculture & Enterprise Development",
                R.drawable.agriculture_nature_vegetable,
                null,
                null
            )
            schools.add(school1)
            val school2 = Schools(
                "Applied Human Sciences",
                R.drawable.health,
                null,
                null
            )
            schools.add(school2)
            val school3 = Schools(
                "Business",
                R.drawable.business_economics_finance,
                null,
                null
            )
            schools.add(school3)
            val school4 = Schools(
                "Economics",
                R.drawable.business_economics_currency,
                null,
                null
            )
            schools.add(school4)
            val school5 = Schools(
                "Education",
                R.drawable.education_class,
                null,
                null
            )
            schools.add(school5)
            val school6 = Schools(
                "Engineering & Technology",
                R.drawable.engineering_electrical_energy_innovation,
                null,
                null
            )
            schools.add(school6)
            val school7 = Schools(
                "Environmental Studies",
                R.drawable.energy_plant,
                null,
                null
            )
            schools.add(school7)
            val school8 = Schools(
                "Hospitality & Tourism",
                R.drawable.hospitality_claw,
                null,
                null
            )
            schools.add(school8)
            val school9 = Schools(
                "Humanities & Social Sciences",
                R.drawable.humanities,
                null,
                null
            )
            schools.add(school9)
            val school10 = Schools(
                "Law",
                R.drawable.law_book,
                null,
                null
            )
            schools.add(school10)
            val school11 = Schools(
                "Medicine",
                R.drawable.medicine_health_hygieia1,
                null,
                null
            )
            schools.add(school11)
            val school12 =
                Schools(
                    "Public Health",
                    R.drawable.coronavirus,
                    null,
                    null
                )
            schools.add(school12)
            val school13 =
                Schools(
                    "Pure & Applied Sciences",
                    R.drawable.chemistry_network,
                    null,
                    null
                )
            schools.add(school13)
            val school14 =
                Schools(
                    "Visual & Performing Art",
                    R.drawable.theatre_shakespeare,
                    null,
                    null
                )
            schools.add(school14)
            val school15 =
                Schools(
                    "Confucius Institute",
                    R.drawable.philosophy_socrates,
                    null,
                    null
                )
            schools.add(school15)
            val school16 =
                Schools(
                    "Peace & Security Studies",
                    R.drawable.education_class,
                    null,
                    null
                )
            schools.add(school16)
            val school17 = Schools(
                "Creative Arts, Film & Media Studies",
                R.drawable.theatre_movie_camera,
                null,
                null
            )
            schools.add(school17)
            val school18 = Schools(
                "Architecture",
                R.drawable.architecture_sketch,
                null,
                null
            )
            schools.add(school18)
            return schools
        }

    }


}
