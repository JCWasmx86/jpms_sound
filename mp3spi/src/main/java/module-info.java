module mp3spi {
	requires jlayer;
	requires tritonus;
	requires transitive java.desktop;
	provides javax.sound.sampled.spi.FormatConversionProvider with javazoom.spi.mpeg.sampled.convert.MpegFormatConversionProvider;
	provides javax.sound.sampled.spi.AudioFileReader with javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
}
