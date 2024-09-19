package com.example.lab4
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ContentView
import java.io.File

class CustomAdapter(var mCtx: Context, var resource: Int, var items:List<Model>)
    :ArrayAdapter<Model> (mCtx, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater :LayoutInflater = LayoutInflater.from(mCtx)

        val view : View = layoutInflater.inflate(resource , null )
        val imageView :ImageView = view.findViewById(R.id.iconIv)
        var textView : TextView = view.findViewById(R.id.titleIv)
        var textView1 : TextView = view.findViewById(R.id.descIv)
        var textView2 : TextView = view.findViewById(R.id.tagIv)
        var person : Model = items[position]

        textView.text = person.title
        textView1.text = person.desc
        textView2.text = person.tag

        val imgFile = File(person.photo)

        if (imgFile.exists()) {
            // Декодируем изображение в Bitmap
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

            // Устанавливаем Bitmap в ImageView
            imageView.setImageBitmap(myBitmap)
        }

        return view
    }
}