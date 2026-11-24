
package com.example.usbmodemmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.usbmodemmonitor.ui.theme.USBModemMonitorTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            USBModemMonitorTheme {
                MonitorScreen()
            }
        }
    }
}

@Composable
fun MonitorScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "USB Modem Monitor",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Состояние подключения",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Отключено",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Download",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "0.00 Мбит/с",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Upload",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "0.00 Мбит/с",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(40.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(20.dp))

            Text("Получено: 0 МБ")
            Text("Отправлено: 0 МБ")
        }
    }
}
