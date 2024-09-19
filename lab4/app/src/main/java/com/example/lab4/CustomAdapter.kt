package com.example.lab4
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import java.io.File
import android.graphics.BitmapFactory

class CustomAdapter(var mCtx: Context, var resource: Int, var items: List<Model>)
    : ArrayAdapter<Model>(mCtx, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)

        // Проверяем convertView для повторного использования представления
        val view: View = convertView ?: layoutInflater.inflate(resource, null)

        val imageView: ImageView = view.findViewById(R.id.iconIv)
        val textView: TextView = view.findViewById(R.id.titleIv)
        val textView1: TextView = view.findViewById(R.id.descIv)
        val textView2: TextView = view.findViewById(R.id.tagIv)
        val person: Model = items[position]

        textView.text = person.title
        textView1.text = person.desc
        textView2.text = person.tag

        val imgFile = File(person.photo)

        if (imgFile.exists()) {
            // Используем BitmapFactory.Options для сжатия изображения
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true

            // Cчитываем размеры изображения
            BitmapFactory.decodeFile(imgFile.absolutePath, options)

            // Определяем размер изображения
            val reqWidth = 100
            val reqHeight = 100

            // Рассчитываем inSampleSize для уменьшения размера изображения
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

            // Декодируем изображение с уже рассчитанным inSampleSize
            options.inJustDecodeBounds = false
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath, options)

            // Устанавливаем уменьшенный Bitmap в ImageView
            imageView.setImageBitmap(myBitmap)
        }

        return view
    }

    // Метод для расчета inSampleSize
    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Увеличиваем inSampleSize в степени двойки, пока высота/ширина больше требуемых
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}
