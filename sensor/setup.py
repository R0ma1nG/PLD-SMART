from setuptools import setup

setup(
    name="Recycl'Lyon - Sensor module",
    version="0.1.0",
    author='H4413',
    description='RaspberryPi-based sensor module',
    install_requires=[
        "requests>=2.10.0,<3.0.0",
        "numpy>=1.14.0,<2.0.0",
        "pandas>=0.22.0,<1.0.0",
        "scikit-learn>=0.19.1,<1.0.0"
    ]
)
