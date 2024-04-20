package com.capa1.switchcontrol.data.mqtt

import com.capa1.switchcontrol.data.Global.FROM_SW
import com.capa1.switchcontrol.data.Global.MQTT_HOST_AND_PORT
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
    UP, DOWN
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
                listener.notifyNewMessage(topic.split("/").last(), message.toString())
            }

            override fun connectionLost(cause: Throwable?) {
                listener.notifyMqttState(MqttState.DOWN)
            }

            override fun deliveryComplete(token: IMqttDeliveryToken) {
            }
        })
        val options = MqttConnectOptions()
        try {
            mqttClient.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    listener.notifyMqttState(MqttState.UP)

                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
    fun subscribe(id: String, qos: Int = 1) {
        try {
            mqttClient.subscribe(FROM_SW + id, qos, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun unsubscribe(id: String) {
        try {
            mqttClient.unsubscribe(FROM_SW + id, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                }
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
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
                }
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
    fun disconnect() {
        try {
            mqttClient.disconnect(null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                }
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
}