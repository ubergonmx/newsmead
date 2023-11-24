package com.newsmead.data

import android.widget.Toast
import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.newsmead.models.Article
import com.newsmead.models.SavedList
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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

        suspend fun getListsCollection(context: Context): ArrayList<SavedList>? = coroutineScope {
            if (uid == "null") {
                Toast.makeText(context, "Please login to view/create a list", Toast.LENGTH_SHORT).show()
                return@coroutineScope null
            }

            val firestore = getFirestoreInstance()
            val userListsRef = firestore.collection("users").document(uid).collection("lists")

            try {
                // Reorder to make readLater first
                val finalList = ArrayList<SavedList>()
                var readLater: SavedList? = null

                val documents = userListsRef.get().await()

                val deferredList = documents.documents.map { document ->
                    async {
                        // Grab collection id
                        val listId = document.id

                        val title = document.data?.get("name").toString()

                        // Get number of articles in list under /articles
                        val numArticles = document.reference.collection("articles").get().await().size()

                        // Create SavedList object
                        val list = SavedList(listId, title, numArticles)
                        if (list.title == "Read Later") {
                            readLater = list

                            // Skip
                            return@async null
                        }

                        list
                    }
                }

                // Clean deferredList by removing nulls

                finalList.addAll(deferredList.awaitAll().filterNotNull())

                // Move the read later list at the top
                if (readLater != null) {
                    finalList.add(0, readLater!!)
                }

                finalList
            } catch (exception: Exception) {
                // Handle failure
                null
            }
        }

        suspend fun getAllArticlesFromLists(context: Context): ArrayList<Article> = coroutineScope {
            if (uid == "null") {
                Toast.makeText(context, "Please login to view/create a list", Toast.LENGTH_SHORT).show()
                return@coroutineScope ArrayList<Article>()
            }

            val articles = ArrayList<Article>()

            val firestore = getFirestoreInstance()
            val userListsRef = firestore.collection("users").document(uid).collection("lists")

            try {
                // Use async to perform the nested query concurrently
                val deferredList = userListsRef.get().await().documents.map { listDocument ->
                    async {
                        val listId = listDocument.id
                        val listRef = userListsRef.document(listId).collection("articles")

                        val listArticles = listRef.get().await().documents.mapNotNull { articleDocument ->
                            // Skips articles already in the list
                            val newsId = articleDocument.data?.get("newsId").toString()
                            val articleExists = articles.any { article ->
                                article.newsId == newsId
                            }

                            // If article already exists, skip it
                            if (articleExists) return@mapNotNull null

                            val imageId: Int = articleDocument.data?.get("image").toString().toInt()
                            val sourceImageId: Int = articleDocument.data?.get("sourceImage").toString().toInt()
                            Article(
                                source = articleDocument.data?.get("source").toString(),
                                sourceImage = sourceImageId,
                                newsId = newsId,
                                title = articleDocument.data?.get("title").toString(),
                                imageId = imageId,
                                readTime = articleDocument.data?.get("readTime").toString(),
                                date = articleDocument.data?.get("date").toString(),
                                url = articleDocument.data?.get("url").toString()
                            )
                        }

                        // Convert to ArrayList
                        listArticles
                    }
                }

                // Add all articles to list
                articles.addAll(deferredList.awaitAll().flatten())

                return@coroutineScope articles
            } catch (exception: Exception) {
                // Handle failure
                throw exception
            }
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

        suspend fun getArticlesFromList(context: Context, listId: String): List<Article> = suspendCoroutine { continuation ->
            if (uid == "null") {
                Toast.makeText(context, "Please login to view/create a list", Toast.LENGTH_SHORT).show()
                continuation.resume(emptyList())
            }

            val articles = mutableListOf<Article>()

            val firestore = getFirestoreInstance()
            val userListsRef = firestore.collection("users").document(uid).collection("lists")
            val listRef = userListsRef.document(listId).collection("articles")

            listRef.get()
                .addOnSuccessListener { documents ->
                    // Parse documents into articles
                    articles.addAll(documents.mapNotNull { document ->
                        val imageId: Int = document.data["image"].toString().toInt()
                        val sourceImageId: Int = document.data["sourceImage"].toString().toInt()
                        Article(
                            source = document.data["source"].toString(),
                            sourceImage = sourceImageId,
                            newsId = document.data["newsId"].toString(),
                            title = document.data["title"].toString(),
                            imageId = imageId,
                            readTime = document.data["readTime"].toString(),
                            date = document.data["date"].toString(),
                            url = document.data["url"].toString()
                        )
                    })

                    continuation.resume(articles)
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                    continuation.resumeWithException(exception)
                }
        }

        suspend fun checkIfArticleSavedInLists(context: Context, articleId: String): List<String> = coroutineScope {
            if (uid == "null") {
                Toast.makeText(context, "Please login to view/create a list", Toast.LENGTH_SHORT).show()
                return@coroutineScope emptyList()
            }

            val firestore = getFirestoreInstance()
            val userListsRef = firestore.collection("users").document(uid).collection("lists")

            try {
                // Use async to perform the nested query concurrently
                val lists = userListsRef.get().await().documents
                    .mapNotNull { listDocument ->
                        async {
                            val listId = listDocument.id
                            val listRef = userListsRef.document(listId).collection("articles")

                            val articleExists = listRef
                                .whereEqualTo("newsId", articleId)
                                .get()
                                .await()
                                .documents
                                .isNotEmpty()

                            if (articleExists) listId else null
                        }
                    }
                    .awaitAll()
                    .filterNotNull()

                return@coroutineScope lists
            } catch (exception: Exception) {
                // Handle failure
                throw exception
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
        suspend fun addListToFireStore(context: Context, newListName: String): String {
            if (uid == "null") {
                Toast.makeText(context, "Please login to create a list", Toast.LENGTH_SHORT).show()
                return ""
            }

            val userListsRef = getFirestoreInstance()
                .collection("users")
                .document(uid)
                .collection("lists")

            val newListId = userListsRef.document().id

            val deferred = CompletableDeferred<String>()

            // Create new list document
            userListsRef.document(newListId).set(
                hashMapOf(
                    "name" to newListName
                )
            )
                .addOnSuccessListener {
                    // Handle Success
                    Toast.makeText(context, "List created", Toast.LENGTH_SHORT).show()
                    deferred.complete(newListId)
                }
                .addOnFailureListener {
                    // Handle Failure
                    Toast.makeText(context, "Error creating list", Toast.LENGTH_SHORT).show()
                    deferred.complete("")
                }

            return deferred.await()
        }

        /**
         * Adds an article id to a list in Firestore
         * @param listId Id of the list to add the article to
         * @param articleId Id of the article to add to the list
         */
        fun addArticleIdToFireStoreList(requireContext: Context, listId: String, articleId: String) {
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


        /**
         * Adds an article to a list in Firestore
         * @param listId Id of the list to add the article to
         * @param articleId Id of the article to add to the list
         */
        fun addArticleToFireStoreList(requireContext: Context, listId: String, article: Article) {
            if (uid == "null") {
                Toast.makeText(requireContext, "Please login to add to a list", Toast.LENGTH_SHORT).show()
                return
            }

            val userListsRef = getFirestoreInstance()
                .collection("users")
                .document(uid)
                .collection("lists")

            // Add article to list
            Log.d("FirebaseHelper", "Adding article ${article.newsId} to list $listId")
            userListsRef.document(listId).collection("articles").document(article.newsId).set(
                hashMapOf(
                    "newsId" to article.newsId,
                    "title" to article.title,
                    "image" to article.imageId,
                    "source" to article.source,
                    "sourceImage" to article.sourceImage,
                    "readTime" to article.readTime,
                    "date" to article.date,
                    "url" to article.url
                )
            )
                .addOnSuccessListener {
                    // Handle Success
                }
                .addOnFailureListener {
                    // Handle Failure
                    Toast.makeText(requireContext, "Error adding article to list", Toast.LENGTH_SHORT).show()
                }
        }

        fun deleteArticleFromFirestoreList(requireContext: Context, listId: String, articleId: String) {
            if (uid == "null") {
                Toast.makeText(requireContext, "Please login to delete from a list", Toast.LENGTH_SHORT).show()
                return
            }

            val userListsRef = getFirestoreInstance()
                .collection("users")
                .document(uid)
                .collection("lists")

            // Delete article from list
            Log.d("FirebaseHelper", "Deleting article $articleId from list $listId")
            userListsRef.document(listId).collection("articles").document(articleId).delete()
                .addOnSuccessListener {
                    // Handle Success
                }
                .addOnFailureListener {
                    // Handle Failure
                    Toast.makeText(requireContext, "Error deleting article from list", Toast.LENGTH_SHORT).show()
                }
        }

        fun deleteArticlesFromFirestoreList(requireContext: Context, listId: String, articleIds: List<String>) {
            if (uid == "null") {
                Toast.makeText(requireContext, "Please login to delete from a list", Toast.LENGTH_SHORT).show()
                return
            }

            val userListsRef = getFirestoreInstance()
                .collection("users")
                .document(uid)
                .collection("lists")

            // Delete article from list
            Log.d("FirebaseHelper", "Deleting articles $articleIds from list $listId")
            for (articleId in articleIds) {
                userListsRef.document(listId).collection("articles").document(articleId).delete()
                    .addOnSuccessListener {
                    }
                    .addOnFailureListener {
                        // Handle Failure
                        Toast.makeText(requireContext, "Error deleting article from list", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        fun renameList(requireContext: Context, listId: String, newName: String) {
            if (uid == "null") {
                Toast.makeText(requireContext, "Please login to rename a list", Toast.LENGTH_SHORT).show()
                return
            }

            val userListsRef = getFirestoreInstance()
                .collection("users")
                .document(uid)
                .collection("lists")

            // Rename list
            Log.d("FirebaseHelper", "Renaming list $listId to $newName")
            userListsRef.document(listId).update(
                hashMapOf(
                    "name" to newName
                ) as Map<String, Any>
            )
                .addOnSuccessListener {
                    // Handle Success
                    Toast.makeText(requireContext, "List renamed", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    // Handle Failure
                    Toast.makeText(requireContext, "Error renaming list", Toast.LENGTH_SHORT).show()
                }
        }

        /**
         * Called when viewing an article. Adds the article to the user's history
         * Note: History stores 50 articles max
         * @param article Article to add to history
         */
        fun addArticleToHistory(requireContext: Context, article: Article) {
            if (uid == "null") {
                Toast.makeText(requireContext, "Please login to add to history", Toast.LENGTH_SHORT).show()
                return
            }

            // Get current date and time to store in history
            val currentDateTime = System.currentTimeMillis()

            val userHistoryRef = getFirestoreInstance()
                .collection("users")
                .document(uid)
                .collection("history")

            // Add article to history
            Log.d("FirebaseHelper", "Adding article ${article.newsId} to history")
            userHistoryRef.document(article.newsId).set(
                hashMapOf(
                    "newsId" to article.newsId,
                    "title" to article.title,
                    "image" to article.imageId,
                    "source" to article.source,
                    "sourceImage" to article.sourceImage,
                    "readTime" to article.readTime,
                    "date" to article.date,
                    "url" to article.url,
                    "viewed" to currentDateTime
                )
            )
                .addOnSuccessListener {
                    // Handle Success
                    // If history has more than 50 articles, delete the oldest one
                    userHistoryRef.orderBy("viewed").limit(50).get()
                        .addOnSuccessListener { documents ->
                            if (documents.size() > 50) {
                                // Delete oldest article
                                val oldestArticle = documents.documents[0]
                                userHistoryRef.document(oldestArticle.id).delete()
                            }
                        }
                }
                .addOnFailureListener {
                    // Handle Failure
                    Toast.makeText(requireContext, "Error adding article to history", Toast.LENGTH_SHORT).show()
                }
        }
    }
}