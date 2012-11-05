package edu.virginia.gfx.gensat.iregistration;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JApplet;
import javax.swing.JComboBox;

public class Main extends JApplet {
	private static final long serialVersionUID = 1L;

	private static String urlRoot = "ftp://ftp.ncbi.nih.gov/pub/gensat/Genes";
	private static ArrayList<String> imgPaths;
	private static ArrayList<String> genes;

	private EditorPanel editor;

	private void initImgUrls() {
		InputStream csvStream = getClass().getResourceAsStream(
				"/adult_sagital_slice8.csv");
		Scanner s = new Scanner(csvStream);
		imgPaths = new ArrayList<String>();
		genes = new ArrayList<String>();
		while (s.hasNextLine()) {
			String ln[] = s.nextLine().split(",");
			String url = ln[0].trim();
			String name = ln[1].trim();
			imgPaths.add(url);
			genes.add(name);
		}
	}

	public void init() {
		System.out.println("initializing... ");
		initImgUrls();

		final JComboBox<Object> imgChooser = new JComboBox<Object>(
				genes.toArray());
		imgChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = imgChooser.getSelectedIndex();
				String gene = genes.get(index);
				String url = urlRoot + imgPaths.get(index);
				if (editor == null) {
					editor = new EditorPanel(gene, url);
					getContentPane().add(editor, BorderLayout.CENTER);
				}
				editor.reset(gene, url);
			}
		});

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(imgChooser, BorderLayout.NORTH);
		editor = new EditorPanel(genes.get(0), urlRoot + imgPaths.get(0));
		getContentPane().add(editor, BorderLayout.CENTER);
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
