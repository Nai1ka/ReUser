package ru.ndevelop.reuser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.sulek.ssml.OnSwipeListener
import io.sulek.ssml.SimpleSwipeMenuLayout
import ru.ndevelop.reuser.R
import ru.ndevelop.reuser.Tag

class TagListAdapter(val editButtonClickListener: OnEditButtonClickListener) :
    RecyclerView.Adapter<TagListAdapter.SingleViewHolder>() {
    private var items: ArrayList<Tag> = arrayListOf()

    inner class SingleViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView),
        View.OnClickListener {
        private val tvTagList: TextView = convertView.findViewById(R.id.tv_tag_name)
        private val tvTagActions: TextView = convertView.findViewById(R.id.tv_tag_actions)
        private val swipeContainer: SimpleSwipeMenuLayout =
            convertView.findViewById(R.id.swipeContainer)
        private val editBtn: FrameLayout = convertView.findViewById(R.id.btn_edit)
        private val deleteBtn: FrameLayout = convertView.findViewById(R.id.btn_delete)
        private var tagId = ""


        fun bind(item: Tag) {
            tvTagList.text = item.name
            tagId = item.tagId
            tvTagActions.text = ""
            item.actions.forEach { tvTagActions.append("${it.actionType.actionName} ${it.specialData}\n") }
            editBtn.setOnClickListener(this)
            deleteBtn.setOnClickListener(this)
            swipeContainer.setOnSwipeListener(object : OnSwipeListener {
                override fun onSwipe(isExpanded: Boolean) {
                    item.isExpanded = isExpanded
                }
            })
            swipeContainer.apply(item.isExpanded)

        }

        override fun onClick(v: View?) {
            if (tagId != "")
                when (v) {
                    editBtn -> {
                        editButtonClickListener.onEditButtonClick(ButtonType.EDIT, tagId)

                    }
                    deleteBtn -> {
                        editButtonClickListener.onEditButtonClick(ButtonType.DELETE,tagId)
                    }
                }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val convertView = inflater.inflate(R.layout.swipe_menu_holder, parent, false)
        return SingleViewHolder(convertView)
    }


    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: SingleViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun loadItems(payload: ArrayList<Tag>) {

        items = payload
        notifyDataSetChanged()
    }


}

interface OnEditButtonClickListener {
    fun onEditButtonClick(button: ButtonType, tagId: String)
}

enum class ButtonType {
    EDIT, DELETE
}