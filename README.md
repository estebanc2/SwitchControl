
![alt_text](images/image1.png "image_tooltip")
 # WiFi Switch


## Summary

This repository shows how to build a switch operated from the Internet.

This is an open-source software and hardware project. The schematics and board layout design, the programs in C for the ESP 8266 microcontroller, the program in Kotlin for the Android App, and the program in SwiftUI for the IOS App. All this is under the GPLv3 license schema. See the file LICENSE.txt in the same directory along with this file.


## Motivation

The project was born because of the need to manage my home heating. I need a schedule if the temperature is lower than N° and also to add the thermostat functionality. But soon I was using it to manage my garden irrigation, the lights from my home, the garage gateway, etc.


## Hardware

The hardware design is what the Espressif 8266 module needs to operate, that is 3.3V regulator, uno or two 6A relays isolated for 220VAC, the smallest possible power source from 220Vac to 5Vdc, 200mA, an easy way to transfer sw to the microcontroller.


![alt_text](images/image2.png "image_tooltip")


Schematic circuit	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;board, top layer


![alt_text](images/image3.png "image_tooltip")


assembly&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cables and power supply added &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;			in the case


## Software

Development is around the MQTT protocol to send orders from the mobile phone to the switch connected to a Wifi network, the name, operating mode, timers, and thresholds are also configured. Additionally, from the mobile app with the mobile connected to the Wi-Fi network to which the switch will connect, the password is sent using the esp-touch protocol developed by Espressif.


### MQTT dialog


![alt_text](images/image4.png "image_tooltip")


MQTT topics to the switch “/mtc/to_sw/8266MACaddress/”, from the switch:  “/mtc/from_sw/8266MACaddress/”, the MQTT message is a JSON string with always the same information structure: 

{"name":"velador","mode":0,"secs":0,"state":"on","prgs":[{"days":0,"start":0,"stop":0},{"days":0,"start":0,"stop":0},{"days":0,"start":0,"stop":0},{"days":0,"start":0,"stop":0}],"tempX10":0}

where:
* name: string up to 32 char								
* mode:	
1. timer		
2. timer/cont		
3. pulse na		
4. pulse nc		
5. timer/temp		
6. temp		
* secs: seconds as auxiliary info for pulse mode				
* tempX10: temperature x 10 (only double switch)			
* days:	bit 0 = Sanday, bit 1 = Monday ... bit 6 = Saturday			
* start:	minutes from 0 hs to sw on			
* stop:	minutes from 0 hs to sw off		
* state:	
1. off		
2. on	
3. get_data	
4. set_data	
5. erase	
6. upgrade	
7. server fails	
8. upgrade fails	
9. upgraded	


### Program in C

The C program for the microcontroller has the following functions:



1. MQTT dialog with the app according to the operation scheme above.
2. Receive configuration data from the app: name, mode, and timers to save them in non-volatile memory to share with other users.
3. Manage esp-touch protocol for the initial connection to a Wi-Fi network and when the network parameters change.
4. Manage the onboard LED of the 8266 board to report the hardware status. See the specifications, below.
5. Manage firmware updates (versions of this C program), through Espressif “over the air” OTA mechanism.


### Firmware upgrade procedure



1. From the ESP8266-RTOS-SDK platform generate a new firmware file:

        make ota

2. The new file will be in the build directory. Rename it from “switch_control_c.ota.bin” to “switch_control.ota”:

        cd build


        rm switch_control.ota


        mv switch_control_c.ota.bin switch_control.ota

3. Turn on an HTTP server in the above directory listening in XX port:

        python -m http.server XX

4. In the Android or IOS app go to config/maintenance/firmware upgrade. From there, load the server address (IP address of the development computer), and XX port.

If the procedure succeeds the switch will turn on with the new firmware and a toast in the app shows “success”, if not, the toast gives some troubleshooting information.


### Android App

This is the switch management app, i.e. switch (turn on and off), manage connection with a wifi network to aggregate new switches to the app, use mode, timers settings, switch name, background color, and position in the screen, firmware upgrade, erase switches from the app and leave them to factory state. Also from this app, you can copy configured switches from other users.

The app was developed in Kotlin using the MVVM architectural design pattern, Jetpack Compose to design the screens, coroutines, and Dagger Hilt dependency injection.


### Android App upload to Google Play procedure

To upgrade the Android market version do the following:



1. In Studio Project, clone the current anemometer project from Bitbucket. 
2. Upgrade the project and test all new features locally
3. In the file “build. gradle (: app)”, increase the version code and version name by one. Google requires this to upload an upgrade
4. Upgrade the below version history table
5. Generate a new Signed Bundle: Build / Android AppBundle / Next
6. Module: anemometer.app, Key store path: C:\repos\private_key.pepk, Key store password: xxx, Key alias: xxx, Key password: xxx, mark: Export encrypted….., Next
7. Build Variants: release, Finish
8. Open the Google Console, e-lanita/Production/launch the new version
9. Upload the file app-release.aab from C:\repos\anemometer\app\release

It will take about 2 days to be available in Google Play.


<table>
  <tr>
   <td><strong>version</strong>
   </td>
   <td><strong>date</strong>
   </td>
   <td><strong>modification</strong>
   </td>
   <td><strong>commit</strong>
   </td>
  </tr>
  <tr>
   <td>4(1,4)
   </td>
   <td><p style="text-align: right">
10/07/2021</p>

   </td>
   <td>colors
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>5(1.5)
   </td>
   <td><p style="text-align: right">
13/07/2021</p>

   </td>
   <td>icon
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>6(1.6)
   </td>
   <td><p style="text-align: right">
5/09/2021</p>

   </td>
   <td>se agregaron modos pulse y reaccion a tecla
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>7(1.7)
   </td>
   <td><p style="text-align: right">
22/09/2021</p>

   </td>
   <td>varias mejoras de performance, me pase a linode
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>8(1.8)
   </td>
   <td><p style="text-align: right">
27/09/2021</p>

   </td>
   <td>se colgaba al iniciar
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>9(1.9)
   </td>
   <td><p style="text-align: right">
27/08/2022</p>

   </td>
   <td>se puede pasar toda la config a otro telefono. Reconoce sw simples
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>10(1.10)
   </td>
   <td><p style="text-align: right">
6/11/2022</p>

   </td>
   <td>primera version con ESP_Touch
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>11(1.11)
   </td>
   <td><p style="text-align: right">
18/11/2022</p>

   </td>
   <td>OTA y switches dobles
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>12(1.12)
   </td>
   <td><p style="text-align: right">
25/11/2022</p>

   </td>
   <td>clean up config multiple
   </td>
   <td>65100d6
   </td>
  </tr>
  <tr>
   <td>13(1,13)
   </td>
   <td><p style="text-align: right">
7/12/2022</p>

   </td>
   <td>niveles de config. Help, timers de + de 24h
   </td>
   <td>f004291
   </td>
  </tr>
  <tr>
   <td>14(1.14)
   </td>
   <td><p style="text-align: right">
31/08/2023</p>

   </td>
   <td>AP1 34, PORQUE LO PIDE GOOGLE
   </td>
   <td>20eec408
   </td>
  </tr>
  <tr>
   <td>15(1.15)
   </td>
   <td><p style="text-align: right">
12/05/2024</p>

   </td>
   <td>Kotlin + JetPackCompose
   </td>
   <td>d29f0ae
   </td>
  </tr>
  <tr>
   <td>16(1.16)
   </td>
   <td><p style="text-align: right">
9/06/2024</p>

   </td>
   <td>MVVM, Dagger-Hilt
   </td>
   <td>dd1f5813
   </td>
  </tr>
  <tr>
   <td>
   </td>
   <td>
   </td>
   <td>
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>
   </td>
   <td>
   </td>
   <td>
   </td>
   <td>
   </td>
  </tr>
</table>



### IOS App

The iPhone app has the same facilities as the Android app except for esp-touch integration. You should use the esp-touch app available in AppleStore for free. The issue is that Espressif gives esp-touch libraries for Objective-C but not for SwifttUI. I have a workaround to “translate “ them. The app was developed in SwiftUI 5, using MVVM architectural design patterns and the declarative programming advantages proposed by Apple.


# Final user manual

You turn on and off (close and open) a switch from your mobile. You can program schedules, days of the week, and an external contact (NO), such as a rain detector. It has a pulse mode (NO or NC from 1 sec. to 10 min.) for, for example: opening gates and triggering sirens. It comes configured at the factory to work when it detects changes in the contact input, so it can be connected to an existing light key and controlled manually.

The initial configuration (association to a Wi-Fi network), programming (modes/timers), and operation (on/off) are done with the Android App “Wifi Switches” available on Google Play. From this app, you can operate all the switches you want and each switch can be operated by several users who have the application installed and the switch added.

You can turn a switch on and off by touching its icon. The icon shows the real status of each switch, the switches programmed as pulse have an hourglass and if a device is not available due to lack of connectivity, it will show it with a “?”, figure 4


![alt_text](images/image5.png "image_tooltip")



## Configuration

a) To operate from your mobile: in the initial screen or menu choose: add switch. The new switch is configured with your mobile phone connected to the Wi-Fi network to which you will associate it. The App will show you the network (SSID) and you must write the password, figure 1. Then if the password is correct, the switch is accessible and not previously configured, and you will go to the configuration screen, where you will have to define: the name, operation mode, response time if pulse mode, background color, and relative position. In the example, figure 3, the name is “alargue” and the mode is “timers”.

b) Add an existing switch to your mobile: The add switch with ID option on the home screen or menu will ask you for a valid ID that can be given to you by whoever configured the switch following the previous point. The ID of that switch is seen below the name on the configuration screen, figure 3.

c) Replicate all the switches from one mobile to another: The receive configuration option on the initial screen or menu will show you an ID to copy to the mobile on which the switches are already configured, in the send configuration menu option. If both mobile phones have connectivity, they will be identically configured. Subsequent modifications to one mobile phone do not affect the other.

You can add or delete switches, and modify the name, operating mode, background color, relative position, and the days and hours of operation of each switch, by going to the menu options. If the configured Wi-Fi network is unreachable, you can rescue it by following the steps in a).


## Installation

To turn on and off outlets or lights, the Wi-Fi switch can be installed inside the existing standard 100 x 50 mm box. Below are the respective diagrams.

![alt_text](images/image6.png "image_tooltip")



## Specifications

Size 33 x 32 x 21 mm

Input voltage 100 to 230 Vac

Maximum load 6A

Wifi 802.11 b/g/n

Blue led status indication. Blink as per the table where 1 is on for 100 msec and O off.
![alt_text](images/image7.png "image_tooltip")


## Futures releases



1. Replace the current switching power supply from 220Vac to 5Vdc for something smoller, chip, and integrated.
2. Replace the relays for a solid-state switch.
3. Compatibility with Alexa and Google Home.
4. Integrate esp-touch to the IOS app.