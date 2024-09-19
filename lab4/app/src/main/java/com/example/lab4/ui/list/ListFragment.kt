package com.example.lab4.ui.list

import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.lab4.CustomAdapter
import com.example.lab4.Model
import com.example.lab4.R
import com.example.lab4.SharedPrefsControl

class ListFragment : Fragment() {

    companion object {
        fun newInstance() = ListFragment()
    }

    private val viewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val listImages: ListView = view.findViewById(R.id.list_view)

        val imagesMap = getImageFiles()
        val imagesNames: List<String> = imagesMap.keys.toList()

        val list = mutableListOf<Model>()

        imagesNames.forEach { item ->
            val metadata = SharedPrefsControl(requireContext()).readMetaData(item)
            metadata?.let {
                val title = it["title"] as String
                var description = it["description"] as String
                var tag = it["tag"] as String

                if (description == "") {
                    description = "-"
                }
                if (tag == "") {
                    tag = "-"
                }
                list.add(Model(title, "DESC: $description", "TAG: $tag", imagesMap[item]))
            }
        }

        val adapter = CustomAdapter(requireContext(), R.layout.row, list)
        listImages.adapter = adapter

        listImages.setOnItemClickListener { _, view, position, _ ->
            val selectedItem = adapter.getItem(position)
            selectedItem?.let {
                val bundle = Bundle().apply {
                    putString("keyItem", it.photo?.takeLast(44))
                }
                findNavController().navigate(R.id.navigation_edit, bundle)
            }
        }
        return view
    }

    private fun getImageFiles(): MutableMap<String, String> {
        // get files names and paths
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imagesDict: MutableMap<String, String> = HashMap()
        storageDir?.let { dir ->
            val files = dir.listFiles()
            files?.forEach { file ->
                if (file.isFile && (file.name.endsWith(".jpg") || file.name.endsWith(".png"))) {
                    imagesDict[file.name] = file.toString()
                }
            }
        }
        return imagesDict
    }
}