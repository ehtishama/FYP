package com.fypcuiatk.feeclearanceapp.Dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.fypcuiatk.feeclearanceapp.R

class ProgressDialog : DialogFragment() {

    private lateinit var progressInfoTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_progress, container, false)
        this.progressInfoTextView = view.findViewById(R.id.progressInfoText)
        this.progressInfoTextView.text = "Performing OCR on the image ... "
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    fun setProgressInfoText(infoText: String) {
        this.progressInfoTextView.text = infoText
    }

}