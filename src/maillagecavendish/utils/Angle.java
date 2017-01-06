/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maillagecavendish.utils;

public class Angle {
    public static int PREC = 10000;
    
    private Point prevP, centerP, nextP;
    private Segment prevSegment, nextSegment;
    private double alphaDeg, alphaRad, signe;
    private int id;
    private Angle nextAngle;
    
    public Angle(Segment s1, Segment s2, int i){
        this.prevSegment = s1;
        this.nextSegment = s2;
        this.prevP = s1.getOrigine();
        this.centerP = s1.getDestination();
        this.nextP = s2.getDestination();
        double pos = calculateAngle(prevSegment, nextSegment);
        this.alphaRad = this.signe *((Math.acos(pos)));
        this.alphaDeg = (alphaRad*180/Math.PI);
        if(this.alphaDeg < 0){
            this.alphaDeg += 360;
        }
        this.id = i;
    }
    
    private double calculateAngle(Segment s1, Segment s2){
        double x1 = s1.getX();
        double y1 = s1.getY();
        double x2 = -s2.getX();
        double y2 = -s2.getY();
        this.signe = 1;
        if(x1*y2-y1*x2 != 0)
            this.signe = -Math.signum((x1*y2-y1*x2));
        double l1 = s1.getLongueur();
        double l2 = s2.getLongueur();
        double angle = ((x1*x2) + (y1*y2))/(l1*l2);
        return angle;
    }

    public Segment getPrevSegment() {
        return prevSegment;
    }

    public void setPrevSegment(Segment prevSegment) {
        this.prevSegment = prevSegment;
    }

    public Segment getNextSegment() {
        return nextSegment;
    }

    public void setNextSegment(Segment nextSegment) {
        this.nextSegment = nextSegment;
    }
    
    public Point getPrevP() {
        return prevP;
    }

    public void setPrevP(Point prevP) {
        this.prevP = prevP;
    }

    public Point getCenterP() {
        return centerP;
    }

    public void setCenterP(Point centerP) {
        this.centerP = centerP;
    }

    public Point getNextP() {
        return nextP;
    }

    public void setNextP(Point nextP) {
        this.nextP = nextP;
    }

    public double getAlphaRad() {
        return alphaRad;
    }

    public void setAlphaRad(double alphaRad) {
        this.alphaRad = alphaRad;
    }

    public double getAlphaDeg() {
        return alphaDeg;
    }

    public void setAlphaDeg(double alphaDeg) {
        this.alphaDeg = alphaDeg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public Angle getNextAngle() {
        return nextAngle;
    }

    public void setNextAngle(Angle nextAngle) {
        this.nextAngle = nextAngle;
    }
    
}
