package com.zeeshan.iauction.controller.dashboard


import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.zeeshan.iauction.R
import com.zeeshan.iauction.model.Item
import com.zeeshan.iauction.model.User
import com.zeeshan.iauction.utilities.AppPref
import kotlinx.android.synthetic.main.fragment_post_item.*
import java.text.SimpleDateFormat
import java.util.*

class PostItemFragment : Fragment() {

    private lateinit var categorySpinner: Spinner
    private lateinit var conditionSpinner: Spinner
    private lateinit var itemImageView: ImageView
    private lateinit var selectItemImageBtn: Button
    private lateinit var datePickerButton: Button
    private lateinit var submitBtn: Button
    var dateFormat = SimpleDateFormat("MMM d, yyyy h:mm a", Locale.US)
    var selectedPhotoUri: Uri? = null
    private lateinit var dialogBuilder: AlertDialog
    private lateinit var appPrefUser: User      //User from App Preference
    private lateinit var dbReference: FirebaseFirestore
    private lateinit var selectedDate: Calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post_item, container, false)

        appPrefUser = AppPref(activity!!).getUser()!!
        dbReference = FirebaseFirestore.getInstance()

        datePickerButton = view.findViewById<Button>(R.id.selectEndBidDateBtn)
        itemImageView = view.findViewById(R.id.itemImageView)
        selectItemImageBtn = view.findViewById<Button>(R.id.itemSelectImageBtn)
        submitBtn = view.findViewById<Button>(R.id.itemSubmitBtn)
        categorySpinner = view.findViewById(R.id.category_spinner)
        conditionSpinner = view.findViewById(R.id.category_condition)

        categorySpinner.adapter = ArrayAdapter(
            activity!!,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.item_category)
        )
//                as SpinnerAdapter?


        conditionSpinner.adapter = ArrayAdapter(
            activity!!,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.item_condition)
        )
//                as SpinnerAdapter?

        datePickerButton.setOnClickListener {
            val now = Calendar.getInstance()
            selectedDate = Calendar.getInstance()

            try {
                if (itemBidEndDateText.text != "") {
                    val date = dateFormat.parse(itemBidEndDateText.text.toString())
                    now.time = date
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val datePicker = DatePickerDialog(
                activity!!,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val timePicker = TimePickerDialog(
                        activity!!,
                        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->

                            selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            selectedDate.set(Calendar.MINUTE, minute)

                            val date = dateFormat.format(selectedDate.time)
                            itemBidEndDateText.setText(date)
                        },
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                    )

                    timePicker.show()
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectItemImageBtn.setOnClickListener {
            Log.d("POSTITEMFRAGMENT", "Item Select Photo Button Pressed")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        submitBtn.setOnClickListener {

            progressShow()
            val dbRef = dbReference.collection("Item").document()

            postItemImageToFirebase(dbRef)

//            postItemToFirestore(
//                dbRef,
//                appPrefUser,
//                categorySpinner.selectedItem.toString(),
//                conditionSpinner.selectedItem.toString(),
//                itemManufacturerEditText.text!!.trim().toString(),
//                itemTitleEditText.text!!.trim().toString(),
//                itemDescriptionEditText.text!!.trim().toString(),
//                itemMinBidAmmEditText.text!!.trim().toString(),
//                selectedDate
//            )


        }

    }

    private fun postItemImageToFirebase(key: DocumentReference) {
        val ref = FirebaseStorage.getInstance().getReference("/item/${key.id}")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener { uri ->
                ref.downloadUrl.addOnSuccessListener {


                    postItemToFirestore(
                        key,
                        appPrefUser,
                        categorySpinner.selectedItem.toString(),
                        conditionSpinner.selectedItem.toString(),
                        itemManufacturerEditText.text!!.trim().toString(),
                        itemTitleEditText.text!!.trim().toString(),
                        itemDescriptionEditText.text!!.trim().toString(),
                        itemMinBidAmmEditText.text!!.trim().toString(),
                        selectedDate,
                        it.toString()
                    )

                }
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Profile Picture failed to update....", Toast.LENGTH_SHORT).show()
                dialogBuilder.dismiss()
            }
    }

    private fun postItemToFirestore(
        dbRef: DocumentReference,
        appPrefUser: User,
        category: String,
        condition: String,
        manufactureName: String,
        title: String,
        description: String,
        bidAmountMin: String,
        bidEndDate: Calendar,
        imageUrl: String
    ) {

        val itemPost = Item(
            dbRef.id,
            category,
            condition,
            manufactureName,
            title,
            description,
            bidAmountMin.toInt(),
            imageUrl,
            System.currentTimeMillis(),
            bidEndDate.timeInMillis.toLong(),
            "Available",
            appPrefUser.userId,
            arrayListOf()
        )

        dbRef.set(itemPost)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "${itemPost.toString()} successfully written!")
                clearInputFields()
                dialogBuilder.dismiss()
            }
            .addOnFailureListener {
                Log.w(ContentValues.TAG, "Error writing document", it)
                dialogBuilder.dismiss()
            }
    }

    private fun clearInputFields() {
        conditionSpinner.setSelection(0)
        categorySpinner.setSelection(0)
        itemManufacturerEditText.setText("")
        itemTitleEditText.setText("")
        itemDescriptionEditText.setText("")
        itemMinBidAmmEditText.setText("")
        itemBidEndDateText.setText("")
        itemImageView.setImageBitmap(null)
    }

    private fun progressShow() {
        val progressDialog = LayoutInflater.from(activity).inflate(R.layout.progress_dialog, null)
        dialogBuilder = AlertDialog.Builder(activity)
            .setCancelable(false)
            .setView(progressDialog)
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val inputStream = activity!!.contentResolver.openInputStream(selectedPhotoUri!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            itemImageView.setImageBitmap(bitmap)
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}

