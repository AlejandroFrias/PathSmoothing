package PathUtil;

import java.util.Comparator;

/**
 * Simple (x, y, z) point class
 * 
 * @author ajf29510
 * @version July 2014
 */
public class Point {
	public static XComparator COMPARE_X = new XComparator();
	public static YComparator COMPARE_Y = new YComparator();
	public static ZComparator COMPARE_Z = new ZComparator();
	
	private final int _x;
	private final int _y;
	private double _z;
	
	public Point(int x, int y, double z) {
		_x = x;
		_y = y;
		_z = z;
	}
	
	public Point(int x, int y) {
		_x = x;
		_y = y;
		_z = 0.0;
	}
	
	public int x() {
		return _x;
	}
	
	public int y() {
		return _y;
	}
	
	public double z() {
		return _z;
	}
	
	public void setZ(double z) {
		_z = z;
	}
	
	public Point clone() {
		return new Point(_x, _y, _z);
	}
	
	@Override
	public String toString() {
		return "(" + _x + ", " + _y + ", " + _z + ")";
	}
	
	@Override
	public int hashCode() {
        return toString().hashCode();
    }

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Point))
			return false;
		if (obj == this)
			return true;

		Point rhs = (Point) obj;
		return rhs.toString().equals(toString());
    }

}

class XComparator implements Comparator<Point> {
    @Override
    public int compare(Point o1, Point o2) {
        return Integer.compare(o1.x(), o2.x());
    }
}

class YComparator implements Comparator<Point> {
    @Override
    public int compare(Point o1, Point o2) {
        return Integer.compare(o1.y(), o2.y());
    }
}

class ZComparator implements Comparator<Point> {
    @Override
    public int compare(Point o1, Point o2) {
        return Double.compare(o1.z(), o2.z());
    }
}

