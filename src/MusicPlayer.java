
import java.io.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

// a class that plays bgm
public class MusicPlayer {
	private static MusicPlayer soundManager = new MusicPlayer();
	private Clip backgroundMusic;
	
	public static MusicPlayer getSoundManager(){
		return soundManager;
	}
	
	
	public void play(String path){
		try {
			backgroundMusic = AudioSystem.getClip();
			backgroundMusic.open(AudioSystem.getAudioInputStream(new File(path).toURL()));
			backgroundMusic.loop(-1);// play the music in loop
		} catch (Exception e) {
		} 
	}
	

	
}
