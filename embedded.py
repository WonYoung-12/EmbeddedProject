import os
import socket
import RPi.GPIO as GPIO
import time
import sys

readPin = 20
isFirst = True

host = ''
port = 8080
BUFFER_SIZE = 1024

serverSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
serverSocket.bind((host, port))
print ("socket build success!!")

serverSocket.listen(10)
clientSocket, addr = serverSocket.accept()

# when connected to Android
print("Got a connection from %s" % str(addr))

def livestream():
    os.system("raspivid -o - -t 99999 -hf -w 640 -h 360 -fps 25 | cvlc -vvv stream:///dev/stdin --sout '#standard{access=http, mux=ts, dst=:3001}' :demux=h264")

# when button is clicked
def handler(channel):
    global isFirst
    global serverSocket
    global clientSocket

    if(isFirst == True):
        print("Make Logic!!")
        isFirst = False
        clientSocket.send('start CCTV')
        livestream()

GPIO.setmode(GPIO.BCM)
GPIO.setup(readPin, GPIO.IN, pull_up_down = GPIO.PUD_UP)
GPIO.add_event_detect(readPin, GPIO.RISING, callback = handler)
       
def destroy():
    global serverSocket
    global clientSocket

    GPIO.cleanup()
    serverSocket.close()
    clientSocket.close()

if __name__ == "__main__":
    try:
        while(True):
            time.sleep(0.1)
    except KeyboardInterrupt:
       destroy()
