package com.example.a2in1app

import android.app.Activity
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class CustomAlert(activity: Activity, title: String) {
    init {
    val dialogBuilder = AlertDialog.Builder(activity)
    dialogBuilder.setMessage(title)

    .setPositiveButton("Yes", DialogInterface.OnClickListener
    {
        dialog, id -> activity.recreate()
    })
    // negative button text and action
    .setNegativeButton("No", DialogInterface.OnClickListener
    {
        dialog, id -> dialog.cancel()
    })

    val alert = dialogBuilder.create()
    alert.setTitle("Game over")
    alert.show()
}
}