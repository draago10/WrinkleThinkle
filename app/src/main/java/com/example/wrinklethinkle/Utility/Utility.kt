package com.example.wrinklethinkle.Utility

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.wrinklethinkle.view.ErrorPopupDialog

class Utility {
    fun showErrorPopup(
        fragmentManager: FragmentManager,
        context: Context,
        image: Int,
        title: String = "Error",
        message: String = "Something went wrong. Please try again.",
        onCancelClicked: () -> Unit = {
            // Default cancel action (can be overridden)
            Toast.makeText(context, "Cancel clicked", Toast.LENGTH_SHORT).show()
        },
        onRetryClicked: () -> Unit = {
            // Default retry action (can be overridden)
            Toast.makeText(context, "Retry clicked", Toast.LENGTH_SHORT).show()
        }
    ) {
        val dialog = ErrorPopupDialog(
            image = image,
            title = title,
            message = message,
            onCancelClicked = {
                onCancelClicked()
            }
        )
        dialog.show(fragmentManager, "ErrorPopupDialog")
    }
}