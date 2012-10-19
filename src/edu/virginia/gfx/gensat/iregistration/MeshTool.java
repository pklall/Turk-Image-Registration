package edu.virginia.gfx.gensat.iregistration;

import java.io.IOException;
import java.io.InputStream;

import javax.media.opengl.GLProfile;

import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import edu.virginia.gfx.gensat.iregistration.util.InteractiveRenderable;
import edu.virginia.gfx.gensat.iregistration.util.RenderablePointSelector;

public class MeshTool extends RenderablePointSelector implements
		InteractiveRenderable {
	private final Warp warp;

	public static MeshTool create(Warp warp, GLProfile profile)
			throws IOException {
		// load texture data
		InputStream solidStream = MeshTool.class
				.getResourceAsStream("/circle_full.png");
		TextureData solid = AWTTextureIO.newTextureData(profile, solidStream,
				false, "png");

		InputStream hollowStream = MeshTool.class
				.getResourceAsStream("/circle_hollow.png");
		TextureData hollow = AWTTextureIO.newTextureData(profile, hollowStream,
				false, "png");
		return new MeshTool(warp, hollow, solid, solid, profile);
	}

	protected MeshTool(Warp warp, TextureData unselected, TextureData hover,
			TextureData selected, GLProfile profile) throws IOException {
		super(warp.dstVertices, unselected, hover, selected, profile);
		setMoveOnSelect(true);

		this.warp = warp;
		pointRenderer.color = 0x0000ffff; // blue
		selectedPointRenderer.color = 0x00ff00ff; // green
		hoverPointRenderer.color = 0xff2400ff; // orange
	}

	@Override
	protected void pointDragged(int p, float x, float y) {
		warp.dstVertices[p * 2 + 0] = x;
		warp.dstVertices[p * 2 + 1] = y;
	}

	@Override
	protected void pointHovered(int p, float x, float y) {
		// do nothing
	}

	@Override
	protected void pointSelected(int p, float x, float y) {
		// do nothing
	}
}
