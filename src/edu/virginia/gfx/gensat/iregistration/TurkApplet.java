package edu.virginia.gfx.gensat.iregistration;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JApplet;
import javax.swing.JDialog;

public class TurkApplet extends JApplet {
	private static final long serialVersionUID = 1L;

	private static String urlRoot = "ftp://ftp.ncbi.nih.gov/pub/gensat/Genes";

	private EditorPanel editor;

	private String geneName;
	private String geneUrl;

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
		getContentPane().setLayout(new BorderLayout());
		Map<String, String> params = parseParams(getDocumentBase().getQuery());

		// geneName = params.get("genename");
		// geneUrl = params.get("geneurl");
		geneUrl = "/csf2rb2/Csf2rb2_adult_S_DAB_IF334_31_20070501-1132_05_11_07/Csf2rb2_adult_S_DAB_10X_08_cryo.med.jpg";
		geneName = "csf2rb2";


		try {
			editor = new EditorPanel(geneName, urlRoot + geneUrl);
		} catch (Exception e) {
			JDialog errorDialog = new JDialog();
			errorDialog.setTitle("Error:  Unable to load task!");
			errorDialog.setVisible(true);
		}
		getContentPane().add(editor, BorderLayout.CENTER);
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
