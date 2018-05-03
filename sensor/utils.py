import threading
import time

__all__ = ['periodic_timer']


class periodic_timer:
    def __init__(self, func, period=1., args=[], kwargs={}):
        self.func = func
        self.period = period
        self._args = args
        self._kwargs = kwargs

    def stop(self):
        if self._timer is not None:
            self._timer.cancel()

    def start(self, count=-1):
        if count != 0:
            start = time.time()
            self.func(*self._args, **self._kwargs)
            delta_t = time.time() - start
            self._timer = threading.Timer(max(0, self.period - delta_t), self.start, args=[count - 1])
            self._timer.start()
