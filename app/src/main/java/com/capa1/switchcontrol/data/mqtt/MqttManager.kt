package com.capa1.switchcontrol.data.mqtt

import android.util.Log
import com.capa1.switchcontrol.data.Global.FROM_SW
import com.capa1.switchcontrol.data.Global.MQTT_HOST_AND_PORT
import com.capa1.switchcontrol.data.Global.TAG
import com.capa1.switchcontrol.data.Global.TO_SW
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.util.UUID
import javax.inject.Inject

enum class MqttState {
    INIT, DOWN, OK, DELIVERED, FAILURE
}
class MqttManager @Inject constructor(
    private val listener: MqttListener
) {
    private lateinit var mqttClient: MqttAsyncClient
    fun connect() {
        mqttClient = MqttAsyncClient(MQTT_HOST_AND_PORT,
            UUID.randomUUID().toString(),
            MemoryPersistence()
        )
        mqttClient.setCallback(object : MqttCallback {
            override fun messageArrived(topic: String, message: MqttMessage?) {
                val mac = topic.split("/").last()
                listener.notifyNewMessage(topic, message.toString())
                listener.notifyMqttState(MqttState.OK)
                Log.i(TAG, "Receive message: ${message.toString()} from topic: $topic")
            }

            override fun connectionLost(cause: Throwable?) {
                listener.notifyMqttState(MqttState.DOWN)
                Log.i(TAG, "Connection lost ${cause.toString()}")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken) {
                listener.notifyMqttState(MqttState.DELIVERED)
            }
        })
        val options = MqttConnectOptions()
        try {
            mqttClient.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    listener.notifyMqttState(MqttState.INIT)
                    Log.i(TAG, "Connection success")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.i(TAG, "Connection failure")
                    listener.notifyMqttState(MqttState.FAILURE)
                }
            })
        } catch (e: MqttException) {
            Log.i(TAG, "hemos caido al catch de la linea 63 del mqttManager")
            e.printStackTrace()
        }
    }
    fun subscribe(id: String, qos: Int = 1) {
        try {
            mqttClient.subscribe(FROM_SW + id, qos, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.i(TAG, "Subscribed to $id")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.i(TAG, "Failed to subscribe $id")
                }
            })
        } catch (e: MqttException) {
            Log.i(TAG, "hemos caido al catch de la linea 79 del mqttManager")
            e.printStackTrace()
        }
    }

    fun unsubscribe(topic: String) {
        try {
            mqttClient.unsubscribe(topic, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.i(TAG, "Unsubscribed to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.i(TAG, "Failed to unsubscribe $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
    fun publish(id: String, msg: String, qos: Int = 1, retained: Boolean = false) {
        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            message.qos = qos
            message.isRetained = retained
            mqttClient.publish(TO_SW + id, message, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.i(TAG, "$msg published to $id")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.i(TAG, "Failed to publish $msg to $id")
                }
            })
        } catch (e: MqttException) {
            Log.i(TAG, "hemos caido al catch de la linea 115 del mqttManager")
            e.printStackTrace()
        }
    }
    fun disconnect() {
        try {
            mqttClient.disconnect(null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.i(TAG, "Disconnected")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.i(TAG, "Failed to disconnect")
                }
            })
        } catch (e: MqttException) {
            Log.i(TAG, "hemos caido al catch de la linea 131 del mqttManager")
            e.printStackTrace()
        }
    }
}