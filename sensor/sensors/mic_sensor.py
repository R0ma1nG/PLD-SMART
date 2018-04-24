import os
import struct
import pyaudio
import numpy as np
from sklearn.externals import joblib
from sklearn.linear_model import LogisticRegression

__all__ = ['MicSensor', 'MicSamplesClassifier']


class MicSensor:
    def __init__(self, chunk=1024, audio_format=pyaudio.paInt16, channels=2, rate=44100, timespan=1):
        self.chunk = chunk
        self.audio_format = audio_format
        self.channels = channels
        self.rate = rate
        self.timespan = timespan
        self._audio = None

    def __enter__(self):
        self.init()
        return self

    def __exit__(self):
        self.close()

    def init(self):
        self._audio = pyaudio.PyAudio()

    def close(self):
        if self._audio is not None:
            self._audio.terminate()
            self._audio = None
        else:
            print('Warning: Audio not initialized')

    def record_window(self):
        if self._audio is not None:
            stream = self._audio.open(format=self.audio_format, channels=self.channels, rate=self.rate, input=True, frames_per_buffer=self.chunk)
            chunk = [list(struct.iter_unpack('<h', stream.read(self.chunk))) for i in range(int(self.rate / self.chunk * self.timespan))]
            stream.stop_stream()
            stream.close()
            return np.reshape(chunk, [-1])
        else:
            print('Warning: Audio not initialized')


class MicSamplesClassifier:
    def __init__(self, sample_rate, model_name='MicClassifierModel.pkl', class_weights='balanced', low_band_filter_threshold=16000):
        self.sample_rate = sample_rate
        self.model_name = model_name
        self.class_weights = class_weights
        self.low_band_filter_threshold = low_band_filter_threshold
        self._logistic = None

    def fit(self, inputs, tragets, model_dir):
        inputs = self._preprocess(inputs)
        self._logistic = LogisticRegression(class_weight=self.class_weights)
        self._logistic.fit(inputs, tragets)
        os.makedirs(model_dir, exist_ok=True)
        joblib.dump(self._logistic, os.path.join(model_dir, self.model_name))

    def load_model(self, model_dir):
        self._logistic = joblib.load(os.path.join(model_dir, self.model_name))

    def predict(self, inputs):
        if self._logistic is not None:
            inputs = self._preprocess(inputs)
            labels = self._logistic.predict(inputs)
            return labels
        else:
            print('Warning: No trained model to predict with')

    def evaluate(self, inputs, targets):
        if self._logistic is not None:
            inputs = self._preprocess(inputs)
            labels = self._logistic.predict(inputs)
            acc = np.sum(np.equal(targets, labels)) / len(inputs)
            return acc
        else:
            print('Warning: No trained model to evaluate')

    def _preprocess(self, inputs):
        fft = np.fft.fft(inputs)
        freqs = np.fft.fftfreq(inputs.shape[-1], 1 / self.sample_rate)
        fft = np.array([fft for f, fft in zip(freqs, fft) if abs(f) < self.low_band_filter_threshold])
        freqs = [f for f in freqs if abs(f) < self.low_band_filter_threshold]
        # TODO: use pretrained audio embedding or umap/t-sne
        return np.reshape([fft.real, fft.imag], newshape=(inputs.shape[0], -1))  # , freqs
