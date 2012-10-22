package edu.virginia.gfx.gensat.iregistration;

import java.io.IOException;
import java.io.InputStream;

import javax.media.opengl.GL2;
import javax.media.opengl.GLProfile;

import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import edu.virginia.gfx.gensat.iregistration.util.InteractiveRenderable;
import edu.virginia.gfx.gensat.iregistration.util.Matrix;
import edu.virginia.gfx.gensat.iregistration.util.PointRenderer;
import edu.virginia.gfx.gensat.iregistration.util.PointSelector;
import edu.virginia.gfx.gensat.iregistration.util.PointSelectorEvent;
import edu.virginia.gfx.gensat.iregistration.util.PointSelector.PointSelectorEventListener;
import edu.virginia.gfx.gensat.iregistration.util.PointSelectorEvent.PointState;

public class AffineTool extends PointSelector implements InteractiveRenderable,
		PointSelectorEventListener {
	private final Warp warp;

	private static final int TRANSLATE = 0;
	private static final int ROTATE = 1;
	private static final int SCALE = 2;

	private static final int COLOR_NONE = 0x0000ffff;
	private static final int COLOR_HOVER = 0x77ff77ff;
	private static final int COLOR_SELECT = 0x00ff00ff;

	private final PointRenderer pointRenderer;

	private static final float[] points = new float[] { 0.5f, 0.5f, 0.75f,
			0.75f, 0.75f, 0.25f };

	public AffineTool(Warp warp, GLProfile profile) throws IOException {
		super(points);
		setEventListener(this);
		setMoveOnSelect(false);

		this.warp = warp;

		InputStream rotateStream = getClass()
				.getResourceAsStream("/rotate.png");
		TextureData rotate = AWTTextureIO.newTextureData(profile, rotateStream,
				true, "png");

		InputStream translateStream = getClass().getResourceAsStream(
				"/translate.png");
		TextureData translate = AWTTextureIO.newTextureData(profile,
				translateStream, true, "png");

		InputStream scaleStream = getClass().getResourceAsStream("/scale.png");
		TextureData scale = AWTTextureIO.newTextureData(profile, scaleStream,
				true, "png");

		TextureData[] textures = new TextureData[3];
		textures[TRANSLATE] = translate;
		textures[ROTATE] = rotate;
		textures[SCALE] = scale;
		this.pointRenderer = new PointRenderer(textures, points);
		this.pointRenderer.texIndex[TRANSLATE] = TRANSLATE;
		this.pointRenderer.texIndex[ROTATE] = ROTATE;
		this.pointRenderer.texIndex[SCALE] = SCALE;
	}

	@Override
	public void mouseDown(float mx, float my, int buttons, float[] mat) {
		float[] tot = new float[16];
		Matrix.multiplyMM(tot, 0, mat, 0, warp.affine, 0);
		super.mouseDown(mx, my, buttons, tot);
	}

	@Override
	public void mouseUp(float mx, float my, int buttons, float[] mat) {
		float[] tot = new float[16];
		Matrix.multiplyMM(tot, 0, mat, 0, warp.affine, 0);
		super.mouseUp(mx, my, buttons, tot);
	}

	@Override
	public void mouseMove(float mx, float my, float[] mat) {
		float[] tot = new float[16];
		Matrix.multiplyMM(tot, 0, mat, 0, warp.affine, 0);
		super.mouseMove(mx, my, tot);
	}

	@Override
	public void render(GL2 gl, float[] parent) {
		float[] tot = new float[16];
		Matrix.multiplyMM(tot, 0, parent, 0, warp.affine, 0);
		pointRenderer.render(gl, tot);
	}

	@Override
	public void destroy(GL2 gl) {
		pointRenderer.destroy(gl);
	}

	@Override
	public void init(GL2 gl) {
		pointRenderer.init(gl);
	}

	@Override
	public void onPointSelectorEvent(PointSelectorEvent e) {
		// if a point was just clicked
		if (e.newState == PointState.SELECT && e.oldState != PointState.SELECT) {
		}
		// if a point is being dragged
		if (e.newState == PointState.SELECT && e.oldState == PointState.SELECT) {
			pointRenderer.color[e.point] = COLOR_SELECT;
			switch (e.point) {
			case TRANSLATE:
				float dx = e.mx - e.pmx;
				float dy = e.my - e.pmy;
				Matrix.translateM(warp.affine, 0, dx, dy, 0);
				break;
			case ROTATE:
				break;
			case SCALE:
				break;
			}
		}
		if (e.newState == PointState.NONE) {
			pointRenderer.color[e.point] = COLOR_NONE;
		}
		if (e.newState == PointState.HOVER) {
			pointRenderer.color[e.point] = COLOR_HOVER;
		}
	}

}
