import Adafruit_DHT
import RPi.GPIO as GPIO
import time
from RPLCD.gpio import CharLCD

# DHT11 setup
DHT_SENSOR = Adafruit_DHT.DHT11
DHT_PIN = 4

# LED setup
LED_RED = 18
LED_GREEN = 23

# Motor (prin releu/tranzistor)
MOTOR_PIN = 12

# LCD setup
lcd = CharLCD(
    numbering_mode=GPIO.BCM,
    cols=16,
    rows=2,
    pin_rs=13,
    pin_e=26,
    pins_data=[16, 24, 5, 6]
)

# Setup GPIO
GPIO.setmode(GPIO.BCM)
GPIO.setup(LED_RED, GPIO.OUT)
GPIO.setup(LED_GREEN, GPIO.OUT)
GPIO.setup(MOTOR_PIN, GPIO.OUT, initial=GPIO.HIGH)

try:
    while True:
        humidity, temperature = Adafruit_DHT.read(DHT_SENSOR, DHT_PIN)

        lcd.clear()
        if humidity is not None and temperature is not None:
            print("Temperature = {0:0.1f}°C Humidity = {1:0.1f}%".format(temperature, humidity))

            lcd.cursor_pos = (0, 0)
            lcd.write_string("Temp: {0:0.1f}C".format(temperature))
            lcd.cursor_pos = (1, 0)

            # Control LED-uri si motor
            if temperature >= 26.0:
                GPIO.output(LED_GREEN, False)
                GPIO.output(LED_RED, True)
                GPIO.output(MOTOR_PIN, GPIO.HIGH)  #Porneste motorul

                lcd.write_string("MOTOR ON        ")
                print("Motor ON")

            else:
                GPIO.output(LED_RED, False)
                GPIO.output(LED_GREEN, True)
                GPIO.output(MOTOR_PIN, GPIO.LOW)  # Opreste motorul
                lcd.write_string("MOTOR OFF       ")
                print("Motor OFF")

        else:            
            print("Sensor failure. Check wiring!")
            lcd.clear()
            lcd.write_string("Sensor failure!")

            # Blink LEDs
            for _ in range(3):
                GPIO.output(LED_GREEN, True)
                GPIO.output(LED_RED, True)
                time.sleep(0.3)
                GPIO.output(LED_GREEN, False)
                GPIO.output(LED_RED, False)
                time.sleep(0.3)

        time.sleep(2)

except KeyboardInterrupt:
    lcd.clear()
    GPIO.cleanup()