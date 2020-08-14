package com.hopto.dtanjp.audio;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
/**
 * Audio.java
 * 
 * Credit to:
 * https://github.com/jabo-bernardo/Kree-Java/blob/master/src/dev/jabo/kree/Audio.java
 * 
 * But since this project does not use files, but instead a cache, I had to rewrite to read byte[] data
 * @author David Tan
 **/
public class Audio {
	
	/** Audio byte[] data **/
	protected byte[] data;
	
	/** Audio clip instance **/
    protected Clip audioClip;
    
    /** Should this audio loop **/
    public boolean loop = false;
    
    /** The volume of this audio instance **/
    private float volume = 1.0f;
    
    /** Constructor **/
    public Audio(byte[] data) {
    	this.data = data;
    }
    
    /** Play audio **/
    public void play() {
        try {
        	audioClip = AudioSystem.getClip();
            audioClip.open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(data)));
            if (loop)
            	audioClip.loop(-1);
            setVolume(volume);
            audioClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    public float getVolume() {
        FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);  
        volume = (float) Math.pow(10f, gainControl.getValue() / 20f);
        return volume;
    }

    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f) throw new IllegalArgumentException("Invalid volume: "+volume);
        this.volume = volume;
        FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);        
        gainControl.setValue(20f * (float) Math.log10(volume));
    }
    
    /** Request the audio to stop **/
    public void stop() {
        audioClip.stop();
    }
    
    /** Is the audio playing **/
    public boolean isPlaying() {
    	if(audioClip == null) return false;
    	return audioClip.isRunning();
    }
}