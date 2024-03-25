package com.example.arduinocontroller

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.arduinocontroller.ui.theme.ArduinoControllerTheme
import java.io.DataOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var bluetoothPermissionsGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        if (!bluetoothPermissionsGranted) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT),
                1)

            bluetoothPermissionsGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        }

        /*ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT),
            1)*/

        val UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        println("checkpoint1")

        val hc05 = bluetoothAdapter.getRemoteDevice("00:0E:EA:CF:7C:37")
        val bluetoothSocket = hc05.createRfcommSocketToServiceRecord(UUID)
        try {
            bluetoothSocket.connect()
        } catch (e: IOException) {
            Log.e("Bluetooth", "IOException")
        }

        println(bluetoothSocket.isConnected)
        val outStream = bluetoothSocket.outputStream

            setContent {
                ArduinoControllerTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                        Greeting("There")

                        Column (horizontalAlignment = Alignment.CenterHorizontally) {
                            Row (verticalAlignment = Alignment.CenterVertically) {
                                FloatingActionButton(onClick = {
                                    //println(bluetoothAdapter.bondedDevices)
                                    //println(bluetoothSocket.isConnected)
                                    sendData("7", outStream);
                                }, modifier = Modifier.width(400.dp)) {
                                    Text("Lift")
                                }

                                FloatingActionButton(onClick = {
                                    //println(bluetoothAdapter.bondedDevices)
                                    //println(bluetoothSocket.isConnected)
                                    sendData("1", outStream);
                                }, modifier = Modifier.width(400.dp)) {
                                    Text("Forward")
                                }

                                FloatingActionButton(onClick = {
                                    //println(bluetoothAdapter.bondedDevices)
                                    //println(bluetoothSocket.isConnected)
                                    sendData("8", outStream);
                                }, modifier = Modifier.width(400.dp)) {
                                    Text("Lower")
                                }

                            }

                            Row (verticalAlignment = Alignment.CenterVertically) {
                                FloatingActionButton(onClick = {
                                    sendData("3", outStream);
                                }, modifier = Modifier.width(400.dp)) {
                                    Text("Left")
                                }
                                FloatingActionButton(onClick = {
                                    sendData("5", outStream);
                                }, modifier = Modifier.width(400.dp)) {
                                    Text("Grab")
                                }
                                FloatingActionButton(onClick = {
                                    sendData("4", outStream);
                                }, modifier = Modifier.width(400.dp))  {
                                    Text("Right")
                                }
                            }

                            Row (verticalAlignment = Alignment.CenterVertically) {
                                FloatingActionButton(onClick = {
                                    sendData("6", outStream);
                                }, modifier = Modifier.width(400.dp)) {
                                    Text("Release")
                                }

                                FloatingActionButton(onClick = {
                                    sendData("2", outStream);
                                }, modifier = Modifier.width(400.dp)) {
                                    Text("Reverse")
                                }

                                FloatingActionButton(onClick = {
                                    try {
                                        bluetoothSocket.connect()
                                    } catch (e: IOException) {
                                        Log.e("Bluetooth", "IOException")
                                    }
                                }, modifier = Modifier.width(400.dp)) {
                                    Text("Connect")
                                }
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
fun sendData(data: String, outStream: OutputStream) {
    try {
        outStream.write(data.toByteArray())
    } catch (e: IOException) {
        Log.e("Bluetooth", "No Outstream")
    }
}