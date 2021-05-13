package ru.ndevelop.reuser.ui.actionsList

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.ndevelop.reuser.R
import ru.ndevelop.reuser.adapters.ActionsAdapter
import ru.ndevelop.reuser.utils.Action
import java.security.Permission

class ActionsListActivity : AppCompatActivity(), View.OnClickListener,
    ActionsAdapter.OnActionClickListener {
    private lateinit var confirmButton: Button
    private lateinit var rvActions: RecyclerView
    private lateinit var actionsAdapter: ActionsAdapter
    private var selectedAction: Action? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actions_list)
        actionsAdapter = ActionsAdapter(this, this)
        initViews()
    }

    private fun initViews() {
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
                    val isPermissionsGranted = checkPermissionStatus(selectedAction!!.actionType.permissions)
                    if(isPermissionsGranted){
                        val intent = Intent()
                        intent.putExtra("action", selectedAction!! )
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                    else{
                        requestPermission(selectedAction!!.actionType.permissions)
                    }

                }
            }
        }

    }

    override fun onActionClicked(action: Action) {
        selectedAction = action
    }
    fun checkPermissionStatus(permissions:Array<String>):Boolean{
        for (i in permissions){
            if(ContextCompat.checkSelfPermission(applicationContext, i) !=PackageManager.PERMISSION_GRANTED) {
               return false
            }
        }
        return true
    }
    fun requestPermission(permissions:Array<String>){
        ActivityCompat.requestPermissions(this, permissions, 1);
    }

}