package edu.virginia.gfx.gensat.iregistration;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
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
			// imgTarget = ImageIO .read(new URL(
			// "http://www.vnvlvokc.com/ow_userfiles/plugins/shoppro/images/product_1.jpg"));
			// imgWarp = ImageIO.read(new URL(
			// "http://www.gensat.org/atlas/ADULT_ATLAS_07.jpg"));
			// imgWarp = ImageIO.read(getClass() .getResource("/ADULT_ATLAS_07.jpg"));
			imgWarp = ImageIO.read(getClass() .getResource("/atlas2.png"));
			// imgWarp = ImageIO.read(new URL("http://www.wyrmcorp.com/galleries/illusions/Hermann%20Grid.png"));
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
			editor = new Editor(warp, imgWarp, imgTarget);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init() {
		System.out.println("initializing... ");
		getContentPane().setLayout(new BorderLayout(0, 0));

		initEditor();

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

		final JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				editor.submit();
			}
		});

		final JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));

		topPanel.add(new JLabel("    Strength: "));
		topPanel.add(strengthSlider);
		topPanel.add(new JLabel("    Radius: "));
		topPanel.add(radiusSlider);
		topPanel.add(new JLabel("    Transparency: "));
		topPanel.add(alphaSlider);
		topPanel.add(submitButton);

		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		getContentPane().add(topPanel);
		getContentPane().add(editor);
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
