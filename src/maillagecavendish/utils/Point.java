package maillagecavendish.utils;

public class Point {
    public static int PREC = 10000;
    
    double x, y, type;
    int id;
    
    public Point(double nx, double ny, double t, int i){
        this.x = nx;
        this.y = ny;
        this.type = t;
        this.id = i;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getType() {
        return type;
    }

    public void setType(double type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
