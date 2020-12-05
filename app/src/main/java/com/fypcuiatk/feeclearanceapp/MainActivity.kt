package com.fypcuiatk.feeclearanceapp

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fypcuiatk.feeclearanceapp.Adapters.ReceiptListAdapter
import com.fypcuiatk.feeclearanceapp.Models.DatabaseModels.Receipt
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

private const val TAG = "MainActivity"
private const val IMAGE_CAPTURE_REQ_CODE: Int = 123

class MainActivity : AppCompatActivity() {

    // widgets
    private lateinit var addBtn: FloatingActionButton
    private lateinit var receiptList: RecyclerView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    // Firebase
    private var mUser: FirebaseUser? = null
    private lateinit var mStorageRef: StorageReference
    private lateinit var mDatabaseRef: DatabaseReference
    private var mReceipts: List<Receipt>? = null

    // methods
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Firebase handles init
        firebaseAuth = FirebaseAuth.getInstance()
        mUser = firebaseAuth.currentUser

        val userId = mUser?.uid ?: "unknown"
        mStorageRef = FirebaseStorage.getInstance().reference.child(userId)
        mDatabaseRef = FirebaseDatabase.getInstance().reference.child(userId)

        loadReceipts()

        // widgets init
        addBtn = findViewById(R.id.addBtn)
        receiptList = findViewById(R.id.receiptList)
        progressBar = findViewById(R.id.progressBar)

        addBtn.setOnClickListener(View.OnClickListener {

            val imageCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(imageCaptureIntent, IMAGE_CAPTURE_REQ_CODE)

        })

        val adapter = ReceiptListAdapter(this, mReceipts)
        receiptList.adapter = adapter
        receiptList.layoutManager = LinearLayoutManager(this)

    }

    override fun onStart() {
        super.onStart()
        if (mUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_out_menu_item -> {
                firebaseAuth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

            R.id.settings_menu_item -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_CAPTURE_REQ_CODE && resultCode == RESULT_OK) {

            // send image to AddReceiptActivity
            val imageBitmap = data?.extras?.get("data") as Bitmap

            val intent = Intent(this, AddReceiptActivity::class.java)
            intent.putExtra("image", imageBitmap)
            startActivity(intent)

        }

    }

    private fun loadReceipts() {

        mDatabaseRef.child("receipts").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressBar.visibility = View.GONE
                val receipts = mutableListOf<Receipt>()
                for (receiptSnapshot in snapshot.children) {

                    val receipt = receiptSnapshot.getValue(Receipt::class.java)
                    receipts.add(receipt ?: Receipt())

                    Log.d(TAG, "onDataChange: $receipt")
                }
                receiptList.adapter = ReceiptListAdapter(this@MainActivity, receipts)

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}