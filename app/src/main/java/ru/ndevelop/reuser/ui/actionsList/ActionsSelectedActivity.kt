package ru.ndevelop.reuser.ui.actionsList

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import ru.ndevelop.reuser.R
import ru.ndevelop.reuser.adapters.OnItemsStateListener
import ru.ndevelop.reuser.adapters.OnStartDragListener
import ru.ndevelop.reuser.adapters.SelectedActionsAdapter
import ru.ndevelop.reuser.interfaces.SimpleItemTouchHelperCallback
import ru.ndevelop.reuser.repositories.DataBaseHandler
import ru.ndevelop.reuser.utils.Action
import ru.ndevelop.reuser.utils.ActionTypes
import ru.ndevelop.reuser.utils.RequestCodes.actionsListRequestCode
import ru.ndevelop.reuser.utils.Utils


class ActionsSelectedActivity : AppCompatActivity(), View.OnClickListener, OnStartDragListener, OnItemsStateListener, View.OnLongClickListener {
    private lateinit var rvSelectedActions: RecyclerView
    private lateinit var selectedActionsAdapter: SelectedActionsAdapter
    private lateinit var llIfActionsNotSelected: ConstraintLayout
    private lateinit var btnAdd:Button
    private lateinit var llNotEmpty:LinearLayout
    private lateinit var etName:TextInputEditText
    private lateinit var touchHelper: ItemTouchHelper
    private lateinit var fabOk:FloatingActionButton
    private lateinit var fabDelete:FloatingActionButton
    private var tagId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actions_select)
        tagId = intent.getStringExtra("tagId") ?: ""
        selectedActionsAdapter = SelectedActionsAdapter(this, this)
        val tagActions = DataBaseHandler.getTagActions(tagId)
        val tagName = DataBaseHandler.getTagName(tagId)
        initViews()
        if(tagActions.size>0) {
            selectedActionsAdapter.loadActions(tagActions)
            etName.setText(tagName)
        }
    }

    private fun initViews() {
        llIfActionsNotSelected = findViewById(R.id.ll_ifEmpty)
        llIfActionsNotSelected.setOnClickListener(this)
        rvSelectedActions = findViewById(R.id.rv_selected_actions)
        llNotEmpty = findViewById(R.id.ll_not_empty)
        btnAdd = findViewById(R.id.btn_add_actions)
        btnAdd.setOnClickListener(this)
        etName = findViewById(R.id.et_name)

        fabOk = findViewById(R.id.fab_ok)
        fabOk.setOnClickListener(this)
        fabDelete = findViewById(R.id.fab_delete)
        fabDelete.setOnLongClickListener(this)
        fabDelete.setOnClickListener(this)
        with(rvSelectedActions) {
            adapter = selectedActionsAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        }
        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(
            selectedActionsAdapter
        )

        touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(rvSelectedActions)
    }
    var fabDeleteClickCounter =0
    override fun onClick(v: View) {
        when (v) {
            llIfActionsNotSelected, btnAdd -> {
                val i = Intent(this, ActionsListActivity::class.java)
                startActivityForResult(i, actionsListRequestCode)
            }
            fabDelete -> {
                fabDeleteClickCounter += 1
                if (fabDeleteClickCounter > 1) {
                    Toast.makeText(this, "Удерживайте, чтобы удалить", Toast.LENGTH_SHORT).show()
                    fabDeleteClickCounter = 0
                }
            }
            fabOk -> {
                if (selectedActionsAdapter.getItems().isNotEmpty()) {
                    val intent = Intent()
                    var tagName = etName.text.toString()
                    if(tagName.isEmpty()) tagName = "New Tag"
                    intent.putExtra("tagId", tagId)
                    intent.putExtra("tagName",tagName)
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
    override fun onLongClick(v: View?): Boolean {
        when(v){
            fabDelete -> {
                selectedActionsAdapter.clear()
                fabDeleteClickCounter = 0
            }
        }
        return true
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
            }

        }

    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        touchHelper.startDrag(viewHolder)
    }
    private fun newActionDetected(action: Action){

        when(action.actionType){
            ActionTypes.SITE -> {
                openSiteEditTextDialog(action)
            }
            ActionTypes.APPLICATION -> {
                openApplicationEditTextDialog(action)
            }
            ActionTypes.DELAY -> {
                openTimerEditTextDialog(action)
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
    private fun openTimerEditTextDialog(action: Action){
        val taskEditText = EditText(this)
        taskEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Введите сколько нужно подождать")
            .setMessage("Введите время в секундах")
            .setView(taskEditText)
            .setPositiveButton("Ок") { dialog, which ->
                val inputTime =  taskEditText.text.toString()
                if(inputTime.isDigitsOnly())  {
                    action.specialData =inputTime
                    selectedActionsAdapter.addAction(action)
                }
                else {
                    Toast.makeText(this, "Время введено неверно", Toast.LENGTH_SHORT).show()
                    openTimerEditTextDialog(action)
                }

            }
            .setNegativeButton("Отмена", null)
            .create()
        dialog.show()
    }

    override fun onItemDeleted(currentItemsSize: Int) {
        if(currentItemsSize==0) {
            llNotEmpty.visibility= View.GONE
            fabDelete.visibility = View.GONE
            llIfActionsNotSelected.visibility = View.VISIBLE
        }
    }

    override fun onItemAdded() {
        llNotEmpty.visibility= View.VISIBLE
        fabDelete.visibility = View.VISIBLE
        llIfActionsNotSelected.visibility = View.GONE
    }



}
