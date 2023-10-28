package com.sekalisubmit.storymu.ui.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.sekalisubmit.storymu.R
import com.sekalisubmit.storymu.data.local.room.story.Story

class StoryListAdapter(
    private val onClick: (Story) -> Unit
): ListAdapter<Story, StoryListAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback: DiffUtil.ItemCallback<Story>() {
        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem.id == newItem.id
        }
    }
    class ViewHolder(itemLayout: View): RecyclerView.ViewHolder(itemLayout) {
        val image: ImageView = itemLayout.findViewById(R.id.item_image)
        val title: TextView = itemLayout.findViewById(R.id.item_title)
        val description: TextView = itemLayout.findViewById(R.id.item_description)
        val author: TextView = itemLayout.findViewById(R.id.item_author)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemLayout = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(itemLayout)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        val requestOptions = RequestOptions()
            .placeholder(R.drawable.loading)
            .error(R.drawable.error)

        Glide.with(holder.itemView.context)
            .load(item.photoUrl)
            .apply(requestOptions)
            .into(holder.image)


        val text = item.description?.split("\\s+".toRegex())
        val maxDesc = 20

        if ((text?.size ?: 0) > 3) {
            holder.title.text = text?.subList(0, 3)?.joinToString(" ") ?: ""

            if ((text?.size ?: 0) > maxDesc) {
                val limitedDesc = text?.subList(3, maxDesc)?.joinToString(" ") ?: ""
                holder.description.text = "$limitedDesc.."
            } else {
                holder.description.text = text?.subList(3, text.size)?.joinToString(" ") ?: ""
            }
        } else {
            item.description?.length?.let {
                if (it in 26..200){
                    holder.title.text = "${item.description?.substring(0, 25)}"
                    holder.description.text = "${item.description?.substring(25, it)}.."
                } else if (it > 200) {
                    holder.title.text = "${item.description?.substring(0, 25)}"
                    holder.description.text = "${item.description?.substring(25, 59)}.."
                } else {
                    holder.title.text = item.description ?: ""
                    holder.description.text = ""
                }
            }
        }


        holder.author.text = "by ${item.name}"
    }
}