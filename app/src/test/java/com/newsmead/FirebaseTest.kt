package com.newsmead

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.newsmead.data.FirebaseHelper
import org.junit.Test
import org.mockito.Mockito

class FirebaseTest {
    @Test
    fun createAccount() {
        val email = "newtestaccount@testeremail.com"
        val pass = "123456"

        // First create account
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            email, pass
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val user = FirebaseAuth.getInstance().currentUser
                assert(user != null)

                val mockContext = Mockito.mock(Context::class.java)

                // Test if user is in database
                FirebaseHelper.addUserToFireStore(mockContext, email, "Test Account")
                val firestore = FirebaseHelper.getFirestoreInstance()
                val userRef = firestore.collection("users").document(user!!.uid)
                userRef.get().addOnSuccessListener { document ->
                    assert(document.exists())
                }

                // Test if lists collection is created and contains readLater and offlineArticles
                val listsRef = userRef.collection("lists")
                listsRef.get().addOnSuccessListener { documents ->
                    assert(documents.size() == 2)

                    // Assert that readLater and offlineArticles are in the lists collection
                    assert(documents.any { it.id == "readLater" })
                    assert(documents.any { it.id == "offlineArticles" })
                }

                // Delete user from database
                userRef.delete()

            } else {
                // If sign in fails, display a message to the user.
                assert(false)
            }
        }
    }
}