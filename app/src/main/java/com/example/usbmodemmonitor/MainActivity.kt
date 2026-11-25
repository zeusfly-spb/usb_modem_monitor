
package com.example.usbmodemmonitor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.usbmodemmonitor.ui.theme.USBModemMonitorTheme
import android.net.TrafficStats
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "USBMonitor"
        private const val USB_STATE =
            "android.hardware.usb.action.USB_STATE"
    }

    private var usbConnected by mutableStateOf(false)
    private var usbConfigured by mutableStateOf(false)
    private var rndisActive by mutableStateOf(false)

    private var receiverRegistered = false

    private val usbReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            usbConnected = intent.getBooleanExtra("connected", false)
            usbConfigured = intent.getBooleanExtra("configured", false)
            rndisActive = intent.getBooleanExtra("rndis", false)

            Log.i(
                TAG,
                "USB: connected=$usbConnected, " +
                        "configured=$usbConfigured, rndis=$rndisActive"
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        registerUsbReceiver()

        setContent {
            USBModemMonitorTheme {
                MonitorScreen(
                    connected = usbConnected,
                    configured = usbConfigured,
                    rndis = rndisActive
                )
            }
        }
    }

    private fun registerUsbReceiver() {
        val filter = IntentFilter(USB_STATE)

        val stickyIntent = if (Build.VERSION.SDK_INT >= 33) {
            registerReceiver(
                usbReceiver,
                filter,
                Context.RECEIVER_EXPORTED
            )
        } else {
            @Suppress("DEPRECATION")
            registerReceiver(usbReceiver, filter)
        }

        receiverRegistered = true

        Log.i(TAG, "USB_STATE receiver registered")

        stickyIntent?.let {
            usbReceiver.onReceive(this, it)
        } ?: Log.i(TAG, "No sticky USB_STATE broadcast")
    }

    override fun onDestroy() {
        if (receiverRegistered) {
            unregisterReceiver(usbReceiver)
            receiverRegistered = false
        }

        super.onDestroy()
    }
}

@Composable
fun MonitorScreen(
    connected: Boolean,
    configured: Boolean,
    rndis: Boolean
) {
    val status = when {
        !connected -> "USB отключён"
        !configured -> "USB подключён, настройка..."
        rndis -> "USB RNDIS активен"
        else -> "USB подключён, модем не обнаружен"
    }

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
                text = status,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text("Download", style = MaterialTheme.typography.titleMedium)
            Text(
                "0.00 Мбит/с",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Upload", style = MaterialTheme.typography.titleMedium)
            Text(
                "0.00 Мбит/с",
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
