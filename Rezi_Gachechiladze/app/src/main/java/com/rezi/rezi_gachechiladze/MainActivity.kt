package com.rezi.rezi_gachechiladze

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF12131C) // Unique deep dark slate background
                ) {
                    StudentFormScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentFormScreen() {
    val context = LocalContext.current

    // Specific state names requested by the assignment guidelines
    var nameState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
    var dateState by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("") }
    var isAgreed by remember { mutableStateOf(false) }

    // Additional state for Surname
    var surnameState by remember { mutableStateOf("") }

    // Date picker control state
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Formatting selected date to DD/MM/YYYY
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis
                    if (selectedDate != null) {
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        dateState = formatter.format(Date(selectedDate))
                    }
                    showDatePicker = false
                }) {
                    Text("OK", color = Color(0xFFBB86FC))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Custom Header
        Text(
            text = "Student Form",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFBB86FC)
        )
        Text(
            text = "Fill in your details",
            fontSize = 14.sp,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Input Fields (Name, Surname, Email)
        CustomTextField(value = nameState, onValueChange = { nameState = it }, label = "Name")
        CustomTextField(value = surnameState, onValueChange = { surnameState = it }, label = "Surname")
        CustomTextField(value = emailState, onValueChange = { emailState = it }, label = "Email Address")

        // Read-only Text Field that opens the Date Picker Dialog
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true }
        ) {
            CustomTextField(
                value = dateState,
                onValueChange = {},
                label = "Select Date (DD/MM/YYYY)",
                enabled = false
            )
        }

        // Radio Buttons Card (Android, iOS, Web)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1F2F)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Select your Track:", color = Color.White, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))

                val tracks = listOf("Android", "iOS", "Web")
                tracks.forEach { track ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedOption = track }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = (selectedOption == track),
                            onClick = { selectedOption = track },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFBB86FC))
                        )
                        Text(text = track, color = Color.White, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        }

        // Terms and Conditions Switch Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1F2F), RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "ვეთანხმები წესებს და პირობებს",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = isAgreed,
                onCheckedChange = { isAgreed = it },
                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFBB86FC))
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Submit Button & Validation Checks
        Button(
            onClick = {
                val isFormInvalid = nameState.isBlank() ||
                        surnameState.isBlank() ||
                        emailState.isBlank() ||
                        dateState.isBlank() ||
                        selectedOption.isBlank() ||
                        !isAgreed

                if (isFormInvalid) {
                    Toast.makeText(context, "შეავსეთ ყველა ველი!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "მონაცემები გაიგზავნა!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBB86FC))
        ) {
            Text("Submit", color = Color(0xFF12131C), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CustomTextField(value: String, onValueChange: (String) -> Unit, label: String, enabled: Boolean = true) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        enabled = enabled,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFBB86FC),
            unfocusedBorderColor = Color(0xFF3A3B50),
            focusedLabelColor = Color(0xFFBB86FC),
            unfocusedLabelColor = Color.Gray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            disabledTextColor = Color.White,
            disabledBorderColor = Color(0xFF3A3B50),
            disabledLabelColor = Color.Gray
        )
    )
}