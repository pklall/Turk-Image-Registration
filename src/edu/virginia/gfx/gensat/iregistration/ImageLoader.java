package edu.virginia.gfx.gensat.iregistration;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageLoader {
	public BufferedImage getImage(String url) throws IOException;
}
