package com.example.arduinocontroller

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.arduinocontroller.ui.theme.ArduinoControllerTheme
import java.io.DataOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import kotlin.math.round
import kotlin.math.truncate

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
                var sliderPosition by remember { mutableStateOf(0f) }
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    //Greeting("There")

                    Column (modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally) {
                        Row (modifier = Modifier.fillMaxWidth(),
                             verticalAlignment = Alignment.CenterVertically,
                             horizontalArrangement = Arrangement.SpaceEvenly) {
                            FloatingActionButton(onClick = {
                                //println(bluetoothAdapter.bondedDevices)
                                //println(bluetoothSocket.isConnected)
                                sendData("7", outStream);
                            }, modifier = Modifier.fillMaxWidth(1/3f)) {
                                Text("Lift")
                            }

                            FloatingActionButton(onClick = {
                                //println(bluetoothAdapter.bondedDevices)
                                //println(bluetoothSocket.isConnected)
                                sendData("1", outStream);
                            }, modifier = Modifier.fillMaxWidth(1/2f), containerColor = Color.Green) {
                                Text("Forward")
                            }

                            FloatingActionButton(onClick = {
                                //println(bluetoothAdapter.bondedDevices)
                                //println(bluetoothSocket.isConnected)
                                sendData("8", outStream);
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text("Lower")
                            }

                        }

                        Row (verticalAlignment = Alignment.CenterVertically) {
                            FloatingActionButton(onClick = {
                                sendData("3", outStream);
                            }, modifier = Modifier.fillMaxWidth(1/3f), containerColor = Color.Green) {
                                Text("Left")
                            }
                            FloatingActionButton(onClick = {
                                sendData("10", outStream);
                            }, modifier = Modifier.fillMaxWidth(1/2f), containerColor = Color.Red) {
                                Text("Stop")
                            }
                            FloatingActionButton(onClick = {
                                sendData("4", outStream);
                            }, modifier = Modifier.fillMaxWidth(), containerColor = Color.Green)  {
                                Text("Right")
                            }
                        }

                        Row (verticalAlignment = Alignment.CenterVertically) {
                            FloatingActionButton(onClick = {
                                sendData("6", outStream);
                            }, modifier = Modifier.fillMaxWidth(1/3f)) {
                                Text("Release")
                            }

                            FloatingActionButton(onClick = {
                                sendData("2", outStream);
                            }, modifier = Modifier.fillMaxWidth(1/2f), containerColor = Color.Green) {
                                Text("Reverse")
                            }

                            FloatingActionButton(onClick = {
                                sendData("5", outStream);
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text("Grab")
                            }
                        }
                        FloatingActionButton(onClick = {
                            try {
                                bluetoothSocket.connect()
                            } catch (e: IOException) {
                                Log.e("Bluetooth", "IOException")
                            }
                        }, modifier = Modifier.fillMaxWidth().padding(10.dp,50.dp)) {
                            Text("Connect")
                        }
                        Slider(
                            value = sliderPosition,
                            valueRange = 0f..100f,
                            steps = 4,
                            onValueChange = { sliderPosition = it },
                            onValueChangeFinished = {sendData((100+sliderPosition).toInt().toString(), outStream)},
                        )
                        Text(text = "Speed: $sliderPosition (Fraction of Max)")
                        }
                    }
                }
            }
        }
    }

fun sendData(data: String, outStream: OutputStream) {
    try {
        outStream.write(data.toByteArray())
    } catch (e: IOException) {
        Log.e("Bluetooth", "No Outstream")
    }
}