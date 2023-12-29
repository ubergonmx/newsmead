package com.newsmead.exceptions

open class CustomNewsApiException(msg: String) : RuntimeException() {
    class InvalidApiKeyException(msg: String): CustomNewsApiException(msg)
    class ArticleNotFoundException(msg: String): CustomNewsApiException(msg)
    class ArticleWontLoadException(msg: String): CustomNewsApiException(msg)
    class ArticleNotSavedException(msg: String): CustomNewsApiException(msg)
    class ArticleNotDeletedException(msg: String): CustomNewsApiException(msg)
    class ArticleHasNoImageException(msg: String): CustomNewsApiException(msg)
    class ArticleHasNoContentException(msg: String): CustomNewsApiException(msg)

}