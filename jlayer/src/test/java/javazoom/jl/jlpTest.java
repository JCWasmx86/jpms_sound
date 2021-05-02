package javazoom.jl;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.jlp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class jlpTest {
	@Test
	void testPlay() {
		String[] args = new String[2];
		args[0] = "-url";
		args[1] = this.getClass().getClassLoader().getResource("test.mp3").toString();
		jlp player = jlp.createInstance(args);
		try {
			player.play();
			Assertions.assertTrue(true, "Play");
		} catch (JavaLayerException e) {
			e.printStackTrace();
			Assertions.assertTrue(false, "JavaLayerException : " + e.getMessage());
		}
	}
}
