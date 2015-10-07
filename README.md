This is a simple python library that can send notification to Android Phone using Google Cloud Messaging (GCM).

### Usage:

1. Download and install pushNotification library 
    ```
      sudo python setup.py install
      ```
      OR
      
      ```  sudo pip install pushNotification ```
      
2. Download and install the [Android APK](https://github.com/mkalioby/Python_Notifications/blob/master/Applications/Android/Notifier.apk) to your phone.
3. Open Application and enter topics to listen on (seprated by ,) e.g warning,mkalioby.
	![Android Image](https://github.com/mkalioby/Python_Notifications/blob/master/Applications/Android/img.png)
4. in your python code, write 
```python
        import pushNotification
        pushNotification.push(MESSAGE,TOPIC)
```
The return is a JSON String.

Finally, check your phone.

###  Next Features:
1. Allow each to use this account and credentials.
2. Changing Ringtone.
3. iOS support. 
	
