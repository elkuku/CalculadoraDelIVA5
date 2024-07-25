package org.elkuku.calculadordeliva5

import android.icu.text.NumberFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.elkuku.calculadordeliva5.ui.theme.CalculadorDelIVA5Theme
import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadorDelIVA5Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Layout(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Layout(modifier: Modifier = Modifier) {
    var amountInput by remember { mutableStateOf("") }

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val withoutTax = calculateWithoutTax(amount)
    val tax = calculateTax(amount)

    val sdf = SimpleDateFormat("dd-MM-yyyy")
    val date = sdf.format(Date())

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calculador_del_iva),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        Text(
            text = date,
            style = MaterialTheme.typography.displaySmall,
            color = Color.Blue,
            modifier = Modifier
                .align(alignment = Alignment.Start)
        )
        Text(
            text = stringResource(R.string.without_tax, withoutTax),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier
                .align(alignment = Alignment.End)
        )
        Text(
            text = stringResource(R.string.tax_value, 12, tax),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier
                .align(alignment = Alignment.End)
        )
        EditNumberField(
            value = amountInput,
            onValueChange = { amountInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        Button(onClick = { amountInput = "" },
            modifier = Modifier.align(alignment = Alignment.End)
        ) {
            Text(text = "Borrar")

        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CalculadorDelIVA5Theme {
        Layout()
    }
}

@Composable
fun EditNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        label = { Text("Total") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier,
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End, fontSize = 38.sp)
    )
}

private fun calculateWithoutTax(amount: Double, taxPercent: Double = 12.0): String {
    val tax = 1 + taxPercent / 100

    val withoutTax = amount / tax
    return NumberFormat.getCurrencyInstance().format(withoutTax)
}

private fun calculateTax(amount: Double, taxPercent: Double = 12.0): String {
    val taxValue = 1 + taxPercent / 100

    val withoutTax = amount / taxValue

    val tax = amount - withoutTax
    return NumberFormat.getCurrencyInstance().format(tax)
}