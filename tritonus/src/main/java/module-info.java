module tritonus {
	requires jorbis;
	requires transitive java.desktop;
	exports org.tritonus.share.sampled.file;
	exports org.tritonus.share;
	exports org.tritonus.share.sampled.convert;
	exports org.tritonus.share.sampled;
}
