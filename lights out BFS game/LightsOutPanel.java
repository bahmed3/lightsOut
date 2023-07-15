
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

public class LightsOutPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Color offColor = Color.BLACK;
	private Color onColor = new Color(217, 217, 132);
	private Color gridColor = Color.GRAY;
	
	private LightsOut lightsOut;
	private int state;
	
	public LightsOutPanel(LightsOut lo) {
		lightsOut = lo;
	}
	
	public void setState(int newState) {
		if (state == newState) return;
		state = newState;
		repaint();
	}
	
	public int getState() {
		return state;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int rows = lightsOut.rows();
		int columns = lightsOut.columns();
		int w = getWidth() / rows;
		int h = getHeight() / columns;
		g.setColor(offColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(onColor);
		for (int i=0; i < rows; ++i) {
			for (int j=0; j < columns; ++j) {
				if (lightsOut.isOn(state, i, j)) {
					g.fillRect(w*j,h*i,w,h);
				}
			}
		}
		g.setColor(gridColor);
		for (int i=1; i < rows; ++i) {
			g.drawLine(0, h*i, getWidth(), h*i);
		}
		for (int j=1; j < columns; ++j) {
			g.drawLine(w*j, 0, w*j, getHeight());
		}
	}
	
	/**
	 * Return the point as a (row,column) pair.
	 * @param p point in this panel to translate, must not be null
	 * @return non-null Pair indicating light at this point.
	 */
	public GridLoc translatePoint(Point p) {
		int rows = lightsOut.rows();
		int columns = lightsOut.columns();
		int w = getWidth() / rows;
		int h = getHeight() / columns;
		return GridLoc.get(p.y / h, p.x / w);
	}
}
