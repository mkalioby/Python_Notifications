This is a simple python library that can send notification to Android Phone using Google Cloud Messaging (GCM).

### Get Sender ID and API Key 

Follow the wizard [here](https://developers.google.com/mobile/add?platform=android&cntapi=gcm&cntapp=Default%20Demo%20App&cntpkg=gcm.play.android.samples.com.gcmquickstart&cnturl=https:%2F%2Fdevelopers.google.com%2Fcloud-messaging%2Fandroid%2Fstart%3Fconfigured%3Dtrue&cntlbl=Continue%20Try%20Cloud%20Messaging)

1. Click 'Choose and Configure Services'.
2. Click to enable 'Cloud Messaging'
3. Add Server Key to ~/.pushNotificatio or /etc/pushNotifications after installing the library.
4. Put Sender ID in the Android applicatuion

### Usage:

1. Download and install pushNotification library 
    ```
      sudo python setup.py install
      ```
      OR
      
      ```  sudo pip install pushNotification ```
      
2. Download and install the [Android APK](https://github.com/mkalioby/Python_Notifications/blob/master/Applications/Android/Notifier.apk?raw=true) to your phone.
3. Open Application and enter topics to listen on (seprated by ,) e.g warning,mkalioby.
	![Android Image](https://github.com/mkalioby/Python_Notifications/blob/master/Applications/Android/img.png)
4. in your python code, write 
```python
        import pushNotification
        pushNotification.push(MESSAGE,TOPIC,CUSTOM_API_KEY="")
```
The return is a JSON String.

Finally, check your phone.

###  Next Features:
3. iOS support. 
	
