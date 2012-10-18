package edu.virginia.gfx.gensat.iregistration;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main extends JApplet {
	private static final long serialVersionUID = 1L;
	private Editor editor;

	protected void initEditor() {
		float[] affine = new float[16];
		Matrix.setIdentityM(affine, 0);
		// float[] points = new float[] { 0, 0, 0, 1, 1, 1, 1, 0 };
		// int[] triangles = new int[] { 0, 1, 2, 0, 2, 3 };

		// create a grid
		int grid = 5; // 3x3
		float[] points = new float[(grid + 1) * (grid + 1) * 2];
		for (int x = 0; x <= grid; x++) {
			for (int y = 0; y <= grid; y++) {
				points[(y * (grid + 1) + x) * 2 + 0] = (float) x * 1.0f
						/ (float) grid;
				points[(y * (grid + 1) + x) * 2 + 1] = (float) y * 1.0f
						/ (float) grid;
			}
		}
		System.out.println("Points: " + Arrays.toString(points));
		int[] triangles = new int[grid * grid * 3 * 2];
		// top-left triangles
		for (int y = 0; y < grid; y++) {
			for (int x = 0; x < grid; x++) {
				triangles[(y * grid + x) * 3 + 0] = y * (grid + 1) + x;
				triangles[(y * grid + x) * 3 + 1] = y * (grid + 1) + (x + 1);
				triangles[(y * grid + x) * 3 + 2] = (y + 1) * (grid + 1) + x;
			}
		}
		// top-left triangles
		int offset = grid * grid * 3;
		for (int y = 0; y < grid; y++) {
			for (int x = 0; x < grid; x++) {
				triangles[(y * grid + x) * 3 + 0 + offset] = y * (grid + 1)
						+ (x + 1);
				triangles[(y * grid + x) * 3 + 1 + offset] = (y + 1)
						* (grid + 1) + (x + 1);
				triangles[(y * grid + x) * 3 + 2 + offset] = (y + 1)
						* (grid + 1) + x;
			}
		}

		Warp warp = new Warp(affine, points, triangles);

		BufferedImage imgWarp = null;
		BufferedImage imgTarget = null;
		try {
			// test image
			// imgWarp = ImageIO.read(new URL(
			// "http://colorvisiontesting.com/plate%20with%205.jpg"));
			// bean
			// imgWarp = ImageIO .read(new URL(
			// "http://cdn6.fotosearch.com/bthumb/FDS/FDS106/redkid4.jpg"));
			imgWarp = ImageIO
					.read(getClass().getResourceAsStream("/brain.jpg"));
			// imgTarget = ImageIO .read(new URL(
			// "http://www.vnvlvokc.com/ow_userfiles/plugins/shoppro/images/product_1.jpg"));
			imgTarget = ImageIO.read(new URL( "http://www.gensat.org/atlas/ADULT_ATLAS_07.jpg"));
			// imgTarget = ImageIO .read(getClass().getResourceAsStream("/brain2.jpg"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			editor = new Editor(warp, imgWarp, imgTarget);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void initAnnotationPanel() throws IOException {
		AnnotatedImage template = new AnnotatedImage("template",
				ImageIO.read(new File(
						"1810041L15Rik_adult_S_DAB_10X_08_cryo.med.jpg")));
		new AnnotatedImage("", null, null);
		AnnotationPanel p = new AnnotationPanel(template, template);
	}

	public void init() {
		System.out.println("initializing... ");
		getContentPane().setLayout(new BorderLayout(0, 0));
<<<<<<< HEAD

		initEditor();

		final JSlider alphaSlider = new JSlider();
		alphaSlider.setMaximum(255);
		alphaSlider.setMinimum(0);
		alphaSlider.setValue(128);
		alphaSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				editor.setAlpha(alphaSlider.getValue());
			}
		});
		
		// FIXME These are terrible UI labels
		final JRadioButton editWarpRadioButton = new JRadioButton("Edit Warp");
		editWarpRadioButton.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if(editWarpRadioButton.isSelected()) {
					editor.setEditorModeWarp();
				}
			}
		});
		final JRadioButton editAffineRadioButton = new JRadioButton("Edit Affine");
		editAffineRadioButton.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if(editAffineRadioButton.isSelected()) {
					editor.setEditorModeAffine();
				}
			}
		});
		final ButtonGroup editButtonGroup = new ButtonGroup();
		editButtonGroup.add(editWarpRadioButton);
		editButtonGroup.add(editAffineRadioButton);
		final JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
		topPanel.add(editAffineRadioButton);
		topPanel.add(editWarpRadioButton);
		
		// start with the affine button selected
		editAffineRadioButton.setSelected(true);
		editor.setEditorModeAffine();
		editWarpRadioButton.setSelected(false);
		
		JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
		sep.setMinimumSize(new Dimension(20, 0));
		topPanel.add(sep);
		topPanel.add(new JLabel("Transparency: "));
		topPanel.add(alphaSlider);
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		getContentPane().add(topPanel);
		getContentPane().add(editor);
=======
		// initAnnotatedImgComponents();
		try {
			initAnnotationPanel();
		} catch (Exception e) {
			System.err.println("");
		}
>>>>>>> 4c843c547c01666edaafea218dd24b5bfcdacb30
	}

	public void start() {
		System.out.println("starting... ");
	}

	@Override
	public void update(Graphics g) {
		super.update(g);
	}

	public void stop() {
		System.out.println("stopping... ");
	}

	public void destroy() {
		System.out.println("destroying...");
	}
}
