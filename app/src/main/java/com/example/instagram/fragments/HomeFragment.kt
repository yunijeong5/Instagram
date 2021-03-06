package com.example.instagram.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.instagram.MainActivity
import com.example.instagram.Post
import com.example.instagram.PostAdapter
import com.example.instagram.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import okhttp3.Headers
import org.json.JSONException

open class HomeFragment : Fragment() {

    lateinit var rvPost: RecyclerView
    lateinit var adapter: PostAdapter
    var allPosts: MutableList<Post> = mutableListOf()
    lateinit var swipeContainer: SwipeRefreshLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // This is where we set up our views and click listeners
        super.onViewCreated(view, savedInstanceState)

        swipeContainer = view.findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            Log.i(MainActivity.TAG, "refreshing timeline")
            populateHomeTimeline()

        }

        rvPost =  view.findViewById(R.id.rvPost)

        // 1. Create layout for each row in list (item_post.xml)
        // 2. Create data source for each row (Post class)
        // 3. Create adapter that will bridge data and row layout (PostAdapter)
        // 4. Set adapter on RecyclerView
        adapter = PostAdapter(requireContext(), allPosts)
        rvPost.adapter = adapter

        // 5. Set layout manager on RecyclerView
        rvPost.layoutManager = LinearLayoutManager(requireContext())
        queryPosts()
    }

    private fun populateHomeTimeline() {
        adapter.clear()
        queryPosts()
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.isRefreshing = false
    }


    // Query for all posts in our server
    open fun queryPosts() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER)
        query.limit = 20;
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


    companion object {
        const val TAG = "HomeFragment"
    }

}