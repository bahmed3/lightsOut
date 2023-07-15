import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Stack;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import javax.swing.plaf.basic.BasicButtonUI;

import javax.swing.border.EmptyBorder;

public class LightsOutFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private final LightsOut lightsOut;
	private final LightsOutPanel main;
	private final JCheckBox litOnlyBox = new JCheckBox("lit only");
	private Random random = new Random();
	private Stack<Integer> undoLog = new Stack<>();
	
	public LightsOutFrame(LightsOut lo) {
			lightsOut = lo;
			JButton allOnButton = createButton("All On", "Turn all lights on", (ae) -> doAllOn());
			JButton allOffButton = createButton("All Off", "Turn all lights off", (ae) -> doAllOff());
			JButton randomButton = createButton("Random", "Randomize light state", (ae) -> doRandom());
			JButton stepButton = createButton("Step", "Perform one step in solving the puzzle", (ae) -> doStep());
			JButton undoButton = createButton("Undo", "Undo the last action", (ae) -> doUndo());			
			JPanel topButtonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
			JPanel leftButtonPanel = new JPanel();
			leftButtonPanel.setLayout(new BoxLayout(leftButtonPanel,BoxLayout.PAGE_AXIS));
			topButtonPanel.add(allOnButton);
			topButtonPanel.add(allOffButton);
			topButtonPanel.add(randomButton);

			Color panelColor = new Color(10, 10, 10);

			topButtonPanel.setBackground(panelColor);
			leftButtonPanel.setBackground(panelColor);

					leftButtonPanel.add(Box.createVerticalStrut(160));
			leftButtonPanel.add(stepButton);
			leftButtonPanel.add(Box.createVerticalStrut(10));
			leftButtonPanel.add(undoButton);
			main = new LightsOutPanel(lo);
			JPanel contentPane = new JPanel();
			contentPane.setLayout(new BorderLayout(10, 10));
			contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
			contentPane.setBackground(panelColor);
			contentPane.add(topButtonPanel,BorderLayout.NORTH);
			contentPane.add(leftButtonPanel,BorderLayout.WEST);
			contentPane.add(main,BorderLayout.CENTER);
			contentPane.add(litOnlyBox,BorderLayout.SOUTH);
			main.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseClicked(MouseEvent e) {
							doClick(e);
					}            
			});
			setContentPane(contentPane);
	}
	
private JButton createButton(String text, String tooltip, Consumer<ActionEvent> action) {
    JButton button = new JButton(text);
    button.setToolTipText(tooltip);
    button.addActionListener(action::accept);
    button.setUI(new BasicButtonUI() {
			@Override
			public void installUI(JComponent c) {
					super.installUI(c);
					Color myColor = new Color(255, 255, 255); 
					Color textColor = new Color(0, 0, 0); 
					c.setBackground(myColor);
					c.setForeground(textColor); 
					c.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
					c.setFont(new Font("Roboto", Font.BOLD, 12));
			}

        @Override
        public void paint(Graphics g, JComponent c) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(c.getBackground());
            g.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);
            super.paint(g, c);
        }
    });
    button.setPreferredSize(new Dimension(100, 50));
    button.setMaximumSize(new Dimension(100, 50));
    button.setMinimumSize(new Dimension(100, 50));
    button.setContentAreaFilled(false);
    button.setFocusable(false);
    return button;
}

	private void doAllOn() {
		undoLog.push(main.getState());
		main.setState(lightsOut.allOn());
	}
	
	private void doAllOff() {
		undoLog.push(main.getState());
		main.setState(lightsOut.allOff());		
	}
	
	private void doRandom() {
		int state = lightsOut.allOff();
		for (GridLoc l : lightsOut.getLights()) {
			if (random.nextBoolean()) {
				state = lightsOut.next(state, l);
			}
		}
		undoLog.push(main.getState());
		main.setState(state);
	}
	
	private void doStep() {
		int state = main.getState();
		long startTime = System.currentTimeMillis();
		Plan p = BreadthFirstLightsOut.findSolution(lightsOut,state,litOnlyBox.isSelected());
		System.out.println("Plan: " + p);
		System.out.println("Solve time: " + (System.currentTimeMillis()-startTime) + " ms.");
		if (p == null) {
			JOptionPane.showMessageDialog(this, "Can't solve", "Step Error", JOptionPane.ERROR_MESSAGE);			
		} else if (p == Plan.EMPTY_PLAN) {
			JOptionPane.showMessageDialog(this, "Already solved ?", "Step Warning", JOptionPane.WARNING_MESSAGE);						
		} else if (litOnlyBox.isSelected() && !lightsOut.isOn(state, p.first())) {
			JOptionPane.showMessageDialog(this, "Invalid solution computed: " + p, "Step Error", JOptionPane.ERROR_MESSAGE);
		} else {
			undoLog.push(state);
			main.setState(lightsOut.next(state, p.first()));
		}
	}
	
	private void doUndo() {
		if (undoLog.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No further undo available", "Undo Warning", JOptionPane.ERROR_MESSAGE);
		} else {
			main.setState(undoLog.pop());
		}
	}
	
	private void doClick(MouseEvent e) {
		GridLoc loc = main.translatePoint(e.getPoint());
		int state = main.getState();
		if (litOnlyBox.isSelected() && !lightsOut.isOn(state, loc)) {
			JOptionPane.showMessageDialog(this, "Can only flip switch if light is on", "Click error", JOptionPane.ERROR_MESSAGE);
		} else {
			undoLog.push(main.getState());
			main.setState(lightsOut.next(state, loc));
		}
	}
	
	public void run() {
		main.setState(lightsOut.allOn());
		setSize(500,500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		int rows = Integer.parseInt(args[0]);
		int cols = Integer.parseInt(args[1]);
		SwingUtilities.invokeLater(() -> new LightsOutFrame(new LightsOut(rows,cols)).run());
	}
}
