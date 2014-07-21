package PathUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Path {
	
	private List<Point> _path;
	
	public Path() {
		_path = new ArrayList<>();
	}
	
	public Path(Collection<Point> path) {
		this();
		for (Point p : path) {
			add(new Point(p.x(), p.y(), p.z()));
		}
	}
	
	/**
	 * Creates a path from a list of indices into a 2D where: index = x + y*width.
	 * 
	 * @param points
	 * @param width
	 */
	public Path(int[] points, int width) {
		this(PixelFunc.listify(points, width));
	}
	
	public void add(Point p) {
		if(size() == 0 || (size() == 1 && PixelFunc.pixelDistance(end(), p) == 1)) {
			_path.add(p);
		} else if (PixelFunc.pixelDistance(end(), p) > 1) {
			//System.out.println("ADDING LINE from " + end() + " TO " + p);
			List<Point> line = PixelFunc.calcBrensenhamLine(end(), p);
			add(line);
		} else if(size() > 1 && PixelFunc.pixelDistance(end(), p) == 1) {
			// If the point before the end can connect directly to p, then remove the end (there is no need for it).
			// Eliminates corners in favor of diagonal movement and prevents doubled back lines
			if (PixelFunc.pixelDistance(_path.get(size() - 2), p) <= 1) {
				_path.remove(size() - 1);
				add(p);
			} else if (PixelFunc.pixelDistance(_path.get(size() - 2), p) > 1) {
				_path.add(p);
			}
		} else {
			// Duplicate point, don't append size() > 0 && PixelFunc.pixelDistance(end(), p) == 0
		}
	}
	
	public void add(int x, int y) {
		add(new Point(x, y));
	}
	
	public void add(int x, int y, double z) {
		add(new Point(x, y, z));
	}
		
	public void add(List<Point> points) {
		for(Point p : points) {
			//System.out.println("Adding POINT from LINE: " + p);
			add(p);
		}
	}
	
	public Point get(int index) {
		return _path.get(index);
	}
	
	public Point getNormalized(int index) {
		Point p = _path.get(index);
		return new Point(p.x() - min_x(), p.y() - min_y(), p.z());
	}
	
	public Point start() {
		return _path.get(0);
	}
	
	public Point end() {
		return _path.get(size() - 1);
	}
	
	public List<Point> start(int n) {
		return (size() >= n) ? _path.subList(0, n) : new ArrayList<Point>();
	}
	
	public List<Point> end(int n) {
		return (size() >= n) ? _path.subList(size() - n, size()) : new ArrayList<Point>();
	}
	
	public void clear() {
		_path.clear();
	}
	
	/**
	 * @return Length of the path (how many points)
	 */
	public int size() {
		return _path.size();
	}
	
	public int width() {
		return max_x() - min_x() + 1;
	}
	
	public int height() {
		return max_y() - min_y() + 1;
	}
	
	public Path clone() {
		Path path = new Path();
		for (int i = 0; i < size(); i++) {
			path.add(get(i).clone());
		}
		return path;
	}
	
	private int min_x() {
		return Collections.min(_path, Point.COMPARE_X).x();
	}
	
	private int max_x () {
		return Collections.max(_path, Point.COMPARE_X).x();

	}
	
	private int min_y () {
		return Collections.min(_path, Point.COMPARE_Y).y();
	}
	
	private int max_y () {
		return Collections.max(_path, Point.COMPARE_Y).y();
	}
	
	@Override
	public String toString() {

		StringBuilder print = new StringBuilder();
		char[] blanks = new char[width()*height()];
		Arrays.fill(blanks, '_');
		print.append(blanks);
		
		
		
		for(Point p : _path) {
			print.setCharAt((p.x() - min_x()) + width()*((p.y() - min_y())), '#');
		}
		print.setCharAt((start().x() - min_x()) + width()*((start().y() - min_y())), 'S');
		print.setCharAt((end().x() - min_x()) + width()*((end().y() - min_y())), 'E');
		
		for (int i = height() - 1; i > 0; i--) {
			print.insert(i*width(), '\n');
		}
		
		return print.toString();
	}
	
}
