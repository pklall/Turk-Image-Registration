package edu.virginia.gfx.gensat.iregistration;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageIOImageLoader implements ImageLoader {
	public BufferedImage getImage(String url) throws IOException {
		try {
			return ImageIO.read(new URL(url));
		} catch (MalformedURLException e) {
			throw new IOException(e);

		}
	}
}
