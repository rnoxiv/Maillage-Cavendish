package maillagecavendish.utils;

public class Element {
    Point p1, p2, p3;
    Segment s1, s2, s3;
    int id;
    Element nextElement;
    
    public Element(Segment s1, Segment s2, Segment s3, int i){
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.p1 = s1.getOrigine();
        this.p2 = s2.getOrigine();
        this.p3 = s3.getDestination();
        this.id = i;
        this.nextElement =  null;
    }

    public Point getP1() {
        return p1;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    public Point getP2() {
        return p2;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }

    public Point getP3() {
        return p3;
    }

    public void setP3(Point p3) {
        this.p3 = p3;
    }

    public Segment getS1() {
        return s1;
    }

    public void setS1(Segment s1) {
        this.s1 = s1;
    }

    public Segment getS2() {
        return s2;
    }

    public void setS2(Segment s2) {
        this.s2 = s2;
    }

    public Segment getS3() {
        return s3;
    }

    public void setS3(Segment s3) {
        this.s3 = s3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Element getNextElement() {
        return nextElement;
    }

    public void setNextElement(Element nextElement) {
        this.nextElement = nextElement;
    }
    
    
}
