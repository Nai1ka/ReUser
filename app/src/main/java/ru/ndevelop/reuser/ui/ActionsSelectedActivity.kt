package ru.ndevelop.reuser.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.ndevelop.reuser.R
import ru.ndevelop.reuser.RequestCodes.actionsListRequestCode
import ru.ndevelop.reuser.SimpleItemTouchHelperCallback
import ru.ndevelop.reuser.adapters.OnStartDragListener
import ru.ndevelop.reuser.adapters.SelectedActionsAdapter
import ru.ndevelop.reuser.repositories.DataBaseHandler
import ru.ndevelop.reuser.utils.Action


class ActionsSelectedActivity : AppCompatActivity(), View.OnClickListener, OnStartDragListener {
    private lateinit var addFirstActionButton: Button
    private lateinit var rvSelectedActions: RecyclerView
    private lateinit var selectedActionsAdapter: SelectedActionsAdapter
    private lateinit var llIfActionsNotSelected: LinearLayout
    private lateinit var btnOk:Button
    private lateinit var touchHelper: ItemTouchHelper
    private lateinit var fabActions:FloatingActionButton
    var tagId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actions_select)
        tagId = intent.getStringExtra("tagId") ?: ""
        selectedActionsAdapter = SelectedActionsAdapter(this)
        initViews()
    }

    fun initViews() {
        addFirstActionButton = findViewById(R.id.btn_add_first_action)
        addFirstActionButton.setOnClickListener(this)
        llIfActionsNotSelected = findViewById(R.id.ll_ifEmpty)
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
            addFirstActionButton, fabActions -> {
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
                        .show();
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
        touchHelper.startDrag(viewHolder);
    }
    fun newActionDetected(action:Action){
        when(action){
            Action.SITE ->{
                openSiteEditTextDialog(action)
            }
            Action.APPLICATION ->{
                openApplicationEditTextDialog(action)
            }
            else ->{
                selectedActionsAdapter.addAction(action)
            }
        }

        llIfActionsNotSelected.visibility = View.GONE
    }
    fun openSiteEditTextDialog(action: Action){
        val taskEditText = EditText(this);
        var resultUrl = ""
        taskEditText.setText(DataBaseHandler.getTagName(tagId), TextView.BufferType.EDITABLE);
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
    fun openApplicationEditTextDialog(action: Action){
        val taskEditText = EditText(this);
        var resultUrl = ""
        taskEditText.setText(DataBaseHandler.getTagName(tagId), TextView.BufferType.EDITABLE);
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

}