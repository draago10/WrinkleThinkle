package com.example.wrinklethinkle.model

import com.example.wrinklethinkle.network.FirebaseManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object Shop
{
    // Buy seeds for a specific flower type
    fun buySeed(player: PlayerCharacter, type: FlowerType, quantity: Int = 1)
    {
        val seedCost = type.cost * quantity
        if (player.gold >= seedCost) {
            // Deduct gold locally
            player.gold -= seedCost

            // Firebase reference for the player's flowers
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val seedRef = FirebaseDatabase.getInstance().reference.child("users").child(userId!!).child("seeds").child(type.name)

            // Fetch the current amount of seeds in Firebase and then add the new quantity
            seedRef.get().addOnSuccessListener { snapshot ->
                val currentQuantity = snapshot.getValue(Int::class.java) ?: 0
                val newQuantity = currentQuantity + quantity

                // Update Firebase with the new quantity
                val updates = hashMapOf<String, Any>(
                    "gold" to player.gold,
                    "seeds/${type.name}" to newQuantity
                )

                // Update in Firebase
                FirebaseDatabase.getInstance().reference.child("users").child(userId).updateChildren(updates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            println("Successfully bought $quantity ${type.name}(s) and updated Firebase.")
                            player.addSeed(type.name, quantity)  // Add seeds locally
                        } else {
                            println("Failed to update Firebase: ${task.exception?.message}")
                        }
                    }
            }.addOnFailureListener {
                println("Error fetching flower data: ${it.message}")
            }
        } else {
            println("Not enough gold to buy seeds.")
        }
    }

    // Buy pesticide
    fun buyPesticide(player: PlayerCharacter, price: Int) {
        if (player.gold >= price) {
            // Deduct gold locally
            player.gold -= price

            // Firebase reference for the player's pesticide count
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val pesticideRef = FirebaseDatabase.getInstance().reference.child("users").child(userId!!).child("pesticide")

            // Fetch the current amount of pesticide in Firebase and add the new amount
            pesticideRef.get().addOnSuccessListener { snapshot ->
                val currentPesticide = snapshot.getValue(Int::class.java) ?: 0
                val newPesticide = currentPesticide + 1

                // Update Firebase with the new pesticide quantity and gold
                val updates = hashMapOf<String, Any>(
                    "gold" to player.gold,
                    "pesticide" to newPesticide
                )

                // Update in Firebase
                FirebaseDatabase.getInstance().reference.child("users").child(userId).updateChildren(updates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            player.addPesticide(1)  // Add pesticide locally
                        } else {
                            println("Failed to update Firebase: ${task.exception?.message}")
                        }
                    }
            }.addOnFailureListener {
                println("Error fetching pesticide data: ${it.message}")
            }
        } else {
            println("Not enough gold to buy pesticide.")
        }
    }

    // Buy fertilizer
    fun buyFertilizer(player: PlayerCharacter, price: Int) {
        if (player.gold >= price) {
            // Deduct gold locally
            player.gold -= price

            // Firebase reference for the player's fertilizer count
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val fertilizerRef = FirebaseDatabase.getInstance().reference.child("users").child(userId!!).child("fertilizer")

            // Fetch the current amount of fertilizer in Firebase and add the new amount
            fertilizerRef.get().addOnSuccessListener { snapshot ->
                val currentFertilizer = snapshot.getValue(Int::class.java) ?: 0
                val newFertilizer = currentFertilizer + 1

                // Update Firebase with the new fertilizer quantity and gold
                val updates = hashMapOf<String, Any>(
                    "gold" to player.gold,
                    "fertilizer" to newFertilizer
                )

                // Update in Firebase
                FirebaseDatabase.getInstance().reference.child("users").child(userId).updateChildren(updates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            println("Successfully bought fertilizer and updated Firebase.")
                            player.addFertilizer(1)
                        } else {
                            println("Failed to update Firebase: ${task.exception?.message}")
                        }
                    }
            }.addOnFailureListener {
                println("Error fetching fertilizer data: ${it.message}")
            }
        } else {
            println("Not enough gold to buy fertilizer.")
        }
    }

}