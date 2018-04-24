import threading
import datetime
import numpy as np
import uuid
import os

from sensors import mic_sensor

__all__ = ['watch_for_events']


def watch_for_events(mic, mic_model_dir, bottle_events_buffer, save_dir=None):
    stop_event = threading.Event()
    events_buffer_lock = threading.Lock()

    def _watch_for_events():
        classifier = mic_sensor.MicSamplesClassifier(mic.rate)
        classifier.load_model(mic_model_dir)

        while not stop_event.is_set():
            record_time = datetime.datetime.now()
            frames = mic.record_window()
            if frames is None:
                print("Can't read frames from mic (did you called .init() method?)")
                return
            if save_dir is not None:
                os.makedirs(save_dir, exist_ok=True)
                with open(os.path.join(save_dir, 'timespan_' + str(record_time).replace(':', '_') + '___' + str(uuid.uuid4()) + '.npz'), mode='wb') as file:
                    np.save(file, frames)
            is_bottle = classifier.predict(np.asarray([frames]))[0]
            if is_bottle:
                with events_buffer_lock:
                    bottle_events_buffer.append(record_time)

    thread = threading.Thread(target=_watch_for_events)
    thread.start()
    return thread, stop_event, events_buffer_lock
