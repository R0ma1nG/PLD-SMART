import os
import time
import requests
import threading

#import level_listener
import bottle_listener
from sensors import mic_sensor, optical_sensor

ID_CAPTEUR = 3485386495
TOKEN_CAPTEUR = 348534593696437587487920546496919
MIC_MODEL_DIR = './sensors/models/mic'
BACKEND_API_URL = "localhost:3455"
#BACKEND_API_URL = "https://pld-smart.azurewebsites.net"
FLUSH_TO_BACKEND_PERIOD = 30
MIC_DATA_DIR = './sensors/data/mic/raw'


def main():
    bottle_events = []
    bottle_events_buffer = []

    # Initialize sensors
    with mic_sensor.MicSensor() as mic, optical_sensor.IRSensor as ir:
        # Start a thread listening to bottle thrown events
        thread, stop_event, events_buffer_lock = bottle_listener.watch_for_events(mic, ir, MIC_MODEL_DIR, bottle_events_buffer, save_dir=MIC_DATA_DIR)

        # Setup a periodic job flushing bottle events buffer to backend server if available
        def _flush_events():
            with events_buffer_lock:
                canReachServer = True  # TODO:...
                if canReachServer:
                    url = os.path.join(BACKEND_API_URL, 'api/depotsEnCours/ajoutDechet', ID_CAPTEUR)
                    resp = requests.post(url, json={'tokenCapteur': TOKEN_CAPTEUR, 'timestamps': [event.isoformat() for event in bottle_events_buffer]})
                    if resp.status_code == 200:
                        bottle_events.extend(bottle_events_buffer)
                        bottle_events_buffer.clear()
                    else:
                        print('ERROR: request to backend API failed: status_code=' + resp.status_code)
            threading.Timer(FLUSH_TO_BACKEND_PERIOD, _flush_events).start()
        _flush_events()

        # TODO: ... if we should stop
        # stop_event.set()
        # thread.join()


if __name__ == "__main__":
    main()
