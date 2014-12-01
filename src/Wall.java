import java.awt.Color;
import acm.graphics.GPolygon;
public class Wall extends GPolygon {
	public static final int WALL_WIDTH = 10;
	private double gradient = 0;
	private double inverseGradient = 0;
	private double left=0;
	private double right=0;
	private double top=0;
	private double bottom=0;
	private String direction = null;
	private double X1;
	private double Y1;
	private double X2;
	private double Y2;
	
	public Wall (double x1, double y1, double x2, double y2) {
		super();
		setX1(x1);
		setY1(y1);
		setX2(x2);
		setY2(y2);
		
		double dX=0;
		double dY=0;
		
		gradient = ((x1-x2)/(y1-y2));
		inverseGradient = -1/gradient;
		dX = Math.sin(Math.atan(inverseGradient))*(WALL_WIDTH/2);
		dY = Math.cos(Math.atan(inverseGradient))*(WALL_WIDTH/2);
		
		if (y1==y2) { //horizontal wall
			setLeft(x1);
			setRight(x2);
			setTop(y1 - (WALL_WIDTH/2));
			setBottom(y1 + (WALL_WIDTH/2));
			setDirection("horizontal");
		} else if (x1==x2) { //vertical wall
			setLeft(x1 - (WALL_WIDTH/2));
			setRight(x1 + (WALL_WIDTH/2));
			setTop(y1);
			setBottom(y2);
			setDirection("vertical");
		}
		
		addVertex(x1+dX,y1+dY);
		addVertex(x1-dX,y1-dY);
		addVertex(x2-dX,y2-dY);
		addVertex(x2+dX,y2+dY);
		
		this.setFilled(true);
		this.setFillColor(Color.black);
		
	}
	public double getLeft() {
		//System.out.println(left);
		return left;
	}
	public void setLeft(double left) {
		this.left = left;
	}
	public double getRight() {
		return right;
	}
	public void setRight(double right) {
		this.right = right;
	}
	public double getBottom() {
		return bottom;
	}
	public void setBottom(double bottom) {
		this.bottom = bottom;
	}
	public double getTop() {
		return top;
	}
	public void setTop(double top) {
		this.top = top;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public double getX1() {
		return X1;
	}
	public void setX1(double x1) {
		X1 = x1;
	}
	public double getY1() {
		return Y1;
	}
	public void setY1(double y1) {
		Y1 = y1;
	}
	public double getX2() {
		return X2;
	}
	public void setX2(double x2) {
		X2 = x2;
	}
	public double getY2() {
		return Y2;
	}
	public void setY2(double y2) {
		Y2 = y2;
	}
	
	public String toString() {
		return ("Wall location = ("+getX1()+", "+getY1()+") ("+getX2()+", "+getY2()+")");
	}
	
}
