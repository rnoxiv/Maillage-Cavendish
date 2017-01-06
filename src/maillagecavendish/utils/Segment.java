package maillagecavendish.utils;

public class Segment {
    public static int PREC = 10000;
    
    Point origine, destination;
    int id, type;
    double l, X, Y;
    
    Segment nextSegment;
    
    public Segment(Point po, Point pd, int i){
        this.origine = po;
        this.destination = pd;
        this.id = i;
        this.nextSegment = null;

        this.X = Math.round((pd.getX() - po.getX())*PREC);
        this.Y = Math.round((pd.getY() - po.getY())*PREC);
        this.X = this.X / PREC;
        this.Y = this.Y / PREC;
        
        this.l = Math.sqrt(Math.pow(this.X,2) + Math.pow(this.Y,2));
        if(po.getType() == 1 && pd.getType() == 1)
            this.type = 1;
        else
            this.type = 2;
    }
    
    public double getSigneX(){
        if(this.X != 0)
            return Math.signum(this.X*100);
        else
            return 1;
    }
    
    public double getSigneY(){
        if(this.Y != 0)
            return Math.signum(this.Y);
        return 0;
    }
    
    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }
    
    public void chekType(){
        if(origine.getType() == 1 && destination.getType() == 1)
            this.type = 1;
        else
            this.type +=1;
    }
    
    public int getType() {
        return type;
    }

    public void setType(int t){
        this.type = t;
    }

    public Point getOrigine() {
        return origine;
    }

    public double getLongueur() {
        this.l = Math.round(Math.sqrt(Math.pow(this.X,2) + Math.pow(this.Y,2))*PREC);
        this.l = this.l / PREC;
        return l;
    }

    public void setOrigine(Point origine) {
        this.origine = origine;
        this.X = Math.round((this.destination.getX() - this.origine.getX())*PREC);
        this.Y = Math.round((this.destination.getY() - this.origine.getY())*PREC);
        this.X = this.X / PREC;
        this.Y = this.Y / PREC;
    }

    public Point getDestination() {
        return destination;
    }

    public void setDestination(Point destination) {
        this.destination = destination;
        this.X = Math.round((this.destination.getX() - this.origine.getX())*PREC);
        this.Y = Math.round((this.destination.getY() - this.origine.getY())*PREC);
        this.X = this.X / PREC;
        this.Y = this.Y / PREC;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Segment getNextSegment() {
        return nextSegment;
    }

    public void setNextSegment(Segment nextSegment) {
        this.nextSegment = nextSegment;
    }
    
    
}