package com.example.instagram

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PostAdapter(val context: Context, val posts: MutableList<Post>): RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        // Specify the layout file to use for this item
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        val post = posts.get(position)
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    // Clean all elements of the recycler
    fun clear() {
        posts.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(tweetList: List<Post>) {
        posts.addAll(tweetList)
        notifyDataSetChanged()
    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProfile: ImageView
        val tvUsername: TextView
        val ivImage: ImageView
        val tvDescription: TextView
        val tvTimeStamp: TextView

        init {
            ivProfile = itemView.findViewById(R.id.iv_profile_image)
            tvUsername = itemView.findViewById(R.id.tv_username_main)
            ivImage = itemView.findViewById(R.id.iv_post_image)
            tvDescription = itemView.findViewById(R.id.tv_description)
            tvTimeStamp = itemView.findViewById(R.id.tv_time)
        }

        fun bind (post: Post) {
            tvDescription.text = post.getDescription()
            tvUsername.text = post.getUser()?.username
            tvTimeStamp.text = TimeFormatter.getTimeDifference(post.createdAt.toString())

            Log.i("PostAdapter", post.getProfileImage()?.url.toString())


            // Populate image
            Glide.with(itemView.context).load(R.drawable.default_face)
                                        .transform(RoundedCorners(50))
                                        .into(ivProfile)
            Glide.with(itemView.context).load(post.getImage()?.url).into(ivImage)
        }


    }



}