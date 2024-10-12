package com.example.wrinklethinkle.model

import android.widget.Toast
import com.example.wrinklethinkle.network.FirebaseManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PlayerCharacter(
    var name: String,          // Player's name
    var level: Int = 1,        // Player's level
    var experience: Int = 0,   // Player's experience points
    var gold: Int = 0,         // Player's gold currency
    var clickPower: Double = 1.0, // Player's click power (damage, growth, etc.)
    var pesticide: Int = 0,    // Amount of pesticide available
    var fertilizer: Int = 0,   // Amount of fertilizer available
    var flowers: MutableMap<String, Int> = mutableMapOf(),  // Flower inventory (name -> amount)
    var seeds: MutableMap<String, Int> = mutableMapOf()     // Seed inventory (name -> amount)
) {
    // Function to add experience points and level up if possible ----------------------------------------------
    fun gainExperience(exp: Int) {
        experience += exp
        if (experience >= level * 100) {
            val leftover: Int = experience - (level * 100)
            level++
            clickPower += 1
            experience = 0 + leftover
        }
    }


    // Functions to add/remove flowers ------------------------------------------------------------------------
    fun addFlower(flowerName: String, amount: Int) {
        // Add firebase ref

        val userId = FirebaseAuth.getInstance().currentUser?.uid
//        if(userId != null) {
//            FirebaseManager().saveUserData(userId, "flowers/${flowerName}", amount) { success ->
//                if(success) {
//                    //pass through a success bool to show a message.
//                } else {
//                    //pass through a success bool to show a message.
//                }
//            }
//
//        }
        val flowerRef = FirebaseDatabase.getInstance().reference.child("users").child(userId!!).child("flowers").child(flowerName)

        flowerRef.get().addOnSuccessListener { snapshot ->
            val currentFlowerQuantity = snapshot.getValue(Int::class.java) ?: 0
            val newFlowerQuantity = currentFlowerQuantity + amount

            val fbUpdates = hashMapOf<String, Any>(
                "flowers/${flowerName}" to newFlowerQuantity
            )

            FirebaseDatabase.getInstance().reference.child("users").child(userId).updateChildren(fbUpdates).addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    flowers[flowerName] = flowers.getOrDefault(flowerName, 0) + amount
                } else {
                    //FB updates failed
                }
            }
        }
    }

    fun removeFlower(flowerName: String, amount: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val currentAmount = flowers.getOrDefault(flowerName, 0)

        if (currentAmount >= amount) {
            // Update locally first
            flowers[flowerName] = currentAmount - amount

            val flowerRef = FirebaseDatabase.getInstance().reference.child("users").child(userId).child("flowers").child(flowerName)

            flowerRef.get().addOnSuccessListener { snapshot ->
                val currentFlowerQuantity = snapshot.getValue(Int::class.java) ?: 0
                val newFlowerQuantity = currentFlowerQuantity - amount

                // Ensure new quantity is not negative
                if (newFlowerQuantity >= 0) {
                    val fbUpdates = mapOf("flowers/$flowerName" to newFlowerQuantity)

                    FirebaseDatabase.getInstance().reference.child("users").child(userId).updateChildren(fbUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Firebase updated successfully
                            } else {
                                // Firebase update failed, rollback local change
                                flowers[flowerName] = currentAmount
                            }
                        }
                } else {
                    // Handle case where new quantity would be negative
                    // Optional: Log a message or show a warning to the user
                }
            }.addOnFailureListener {
                // Handle the failure when trying to get the snapshot from Firebase
                // Optional: Log a message or show a warning to the user
            }
        } else {
            // Handle case when the local flower amount is insufficient
            // Optional: Log a message or show a warning to the user
        }
    }


    // Functions to add/remove seeds --------------------------------------------------------------------------
    fun addSeed(seedName: String, amount: Int) {
        seeds[seedName] = seeds.getOrDefault(seedName, 0) + amount
    }

    fun removeSeed(seedName: String, amount: Int) {
        val currentAmount = seeds.getOrDefault(seedName, 0)
        if (currentAmount >= amount) {
            seeds[seedName] = currentAmount - amount
        }
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val seedsRef = FirebaseDatabase.getInstance().reference.child("users").child(userId!!).child("seeds").child(seedName)

        seedsRef.get().addOnSuccessListener { snapshot ->
            val currentFlowerQuantity = snapshot.getValue(Int::class.java) ?: 0
            val newFlowerQuantity = currentFlowerQuantity - amount

            val fbUpdates = hashMapOf<String, Any>(
                "seeds/${seedName}" to newFlowerQuantity
            )

            FirebaseDatabase.getInstance().reference.child("users").child(userId).updateChildren(fbUpdates).addOnCompleteListener { task ->
                if(task.isSuccessful && seeds.isNotEmpty()) {
                    seeds[seedName] = seeds.getOrDefault(seedName, 0) - amount
                } else {
                    //FB updates failed
                }
            }
        }
    }

    // Functions to add/remove gold ---------------------------------------------------------------------------
    fun addGold(goldName: String, amount: Int) {
        gold += amount
    }

    fun removeGold(goldName: String, amount: Int) {
        val currentAmount = gold
        if (currentAmount >= amount) {
            gold -= amount
        }
    }

    // Functions to add/remove pesticide ----------------------------------------------------------------------
    fun addPesticide(amount: Int) {
        pesticide += amount
    }

    fun removePesticide(amount: Int) {
        val currentAmount = pesticide
        if (currentAmount >= amount) {
            pesticide -= amount
        }
    }

    // Functions to add/remove fertilizer ---------------------------------------------------------------------
    fun addFertilizer(amount: Int) {
        fertilizer += amount
    }

    fun removeFertilizer(amount: Int) {
        val currentAmount = fertilizer
        if (currentAmount >= amount) {
            fertilizer -= amount
        }
    }
}