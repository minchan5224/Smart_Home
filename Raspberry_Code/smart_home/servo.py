#-*- coding:utf-8 -*-
import RPi.GPIO as GPIO
import time

def move(camera_value):
    GPIO.setmode(GPIO.BCM)
    GPIO.setwarnings(False)
    GPIO.setup(18, GPIO.OUT)
    p1 = GPIO.PWM(18, 50)
    p1.start(0)
    p1.ChangeDutyCycle((camera_value+1)*1.25)
    time.sleep(3)
    p1.stop()

    return camera_value

def move_g(gas_value):
    GPIO.setmode(GPIO.BCM)
    GPIO.setwarnings(False)
    GPIO.setup(23, GPIO.OUT)
    p2 = GPIO.PWM(23, 50)
    p2.start(0)
    p2.ChangeDutyCycle(gas_value)
    time.sleep(3)
    p2.stop()

