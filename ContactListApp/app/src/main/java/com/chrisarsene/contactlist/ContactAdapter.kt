package com.chrisarsene.contactlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(
    private val allContacts: List<Contact>,
    private val onItemClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>(), Filterable {

    private var filteredList = allContacts.toMutableList()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.findViewById(R.id.ivAvatar)
        val name: TextView = view.findViewById(R.id.tvName)
        val phone: TextView = view.findViewById(R.id.tvPhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = filteredList[position]
        holder.name.text = contact.name
        holder.phone.text = contact.phone
        holder.avatar.setImageResource(contact.avatarRes)
        holder.itemView.setOnClickListener { onItemClick(contact) }
    }

    override fun getItemCount() = filteredList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val results = FilterResults()
                if (query.isNullOrBlank()) {
                    results.values = allContacts.toMutableList()
                } else {
                    val q = query.toString().trim().lowercase()
                    results.values = allContacts.filter {
                        it.name.lowercase().contains(q)
                    }.toMutableList()
                }
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(query: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as MutableList<Contact>
                notifyDataSetChanged()
            }
        }
    }
}
