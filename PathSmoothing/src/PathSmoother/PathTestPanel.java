package PathSmoother;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import PathUtil.Point;

public class PathTestPanel extends JPanel implements KeyListener, MouseListener {
	private static final long serialVersionUID = 1L;
	
	static final int DEFAULT_WIDTH = 1920;
	static final int DEFAULT_HEIGHT = 1080;
	
	static final Color ORIG_PATH_COLOR = new Color(0,255,0,200);
	static final Color SMOOTH_PATH_COLOR = new Color(0,0,255,200);
	static final Color BACKGROUND_COLOR = new Color(255,255,255,255);
	
	int _width;
	int _height;
	boolean _draw_new_path = false;

	BufferedImage _buffer, _bufferPath;
	WeightedPathSmoother _wps;
	double _tolerance;
	
	public PathTestPanel(WeightedPathSmoother wps, double smooth_rate) {
		super();
		_wps = wps;
		_tolerance = smooth_rate;
		
		_width = _wps.getMap().w();
		_height = _wps.getMap().h();
		
		addKeyListener(this);
		addMouseListener(this);
		
		_buffer = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);
		_bufferPath = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		update();
	}
	
	public void update() {
		Graphics2D g = _buffer.createGraphics();
		
		double pixX = DEFAULT_WIDTH / (double)_width;
		double pixY = DEFAULT_HEIGHT / (double)_height;
		for (int i = 0; i < _height; i++) {
			for (int j = 0; j < _width; j++) {
				//g.setColor(BACKGROUND_COLOR);
				int w = (int)(_wps.getMap().getWeight(j, i) / 4);
				if (w > 255) w = 255;
				int gr = (int)((w > 128 ? 1-2*(w-128)/256.0 : 1.0) * 210);
				int rd = (int)((w > 128 ? 1.0 : 2*w/256.0) * 210);
				g.setColor(new Color(rd,gr,80,255));
				
				g.fillRect((int)(j*pixX), (int)(i*pixY), (int)pixX+1, (int)pixY+1);
			}
		}
		
		for (int i = 0; i < _wps.getPath().size(); i++) {
			Point p = _wps.getPath().get(i);
			g.setColor(ORIG_PATH_COLOR);
			g.fillRect((int)(p.x()*pixX), (int)(p.y()*pixY), (int)pixX+1, (int)pixY+1);
		}

		for (int i = 0; i < _wps.getSmoothPath().size(); i++) {
			Point p = _wps.getSmoothPath().get(i);
			g.setColor(SMOOTH_PATH_COLOR);
			g.fillRect((int)(p.x()*pixX), (int)(p.y()*pixY), (int)pixX+1, (int)pixY+1);
		}

			

		
		g.dispose();
	}
	
	@Override
	public void paintComponent(Graphics gr) {		
		super.paintComponent(gr);
		Dimension sz = getSize();

		
		Graphics2D g = (Graphics2D)gr;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(_buffer, 0, 0, sz.width, sz.height, 0, 0, 
				DEFAULT_WIDTH, DEFAULT_HEIGHT, this);
		g.drawImage(_bufferPath, 0, 0, sz.width, sz.height, 0, 0, 
				DEFAULT_WIDTH, DEFAULT_HEIGHT, this);		
	}
	
    public void keyPressed(KeyEvent e) {
    	int key = e.getKeyCode();
    	
    	switch(key) {
    	case KeyEvent.VK_L:
    		if (!_draw_new_path) {
        		_wps.getSmoothPath().clear();
        		_wps.getPath().clear();
        		_draw_new_path = true;
        		
        		update();
        		repaint();
    		}
    		break;
    	default:
    		// nothing to do
    	}
    }
    
    public void keyReleased(KeyEvent e) {
    	int key = e.getKeyCode();
    	
    	switch(key) {
    	case KeyEvent.VK_L:
    		//System.out.println("L");
    		_draw_new_path = false;

    		break;
    	default:
    		// nothing to do
    	}
    }
    
    public void keyTyped(KeyEvent e) {
    	char key = e.getKeyChar();
    	System.out.println("KEY TYPED: " + key);
    	switch(key) {
    	case 's':
    		//System.out.println("About to smooth!");
    		if(_wps.getSmoothPath().size() > 2) _wps.smoothWeighted(_tolerance);
    		update();
    		repaint();
    		break;
    	case 'r':
    		//System.out.println("About to smooth!");
    		if(_wps.getSmoothPath().size() > 2) _wps.reset();
    		update();
    		repaint();
    		break;
    	default:
    		//System.out.println("Key " + key + " Typed");
    	}
    }

	@Override
	public void mouseClicked(MouseEvent e) {
        
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("Clicked pressed at pixel (" + e.getX() + ", " + e.getY() + ")");
		
		int x =  e.getX() * _width / getWidth();
        int y = e.getY() * _height / getHeight();
        System.out.println("Which is cell (" + x + ", " + y + ")");

        if (_draw_new_path) {
        	_wps.getSmoothPath().add(x, y);
        	_wps.getPath().add(x, y);
            update();
            repaint();
        }
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
