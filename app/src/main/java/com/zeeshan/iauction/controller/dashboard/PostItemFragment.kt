package com.zeeshan.iauction.controller.dashboard


import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.zeeshan.iauction.R
import kotlinx.android.synthetic.main.fragment_post_item.*
import java.text.SimpleDateFormat
import java.util.*

class PostItemFragment : Fragment() {

    private lateinit var categorySpinner: Spinner
    private lateinit var conditionSpinner: Spinner
    private lateinit var itemImageView: ImageView
    private lateinit var selectItemImageBtn : Button
    var dateFormat = SimpleDateFormat("MMM d, yyyy h:mm a", Locale.US)
    var selectedPhotoUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post_item, container, false)

        val datePickerButton = view.findViewById<Button>(R.id.selectEndBidDateBtn)
//        val floatingAddItemBtn = view.findViewById<Button>(R.id.floating_add_item_btn)
//        floatingAddItemBtn.visibility = View.INVISIBLE

        itemImageView = view.findViewById(R.id.itemImageView)
        selectItemImageBtn = view.findViewById<Button>(R.id.itemSelectImageBtn)
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
            val selectedDate = Calendar.getInstance()

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
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val inputStream = activity!!.contentResolver.openInputStream(selectedPhotoUri!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            itemImageView.setImageBitmap(bitmap)
//            itemImageView.alpha = 0f


//            progress.setMessage("Please wait, while we are updating profile picture.....")
//            progress.setCancelable(false)
//            progress.show()
//
//
//
//            uploadImageToFirebase()
            super.onActivityResult(requestCode, resultCode, data)

        }
    }

}

