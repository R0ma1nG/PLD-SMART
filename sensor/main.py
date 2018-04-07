#import api
import level_listener
import bottle_listener
from sensors import *


def init():
    # Initializa sensors

    # Setup a periodic job flushing log to backend server if available

    return logger, (camera_sensor, mic_sensor, optical_sensor)


def main():
    logger, sensors = init()
    level_listener.watch_for_events(logger, sensors)
    bottle_listener.watch_for_events(logger, sensors)
    # api.run()


if __name__ == "__main__":
    main()
