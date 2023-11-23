package com.newsmead.data

import android.widget.Toast
import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.newsmead.models.Article
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


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

        suspend fun getArticleIdsFromList(context: Context, listId: String): List<String> = suspendCoroutine { continuation ->
            if (uid == "null") {
                Toast.makeText(context, "Please login to view/create a list", Toast.LENGTH_SHORT).show()
                continuation.resume(emptyList())
            }

            val articleIds = mutableListOf<String>()

            val firestore = getFirestoreInstance()
            val userListsRef = firestore.collection("users").document(uid).collection("lists")
            val listRef = userListsRef.document(listId).collection("articles")

            listRef.get()
                .addOnSuccessListener { documents ->
                    // Get article ids from list
                    articleIds.addAll(documents.mapNotNull { document ->
                        document.data["articleId"].toString()
                    })
                    continuation.resume(articleIds)
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                    continuation.resumeWithException(exception)
                }
        }

        /**
         * Adds a new user to Firestore with a "Read Later" list
         * @param email Email of the new user
         * @return Id of the new user; "" if error
         */
        fun addUserToFirestore(context: Context, email: String, name: String = "User"): String {
            var uid = FirebaseAuth.getInstance().currentUser?.uid
            val firestore = getFirestoreInstance()
            var parsedName = name

            if (uid == null) {
                Toast.makeText(context, "Error creating user", Toast.LENGTH_SHORT).show()
                return ""
            }

            if (name == "") {
                parsedName = "User"
            }

            val userRef = firestore.collection("users").document(uid)

            // Create new user document
            userRef.set(
                hashMapOf(
                    "email" to email,
                    "name" to parsedName
                )
            )
                .addOnSuccessListener {
                    // Add "Read Later" list to Firestore
                    val userListsRef = firestore.collection("users").document(uid!!).collection("lists")
                    val newListId = "readLater" // This is the id of the "Read Later" list

                    userListsRef.document(newListId).set(
                        hashMapOf(
                            "name" to "Read Later"
                        )
                    )

                    // Handle Success
                    Toast.makeText(context, "User created", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    // Handle Failure
                    Toast.makeText(context, "Error creating user", Toast.LENGTH_SHORT).show()
                    uid = ""
                }

            return uid as String
        }

        /**
         * Adds a new list to Firestore
         * @param newListName Name of the new list
         * @return Id of the new list; "" if error
         */
        fun addListToFireStore(context: Context, newListName: String): String {
            if (uid == "null") {
                Toast.makeText(context, "Please login to create a list", Toast.LENGTH_SHORT).show()
                return ""
            }

            val userListsRef = getFirestoreInstance()
                .collection("users")
                .document(uid)
                .collection("lists")

            val newListId = userListsRef.document().id

            var listExists = ""

            // Create new list document
            userListsRef.document(newListId).set(
                hashMapOf(
                    "name" to newListName
                )
            )
                .addOnSuccessListener {
                    // Handle Success
                    Toast.makeText(context, "List created", Toast.LENGTH_SHORT).show()
                    listExists = newListId
                }
                .addOnFailureListener {
                    // Handle Failure
                    Toast.makeText(context, "Error creating list", Toast.LENGTH_SHORT).show()
                    listExists = ""
                }

            return listExists
        }

        /**
         * Adds an article to a list in Firestore
         * @param listId Id of the list to add the article to
         * @param articleId Id of the article to add to the list
         */
        fun addArticleToFireStoreList(requireContext: Context, listId: String, articleId: String) {
            if (uid == "null") {
                Toast.makeText(requireContext, "Please login to add to a list", Toast.LENGTH_SHORT).show()
                return
            }

            val userListsRef = getFirestoreInstance()
                .collection("users")
                .document(uid)
                .collection("lists")

            // Add article to list
            Log.d("FirebaseHelper", "Adding article $articleId to list $listId")
            userListsRef.document(listId).collection("articles").document(articleId).set(
                hashMapOf(
                    "articleId" to articleId
                )
            )
                .addOnSuccessListener {
                    // Handle Success
                    Toast.makeText(requireContext, "Article added to list", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    // Handle Failure
                    Toast.makeText(requireContext, "Error adding article to list", Toast.LENGTH_SHORT).show()
                }
        }
    }
}