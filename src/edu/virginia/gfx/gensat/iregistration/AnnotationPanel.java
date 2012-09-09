package edu.virginia.gfx.gensat.iregistration;

import javax.swing.JPanel;
import java.awt.GridLayout;

/**
 * Creates a user interface for manual image registration by drawing line
 * correspondences.
 * 
 * UI flow:
 * 
 * Prompt user to add a line correspondence
 * 
 * Allow user to edit line until "next" is pushed
 * 
 * Repeat until done (a line correspondence has been drawn for all in the
 * template)
 * 
 * Loop through again allowing user to edit line correspondences
 * 
 */
public class AnnotationPanel extends JPanel {
	protected AnnotatedImage template;
	protected AnnotatedImage target;
	protected AnnotationViewer templateViewer;
	protected AnnotationEditor targetEditor;

	protected int currentAnnotation = -1;
	private boolean done;

	public boolean isDone() {
		return done;
	}
	
	/**
	 * @return The total number of annotations completed.
	 */
	public int getFinishedAnnotations() {
		return target.getAnnotations().size();
	}

	/**
	 * @return The total number of annotations required.
	 */
	public int getNumAnnotations() {
		return template.getAnnotations().size();
	}

	protected void setShowAll(boolean b) {
		templateViewer.setShowAll(b);
		targetEditor.setShowAll(b);
	}

	/**
	 * Returns true if the user is ready to move to the next line correspondence
	 */
	protected boolean isReady() {
		return done || target.getAnnotations().size() == (currentAnnotation - 1);
	}

	protected void next() {
		currentAnnotation++;
		if(currentAnnotation > getNumAnnotations()) {
			done = true;
			currentAnnotation = 0;
		}
		templateViewer.setSelected(currentAnnotation);
		if(done) {
			targetEditor.setSelected(currentAnnotation);
		} else {
			targetEditor.setAddMode();
		}
	}

	public AnnotationPanel(AnnotatedImage target, AnnotatedImage template) {
		this.template = template;
		this.target = target;
		this.templateViewer = new AnnotationViewer(template);
		this.targetEditor = new AnnotationEditor(target);
		setLayout(new GridLayout(1, 0, 0, 0));
		next();
	}
}
