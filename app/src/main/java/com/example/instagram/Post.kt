package com.example.instagram

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import com.example.instagram.TimeFormatter

// We defined that every post class has description, image, and user:
// Description: String
// Image: File
// User: User
@ParseClassName("Post")
class Post (): ParseObject() {
    fun getDescription(): String? {
        return getString(KEY_DESCRIPTION)
    }

    fun setDescription(description: String) {
        put(KEY_DESCRIPTION, description)
    }

    fun getImage(): ParseFile? {
        return getParseFile(KEY_IMAGE)
    }

    fun setImage(parseFile: ParseFile) {
        put(KEY_IMAGE, parseFile)
    }

    fun getProfileImage(): ParseFile? {
        return getParseFile(KEY_PROFILE)
    }

    fun setProfileImage(parseFile: ParseFile)  {
        put(KEY_PROFILE, parseFile)
    }

    fun getUser(): ParseUser? {
        return getParseUser(KEY_USER)
    }

    fun setUser(user: ParseUser) {
        put(KEY_USER, user)
    }


    companion object {
        const val KEY_DESCRIPTION = "description"
        const val KEY_IMAGE = "image"
        const val KEY_PROFILE = "profilePhoto"
        const val KEY_USER = "user"
    }
}