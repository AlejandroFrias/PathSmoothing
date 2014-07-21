package PathSmoother;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import PathUtil.Path;

public class PathTest extends JFrame {
	private static final long serialVersionUID = 2L;
	static final double TOLERANCE = 0.05;
	static final int SIZE = 500;
	
	static final int FRAME_WIDTH = 800;
	static final String TITLE = "Path Smoothing Test";
	
	
	public static void main(String[] args) throws Exception, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new PathTest(TITLE);
	}
	
	public PathTest(String title) throws Exception {
		String file = "map1.dtm";
		System.out.println("Loading "+file);
		long start = System.currentTimeMillis();
		WeightsMap m = new WeightsMap(300, 200);
		long stop = System.currentTimeMillis();
		System.out.println("Total time to load: " + (stop - start));
		System.out.println("Number of points: " + (m.h() * m.w()) );
		System.out.println("Average Load Time per Point: " + (double)(stop - start)/(double)(m.h() * m.w()) + " milliseconds");
		
		
		Path path = new Path();
		path.add(0, 0);
		path.add(m.w() - 1, 0);
		path.add(m.w() - 1, m.h()/2);
		path.add(0, m.h()/2);
		path.add(0, m.h() - 1);
		path.add(m.w() - 1 , m.h() - 1);
		


		WeightedPathSmoother ps = new WeightedPathSmoother(path, m);
		PathTestPanel panel = new PathTestPanel(ps, TOLERANCE);
		
		double ratio =  (double) ps.getMap().h() / (double)ps.getMap().w();
		panel.setPreferredSize(new Dimension(FRAME_WIDTH, (int) (FRAME_WIDTH * ratio)));
		panel.setFocusable(true);
		panel.requestFocusInWindow();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Container main = getContentPane();
		main.setLayout(new BorderLayout());
		main.add(panel, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
