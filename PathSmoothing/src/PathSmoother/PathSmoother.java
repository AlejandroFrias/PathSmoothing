package PathSmoother;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
//import java.util.Random;


import PathUtil.Path;
import PathUtil.PixelFunc;
import PathUtil.Point;


public class PathSmoother {
	
	private Path _path;
	private Path _previous_smooth_path;
	private Path _smooth_path;
	
	public PathSmoother(Collection<Point> path) {
		_path = new Path(path);
		_smooth_path = new Path(path);
		_previous_smooth_path = new Path(path);
	}

	public PathSmoother(Path path) {
		_path = path.clone();
		_smooth_path = path.clone();
		_previous_smooth_path = path.clone();
	}
	
	public PathSmoother(int[] path, int width) {
		_path = new Path(path, width);
		_smooth_path = new Path(path, width);
		_previous_smooth_path = new Path(path, width);
	}
	
	/**
	 * Smoothes by averaging nearest neighbors' values to determine new value
	 * 
	 * Do not call smoothing on a path of length 2 or less.
	 * 
	 * @param nearest How many neighbors to look at
	 */
	public void smooth(int nearest) {

		Path new_path = new Path();
		
		// Make an even number of neighbors to look at
		nearest = nearest/2 * 2;
		int size = _smooth_path.size();
		int half = nearest/2;
		
		new_path.add(_smooth_path.start());
		for (int i = 1; i < size - 1; i++) {
			float x = 0;
			float y = 0;
			int left = Math.max(0, i - half);
			int right = Math.min(size - 1, i + half);
			
			/*
			// Keeps the points closer to start and end looking at a the same number of neighbors on each side
			int left_dist = i - left;
			int right_dist = right - i;
			
			int dist = Math.min(left_dist, right_dist);
			
			int left_bound = i - dist;
			int right_bound = i + dist;
			*/
			
			int count = 0;
			Point p;
			for (int j = left; j < right; j++) {
				p = _smooth_path.get(j);
				x += p.x();
				y += p.y();
				count++;
			}
			x /= (float) count;
			y /= (float) count;
			
			Point next_point = new Point(Math.round(x), Math.round(y), _smooth_path.get(i).z());
			//System.out.println("ADDING POINT: " + next_point + " AFTER POINT: " + new_path.end());
			new_path.add(next_point);
		}
		new_path.add(_smooth_path.end());
		
		_previous_smooth_path = _smooth_path;
		_smooth_path = new_path;
	}
	
	/**
	 * Smoothes by averaging nearest neighbors' values to determine new value
	 * 
	 * @param nearestPercent What percentage of the path length is considered as a neighbor
	 */
	public void smooth(double nearestPercent) {
		
		int nearest = (int) Math.ceil(_smooth_path.size() * nearestPercent);
		if (nearest < 6) nearest = 6;
		smooth(nearest);
	}
	
	/**
	 * Eliminates unnecessary pixel corners that can be made with a simple diagonal
	 * 
	 * NOTE: DEPRECATED by the way points are added
	 */
	public void simpleCornerElimination() {
		int size = _smooth_path.size();
		Path new_path = new Path();
		new_path.add(_smooth_path.start());
		for (int i = 1; i < size - 1; i++) {
			Point p1 = _smooth_path.get(i - 1);
			Point p2 = _smooth_path.get(i + 1);
			//System.out.println("DIST from " + p1 + " to " + p2 + " = " + Point.pixelDistance(p1,p2));

			if(PixelFunc.pixelDistance(p1, p2) > 1) {
				new_path.add(_smooth_path.get(i));
			} else {
				new_path.add(p2);
				i++;
			}
		}
		new_path.add(_smooth_path.end());
		_smooth_path = new_path;
	}
	
	public void printPath() {
		System.out.println(_path);
	}
	
	public void printOptimalPath() {
		System.out.println(_smooth_path);
	}

	public Path getPath() {
		return _path;
	}

	public Path getSmoothPath() {
		return _smooth_path;
	}
	
	public void reset() {
		_smooth_path = _path.clone();
		_previous_smooth_path = _path.clone();
	}
	
	// Undoes the last the smoothing (only works once)
	public void undoSmooth() {
		_smooth_path = _previous_smooth_path;
	}
	
	// TODO: keep track of original position of each point in the Z field
	public void deSmooth(double rate) {
		// TODO: move each one back towards its original position by the rate * distance to orig pos
	}
	
	public static void main(String[] args) {
		// test deep copy
		//Random rn = new Random();
		
		List<Point> path = new ArrayList<>();
		int size = 10;
		for (int i = 0; i < size - 1; i++) {
			path.add(new Point(i, 0, 0.0));
		}
		
		for (int i = 1; i < size - 1; i++) {
			path.add(new Point(size - 1, i, 0.0));
		}
		
		for (int i = size - 2; i > 0; i--) {
			path.add(new Point(i, size - 1, 0.0));
		}
		
		for (int i = 0; i < size - 1; i++) {
			path.add(new Point(0, i + size, 0.0));
		}
		
		for (int i = 1; i < size; i++) {
			path.add(new Point(i,  2*size - 1, 0.0));
		}
		
		PathSmoother nns = new PathSmoother(path);
		
		System.out.println("PATH SIZE: " + nns.getPath().size());
		nns.printPath();
		
		
		for(int i = 1; i < 15; i++){
			nns.smooth(6);
			System.out.println("SMOOTH: " + i);
			System.out.println("OPTIMAL PATH SIZE: " + nns.getSmoothPath().size());
			
			nns.printOptimalPath();
			
			nns.simpleCornerElimination();
			
			System.out.println("CORNER CLEAN: " + i);
			System.out.println("OPTIMAL PATH SIZE: " + nns.getSmoothPath().size());
			
			nns.printOptimalPath();
		}

		
	}

}
