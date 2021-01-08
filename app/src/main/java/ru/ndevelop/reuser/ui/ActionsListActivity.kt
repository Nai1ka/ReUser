package ru.ndevelop.reuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ru.ndevelop.reuser.R
import ru.ndevelop.reuser.adapters.ActionsAdapter
import ru.ndevelop.reuser.utils.Action

class ActionsListActivity : AppCompatActivity(), View.OnClickListener,
    ActionsAdapter.onActionClickListener {
    private lateinit var confirmButton: Button
    private lateinit var rvActions: RecyclerView
    private lateinit var actionsAdapter: ActionsAdapter
    var selectedAction: Action? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actions_list)
        actionsAdapter = ActionsAdapter(this, this)
        initViews()
    }

    fun initViews() {
        confirmButton = findViewById(R.id.btn_action_confirm)
        confirmButton.setOnClickListener(this)
        rvActions = findViewById(R.id.rv_actions)
        with(rvActions) {
            adapter = actionsAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        }
    }

    override fun onClick(v: View) {
        when (v) {
            confirmButton -> {
                if ( selectedAction!=null) {
                    val intent = Intent()
                    intent.putExtra("action", selectedAction)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }

    }

    override fun onActionClicked(action: Action) {
        selectedAction = action
    }

}