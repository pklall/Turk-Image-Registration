package edu.virginia.gfx.gensat.iregistration;

import java.io.IOException;
import java.io.InputStream;

import javax.media.opengl.GL2;
import javax.media.opengl.GLProfile;

import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import edu.virginia.gfx.gensat.iregistration.util.InteractiveRenderable;
import edu.virginia.gfx.gensat.iregistration.util.PointRenderer;

public class AffineTool implements InteractiveRenderable {
	private final Warp warp;

	// padding to use when displaying the corners of the inner rectangle
	// this should be large enough to ensure selector points aren't clipped
	// outside the viewport under typical conditions (there's no simple way
	// to handle clipping under extreme translations)
	private static final float PADDING = 0.30f;

	// use the bottom left and top right corners for rotation handles
	private final float[] rotatePoints = new float[] { 0, 0, 1, 1 };
	// use the center for translation handles
	private final float[] translatePoints = new float[] { 0.5f, 0.5f };
	// use the center for translation handles
	private final float[] scalePoints = new float[] { 0, 1, 1, 0 };

	public AffineTool(Warp warp, GLProfile profile) throws IOException {
		this.warp = warp;
		
		// load texture data
		InputStream solidStream = getClass().getResourceAsStream(
				"/circle_full.png");
		TextureData solid = AWTTextureIO.newTextureData(profile, solidStream,
				false, "png");

		InputStream hollowStream = getClass().getResourceAsStream(
				"/circle_hollow.png");
		TextureData hollow = AWTTextureIO.newTextureData(profile, hollowStream,
				false, "png");

		// initialize renderers
		// toolPointRenderer = new PointRenderer(solid, );
	}

	@Override
	public void init(GL2 gl) {

	}

	@Override
	public void render(GL2 gl, float[] parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy(GL2 gl) {

	}

	@Override
	public void mouseDown(float x, float y, int button, float[] mat) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseUp(float x, float y, int button, float[] mat) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMove(float x, float y, float[] mat) {
		// TODO Auto-generated method stub

	}

}
