package ru.ndevelop.reuser.adapters


import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import ru.ndevelop.reuser.R
import ru.ndevelop.reuser.interfaces.ItemTouchHelperAdapter
import ru.ndevelop.reuser.utils.Action
import java.util.*
import kotlin.collections.ArrayList

class SelectedActionsAdapter(val mDragStartListener: OnStartDragListener, private val onItemsStateListener: OnItemsStateListener) :
    RecyclerView.Adapter<SelectedActionsAdapter.SingleViewHolder>(), ItemTouchHelperAdapter {
    private var items: ArrayList<Action> = arrayListOf()
    inner class SingleViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView), View.OnTouchListener{
        private val tvActionName: TextView = convertView.findViewById(R.id.tv_action_name)
        private val llAction: LinearLayout = convertView.findViewById(R.id.ll_actions)
        private val ivAction:ImageView = convertView.findViewById(R.id.iv_action)
        private val toggleButton: ToggleButton = convertView.findViewById(R.id.toggle_button_rv)
        fun bind(item: Action) {
            tvActionName.text = item.actionType.actionName
            llAction.tag = item.actionType.name
            llAction.setOnTouchListener(this)
            ivAction.setImageResource(item.actionType.icon)
            toggleButton.isClickable = false
            if (item.actionType.isTwoStatuses) {
                toggleButton.visibility = View.VISIBLE
                toggleButton.isChecked = item.status
            } else toggleButton.visibility = View.INVISIBLE
        }

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (MotionEventCompat.getActionMasked(event) ==
                MotionEvent.ACTION_DOWN) {
                mDragStartListener.onStartDrag(this)
            }
            return false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val convertView = inflater.inflate(R.layout.single_action_selecting, parent, false)
        return SingleViewHolder(convertView)
    }


    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: SingleViewHolder, position: Int) {
        holder.bind(items[position])

    }
    fun addAction(action: Action){
        items.add(action)
        onItemsStateListener.onItemAdded()
       notifyDataSetChanged()

    }
    fun loadActions(actions: ArrayList<Action>){
        items = actions
        onItemsStateListener.onItemAdded()
        notifyDataSetChanged()

    }
    fun clear(){
        items = arrayListOf()
        onItemsStateListener.onItemDeleted(0)
        notifyDataSetChanged()
    }
    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(items, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(items, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        items.removeAt(position)
        onItemsStateListener.onItemDeleted(items.size)
        notifyItemRemoved(position)
    }
fun getItems():ArrayList<Action> = items
}
interface OnStartDragListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}
interface OnItemsStateListener{
    fun onItemDeleted(currentItemsSize:Int)
    fun onItemAdded()

}
