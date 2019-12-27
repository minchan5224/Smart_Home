import RPi.GPIO as GPIO
import time
from servo import *

GPIO.setmode(GPIO.BCM)
GPIO.setup(4, GPIO.IN)
PREV_TIME = time.time()
CUR_TIME = time.time()

try:
    i = 0
    while True:
        CUR_TIME = time.time()
        if GPIO.input(4) == 1:
            i += 1
            if CUR_TIME - PREV_TIME > 5:
                if i > 20:
                    print('Warnnig')
                    move(int(5))
                else:
                    print('-')
                PREV_TIME = time.time()
                print("COUNT >> ", i)
                i = 0
            time.sleep(0.1)

finally:

    GPIO.cleanup()