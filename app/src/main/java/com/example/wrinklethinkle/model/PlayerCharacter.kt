package com.example.wrinklethinkle.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val _updateSuccessFul = MutableLiveData<Boolean?>()
    val updateSuccessFul: LiveData<Boolean?> = _updateSuccessFul
    // Function to add experience points and level up if possible ----------------------------------------------
    fun gainExperience(exp: Int) {
        experience += exp
        if (experience >= level * 100) {
            val leftover: Int = experience - (level * 100)
            level++
            clickPower += 1
            experience = leftover // Add leftover exp to the next level's experience
        }

        // Save updated values to Firebase
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val playerRef = FirebaseDatabase.getInstance().reference.child("users").child(userId)

        val updatedData = mapOf(
            "level" to level,
            "experience" to experience,
            "clickPower" to clickPower
        )

        playerRef.updateChildren(updatedData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Optionally handle success (e.g., notify the player, update UI)
                _updateSuccessFul.postValue(true)
            } else {
                // Handle failure (e.g., rollback local changes, log error)
            }
        }
    }


    // Functions to add/remove flowers ------------------------------------------------------------------------
    fun addFlower(flowerName: String, amount: Int) {
        // Add firebase ref

        val userId = FirebaseAuth.getInstance().currentUser?.uid
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

    fun removeFlower(flowerName: String, amount: Int, value: Int = 0) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val currentAmount = flowers.getOrDefault(flowerName, 0)

        if (currentAmount >= amount) {
            // Update locally first
            flowers[flowerName] = currentAmount - amount
            addGold(value)

            val flowerRef = FirebaseDatabase.getInstance().reference.child("users").child(userId).child("flowers").child(flowerName)

            flowerRef.get().addOnSuccessListener { snapshot ->
                val currentFlowerQuantity = snapshot.getValue(Int::class.java) ?: 0
                val newFlowerQuantity = currentFlowerQuantity - amount

                if (newFlowerQuantity > 0) {
                    // Update the quantity in Firebase if it's greater than zero
                    val fbUpdates = mapOf("flowers/$flowerName" to newFlowerQuantity)
                    FirebaseDatabase.getInstance().reference.child("users").child(userId).updateChildren(fbUpdates)
                        .addOnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                // Firebase update failed, rollback local change
                                flowers[flowerName] = currentAmount
                            }
                        }
                } else {
                    // If the new quantity is 0 or less, remove the flower from Firebase
                    flowerRef.removeValue().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Optionally remove from the local flowers map if quantity is 0
                            flowers.remove(flowerName)
                        } else {
                            // Firebase removal failed, rollback local change
                            flowers[flowerName] = currentAmount
                        }
                    }
                }
            }.addOnFailureListener {
                // Handle failure when trying to fetch the current flower quantity
                flowers[flowerName] = currentAmount // Optionally rollback local change
            }
        } else {
            // Handle the case where the amount to remove is more than available
            // Optional: Display a warning or log an error
        }
    }



    // Functions to add/remove seeds --------------------------------------------------------------------------
    fun addSeed(seedName: String, amount: Int) {
        seeds[seedName] = seeds.getOrDefault(seedName, 0) + amount
    }

    fun removeSeed(seedName: String, amount: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val currentAmount = seeds.getOrDefault(seedName, 0)

        if (currentAmount >= amount) {
            seeds[seedName] = currentAmount - amount

            val seedsRef =
                FirebaseDatabase.getInstance().reference.child("users").child(userId).child("seeds")
                    .child(seedName)

            seedsRef.get().addOnSuccessListener { snapshot ->
                val currentSeedQuantity = snapshot.getValue(Int::class.java) ?: 0
                val newSeedQuantity = currentSeedQuantity - amount

                if (newSeedQuantity > 0) {
                    // Update the quantity in Firebase if it's greater than zero
                    val fbUpdates = mapOf("seeds/$seedName" to newSeedQuantity)
                    FirebaseDatabase.getInstance().reference.child("users").child(userId)
                        .updateChildren(fbUpdates)
                        .addOnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                // Firebase update failed, rollback local change
                                seeds[seedName] = currentAmount
                            }
                        }
                } else {
                    // If the new quantity is 0 or less, remove the seed from Firebase
                    seedsRef.removeValue().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Optionally remove from the local seeds map if quantity is 0
                            seeds.remove(seedName)
                        } else {
                            // Firebase removal failed, rollback local change
                            seeds[seedName] = currentAmount
                        }
                    }
                }
            }
        }
    }


    // Functions to add/remove gold ---------------------------------------------------------------------------
    fun addGold(amount: Int) {
        gold += amount
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val goldRef = FirebaseDatabase.getInstance().reference.child("users").child(userId!!).child("gold")

        goldRef.get().addOnSuccessListener { snapshot ->
            val currentGoldValue = snapshot.getValue(Int::class.java) ?: 0
            val newGoldValue = currentGoldValue + amount

            val fbUpdates = hashMapOf<String, Any>(
                "gold" to newGoldValue
            )

            FirebaseDatabase.getInstance().reference.child("users").child(userId).updateChildren(fbUpdates).addOnCompleteListener { task ->
                if(task.isSuccessful) {

                } else {
                    //FB updates failed
                }
            }
        }
    }

    fun removeGold(amount: Int) {
        if (gold >= amount) {
            gold -= amount
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
            val goldRef = FirebaseDatabase.getInstance().reference.child("users").child(userId).child("gold")

            goldRef.get().addOnSuccessListener { snapshot ->
                val currentGoldValue = snapshot.getValue(Int::class.java) ?: 0
                val newGoldValue = currentGoldValue - amount

                val fbUpdates = hashMapOf<String, Any>(
                    "gold" to newGoldValue
                )

                FirebaseDatabase.getInstance().reference.child("users").child(userId).updateChildren(fbUpdates).addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        // Handle Firebase update failure
                    }
                }
            }
        }
    }

    // Functions to add/remove pesticide ----------------------------------------------------------------------
    fun addPesticide(amount: Int) {
        pesticide += amount
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val pesticideRef = FirebaseDatabase.getInstance().reference.child("users").child(userId).child("pesticide")

        pesticideRef.get().addOnSuccessListener { snapshot ->
            val currentPesticideValue = snapshot.getValue(Int::class.java) ?: 0
            val newPesticideValue = currentPesticideValue + amount

            val fbUpdates = hashMapOf<String, Any>(
                "pesticide" to newPesticideValue
            )

            FirebaseDatabase.getInstance().reference.child("users").child(userId).updateChildren(fbUpdates).addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    // Handle Firebase update failure
                }
            }
        }
    }

    fun removePesticide(amount: Int) {
        if (pesticide >= amount) {
            pesticide -= amount
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
            val pesticideRef = FirebaseDatabase.getInstance().reference.child("users").child(userId).child("pesticide")

            pesticideRef.get().addOnSuccessListener { snapshot ->
                val currentPesticideValue = snapshot.getValue(Int::class.java) ?: 0
                val newPesticideValue = currentPesticideValue - amount

                val fbUpdates = hashMapOf<String, Any>(
                    "pesticide" to newPesticideValue
                )

                FirebaseDatabase.getInstance().reference.child("users").child(userId).updateChildren(fbUpdates).addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        // Handle Firebase update failure
                    }
                }
            }
        }
    }

    // Functions to add/remove fertilizer ---------------------------------------------------------------------
    fun addFertilizer(amount: Int) {
        fertilizer += amount
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val fertilizerRef = FirebaseDatabase.getInstance().reference.child("users").child(userId).child("fertilizer")

        fertilizerRef.get().addOnSuccessListener { snapshot ->
            val currentFertilizerValue = snapshot.getValue(Int::class.java) ?: 0
            val newFertilizerValue = currentFertilizerValue + amount

            val fbUpdates = hashMapOf<String, Any>(
                "fertilizer" to newFertilizerValue
            )

            FirebaseDatabase.getInstance().reference.child("users").child(userId).updateChildren(fbUpdates).addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    // Handle Firebase update failure
                }
            }
        }
    }

    fun removeFertilizer(amount: Int) {
        if (fertilizer >= amount) {
            fertilizer -= amount
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
            val fertilizerRef = FirebaseDatabase.getInstance().reference.child("users").child(userId).child("fertilizer")

            fertilizerRef.get().addOnSuccessListener { snapshot ->
                val currentFertilizerValue = snapshot.getValue(Int::class.java) ?: 0
                val newFertilizerValue = currentFertilizerValue - amount

                val fbUpdates = hashMapOf<String, Any>(
                    "fertilizer" to newFertilizerValue
                )

                FirebaseDatabase.getInstance().reference.child("users").child(userId).updateChildren(fbUpdates).addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        // Handle Firebase update failure
                    }
                }
            }
        }
    }
}