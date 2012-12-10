package edu.virginia.gfx.gensat.iregistration;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.media.opengl.GLProfile;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class TurkApplet extends JFrame {
	private static final long serialVersionUID = 1L;

	// FIXME
	// private static String urlRoot = "ftp://ftp.ncbi.nih.gov/pub/gensat/Genes/";
	private static String urlRoot = "http://localhost:8000/data/";

	private EditorPanel editor;

	private String geneName;
	private String geneUrl;

	public static void main(String[] args) {
		TurkApplet applet = new TurkApplet();
		applet.init();
		applet.setSize(550, 300);
		applet.start();
		applet.setVisible(true);
		applet.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private Map<String, String> parseParams(String parameters) {
		StringTokenizer paramGroup = new StringTokenizer(parameters, "&");
		HashMap<String, String> params = new HashMap<String, String>();

		while (paramGroup.hasMoreTokens()) {
			StringTokenizer value = new StringTokenizer(paramGroup.nextToken(),
					"=");
			params.put(value.nextToken(), value.nextToken());
		}
		return params;
	}

	public void init() {
		System.out.println("initializing... ");
		GLProfile.initSingleton();

		getContentPane().setLayout(new BorderLayout());
		// Map<String, String> params =
		// parseParams(getDocumentBase().getQuery());

		// geneName = params.get("genename");
		// geneUrl = params.get("geneurl");
		geneUrl = "Csf2rb2_adult_S_DAB_10X_08_cryo.med.jpg";
		geneName = "csf2rb2";

		final JDialog loading = new JDialog();
		loading.setSize(200, 100);
		loading.setTitle("Loading");
		loading.add(new JLabel("Loading... please wait"));
		loading.setVisible(true);
		try {
			loading.add(new JLabel("Creating editor..."));
			editor = new EditorPanel(geneName, urlRoot + geneUrl,
					new ImageLoader() {
						@Override
						public BufferedImage getImage(String url)
								throws IOException {
							/*
							 * loading.add(new JLabel("Loading image at: " +
							 * url)); Image i = TurkApplet.this.getImage(new
							 * URL(url)); loading.add(new JLabel("Success!"));
							 * BufferedImage img = new
							 * BufferedImage(i.getWidth(null),
							 * i.getHeight(null), BufferedImage.TYPE_INT_RGB);
							 * img.getGraphics().drawImage(i, 0, 0, null);
							 * return img;
							 */
							return ImageIO.read(new URL(url));
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
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		editor.getWarp().writeWarp(os);
		return new String(os.toByteArray());
	}

	public String getGene() {
		return geneName;
	}

	public String getGeneUrl() {
		return geneUrl;
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
