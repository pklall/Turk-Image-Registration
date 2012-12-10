package edu.virginia.gfx.gensat.iregistration;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.media.opengl.GLProfile;
import javax.swing.JApplet;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class TurkApplet extends JApplet {
	private static final long serialVersionUID = 1L;

	private EditorPanel editor;

	private String name;
	private String url;

	private Map<String, String> parseParams(String parameters) {
		StringTokenizer paramGroup = new StringTokenizer(parameters, "&");
		HashMap<String, String> params = new HashMap<String, String>();
		URLDecoder decoder = new URLDecoder();

		while (paramGroup.hasMoreTokens()) {
			StringTokenizer value = new StringTokenizer(paramGroup.nextToken(),
					"=");
			params.put(decoder.decode(value.nextToken()),
					decoder.decode(value.nextToken()));
		}
		return params;
	}

	public void init() {
		System.out.println("initializing... ");
		GLProfile.initSingleton();

		getContentPane().setLayout(new BorderLayout());
		Map<String, String> params = parseParams(getDocumentBase().getQuery());

		name = params.get("name");
		url = params.get("url");

		final JDialog loading = new JDialog();
		loading.setSize(200, 100);
		loading.setTitle("Loading");
		loading.add(new JLabel("Loading... please wait"));
		loading.setVisible(true);
		try {
			loading.add(new JLabel("Creating editor..."));
			editor = new EditorPanel(name, url, new ImageLoader() {
				@Override
				public BufferedImage getImage(String url) throws IOException {
					// loading.add(new JLabel("Loading image at: " + url));
					Image i = TurkApplet.this.getImage(new URL(url));
					MediaTracker tracker = new MediaTracker(TurkApplet.this);
					tracker.addImage(i, 0);
					try {
						tracker.waitForAll();
					} catch (InterruptedException e) {
					}
					BufferedImage img = new BufferedImage(i.getWidth(null),
							i.getHeight(null), BufferedImage.TYPE_INT_RGB);
					img.getGraphics().drawImage(i, 0, 0, null);
					return img;
					// return ImageIO.read(new URL(url));
				}
			});
			loading.add(new JLabel("Created editor"));
			getContentPane().add(editor, BorderLayout.CENTER);
		} catch (Throwable t) {
			JDialog errorDialog = new JDialog();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			t.printStackTrace(new PrintStream(os, true));
			errorDialog.setTitle("Error");
			errorDialog.add(new JTextArea(new String(os.toByteArray())));
			errorDialog.setSize(200, 100);
			errorDialog.setVisible(true);
		}
		loading.add(new JLabel("Done"));
		loading.setVisible(false);
		loading.dispose();
	}

	public String getWarp() {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			editor.getWarp().writeWarp(os);
			return new String(os.toByteArray());
		} catch (Throwable t) {
			return new String("");
		}
	}

	public void start() {
	}

	@Override
	public void update(Graphics g) {
		super.update(g);
	}

	public void stop() {
	}

	public void destroy() {
	}
}
