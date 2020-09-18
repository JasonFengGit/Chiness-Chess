import java.io.FileNotFoundException;

public class StartGame {
	public static void main(String[] args) throws FileNotFoundException {
		new MusicPlayer().play("media//bgm.wav");//play music
		new ChessFrame("Chess", new ChessGame());// open the game window
	}
}
