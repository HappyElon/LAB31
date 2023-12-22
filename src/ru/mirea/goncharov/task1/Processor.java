package ru.mirea.goncharov.task1;

class Processor {
    int key;
    String name;
    double clockFrequency;
    int cacheSize;
    double busFrequency;
    int specInt;
    int specFp;

    Processor(int key, String name, double clockFrequency, int cacheSize, double busFrequency, int specInt, int specFp) {
        this.key = key;
        this.name = name;
        this.clockFrequency = clockFrequency;
        this.cacheSize = cacheSize;
        this.busFrequency = busFrequency;
        this.specInt = specInt;
        this.specFp = specFp;
    }
}
