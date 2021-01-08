package ru.ndevelop.reuser.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import ru.ndevelop.reuser.R
import ru.ndevelop.reuser.utils.Action
import ru.ndevelop.reuser.utils.ActionTypes
import ru.ndevelop.reuser.utils.Utils


class ActionsAdapter(val context: Context, val clickListener: OnActionClickListener) :
    RecyclerView.Adapter<ActionsAdapter.SingleViewHolder>() {
    private var items: ArrayList<Action> = Utils.getActionsList()
    var lastClickPosition:Int = 0

    inner class SingleViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView),
        View.OnClickListener {
        private val tvActionName: TextView = convertView.findViewById(R.id.tv_action_name)
        private val llAction: LinearLayout = convertView.findViewById(R.id.ll_actions)
        private val switch: SwitchMaterial = convertView.findViewById(R.id.switch_rv)
        private val ivAction: ImageView = convertView.findViewById(R.id.iv_action)
        fun bind(item: Action) {
            tvActionName.text = item.actionType.actionName
            llAction.tag = item.actionType.name
            switch.isChecked = false
            ivAction.setImageResource(item.actionType.icon)
            llAction.setBackgroundResource(R.color.white)
            if (item.actionType.isTwoStatuses) switch.visibility = View.VISIBLE
            else switch.visibility = View.INVISIBLE
            switch.setOnClickListener(this)
            llAction.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (v != null) {

                llAction.setBackgroundResource(R.color.lightGrey)
                val tempAction = Action(ActionTypes.valueOf(llAction.tag as String))
                notifyItemChanged(lastClickPosition)
                lastClickPosition = tempAction.actionType.ordinal

                tempAction.status = switch.isChecked
                clickListener.onActionClicked(tempAction)


            }

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

    interface OnActionClickListener {
        fun onActionClicked(action: Action)
    }

}
