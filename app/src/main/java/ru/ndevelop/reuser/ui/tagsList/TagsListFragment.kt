package ru.ndevelop.reuser.ui.tagsList

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import io.sulek.ssml.SSMLLinearLayoutManager
import ru.ndevelop.reuser.R
import ru.ndevelop.reuser.adapters.ButtonType
import ru.ndevelop.reuser.adapters.TagListAdapter
import ru.ndevelop.reuser.adapters.onEditButtonClickListener
import ru.ndevelop.reuser.repositories.DataBaseHandler


class TagsListFragment : Fragment(), onEditButtonClickListener {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var tagListAdapter: TagListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tagListAdapter = TagListAdapter(this)
        tagListAdapter.loadItems(DataBaseHandler.getTagsList())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_taglist, container, false)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
        })
        initView(root)
        return root
    }

    fun initView(r: View) {
        recyclerView = r.findViewById(R.id.rv_taglist)
        with(recyclerView) {
            adapter = tagListAdapter
            layoutManager = SSMLLinearLayoutManager(requireContext())
        }

    }

    fun showEditTextDialog(tagId: String) {
        val taskEditText = EditText(requireContext());
        taskEditText.setText(DataBaseHandler.getTagName(tagId), TextView.BufferType.EDITABLE);
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Введите новое название")
            .setMessage("Как следует называть эту метку?")
            .setView(taskEditText)
            .setPositiveButton("Изменить") { dialog, which ->
                DataBaseHandler.updateTagName(tagId, taskEditText.text.toString())
                tagListAdapter.loadItems(DataBaseHandler.getTagsList())
                tagListAdapter.notifyDataSetChanged()
            }
            .setNegativeButton("Отмена", null)
            .create()
        dialog.show()
    }

    fun showDeleteConfirmationDialog(tagId: String) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Вы уверены, что хотите удалить эту метку?")
            .setMessage("Это действие нельзя отменить")
            .setPositiveButton("Удалить!") { dialog, which ->
                DataBaseHandler.deleteData(tagId)
                tagListAdapter.loadItems(DataBaseHandler.getTagsList())
                tagListAdapter.notifyDataSetChanged()
            }
            .setNegativeButton("Не надо", null)
            .create()
        dialog.show()
    }

    override fun onEditButtonClick(button: ButtonType, tagId: String) {
        when (button) {
            ButtonType.EDIT -> {
                showEditTextDialog(tagId)

            }
            ButtonType.DELETE -> {
                showDeleteConfirmationDialog(tagId)
            }
        }
    }

}