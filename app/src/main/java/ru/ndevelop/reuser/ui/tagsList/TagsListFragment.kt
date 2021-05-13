package ru.ndevelop.reuser.ui.tagsList

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import io.sulek.ssml.SSMLLinearLayoutManager
import ru.ndevelop.reuser.MainActivity
import ru.ndevelop.reuser.R
import ru.ndevelop.reuser.adapters.ButtonType
import ru.ndevelop.reuser.adapters.OnEditButtonClickListener
import ru.ndevelop.reuser.adapters.TagListAdapter
import ru.ndevelop.reuser.repositories.DataBaseHandler
import ru.ndevelop.reuser.ui.actionsList.ActionsSelectedActivity


class TagsListFragment : Fragment(), OnEditButtonClickListener {


    private lateinit var recyclerView: RecyclerView
    private lateinit var tagListAdapter: TagListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tagListAdapter = TagListAdapter(this)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_taglist, container, false)
        initView(root)
        return root
    }

    private fun initView(r: View) {
        recyclerView = r.findViewById(R.id.rv_taglist)
        with(recyclerView) {
            adapter = tagListAdapter
            layoutManager = SSMLLinearLayoutManager(requireContext())
        }

    }


    private fun showDeleteConfirmationDialog(tagId: String) {
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
                (requireActivity() as MainActivity).startActionsSelectionActivity(tagId)

            }
            ButtonType.DELETE -> {
                showDeleteConfirmationDialog(tagId)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        tagListAdapter.loadItems(DataBaseHandler.getTagsList())
    }



}