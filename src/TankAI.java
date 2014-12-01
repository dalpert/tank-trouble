
public class TankAI extends Tank {
	Tank target;
	public double angleToTarget=-1;
	//public String tankState = "ready";

	public TankAI(int player, double angle, double speed, double X, double Y, Tank target) {
		super(player, angle, speed, X, Y);
		this.target = target;
		this.type = "computer";
	}

	public void findTargetAngle() {

		double dX = target.getX() - getX();
		double dY = target.getY() - getY();

		if ((target.getY() == getY()) && (target.getX() > getX())) {
			angleToTarget = 90;
			dX = Math.sin(Math.toRadians(angleToTarget));
			dY = -Math.cos(Math.toRadians(angleToTarget));
		} else if ((target.getY() == getY()) && (target.getX() < getX())) {
			angleToTarget = 270;
			dX = Math.sin(Math.toRadians(angleToTarget));
			dY = -Math.cos(Math.toRadians(angleToTarget));
		} else if ((target.getX() == getX()) && (target.getY() > getY())) {
			angleToTarget = 180;
			dX = Math.sin(Math.toRadians(angleToTarget));
			dY = -Math.cos(Math.toRadians(angleToTarget));
		} else if ((target.getX() == getX()) && (target.getY() < getY())) {
			angleToTarget = 0;
			dX = Math.sin(Math.toRadians(angleToTarget));
			dY = -Math.cos(Math.toRadians(angleToTarget));
		} else if ((target.getY()>getY())&&(target.getX()>getX())) {
			angleToTarget = 180-Math.toDegrees(Math.atan(dX/dY));
			dX = Math.sin(Math.toRadians(angleToTarget));
			dY = -Math.cos(Math.toRadians(angleToTarget));
		} else if ((target.getY()<getY())&&(target.getX()<getX())) {
			angleToTarget = 360-Math.toDegrees(Math.atan(dX/dY));
			dX = Math.sin(Math.toRadians(angleToTarget));
			dY = -Math.cos(Math.toRadians(angleToTarget));
		} else if ((target.getY()<getY())&&(target.getX()>getX())) {
			angleToTarget = 360-Math.toDegrees(Math.atan(dX/dY));
			dX = Math.sin(Math.toRadians(angleToTarget));
			dY = -Math.cos(Math.toRadians(angleToTarget));
		} else if ((target.getY()>getY())&&(target.getX()<getX())) {
			angleToTarget = 180-Math.toDegrees(Math.atan(dX/dY));
			dX = Math.sin(Math.toRadians(angleToTarget));
			dY = -Math.cos(Math.toRadians(angleToTarget));
		}

		if (angleToTarget>360) {
			angleToTarget%=360;
		}
		if (angleToTarget<0) {
			angleToTarget+=360;
		}
	}

	public void setRotationToTarget() {
		double difference = (angleToTarget-getAngle());
		
		if (Math.round(difference) == 0) {
			//setTankState("shooting");
			setRightKeyPressed(false);
			setLeftKeyPressed(false);
			setShootKeyPressed(true);
		} else if (getAngle() != (int)angleToTarget) {
			if (difference < 0) {
				difference+=360;
			}
			if ((difference>180)||(difference<0)) {
				setTankState("turning left");
				setLeftKeyPressed(true);
				setRightKeyPressed(false);
				setShootKeyPressed(false);
			} else if ((difference<=180)&&(difference>=0)) {
				setTankState("turning right");
				setRightKeyPressed(true);
				setLeftKeyPressed(false);
				setShootKeyPressed(false);
			}
		}
	}

	public String getTankState() {
		return tankState;
	}

	public void setTankState(String tankState) {
		this.tankState = tankState;
	}



}
