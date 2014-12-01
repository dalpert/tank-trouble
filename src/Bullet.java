import java.awt.Color;
import java.util.Timer;

import acm.graphics.GOval;


public class Bullet extends GOval {
	private double speed;
	private double radius;
	private double angle;
	Timer bulletTimer;
	
	public static final int BULLET_TIME = 5000;

	public Bullet(double X, double Y, double radius, double angle, double speed) {
		super(X, Y , radius, radius);
		this.angle = angle;
		this.setRadius(radius);
		this.speed = speed;
		this.setFilled(true);
		this.setFillColor(Color.black);
	}
	
	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = Math.abs(angle%360);
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public void move() {
		double dX = 0;
		double dY = 0;
		
		dX = Math.sin(Math.toRadians(angle)) * speed;
		dY = -Math.cos(Math.toRadians(angle)) * speed;
		
		this.setLocation(this.getX()+dX, this.getY()+dY);

	}
	public void collideVertical() {
		if ((angle>=0)&&(angle<=90)) {
			setAngle(360-angle);
		} else if ((angle>90)&&(angle<180)) {
			setAngle(360-angle);
		} else if ((angle>=180)&&(angle<=270)) {
			setAngle(360-angle);
		} else if ((angle>270)&&(angle<360)) {
			setAngle(360-angle);
		}
	}
	public void collideHorizontal() {
		if ((angle>=0)&&(angle<=90)) { //checked
			setAngle(180-angle);
		} else if ((angle>90)&&(angle<180)) { //checked
			setAngle(180-angle);
		} else if ((angle>=180)&&(angle<=270)) { //checked
			setAngle(360-(angle%180));
		} else if ((angle>270)&&(angle<360)) {                                              
			setAngle(360-(angle%180));
		}
	}
	public void checkCollision(int width, int height) {
		if (this.getX() > width-this.getWidth()) {
			collideVertical();
			this.setX(width-this.getWidth());
		} else if (this.getX() < 0) {
			collideVertical();
			this.setX(0);
		} else if (this.getY() > height-this.getHeight()) {
			collideHorizontal();
			this.setY(height-this.getHeight());
		} else if (this.getY() < 0) {
			collideHorizontal();
			//System.out.println("colliding top");
			this.setY(0);
		} 
	}
	
	public void checkCollision(double left, double right, double top, double bottom) {
		double bulletLeft = (getX()-(getWidth()/2));
		double bulletRight = (getX()+(getWidth()/2));
		double bulletTop = (getY()-(getHeight()/2));
		double tankBottom = (getY()+(getHeight()/2));
		double bulletWidth = getRadius();
		double bulletHeight = getRadius();
		double closeness = 0;
		
		
		
		if ((tankBottom>top)&&(tankBottom<bottom)&&(bulletLeft+bulletWidth*closeness>left)&&(bulletRight-bulletWidth*closeness<right)) {
			//System.out.println("topSide");
			setY(top-bulletHeight);
			collideHorizontal();
		} else if ((bulletTop>top)&&(bulletTop<bottom)&&(bulletLeft+bulletWidth*closeness>left)&&(bulletRight-bulletWidth*closeness<right)) {
			//System.out.println("bottomSide");
			setY(bottom+bulletHeight);
			collideHorizontal();
		} else if ((bulletRight>left)&&(bulletRight<right)&&(bulletTop+bulletHeight*closeness>top)&&(tankBottom-bulletHeight*closeness<bottom)) {
			//System.out.println("leftSide");
			setX(left-bulletWidth);
			collideVertical();
		} else if ((bulletLeft>left)&&(bulletLeft<right)&&(bulletTop+bulletHeight*closeness>top)&&(tankBottom-bulletHeight*closeness<bottom)) {
			//System.out.println("rightSide");
			setX(right+bulletWidth);
			collideVertical();
		}	
	}
	public void advanceOneStep(Tank tank) {
		this.move();
		this.checkCollision(TankTrouble.WINDOW_WIDTH,TankTrouble.WINDOW_HEIGHT);
		for (int count=0;count<TankTrouble.objectList.size();count++) {
			checkCollision(TankTrouble.objectList.get(count).getLeft(), TankTrouble.objectList.get(count).getRight(), TankTrouble.objectList.get(count).getTop(), TankTrouble.objectList.get(count).getBottom());
		}
		if (TankTrouble.tank1.contains(getX()-(getWidth()/2), getY()-(getWidth()/2))) {
			TankTrouble.tank1.explode();
			this.setVisible(false);
			this.speed=0;
			this.setLocation(0, 0);
			TankTrouble.p2++;
			TankTrouble.p1Score.setLabel("Daniel: "+TankTrouble.p1);
			TankTrouble.p2Score.setLabel("Luca: "+TankTrouble.p2);
		}
		if (TankTrouble.tank2.contains(getX()-(getWidth()/2), getY()-(getWidth()/2))) {
			TankTrouble.tank2.explode();
			this.setVisible(false);
			this.speed=0;
			this.setLocation(0, 0);
			TankTrouble.p1++;
			TankTrouble.p1Score.setLabel("Daniel: "+TankTrouble.p1);
			TankTrouble.p2Score.setLabel("Luca: "+TankTrouble.p2);
		}
		
		//bulletTimer = new Timer();
		//bulletTimer.schedule(new BulletTimerTask(tank, this), BULLET_TIME);
	}
	public void setX(double X) {
		this.setLocation(X,this.getY());
	}
	public void setY(double Y) {
		this.setLocation(this.getX(),Y);
	}
	
//	public String toString() {
//		return "Bullet
//	}
}
