import datetime
import numpy as np
import collections
import Adafruit_ADS1x15
import threading

import utils


class IRSensor:
    """
    IR Sensor made of two IR leds connected to raspberry PI throught an I²C ADC converter (ADS1115).
    Values are read in differential mode from ADC.
    """

    def __init__(self, gain=1., differential=0, buffer_size=64, buffer_update_period=0.001):
        """
        Comments from https://github.com/adafruit/Adafruit_Python_ADS1x15 :
            * Choose a gain of 1 for reading voltages from 0 to 4.09V.
            Or pick a different gain to change the range of voltages that are read:
            - 2/3 = +/-6.144V
            -   1 = +/-4.096V
            -   2 = +/-2.048V
            -   4 = +/-1.024V
            -   8 = +/-0.512V
            -  16 = +/-0.256V
            See table 3 in the ADS1015/ADS1115 datasheet for more info on gain.

            * By default, reads the difference between channel 0 and 1 (i.e. channel 0 minus channel 1).
            Note you can change the differential value to the following:
            - 0 = Channel 0 minus channel 1
            - 1 = Channel 0 minus channel 3
            - 2 = Channel 1 minus channel 3
            - 3 = Channel 2 minus channel 3
        """
        self.gain = gain
        self.lock = threading.Lock()
        self.differential = differential
        self._adc = Adafruit_ADS1x15.ADS1115()
        self._timestamp_buffer = collections.deque(buffer_size * [None], buffer_size)
        self._value_buffer = collections.deque(buffer_size * [None], buffer_size)
        self._timer = utils.periodic_timer(self._update_buffer, period=buffer_update_period)

    def __enter__(self):
        self.init()
        return self

    def __exit__(self, exc_type, exc_value, traceback):
        self.close()

    def init(self):
        self._adc.start_adc_difference(self.differential, self.gain)
        self._timer.start()

    def close(self):
        self._timer.stop()
        self._adc.stop_adc()

    def detects(self, threshold=-400.):
        with self.lock:
            none_vals = np.sum([v is None for v in self._timestamp_buffer])
            ts_buffer, v_buffer = np.asarray(self._timestamp_buffer)[none_vals:], np.asarray(self._value_buffer)[none_vals:]
            if len(ts_buffer) > 0:
                ts = [t.timestamp() for t in ts_buffer]
#                mean = np.trapz(v_buffer, x=(ts / np.ptp(ts))) / len(v_buffer)
                mean = np.sum(v_buffer) / len(v_buffer)
                print(mean)
                return mean > threshold
            return False

    def _update_buffer(self):
        with self.lock:
            self._timestamp_buffer.append(datetime.datetime.now())
            self._value_buffer.append(self._adc.get_last_result())
