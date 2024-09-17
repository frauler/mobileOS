package com.example.lab3
import Currency
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import fetchCurrencyRates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val currentValuteSpinner: Spinner = findViewById(R.id.current_valute_spinner)
        val convertedValuteSpinner: Spinner = findViewById(R.id.converted_valute_spinner)
        val currentValuteEnter: EditText = findViewById(R.id.current_valute_enter)
        val swapButton: ImageButton = findViewById(R.id.imageButton)

        currentValuteSpinner.setSelection(0)
        convertedValuteSpinner.setSelection(1)

        swapButton.setOnClickListener() {
            val selectedItem1 = currentValuteSpinner.selectedItemPosition
            val selectedItem2 = convertedValuteSpinner.selectedItemPosition
            currentValuteSpinner.setSelection(selectedItem2)
            convertedValuteSpinner.setSelection(selectedItem1)
        }

        currentValuteEnter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                CoroutineScope(Dispatchers.Main).launch {
                    onChange()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                CoroutineScope(Dispatchers.Main).launch {
                    onChange()
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                CoroutineScope(Dispatchers.Main).launch {
                    onChange()
                }
            }
        })

        currentValuteSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                CoroutineScope(Dispatchers.Main).launch {
                    onChange()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Этот метод вызывается, если ничего не выбрано (редко используется)
            }
        }

        convertedValuteSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                CoroutineScope(Dispatchers.Main).launch {
                    onChange()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Этот метод вызывается, если ничего не выбрано (редко используется)
            }
        }
    }

    suspend fun onChange() {
        val currentValuteEnter: EditText = findViewById(R.id.current_valute_enter)
        val convertedValuteEnter: EditText = findViewById(R.id.converted_valute_enter)
        val currentValuteSpinner: Spinner = findViewById(R.id.current_valute_spinner)
        val convertedValuteSpinner: Spinner = findViewById(R.id.converted_valute_spinner)
        // Вызывается во время изменения текста
        val currentCode = currentValuteSpinner.selectedItem.toString()
        val convertCode = convertedValuteSpinner.selectedItem.toString()
        val amountText = currentValuteEnter.text.toString()
        if (amountText.isNotBlank()) {
            CoroutineScope(Dispatchers.Main).launch {
                val result = String.format("%.3f", convertValute(currentCode, convertCode, amountText.toDouble()))
                if (result == "NaN") {
                    convertedValuteEnter.setText("No Internet")
                } else {
                    convertedValuteEnter.setText(result)
                }
            }
        } else {
            convertedValuteEnter.setText("")
        }
    }

    private fun findCurrencyByCode(currencies: List<Currency>, code: Any): Currency? {
        return currencies.find { it.code == code }
    }

    private suspend fun convertValute(currentCode: String, convertCode: String, amount: Double): Double {
        var result: Double = 0.0

        // Переключаем выполнение на фоновый поток для сетевого запроса
        withContext(Dispatchers.IO) {
            val url = "https://www.cbr-xml-daily.ru/daily_eng.xml"
            val currencies = fetchCurrencyRates(url)

            val currencyFrom = findCurrencyByCode(currencies, currentCode)
            val currencyTo = findCurrencyByCode(currencies, convertCode)

            // Переключаемся на главный поток для обработки данных и взаимодействия с UI
            withContext(Dispatchers.Main) {
                var rate1: Double = Double.NaN
                var rate2: Double = Double.NaN
                if (currencyFrom != null) {
                    rate1 = "${currencyFrom.rate}".toDouble()
                }
                if (currencyTo != null) {
                    rate2 = "${currencyTo.rate}".toDouble()
                }

                result = if (currentCode == "RUB" && convertCode == "RUB") {
                    amount
                } else if (currentCode == "RUB") {
                    amount / rate2
                } else if (convertCode == "RUB") {
                    amount * rate1
                } else {
                    (amount * rate1) / rate2
                }
            }
        }
        return result
    }
}