import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anandarh.storyapp.databinding.ItemStoryBinding
import com.anandarh.storyapp.models.StoryModel
import com.anandarh.storyapp.ui.activities.DetailStoryActivity
import com.squareup.picasso.Picasso
import org.ocpsoft.prettytime.PrettyTime
import java.time.ZonedDateTime

class StoryAdapter(private val activity: Activity) :
    PagingDataAdapter<StoryModel, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding =
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.binding.apply {
                tvItemName.text = data.name
                tvItemDescription.text = data.description
                Picasso.get().load(data.photoUrl).into(ivItemPhoto)
                tvItemDate.text = PrettyTime().format(ZonedDateTime.parse(data.createdAt))
            }

            holder.itemView.setOnClickListener {
                val intent = Intent(activity, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.EXTRA_STORY, data)
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


    class StoryViewHolder(val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryModel>() {
            override fun areItemsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}