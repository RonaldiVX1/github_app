package com.example.androidsubmissionintermediate.ui.listStory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidsubmissionintermediate.data.response.story.Story
import com.example.androidsubmissionintermediate.databinding.CardStoriesBinding


class ListStoriesAdapter :
    PagingDataAdapter<Story, ListStoriesAdapter.ListStoryViewHolder>(DIFF_CALLBACK) {


    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListStoryViewHolder(val binding: CardStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.tvName.text = story.name
            Glide.with(itemView).load(story.photoUrl).into(binding.imageStory)
            binding.tvDescription.text = story.description

            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClick(story)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStoryViewHolder {
        val view = CardStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListStoryViewHolder(view)
    }


    override fun onBindViewHolder(holder: ListStoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    interface OnItemClickCallback {
        fun onItemClick(data: Story)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
