package com.example.wrinklethinkle.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.wrinklethinkle.databinding.FragmentErrorDialogBinding

class ErrorPopupDialog(
    private val image: Int,
    private val title: String,
    private val message: String,
    private val onCancelClicked: () -> Unit
) : DialogFragment() {

    private lateinit var binding: FragmentErrorDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentErrorDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set title image, and message
        binding.popupImage.setImageResource(image)
        binding.popupTitle.text = title
        binding.popupMessage.text = message

        // Handle Cancel button click
        binding.errorScreenOkButton.setOnClickListener {
            onCancelClicked()
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        // Set the dialog window background to transparent
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }


    // Set Dialog Fragment to full-screen or other styles if needed
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}
