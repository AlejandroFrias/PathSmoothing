package PathSmoother;

import java.util.Collection;

import PathUtil.Path;
import PathUtil.Point;

/**
 * An extension of Path Smoother that allows for smoothing a path along a map of weights.
 * 
 * 
 * @author ajf29510
 * @version July 2014
 */
public class WeightedPathSmoother extends PathSmoother {
	private static final double MIN_SMOOTH_RATE = 0.001;
	private static final int DEFAULT_ITERATIONS = 1;
	
	
	private WeightsMap _map;
	private double _path_weight;
	private double _smooth_path_weight;
	
	
	public WeightedPathSmoother(int[] path, int width, WeightsMap map) {
		this(path, width);
		_map = map;
		_path_weight = calcPathWeight(getPath());
		_smooth_path_weight = calcPathWeight(getSmoothPath());
	}
	
	public WeightedPathSmoother(Collection<Point> path, WeightsMap map) {
		this(path);
		_map = map;
		_path_weight = calcPathWeight(getPath());
		_smooth_path_weight = calcPathWeight(getSmoothPath());
	}
	
	public WeightedPathSmoother(Path path, WeightsMap map) {
		this(path);
		_map = map;
		_path_weight = calcPathWeight(getPath());
		_smooth_path_weight = calcPathWeight(getSmoothPath());
	}
	
	private WeightedPathSmoother(Collection<Point> path) {
		super(path);
		// Not to be used by public without a given weights map
	}

	private WeightedPathSmoother(Path path) {
		super(path);
		// Not to be used by public without a given weights map
	}
	
	private WeightedPathSmoother(int[] path, int width) {
		super(path, width);
		// Not to be used by public without a given weights map
	}
	
	public WeightsMap getMap() {
		return _map;
	}
	
	public void smoothWeighted(double tolerance) {
		smoothWeighted(tolerance, DEFAULT_ITERATIONS);
	}
	
	public void smoothWeighted(double tolerance, int max_iterations) {
		_path_weight = calcPathWeight(getPath());
		_smooth_path_weight = calcPathWeight(getSmoothPath());
		
		final double threshold = _path_weight * tolerance;
		double smoothing_rate = tolerance;
		
		
		int iter = 0;
		while (Math.abs(_path_weight - _smooth_path_weight) <= threshold && 
			   iter < max_iterations && 
			   smoothing_rate > MIN_SMOOTH_RATE) {
			
			smooth(smoothing_rate);
			_smooth_path_weight = calcPathWeight(getSmoothPath());
			if(Math.abs(_path_weight - _smooth_path_weight) > threshold) {
				undoSmooth();
				_smooth_path_weight = calcPathWeight(getSmoothPath());
				smoothing_rate /= 2;
			}
			iter++;
			
		}
	}
	
	public double calcPathWeight(Path path) {
		double total_weight = 0.0;
		Point p;
		for (int i = 0; i < path.size(); i++) {
			p = path.get(i);
			// TODO: check if weighted by weight or source values (probable weighted, right?)
			total_weight += _map.getSource(p.x(), p.y());
		}
		return total_weight;
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
