package com.example.lab4.ui.edit

import android.graphics.BitmapFactory
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.lab4.Model
import com.example.lab4.R
import com.example.lab4.SharedPrefsControl
import java.io.File

class EditFragment : Fragment() {

    companion object {
        fun newInstance() = EditFragment()
    }

    private val viewModel: EditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_edit, container, false)
        val key = arguments?.getString("keyItem")

        val enterTitle: EditText = view.findViewById(R.id.edit_text_title)
        val enterDescription: EditText = view.findViewById(R.id.edit_text_description)
        val enterTag: EditText = view.findViewById(R.id.edit_text_tag)
        val imageView: ImageView = view.findViewById(R.id.imageView)

        val metadata = key?.let { SharedPrefsControl(requireContext()).readMetaData(it) }
        metadata?.let {
            enterTitle.setText(it["title"] as String)
            enterDescription.setText(it["description"] as String)
            enterTag.setText(it["tag"] as String)
        }

        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imgFile = File("$storageDir/$key")

        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            imageView.setImageBitmap(myBitmap)
        }

        val confirmButton: Button = view.findViewById(R.id.button_confirm)

        confirmButton.setOnClickListener() {
            // save new name metadata
            val edit = SharedPrefsControl(requireContext())
            val titleNew = enterTitle.getText().toString()
            val descNew = enterDescription.getText().toString()
            val tagNew = enterTag.getText().toString()
            if (key != null) {
                edit.saveMetaData(key, titleNew, descNew, tagNew)
            }
            findNavController().navigateUp()
        }

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}