package com.example.alabaster

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*

class AddTestimonyActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etName2: EditText
    private lateinit var etDate: EditText
    private lateinit var etTestimony: EditText
    private lateinit var ivPreview: ImageView
    private lateinit var btnSelectImage: Button
    private lateinit var btnAdd: Button

    private lateinit var dbRef: DatabaseReference
    private var imageUri: Uri? = null
    private var encodedImage: String? = null

    companion object {
        private const val PICK_IMAGE_REQUEST = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_testimony)

        etName = findViewById(R.id.etName)
        etName2 = findViewById(R.id.etName2)
        etDate = findViewById(R.id.etDate)
        etTestimony = findViewById(R.id.etTestimony)
        ivPreview = findViewById(R.id.ivPreview)
        btnSelectImage = findViewById(R.id.btnSelectImage)
        btnAdd = findViewById(R.id.btnAdd)

        dbRef = FirebaseDatabase.getInstance().getReference("testimonies")

        // Date picker
        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    etDate.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
                },
                year, month, day
            )
            datePicker.show()
        }

        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
        }

        btnAdd.setOnClickListener {
            saveTestimony()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data?.data != null) {
            imageUri = data.data
            ivPreview.setImageURI(imageUri)

            val inputStream: InputStream? = contentResolver.openInputStream(imageUri!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            encodedImage = encodeImageToBase64(bitmap)
        }
    }

    private fun encodeImageToBase64(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
    }

    private fun saveTestimony() {
        val name = etName.text.toString().trim()
        val title = etName2.text.toString().trim()
        val date = etDate.text.toString().trim()
        val testimonyText = etTestimony.text.toString().trim()

        if (name.isEmpty() || title.isEmpty() || date.isEmpty() || testimonyText.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val testimonyId = dbRef.push().key!!

        val testimony = mapOf(
            "id" to testimonyId,
            "name" to name,
            "title" to title,
            "date" to date,
            "testimony" to testimonyText,
            "imageBase64" to (encodedImage ?: ""),
            "status" to "pending" // 👈 mark as pending until admin approves
        )

        dbRef.child(testimonyId).setValue(testimony)
            .addOnSuccessListener {
                Toast.makeText(this, "Testimony submitted for approval", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to submit testimony", Toast.LENGTH_SHORT).show()
            }
    }
}
