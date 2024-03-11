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
import java.io.DataOutputStream
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT),
            1)
        val UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        val bluetoothPermissionsGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        if (bluetoothPermissionsGranted) {
            println(bluetoothAdapter.bondedDevices)
            val hc05 = bluetoothAdapter.getRemoteDevice("00:0E:EA:CF:7C:37")
            println(hc05.getName())
            val bluetoothSocket = hc05.createRfcommSocketToServiceRecord(UUID)
            bluetoothSocket.connect()
            println(bluetoothSocket.isConnected)
            val outStream = bluetoothSocket.outputStream
            val dataOutStream = DataOutputStream(outStream)

            //setContentView(R.layout.layout)
            setContent {
                ArduinoControllerTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                        Greeting("There")

                        Column {
                            Button(onClick = {
                                println(bluetoothAdapter.bondedDevices)
                                println(bluetoothSocket.isConnected)
                                /*TODO
                        Send signal for motion*/
                            }, modifier = Modifier.width(100.dp)) {
                                Text("Up")
                            }
                            Row {
                                Button(onClick = {
                                                 /*TODO
                    Send signal for motion*/
                                }, modifier = Modifier.width(100.dp)) {
                                    Text("Left")
                                }

                                Button(onClick = {
                                    outStream.write("1".toByteArray())
                                             /*TODO
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
                            Button(onClick = {
                                outStream.write("2".toByteArray())
                                             /*TODO
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
fun sendData(data: Int, dataoutstream: DataOutputStream) {
    dataoutstream.writeInt(data)
}