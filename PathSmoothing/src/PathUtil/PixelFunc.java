package PathUtil;

import java.util.ArrayList;
import java.util.List;

public class PixelFunc {

	private PixelFunc() {
		//disabled constructor for static function class
	}
	
    /**
     * Credit for Brensenham line implementation to http://tech-algorithm.com/articles/drawing-line-using-bresenham-algorithm/
     * 
     * @param x X-Pos of start point (observer)
     * @param y Y-Pos of start point (observer)
     * @param x2 X-Pos of end point (target)
     * @param y2 Y-Pos of end point (target)
     * @return List of Points along the rasterized line from (x, y) to (x2, y2)
     */
    public static List<Point> calcBrensenhamLine(int x, int y, int x2, int y2) {
        List<Point> line = new ArrayList<>();
        int w = x2 - x ;
        int h = y2 - y ;
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
        if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
        if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
        if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
        int longest = Math.abs(w) ;
        int shortest = Math.abs(h) ;
        if (!(longest>shortest)) {
            longest = Math.abs(h) ;
            shortest = Math.abs(w) ;
            if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
            dx2 = 0 ;            
        }
        int numerator = longest >> 1 ;
        for (int i=0;i<=longest;i++) {
            line.add(new Point(x, y));
            numerator += shortest ;
            if (!(numerator<longest)) {
                numerator -= longest ;
                x += dx1 ;
                y += dy1 ;
            } else {
                x += dx2 ;
                y += dy2 ;
            }
        }
        return line;
    }
    
    public static List<Point> calcBrensenhamLine(Point p1, Point p2) {
    	return calcBrensenhamLine(p1.x(), p1.y(), p2.x(), p2.y());
    }

    
	public static int pixelDistance(Point p1, Point p2) {
		return Math.max(Math.abs(p1.x() - p2.x()), Math.abs(p1.y() - p2.y()) );
	}
	
	public static List<Point> listify(int[] points, int width) {
		List<Point> list = new ArrayList<Point>();
		int x, y;
		for (int i = 0; i < points.length; i++) {
			x = points[i] % width;
			y = points[i] / width;
			list.add(new Point(x, y));
		}
		return list;
	}
}
