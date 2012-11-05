package edu.virginia.gfx.gensat.iregistration;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EditorPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Editor editor;

	private String name;
	private String imgUrl;

	protected void initEditor() {
		// float[] points = new float[] { 0, 0, 0, 1, 1, 1, 1, 0 };
		// int[] triangles = new int[] { 0, 1, 2, 0, 2, 3 };

		Warp warp = new Warp(50, 50);

		BufferedImage imgWarp = null;
		BufferedImage imgTarget = null;
		try {
			// test image
			// imgWarp = ImageIO.read(new URL(
			// "http://colorvisiontesting.com/plate%20with%205.jpg"));
			// bean
			// imgWarp = ImageIO .read(new URL(
			// "http://cdn6.fotosearch.com/bthumb/FDS/FDS106/redkid4.jpg"));

			imgTarget = ImageIO.read(getClass().getResourceAsStream(
					"/brain.jpg"));
			imgTarget = ImageIO.read(new URL(imgUrl));
			// imgTarget = ImageIO .read(new URL(
			// "http://www.vnvlvokc.com/ow_userfiles/plugins/shoppro/images/product_1.jpg"));

			// imgWarp = ImageIO.read(new URL(
			// "http://www.gensat.org/atlas/ADULT_ATLAS_07.jpg"));
			// imgWarp = ImageIO.read(getClass()
			// .getResource("/ADULT_ATLAS_07.jpg"));
			imgWarp = ImageIO.read(getClass().getResource("/atlas2.png"));
			// imgWarp = ImageIO.read(new
			// URL("http://www.wyrmcorp.com/galleries/illusions/Hermann%20Grid.png"));
			// imgTarget = ImageIO
			// .read(getClass().getResourceAsStream("/brain2.jpg"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if (editor == null) {
				System.out.println("Creating new editor");
				editor = new Editor(warp, imgWarp, imgTarget);
			} else {
				System.out.println("Reseting editor");
				editor.reset(warp, imgWarp, imgTarget);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public EditorPanel(String name, String imgUrl) {
		reset(name, imgUrl);

		// Editor-related controls
		final JSlider radiusSlider = new JSlider();
		radiusSlider.setMaximum(255);
		radiusSlider.setMinimum(0);
		radiusSlider.setValue(255);
		radiusSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				editor.getMeshTool()
						.setRadius(radiusSlider.getValue() / 255.0f);
				editor.repaint();
			}
		});

		final JSlider strengthSlider = new JSlider();
		strengthSlider.setMaximum(255);
		strengthSlider.setMinimum(0);
		strengthSlider.setValue(255);
		strengthSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				editor.getMeshTool().setStrength(
						strengthSlider.getValue() / 255.0f);
			}
		});

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

		final JPanel editorControlPanel = new JPanel();
		editorControlPanel.setLayout(new BoxLayout(editorControlPanel,
				BoxLayout.LINE_AXIS));
		editorControlPanel.add(new JLabel("    Strength: "));
		editorControlPanel.add(strengthSlider);
		editorControlPanel.add(new JLabel("    Radius: "));
		editorControlPanel.add(radiusSlider);
		editorControlPanel.add(new JLabel("    Transparency: "));
		editorControlPanel.add(alphaSlider);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(editorControlPanel);
		add(editor);

		System.out.println("done");
		repaint();
	}

	public void dispose() {
		editor.destroy();
	}

	public void reset(String gene, String url) {
		this.name = gene;
		this.imgUrl = url;

		initEditor();
	}
}
