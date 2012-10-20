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
import edu.virginia.gfx.gensat.iregistration.util.PointSelector.PointSelectorEventListener;
import edu.virginia.gfx.gensat.iregistration.util.PointSelectorEvent;
import edu.virginia.gfx.gensat.iregistration.util.PointSelectorEvent.PointState;

public class MeshTool extends PointSelector implements InteractiveRenderable,
		PointSelectorEventListener {
	private final Warp warp;

	private static final int TEX_NONE = 0;
	private static final int TEX_HOVER = 1;
	private static final int TEX_SELECT = 1;

	private static final int COLOR_NONE = 0x0000ffff; // blue
	private static final int COLOR_HOVER = 0xff7700ff; // orange
	private static final int COLOR_SELECT = 0x00ff00ff; // green

	private final PointRenderer pointRenderer;

	public MeshTool(Warp warp, GLProfile profile) throws IOException {
		super(warp.dstVertices);
		setEventListener(this);
		setMoveOnSelect(true);

		this.warp = warp;

		InputStream solidStream = MeshTool.class
				.getResourceAsStream("/circle_full.png");
		TextureData solid = AWTTextureIO.newTextureData(profile, solidStream,
				true, "png");

		InputStream hollowStream = MeshTool.class
				.getResourceAsStream("/circle_hollow.png");
		TextureData hollow = AWTTextureIO.newTextureData(profile, hollowStream,
				true, "png");
		this.pointRenderer = new PointRenderer(new TextureData[] { hollow,
				solid }, warp.dstVertices, 0, 0x0000ffff);
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
		if (e.newState == PointState.SELECT) {
			pointRenderer.color[e.point] = COLOR_SELECT;
			pointRenderer.texIndex[e.point] = TEX_SELECT;
			warp.dstVertices[e.point * 2 + 0] = e.mx;
			warp.dstVertices[e.point * 2 + 1] = e.my;
		}
		if (e.newState == PointState.NONE) {
			pointRenderer.color[e.point] = COLOR_NONE;
			pointRenderer.texIndex[e.point] = TEX_NONE;
		}
		if (e.newState == PointState.HOVER) {
			pointRenderer.color[e.point] = COLOR_HOVER;
			pointRenderer.texIndex[e.point] = TEX_HOVER;
		}
	}
}
