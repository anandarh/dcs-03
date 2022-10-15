package com.anandarh.storyapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anandarh.storyapp.databinding.ItemStoryBinding
import com.anandarh.storyapp.models.StoryModel
import com.squareup.picasso.Picasso
import org.ocpsoft.prettytime.PrettyTime
import java.time.ZonedDateTime

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
    private var listStory: ArrayList<StoryModel> = ArrayList()

    private lateinit var onItemClickListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun getItemCount() = listStory.size

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        with(listStory[position]) {
            holder.binding.apply {
                tvItemName.text = name
                tvItemDescription.text = description
                Picasso.get().load(photoUrl).into(ivItemPhoto)
                tvItemDate.text = PrettyTime().format(ZonedDateTime.parse(createdAt))
            }

            holder.itemView.setOnClickListener {
                onItemClickListener.onItemClick(this)
            }
        }
    }

    fun addData(listStory: ArrayList<StoryModel>) {
        val size = this.listStory.size
        this.listStory.addAll(listStory)
        val sizeNew = this.listStory.size
        notifyItemRangeChanged(size, sizeNew)
    }

    fun setOnItemClickListener(onItemClickListener: ItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class StoryViewHolder(val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface ItemClickListener {
        fun onItemClick(story: StoryModel)
    }
}