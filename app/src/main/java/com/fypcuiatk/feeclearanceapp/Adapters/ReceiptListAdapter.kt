package com.fypcuiatk.feeclearanceapp.Adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fypcuiatk.feeclearanceapp.Models.DatabaseModels.Receipt
import com.fypcuiatk.feeclearanceapp.R
import com.fypcuiatk.feeclearanceapp.ReceiptDetailsActivity


import com.google.firebase.storage.StorageReference
import java.text.DateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class ReceiptListAdapter(private val context: Context, private val receipts: List<Receipt>?) :
    RecyclerView.Adapter<ReceiptListAdapter.MyViewHolder>() {


    class MyViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {

        var receiptImage: ImageView = view.findViewById(R.id.receiptImageView)
        val transactionNumberTV: TextView = view.findViewById(R.id.transactionNum)
        val amountTV: TextView = view.findViewById(R.id.amountTV)
        val dateAdded: TextView = view.findViewById(R.id.dateTv)
        val moreButton: ImageButton = view.findViewById(R.id.moreBtn)
        val viewDetailsButton: Button = view.findViewById(R.id.viewDetailsBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_single_receipt_list, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        receipts?.get(position)?.let { receipt ->

            holder.transactionNumberTV.text = receipt.transactionNum
            holder.amountTV.text = receipt.amount

            val timestamp: Long =
                receipt.timestamp?.toString()?.toLong() ?: System.currentTimeMillis()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp
            val date = android.text.format.DateFormat.format("dd/MM/yy ", calendar).toString()

            holder.dateAdded.text = date


            val imageUrl = receipt.url
            Glide.with(context)
                .load(imageUrl)
                .into(holder.receiptImage)

            holder.moreButton.setOnClickListener {
                showPopupMenu(context, holder.moreButton)
            }

            holder.viewDetailsButton.setOnClickListener {
                context.startActivity(Intent(context, ReceiptDetailsActivity::class.java))
            }
        }
    }


    private fun showPopupMenu(context: Context, v: View) {
        val popupMenu = PopupMenu(context, v)
        popupMenu.menuInflater.inflate(R.menu.reciept_popup_menu, popupMenu.menu)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            popupMenu.gravity = Gravity.END
        }
        popupMenu.show()
    }

    override fun getItemCount(): Int {
        return receipts?.size ?: 0
    }

}