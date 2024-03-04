package com.example.arduinocontroller

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.arduinocontroller.ui.theme.ArduinoControllerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN),
            1)
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        val bluetoothPermissionsGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        if (bluetoothPermissionsGranted) {
            println(bluetoothAdapter.bondedDevices)
            //val HC05 = bluetoothAdapter.getRemoteDevice("MACADDRESS")
            //println(HC05.getName())
        }
        //setContentView(R.layout.layout)
        setContent {
            ArduinoControllerTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")

                    Column {
                            Button(onClick = { println(bluetoothAdapter.bondedDevices)/*TODO
                        Send signal for motion*/
                            }, modifier = Modifier.width(100.dp)) {
                                Text("Up")
                            }
                        Row {
                            Button(onClick = { /*TODO
                    Send signal for motion*/
                            }, modifier = Modifier.width(100.dp)) {
                                Text("Left")
                            }

                            Button(onClick = { /*TODO
                    Send signal for grabbing*/
                            }, modifier = Modifier.width(100.dp)) {
                                Text("Grab")
                            }
                            Button(onClick = { /*TODO
                    Send signal for motion*/
                            }, modifier = Modifier.width(100.dp)) {
                                Text("Right")
                            }
                        }
                            Button(onClick = { /*TODO
                        Send signal for motion*/
                            }, modifier = Modifier.width(100.dp)) {
                                Text("Down")
                            }
                            Button(onClick = { /*TODO
                        Send signal for forward motion*/
                            }, modifier = Modifier.width(100.dp)) {
                                Text("Release")
                            }
                        }
                    }
                }
            }


        /*val bluetoothPermissions = arrayOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN
        )*/
        /*val bluetoothPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                val permissionsGranted = permissions.values.reduce { acc, isPermissionGranted ->
                    acc && isPermissionGranted
                }
            })*/


    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ArduinoControllerTheme {
        Greeting("Android")
    }
}

/*@Composable
fun showBluetoothPermissionRationale() {
    AlertDialog(
        onDismissRequest = {
            //Logic when dismiss happens
        },
        title = {
            Text("Permission Required")
        },
        text = {
            Text("Bluetooth Required for App Functionality")
        },
        confirmButton = {
            TextButton(onClick = {
                //Logic when user confirms to accept permissions
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                //Logic when user denies to accept permissions
            }) {
                Text("Deny")
            }
        })
}*/