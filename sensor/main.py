#!/home/pi/miniconda3/bin/python
import os
import time
import requests
import threading

#import level_listener
import bottle_listener
from sensors import optical_sensor
import utils

ID_CAPTEUR = "5add92e9e36c0adec0253805"
TOKEN_CAPTEUR = "348534593696437587487920546496919"
MIC_MODEL_DIR = './sensors/models/mic'
BACKEND_API_URL = "https://pld-smart.azurewebsites.net" # "localhost:3455"
FLUSH_TO_BACKEND_PERIOD = 0.1 
MIC_DATA_DIR = './sensors/data/mic/raw'

def _flush_events(events_buffer_lock, bottle_events, bottle_events_buffer):
    with events_buffer_lock:
        canReachServer = True  # TODO:...
        if canReachServer and len(bottle_events_buffer) > 0:
            print('Flush events to backend API')
            url = os.path.join(BACKEND_API_URL, 'api/depotsEnCours/ajoutDechet', ID_CAPTEUR)
            for e in bottle_events_buffer:
                resp = requests.put(url)
                if resp.status_code == 200:
                    bottle_events.extend(bottle_events_buffer)
                    #bottle_events_buffer.clear()
                else:
                    print('ERROR: request to backend API failed: status_code=' + str(resp.status_code))
            bottle_events_buffer.clear()
def main():
    bottle_events = []
    bottle_events_buffer = []

    # Initialize sensors
    ir = optical_sensor.IRSensor()
    ir.init()
    if True:
        # Start a thread listening to bottle thrown events
        thread, stop_event, events_buffer_lock = bottle_listener.watch_for_events(None, ir, MIC_MODEL_DIR, bottle_events_buffer, save_dir=MIC_DATA_DIR)

        # Setup a periodic job flushing bottle events buffer to backend server if available
        timer = utils.periodic_timer(_flush_events, period=FLUSH_TO_BACKEND_PERIOD, args=[events_buffer_lock, bottle_events, bottle_events_buffer])
        timer.start()

        # TODO: ... if we should stop
        # stop_event.set()
        # thread.join()


if __name__ == "__main__":
    main()
