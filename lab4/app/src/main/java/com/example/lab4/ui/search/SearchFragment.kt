package com.example.lab4.ui.search

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.example.lab4.CustomAdapter
import com.example.lab4.Model
import com.example.lab4.R
import com.example.lab4.SharedPrefsControl
import org.json.JSONObject

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val searchButton: Button = view.findViewById(R.id.button_search)


        searchButton.setOnClickListener() {
            val textSearch: EditText = view.findViewById(R.id.edit_text_search)
            val toFind = textSearch.getText().toString()
            val imagesMap = getImageFiles(toFind)
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
            val listImages: ListView = view.findViewById(R.id.list_view)
            val adapter = CustomAdapter(requireContext(), R.layout.row, list)
            listImages.adapter = adapter
        }

        return view
    }

    private fun getImageFiles(toFind: String): MutableMap<String, String> {
        val fileNames = SharedPrefsControl(requireContext()).findKeyByTag(toFind)
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imagesDict: MutableMap<String, String> = HashMap()

        storageDir?.let { dir ->
            val files = dir.listFiles()
            files?.forEach { file ->
                if (file.isFile && fileNames.contains(file.name)) {
                    imagesDict[file.name] = file.toString()
                }
            }
        }
        return imagesDict
    }

}