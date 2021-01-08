package ru.ndevelop.reuser.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.ndevelop.reuser.R
import ru.ndevelop.reuser.RequestCodes.actionsListRequestCode
import ru.ndevelop.reuser.SimpleItemTouchHelperCallback
import ru.ndevelop.reuser.adapters.OnItemsStateListener
import ru.ndevelop.reuser.adapters.OnStartDragListener
import ru.ndevelop.reuser.adapters.SelectedActionsAdapter
import ru.ndevelop.reuser.repositories.DataBaseHandler
import ru.ndevelop.reuser.utils.Action
import ru.ndevelop.reuser.utils.ActionTypes
import ru.ndevelop.reuser.utils.Utils


class ActionsSelectedActivity : AppCompatActivity(), View.OnClickListener, OnStartDragListener, OnItemsStateListener {
/*    private lateinit var addFirstActionButton: Button*/
    private lateinit var rvSelectedActions: RecyclerView
    private lateinit var selectedActionsAdapter: SelectedActionsAdapter
    private lateinit var llIfActionsNotSelected: ConstraintLayout
    private lateinit var btnOk:Button
    private lateinit var touchHelper: ItemTouchHelper
    private lateinit var fabActions:FloatingActionButton
    private var tagId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actions_select)
        tagId = intent.getStringExtra("tagId") ?: ""
        selectedActionsAdapter = SelectedActionsAdapter(this,this)
        initViews()
    }

    private fun initViews() {
       /* addFirstActionButton = findViewById(R.id.btn_add_first_action)*/
       /* addFirstActionButton.setOnClickListener(this)*/
        llIfActionsNotSelected = findViewById(R.id.ll_ifEmpty)
        llIfActionsNotSelected.setOnClickListener(this)
        rvSelectedActions = findViewById(R.id.rv_selected_actions)
        btnOk = findViewById(R.id.btn_actions_selected)
        btnOk.setOnClickListener(this)
        fabActions = findViewById(R.id.fab_actions)
        fabActions.setOnClickListener(this)
        with(rvSelectedActions) {
            adapter = selectedActionsAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        }
        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(selectedActionsAdapter)

        touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(rvSelectedActions)
    }

    override fun onClick(v: View) {
        when (v) {
           /* addFirstActionButton, fabActions -> {
                val i = Intent(this, ActionsListActivity::class.java)
                startActivityForResult(i, actionsListRequestCode)
            }*/
                llIfActionsNotSelected,fabActions ->{
                    val i = Intent(this, ActionsListActivity::class.java)
                    startActivityForResult(i, actionsListRequestCode)
                }
            btnOk -> {
                if (selectedActionsAdapter.getItems().isNotEmpty()) {
                    val intent = Intent()
                    intent.putExtra("tagId", tagId)
                    intent.putExtra("actions", selectedActionsAdapter.getItems())
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    Toast.makeText(this, "Вы не выбрали ни одного действия!", Toast.LENGTH_SHORT)
                        .show()
                }

            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        when (requestCode) {
            actionsListRequestCode -> {
                val action = data.getSerializableExtra("action") as Action
               newActionDetected(action)

                //actionsStateChanged(true)
            }

        }

    }
   /* fun actionsStateChanged(isAdded: Boolean){
        if(isAdded){
            llIfActionsNotSelected.visibility = View.GONE
        }
        else{
            if(selectedActionsList.size == 0) llIfActionsNotSelected.visibility = View.VISIBLE
        }
    }*/

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        touchHelper.startDrag(viewHolder)
    }
    private fun newActionDetected(action:Action){

        when(action.actionType){
            ActionTypes.SITE ->{
                openSiteEditTextDialog(action)
            }
            ActionTypes.APPLICATION ->{
                openApplicationEditTextDialog(action)
            }
            ActionTypes.CAMERA -> {
                if (Utils.checkCameraPermission(this))
                    selectedActionsAdapter.addAction(action)
                else {
                    Toast.makeText(this, "Вы не разрешили доступ к камере :(", Toast.LENGTH_SHORT).show()
                }

            }
            else ->{
                selectedActionsAdapter.addAction(action)
            }
        }

    }
    private fun openSiteEditTextDialog(action: Action){
        val taskEditText = EditText(this)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Введите адресс сайта")
            .setMessage("Какой сайт открыть?")
            .setView(taskEditText)
            .setPositiveButton("Ок") { dialog, which ->
                var url = taskEditText.text.toString()
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://$url"
                action.specialData = url
                selectedActionsAdapter.addAction(action)


            }
            .setNegativeButton("Отмена", null)
            .create()
        dialog.show()
    }
    private fun openApplicationEditTextDialog(action: Action){
        val taskEditText = EditText(this)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Введите название пакета приложения")
            .setMessage("Например ru.ndevelop.reuser")
            .setView(taskEditText)
            .setPositiveButton("Ок") { dialog, which ->
                action.specialData = taskEditText.text.toString()
                selectedActionsAdapter.addAction(action)
            }
            .setNegativeButton("Отмена", null)
            .create()
        dialog.show()
    }

    override fun onItemDeleted(currentItemsSize: Int) {
        if(currentItemsSize==0) {
            btnOk.visibility = View.GONE
            llIfActionsNotSelected.visibility = View.VISIBLE
        }
    }

    override fun onItemAdded() {
        btnOk.visibility = View.VISIBLE
        llIfActionsNotSelected.visibility = View.GONE
    }

}