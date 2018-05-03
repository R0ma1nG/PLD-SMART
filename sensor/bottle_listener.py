import threading
import datetime
import numpy as np
import uuid
import os
import RPi.GPIO as GPIO
import time

from sensors import mic_sensor

__all__ = ['watch_for_events']

BUTTON_PIN = 20
LED_PIN = 21


def watch_for_events(mic, ir, mic_model_dir, bottle_events_buffer, save_dir=None):
    stop_event = threading.Event()
    rec_event = threading.Event()
    events_buffer_lock = threading.Lock()

    #if save_dir is not None:
    #    _setup_rec_button(rec_event)

    def _watch_for_events():
        print('listening for bottles')
        #classifier = mic_sensor.MicSamplesClassifier(mic.rate)
        #classifier.load_model(mic_model_dir)

        detected = False
        while not stop_event.is_set():
            time.sleep(0.1)
            record_time = datetime.datetime.now()
            #frames = mic.record_window()
            #if frames is None:
            #    print("Can't read frames from mic (did you called .init() method?)")
            #    continue
            if save_dir is not None and rec_event.is_set():
                os.makedirs(save_dir, exist_ok=True)

            #    with open(os.path.join(save_dir, 'timespan_' + str(record_time).replace(':', '_') + '___' + str(uuid.uuid4()) + '.npz'), mode='wb') as file:
            #        np.save(file, frames)
            is_bottle = True # classifier.predict(np.asarray([frames]))[0]
            if is_bottle and ir.detects():
                if not detected:
                    detected = True
                    print('Bottle detected')
                    with events_buffer_lock:
                        bottle_events_buffer.append(record_time.isoformat())
            else:
                detected = False

    #if save_dir is not None:
    #    GPIO.output(LED_PIN, GPIO.LOW)
    #    GPIO.remove_event_dectect(BUTTON_PIN)

    thread = threading.Thread(target=_watch_for_events)
    thread.start()
    return thread, stop_event, events_buffer_lock


def _setup_rec_button(rec_event):
    def _button_callback(channel):
        if rec_event.is_set():
            GPIO.output(LED_PIN, GPIO.HIGH)
            rec_event.set()
        else:
            GPIO.output(LED_PIN, GPIO.LOW)
            rec_event.clear()

    GPIO.setmode(GPIO.BCM)
    GPIO.setup(BUTTON_PIN, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
    GPIO.add_event_detect(BUTTON_PIN, GPIO.RISING)
    GPIO.add_event_callback(BUTTON_PIN, _button_callback)
    GPIO.setup(LED_PIN, GPIO.OUT, initial=GPIO.LOW)
