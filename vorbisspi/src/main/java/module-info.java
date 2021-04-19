module vorbisspi {
	requires jorbis;
	requires tritonus;
	requires java.desktop;
	
	provides javax.sound.sampled.spi.FormatConversionProvider with javazoom.spi.vorbis.sampled.convert.VorbisFormatConversionProvider;
	provides javax.sound.sampled.spi.AudioFileReader with javazoom.spi.vorbis.sampled.file.VorbisAudioFileReader;
}
