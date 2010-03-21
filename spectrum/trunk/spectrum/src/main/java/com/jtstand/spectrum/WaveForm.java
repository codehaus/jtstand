package com.jtstand.spectrum;

public interface WaveForm {

    public int getRecords();

    public int getChannels();

    public long getLength();

    public double getRate(int record, int channel);

    public double[] read(int record, int channel, int len, long skip, double[] outputData);

    public void setScale(double[] scale);
}
