package edu.virginia.gfx.gensat.iregistration;

import java.awt.image.BufferedImage;
import java.util.List;

public class MorphedImage {
	private List<Arrow> target;
	private List<Arrow> template;
	private BufferedImage img;

	/**
	 * Constructor
	 * 
	 * Note that "target" corresponds to the "source", and template corresponds
	 * to the "destination" as per the Beier Neeley Image Morph paper.
	 * 
	 * @param target
	 *            Arrow correspondences for the target image
	 * @param template
	 *            Arrow correspondences for the template image
	 * @param oringinal
	 *            The original target image (will be warped to match the
	 *            template)
	 */
	public MorphedImage(List<Arrow> target, List<Arrow> template,
			BufferedImage original) {
		this.target = target;
		this.template = template;
		this.img = original;
	}

	private void computeXsrc(Arrow src, Arrow dst, Vector xdst, Vector result) {
		Vector sHead = src.getHead();
		Vector sTail = src.getTail();
		Vector dHead = dst.getHead();
		Vector dTail = dst.getTail();

		float xd1 = xdst.x;
		float xd2 = xdst.y;
		float pd1 = sHead.x;
		float pd2 = sHead.y;
		float qd1 = sTail.x;
		float qd2 = sTail.y;
		float ps1 = dHead.x;
		float ps2 = dHead.y;
		float qs1 = dTail.x;
		float qs2 = dTail.y;
		// SEE MorphFunction.nb for derivation according to Beier Neeley (1992)
		float u = (pd1 * pd1 + pd2 * pd2 + qd1 * xd1 - pd1 * (qd1 + xd1) + qd2
				* xd2 - pd2 * (qd2 + xd2))
				/ (pd1 * pd1 + pd2 * pd2 - 2 * pd1 * qd1 + qd1 * qd1 - 2 * pd2
						* qd2 + qd2 * qd2);
		float v = (float) ((float) (pd2 * (qd1 - xd1) + qd2 * xd1 - qd1 * xd2 + pd1
				* (-qd2 + xd2)) / Math.sqrt((pd1 - qd1) * (pd1 - qd1)
				+ (pd2 - qd2) * (pd2 - qd2)));
		float xs1 = (float) ((float) ps1
				+ ((-ps1 + qs1) * (pd1 * pd1 + pd2 * pd2 + qd1 * xd1 - pd1
						* (qd1 + xd1) + qd2 * xd2 - pd2 * (qd2 + xd2)))
				/ (pd1 * pd1 + pd2 * pd2 - 2 * pd1 * qd1 + qd1 * qd1 - 2 * pd2
						* qd2 + qd2 * qd2) - ((ps2 - qs2) * (pd2 * (qd1 - xd1)
				+ qd2 * xd1 - qd1 * xd2 + pd1 * (-qd2 + xd2)))
				/ Math.sqrt(((pd1 - qd1) * (pd1 - qd1) + (pd2 - qd2)
						* (pd2 - qd2))
						* ((ps1 - qs1) * (ps1 - qs1) + (ps2 - qs2)
								* (ps2 - qs2))));
		float xs2 = (float) ((float) ps2
				+ ((-ps2 + qs2) * (pd1 * pd1 + pd2 * pd2 + qd1 * xd1 - pd1
						* (qd1 + xd1) + qd2 * xd2 - pd2 * (qd2 + xd2)))
				/ (pd1 * pd1 + pd2 * pd2 - 2 * pd1 * qd1 + qd1 * qd1 - 2 * pd2
						* qd2 + qd2 * qd2 * qd2) + ((ps1 - qs1) * (pd2
				* (qd1 - xd1) + qd2 * xd1 - qd1 * xd2 + pd1 * (-qd2 + xd2)))
				/ Math.sqrt(((pd1 - qd1) * (pd1 - qd1) + (pd2 - qd2)
						* (pd2 - qd2))
						* ((ps1 - qs1) * (ps1 - qs1) + (ps2 - qs2)
								* (ps2 - qs2))));
		result.x = xs1;
		result.y = xs2;
	}
	
	public BufferedImage computeWarp() {
		return img;
	}
}