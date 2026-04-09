package ug.ac.ndejje.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * MainActivity is the primary entry point for the Android application.
 * It inherits from ComponentActivity, which is the base class for activities using Jetpack Compose.
 */
class MainActivity : ComponentActivity() {
    
    /**
     * onCreate is the first lifecycle method called when the activity starts.
     * We use it to set the UI content using the setContent function.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // MaterialTheme applies the default Material Design 3 styling to the app.
            MaterialTheme {
                // Surface provides a background layer that adapts to the theme colors.
                Surface {
                    // Call our main screen Composable function.
                    MoMoCalcScreen()
                }
            }
        }
    }

    /**
     * A reusable UI component for entering the amount.
     * This follows the 'State Hoisting' pattern: the state is managed by the caller
     * and passed in as parameters.
     *
     * @param amount The current text to display in the input field.
     * @param onAmountChange Callback function triggered whenever the user types.
     * @param isError Boolean flag that turns on error styling if the input is invalid.
     */
    @Composable
    fun HoistedAmountInput(
        amount: String,
        onAmountChange: (String) -> Unit,
        isError: Boolean = false
    ) {
        Column {
            // TextField allows the user to input text.
            TextField(
                value = amount,
                onValueChange = onAmountChange,
                isError = isError,
                // The label shows a hint ("Enter Amount") from string resources.
                label = { Text(stringResource(R.string.enter_amount)) }
            )
            
            // If the input is invalid, display a red error message below the field.
            if (isError) {
                Text(
                    text = stringResource(R.string.error_numbers_only),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    /**
     * The main screen of the Mobile Money Fee Calculator.
     * This component manages the logic for validation and fee calculation.
     */
    @Composable
    fun MoMoCalcScreen() {
        // 'amountInput' holds the raw text entered by the user.
        // 'remember' ensures the state is preserved during UI updates (recompositions).
        var amountInput by remember { mutableStateOf("") }

        // Logic: Try to convert the input string into a number.
        val numericAmount = amountInput.toDoubleOrNull()
        
        // Validation: If the input isn't empty and isn't a valid number, mark it as an error.
        val isError = amountInput.isNotEmpty() && numericAmount == null
        
        // Calculation: We calculate a 3% fee based on the numeric input.
        val fee = (numericAmount ?: 0.0) * 0.03
        
        // Formatting: Convert the fee number into a readable currency string (e.g., "UGX 3,000").
        val formattedFee = "UGX %,.0f".format(fee)

        // Column arranges the UI elements vertically with 16dp of padding around the edges.
        Column(modifier = Modifier.padding(16.dp)) {
            // App Title using a headline style.
            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.headlineMedium
            )
            
            // Spacer adds vertical gap between the title and the input field.
            Spacer(modifier = Modifier.height(16.dp))

            // The input field component defined above.
            HoistedAmountInput(
                amount = amountInput,
                onAmountChange = { amountInput = it },
                isError = isError
            )
            
            // Spacer adds a gap between the input and the result text.
            Spacer(modifier = Modifier.height(12.dp))

            // Display the final calculated fee.
            Text(
                text = stringResource(R.string.fee_label, formattedFee),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    /**
     * Preview allows us to see how the UI looks in the Android Studio Design tab
     * without having to run the app on a real device.
     */
    @Preview(showBackground = true)
    @Composable
    fun MoMoCalcPreview() {
        MaterialTheme {
            MoMoCalcScreen()
        }
    }
}
