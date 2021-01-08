package ru.ndevelop.reuser.adapters


import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import ru.ndevelop.reuser.ItemTouchHelperAdapter
import ru.ndevelop.reuser.R
import ru.ndevelop.reuser.utils.Action
import java.util.*
import kotlin.collections.ArrayList


class SelectedActionsAdapter(val mDragStartListener: OnStartDragListener) :
    RecyclerView.Adapter<SelectedActionsAdapter.SingleViewHolder>(), ItemTouchHelperAdapter {
    private var items: ArrayList<Action> = arrayListOf()
    inner class SingleViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView), View.OnTouchListener{
        private val tvActionName: TextView = convertView.findViewById(R.id.tv_action_name)
        private val llAction: LinearLayout = convertView.findViewById(R.id.ll_actions)
        private val ivAction:ImageView = convertView.findViewById(R.id.iv_action)
        private val switch: SwitchMaterial = convertView.findViewById(R.id.switch_rv)
        fun bind(item: Action) {
            tvActionName.text = item.actionName
            llAction.tag = item.name
            llAction.setOnTouchListener(this)
            ivAction.setImageResource(item.icon)
            switch.isClickable = false
            if (item.isTwoStatuses) {
                switch.visibility = View.VISIBLE
                switch.isChecked = item.status
            } else switch.visibility = View.INVISIBLE
        }

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (MotionEventCompat.getActionMasked(event) ==
                MotionEvent.ACTION_DOWN) {
                mDragStartListener.onStartDrag(this)
            }
            return false;
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
        items.removeAt(position);
        notifyItemRemoved(position);
    }
fun getItems():ArrayList<Action> = items

}
interface OnStartDragListener {

    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}
