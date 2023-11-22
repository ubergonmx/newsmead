package com.newsmead.data

import android.widget.Toast
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseHelper {
    companion object {
        private var uid = FirebaseAuth.getInstance().currentUser?.uid ?: "null"

        fun getUid(): String {
            if (uid == "null") {
                uid = FirebaseAuth.getInstance().currentUser?.uid ?: "null"
                if (uid == "null") {
                    return "null"
                } else {
                    return uid
                }

            } else {
                return uid

            }
        }

        fun getFirestoreInstance(): FirebaseFirestore {
            return FirebaseFirestore.getInstance()
        }

        fun getListsCollection(context: Context): CollectionReference? {
            if (uid == "null") {
                Toast.makeText(context, "Please login to view/create a list", Toast.LENGTH_SHORT).show()
                return null
            }

            return getFirestoreInstance().collection("users")
                .document(uid).collection("lists")
        }

        /**
         * Adds a new list to Firestore
         * @param newListName Name of the new list
         */
        fun addListToFireStore(context: Context, newListName: String): Boolean {
            if (uid == "null") {
                Toast.makeText(context, "Please login to create a list", Toast.LENGTH_SHORT).show()
                return false
            }

            val userListsRef = getFirestoreInstance()
                .collection("users")
                .document(uid)
                .collection("lists")

            val newListId = userListsRef.document().id

            var listExists = false

            // Create new list document
            userListsRef.document(newListId).set(
                hashMapOf(
                    "name" to newListName
                )
            )
                .addOnSuccessListener {
                    // Handle Success
                    Toast.makeText(context, "List created", Toast.LENGTH_SHORT).show()
                    listExists = true
                }
                .addOnFailureListener {
                    // Handle Failure
                    Toast.makeText(context, "Error creating list", Toast.LENGTH_SHORT).show()
                    listExists = false
                }

            return listExists
        }
    }
}