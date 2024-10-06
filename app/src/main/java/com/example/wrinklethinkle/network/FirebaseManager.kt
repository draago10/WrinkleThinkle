package com.example.wrinklethinkle.network

import com.google.firebase.database.FirebaseDatabase

class FirebaseManager {
    fun saveUserData(
        userId: String,
        path: String,
        amount: Int,
        onComplete: (Boolean) -> Unit
    ) {
        // Reference to the specific path in Firebase
        val dataRef = FirebaseDatabase.getInstance().reference.child("users").child(userId).child(path)

        // Fetch the current value from the given path
        dataRef.get().addOnSuccessListener { snapshot ->
            val currentValue = snapshot.getValue(Int::class.java) ?: 0
            val newValue = currentValue + amount

            // Create a map for the update
            val updates = hashMapOf<String, Any>(
                path to newValue
            )

            // Update the path in Firebase
            FirebaseDatabase.getInstance().reference.child("users").child(userId).updateChildren(updates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onComplete(true)
                    } else {
                        onComplete(false)
                    }
                }
        }.addOnFailureListener {
            onComplete(false)  // Failure to fetch the current value
        }
    }

    //HOW TO CALL: FirebaseManager().saveUserData()
}