package com.example.instagram.fragments

import android.util.Log
import com.example.instagram.MainActivity
import com.example.instagram.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment: HomeFragment() {

    override fun queryPosts() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER)

        // only return posts from currently signed in user
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
        query.addDescendingOrder("createdAt")
        // Find all Post objects
        query.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    // Something has gone wrong
                    Log.e(MainActivity.TAG, "Error fetching posts")
                } else {
                    if (posts != null) {
                        for (post in posts) {
                            Log.i(
                                MainActivity.TAG, "Post: " + post.getDescription() + " , User: "
                                        + post.getUser()?.username)
                        }

                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                    }
                }

            }

        })
    }
}