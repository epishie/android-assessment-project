package com.vp.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vp.list.model.ListItem
import kotlin.math.roundToInt

class ListAdapter(
    private val onItemClickListener: (String) -> Unit
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    private var listItems: List<ListItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list, parent, false)
        ) {
           onItemClickListener(listItems[it].imdbID)
        }
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listItems[position])
    }

    override fun getItemCount(): Int = listItems.size

    fun setItems(listItems: List<ListItem>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }

    fun clearItems() {
        listItems = emptyList()
    }

    class ListViewHolder(
        itemView: View,
        private val onClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView

        init {
            itemView.setOnClickListener {
                onClick(bindingAdapterPosition)
            }
            image = itemView.findViewById(R.id.poster)
        }

        fun bind(listItem: ListItem) {
            if (listItem.poster != null && listItem.poster != NO_IMAGE) {
                val density = image.resources.displayMetrics.density
                GlideApp.with(image)
                    .load(listItem.poster)
                    .override((300 * density).roundToInt(), (600 * density).roundToInt())
                    .into(image)
            } else {
                image.setImageResource(R.drawable.placeholder)
            }
        }
    }

    companion object {
        private const val NO_IMAGE = "N/A"
    }
}
