package com.fypcuiatk.feeclearanceapp

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.fypcuiatk.feeclearanceapp.Models.DatabaseModels.Receipt
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_receipt.*
import java.io.ByteArrayOutputStream
import java.util.*

private const val TAG = "AddReceiptActivity"

class AddReceiptActivity : AppCompatActivity() {

    // widgets
    private lateinit var transactionNumET: TextInputEditText
    private lateinit var amountET: TextInputEditText
    private lateinit var uploadButton: MaterialButton
    private lateinit var receiptImageView: ImageView
    private lateinit var progressBar: ProgressBar

    // firebase handles
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var user: FirebaseUser? = null

    //
    private lateinit var receiptBitmap: Bitmap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_receipt)

        // widgets init
        receiptImageView = findViewById(R.id.receiptImageView)
        amountET = findViewById(R.id.amountET)
        transactionNumET = findViewById(R.id.transactionNumET)
        uploadButton = findViewById(R.id.uploadBtn)
        progressBar = findViewById(R.id.progressBar)

        // get data from intent
        receiptBitmap = intent.extras?.get("image") as Bitmap
        receiptImageView.setImageBitmap(receiptBitmap)

        // firebase init

        user = FirebaseAuth.getInstance().currentUser
        storageReference = FirebaseStorage.getInstance().reference.child(user?.uid ?: "unknown")
        databaseReference = FirebaseDatabase.getInstance().reference.child(user?.uid ?: "unknown")

        uploadButton.setOnClickListener {

            uploadButton.isEnabled = false
            progressBar.visibility = View.VISIBLE

            val timeStamp = Date().time.toString()
            val outputStream = ByteArrayOutputStream()

            receiptBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

            val imageAsByteArray = outputStream.toByteArray()
            val totalBytes = imageAsByteArray.size

            // upload image to firebase storage
            val imageRef = storageReference.child("${timeStamp}.jpg")
            val uploadTask = imageRef.putBytes(imageAsByteArray)

            uploadTask
                .addOnSuccessListener {
                    Toast.makeText(this, "Image uploaded successfully.", Toast.LENGTH_SHORT).show()

                    // add image url to firebase realtime database
                    imageRef.downloadUrl.addOnSuccessListener { uri ->

                        val transactionNum = transactionNumET.text.toString()
                        val amount = amountET.text.toString()

                        addDataToDatabase(uri.toString(), amount, transactionNum)
                    }

                }
                .addOnFailureListener {
                    Toast.makeText(this, "Task failed", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "onActivityResult: ${it.message}")
                    progressBar.visibility = View.GONE
                }
                .addOnProgressListener {
                    val percentageTransferred = (it.bytesTransferred / totalBytes) * 100
                    Log.d(TAG, "onActivityResult: $percentageTransferred% completed.")
                }


            // TODO :: Display upload success message

        }


    }

    private fun addDataToDatabase(url: String, amountPaid: String, transactionNum: String) {
        val receipt = Receipt(url, transactionNum, amountPaid, ServerValue.TIMESTAMP)
        val entryReference = databaseReference.child("receipts").push()
        entryReference.setValue(receipt)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Added to the database.", Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(this, "There has been an error.", Toast.LENGTH_SHORT).show()

                progressBar.visibility = View.GONE
                uploadButton.isEnabled = true
            }
    }
}