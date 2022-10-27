package com.anandarh.storyapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.anandarh.storyapp.databinding.ItemLoadingBinding
import com.anandarh.storyapp.databinding.ItemStoryBinding
import com.anandarh.storyapp.models.StoryModel
import com.anandarh.storyapp.ui.activities.DetailStoryActivity
import com.anandarh.storyapp.ui.activities.ListStoryActivity
import com.squareup.picasso.Picasso
import org.ocpsoft.prettytime.PrettyTime
import java.time.ZonedDateTime


class StoryAdapter(
    private val activity: ListStoryActivity,
    private var listStory: ArrayList<StoryModel?>
) :
    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    companion object {
        private const val VIEW_TYPE_CONTENT = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        return if (viewType == VIEW_TYPE_CONTENT) {
            val binding =
                ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ContentViewHolder(binding)
        } else {
            val binding =
                ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(binding)
        }

    }

    override fun getItemCount() = listStory.size

    override fun getItemViewType(position: Int): Int {
        return if (listStory[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_CONTENT
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {

        if (holder is ContentViewHolder) {
            populateItemRows(holder, position)
        } else if (holder is LoadingViewHolder) {
            showLoadingView(holder, position)
        }

    }

    private fun showLoadingView(holder: LoadingViewHolder, position: Int) {
        //ProgressBar would be displayed
    }

    private fun populateItemRows(holder: ContentViewHolder, position: Int) {
        with(listStory[position]!!) {
            holder.binding.apply {
                tvItemName.text = name
                tvItemDescription.text = description
                Picasso.get().load(photoUrl).into(ivItemPhoto)
                tvItemDate.text = PrettyTime().format(ZonedDateTime.parse(createdAt))
            }

            holder.itemView.setOnClickListener {
                val intent = Intent(activity, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.EXTRA_STORY, this)
                val p1: Pair<View, String> =
                    Pair.create(holder.binding.ivItemPhoto as View, "photo")
                val p2: Pair<View, String> = Pair.create(holder.binding.tvItemName as View, "name")
                val p3: Pair<View, String> = Pair.create(holder.binding.tvItemDate as View, "date")
                val p4: Pair<View, String> =
                    Pair.create(holder.binding.tvItemDescription as View, "description")
                val options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity, p1, p2, p3, p4)
                activity.startActivity(intent, options.toBundle())

            }
        }
    }

    fun addData(listStory: ArrayList<StoryModel>, refresh: Boolean) {
        if (refresh) {
            this.listStory.clear()
        }
        val size = this.listStory.size
        this.listStory.addAll(listStory)
        val sizeNew = this.listStory.size
        notifyItemRangeChanged(size, sizeNew)
    }

    fun addLoading() {
        listStory.add(null)
        notifyItemInserted(listStory.size - 1)
    }

    fun removeLoading() {
        val hasLoading = listStory.contains(null)
        if (hasLoading) {
            listStory.removeAt(listStory.size - 1)
            notifyItemRemoved(listStory.size)
        }
    }

    open class StoryViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    inner class ContentViewHolder(val binding: ItemStoryBinding) :
        StoryViewHolder(binding.root)

    inner class LoadingViewHolder(binding: ItemLoadingBinding) :
        StoryViewHolder(binding.root)
}