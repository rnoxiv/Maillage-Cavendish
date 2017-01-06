package maillagecavendish;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import maillagecavendish.utils.*;


class MaillageCavendish {
    public static final String FILECAL = "MPmaillecal.cal";
    
    public static final String POINTS = "$points";
    public static final String COURBES = "$courbes";
    public static final String FIN = "////";
    public static final String SEG = "segment";
    
    public static final int PREC = 10000;
    
    public static final double PAS = 0.1; //0.05

    private ArrayList<Point> points = new ArrayList<>();
    private ArrayList<Segment> segments = new ArrayList<>();
    private ArrayList<Segment> contour = new ArrayList<>();
    private ArrayList<Angle> angles = new ArrayList<>();
    private ArrayList<Element> elements = new ArrayList<>();
    
    private Angle smallAngle;
    private int numSegments = 0;
    private int numPointStart = 0;
    
    public MaillageCavendish(String fileName){
        try{
            File file = new File(fileName);
            FileInputStream ft = new FileInputStream(file);
            DataInputStream in = new DataInputStream(ft);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strline;
            boolean getPoints = false;
            boolean getCourbes = false;
            boolean firstLap = true;
            boolean finished = false;
          
            while((strline = br.readLine()) != null && !finished){
               
                ArrayList<String> words = new ArrayList<>(Arrays.asList(strline.split(" ")));
                for(int i = 0; i < words.size(); i++){
                    if (words.get(i).isEmpty() || words.get(i) == ""){
                        words.remove(i);
                    }
                    if(words.size()>0){
                        switch(words.get(i)){
                            case POINTS : getPoints = true; firstLap = true; break;
                            case COURBES : getCourbes = true; getPoints = false; firstLap = true; break;
                            case FIN : System.out.println("test fin");finished = true; getCourbes = false; break;
                            default : break;
                        }
                    }
                }
                
                if(getPoints){
                    if(firstLap){
                        firstLap = false;
                        strline = br.readLine();
                    }else{
                        Point point = new Point(Double.parseDouble(words.get(2)), Double.parseDouble(words.get(3)), 
                                                Double.parseDouble(words.get(4)), Integer.parseInt(words.get(1)));
                        points.add(point);
                        numPointStart++;
                    }
                }else if(getCourbes){
                    //System.out.println(words);
                    if(firstLap){
                        firstLap = false;
                        strline = br.readLine();
                        words = new ArrayList<String>(Arrays.asList(strline.split(" ")));
                    }
                    Point po = points.get(Integer.parseInt(words.get(2))-1);
                    Point pd = points.get(Integer.parseInt(words.get(1))-1);
                    Segment segment = new Segment(po, pd ,Integer.parseInt(words.get(4)));
                    segments.add(segment);
                }
                
            }
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        run();
        
        try{
            File newFile = new File("./rsc/" + FILECAL);
            FileWriter fileWriter = new FileWriter(newFile);
            
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat hourFormat = new SimpleDateFormat("HH/mm/ss");
            Date date = new Date();
            
            NumberFormat formatter = new DecimalFormat("0.0000000000E0");
            
            fileWriter.write("");
            fileWriter.append("\nRDM - Eléments finis");
            fileWriter.append("\nCalcul des Structures par la Méthode des Eléments Finis");
            fileWriter.append("\n");
            fileWriter.append("\nVersion  - 6.14 - 22 Janvier 2002");
            fileWriter.append("\n");
            fileWriter.append("\nUtilisateur : Ecole Nationale d'Ingénieurs - BREST");
            fileWriter.append("\n");
            fileWriter.append("\n$debut du fichier");
            fileWriter.append("\n$version");
            fileWriter.append("\n6.14");
            fileWriter.append("\n$SI unites");
            fileWriter.append("\n$nom du fichier");
            fileWriter.append("\n" + FILECAL);
            fileWriter.append("\n$date");
            fileWriter.append("\n" + dateFormat.format(date));
            fileWriter.append("\n$HEURE");
            fileWriter.append("\n" + hourFormat.format(date));
            fileWriter.append("\n$PROBLEME");
            fileWriter.append("\nTHPLAN");
            fileWriter.append("\n$NOEUDS");
            fileWriter.append("\n" + (points.size()-1));
            for(int i = 0; i < points.size(); i++){
                fileWriter.append("\n" + points.get(i).getId() + " " + formatter.format(points.get(i).getX()).replace(",",".") + " " + formatter.format(points.get(i).getY()).replace(",",".") + " 1 " + (int)points.get(i).getType());
            }
            fileWriter.append("\n$limites de zones");
            fileWriter.append("\n6");
            fileWriter.append("\n  1    1    2");
            fileWriter.append("\n  2    3    4");
            fileWriter.append("\n  3    4    1");
            fileWriter.append("\n  4    5    6");
            fileWriter.append("\n  5    5    2");
            fileWriter.append("\n  6    6    3");
            fileWriter.append("\n$points a mailler");
            for(int i = 0; i < numPointStart; i++){
                fileWriter.append("\n" + points.get(i).getId() + " " + formatter.format(points.get(i).getX()).replace(",",".") + " " + formatter.format(points.get(i).getY()).replace(",","."));
            }
            fileWriter.append("\n0");
            fileWriter.append("\n$ELEMENTS");
            fileWriter.append("\n" + (elements.size()-1));
            for(int i = 0; i < elements.size(); i++){
                fileWriter.append("\n   " + (i+1) + " TRI3 1   1  11  11     " + elements.get(i).getP1().getId() + "     " + elements.get(i).getP2().getId() + "     " + elements.get(i).getP3().getId());
            }
            fileWriter.append("\n$materiaux");
            fileWriter.append("\n11");
            fileWriter.append("\nMOD  2.100000E+11");
            fileWriter.append("\nPOI  3.000000E-01");
            fileWriter.append("\nMAS  7.800000E+03");
            fileWriter.append("\nDIL  1.300000E-05");
            fileWriter.append("\nCON  5.000000E+01");
            fileWriter.append("\nLIM  2.500000E+08");
            fileWriter.append("\nCAP  4.500000E+02");
            fileWriter.append("\nNOM Acier");
            fileWriter.append("\n///");
            fileWriter.append("\n0");
            fileWriter.append("\n$epaisseurs");
            fileWriter.append("\n11 0.00000E+00");
            fileWriter.append("\n0");
            fileWriter.append("\n$fin du fichier");
            
            fileWriter.close();
            FileReader fileReader = new FileReader(newFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            fileReader.close();
            bufferedReader.close();
            System.out.println("Done");
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
    
    public void run(){
        
        ordonnancement(segments.get(0), 0);
        checkSegs();
        for(int i = 0; i < contour.size(); i++){
            int next = i + 1;
            if(i == (contour.size() - 1))
                next = 0;
            segmentation(contour.get(i), contour.get(next));
        }
        
        
        System.out.println("p : " + points.size() + " / s : " + segments.size() + " / a : " + angles.size());
        for(int i = 0; i < segments.size(); i++){
            System.out.println("id :" + i + " origine : x - " + 
                    segments.get(i).getOrigine().getX() + " / y - " + segments.get(i).getOrigine().getY() + " destination : x - " + 
                    segments.get(i).getDestination().getX() + " / y - " + segments.get(i).getDestination().getY());
        }
        
        boolean finished = false;
        while(!finished){
            System.out.println("NOUVELLE BOUCLE!!!!!");
            checkSegs();
            for(int i = 0 ; i < contour.size(); i++){
                System.out.println("P1 : " + contour.get(i).getOrigine().getX() + " / " + contour.get(i).getOrigine().getY() + " P2 : " + contour.get(i).getDestination().getX() + " / " + contour.get(i).getDestination().getY());
            }
            initAngles();
            checkSmallAngle();
            //System.out.println(smallAngle.getAlphaDeg());
            createElements();
            //System.out.println(contour.size());
            if(contour.size() <= 2)
                finished = true;
            angles = new ArrayList<>();
            contour = new ArrayList<>();
        }
        
        /*for(int i = 0; i < points.size(); i++){
            System.out.println("id : " + i + " type : " + points.get(i).getType() + " x : " + points.get(i).getX() + " y : " + points.get(i).getY());
        }*/
        
    }
    
    public void checkSegs(){
        for(int i = 0; i < segments.size(); i++){
            segments.get(i).chekType();
            if(segments.get(i).getType() == 1){
                contour.add(segments.get(i));
            }
        }
    }
    
    public void ordonnancement(Segment s, int index){
        if(index < segments.size()){
             //System.out.println(s.getOrigine().getId() + " / " + s.getDestination().getId());
            boolean found = false;
            int sId = segments.indexOf(s);

            Point or = s.getOrigine();
            Point dest = s.getDestination();

            for(int i = 0; i < segments.size(); i++){
                if(segments.get(i).getOrigine() == dest){
                    found = true;
                    Collections.swap(segments,i, index);
                    if(index == 0)
                        Collections.swap(segments,sId,segments.size()-1);
                    break;
                }
            }
            if(!found){
                for(int i = 0; i < segments.size(); i++){
                    if(segments.get(i).getDestination() == dest && segments.get(i) != s){
                        segments.get(i).setDestination(segments.get(i).getOrigine());
                        segments.get(i).setOrigine(dest);
                        if(i != index){
                            Collections.swap(segments,i, index);
                        }
                        if(index == 0)
                            Collections.swap(segments,sId,segments.size()-1);
                        break;
                    }
                }
            }
            index++;
            ordonnancement(segments.get(index-1), index);
        }
    }
    
    public void initAngles(){
        for(int i = 0; i < contour.size(); i++){
            int next = i+1;
            boolean found = false;
            for(int j = 0; j < contour.size(); j++){
                //System.out.println("trouvé! " + segments.get(i) + " - " + segments.get(i).getType() + " / " + segments.get(j) + " - " + segments.get(j).getType());
                if(contour.get(j).getOrigine() == contour.get(i).getDestination()){
                    found = true;
                    next = j; 
                    break;
                }
            }
            if(!found){
                System.out.println("something is fucked up : " + segments.get(i));
            }

            if(found){
                Angle angle = new Angle(contour.get(i), contour.get(next), i);
                angles.add(angle);
            }
        }
     }
    
    public void checkSmallAngle(){
        smallAngle = angles.get(0);
        for(int i = 1; i < angles.size(); i++){
                if(angles.get(i).getAlphaDeg() <= smallAngle.getAlphaDeg()){
                    smallAngle = angles.get(i);
                }/*else if(angles.get(i).getAlphaDeg() == smallAngles.get(0).getAlphaDeg()){
                    smallAngles.add(angles.get(i));
                }*/
        }
    }
    
    public void createElements(){
        int idAngle = angles.indexOf(smallAngle);
        double angle = smallAngle.getAlphaDeg();
        Point ori = smallAngle.getPrevP();
        Point mid = smallAngle.getCenterP();
        Point dest = smallAngle.getNextP();
        Segment A = smallAngle.getPrevSegment();
        Segment B = smallAngle.getNextSegment();
        
        System.out.println("Taille : " + (segments.size() - numSegments) + " segment A : " + (segments.indexOf(A)) + " / segment B : " + (segments.indexOf(B)));
        
        //System.out.println("points : " + ori.getX() + " - " + ori.getY() + " / " + mid.getX() + "-" + mid.getY() + " / " + dest.getX() + "-" + dest.getY());

        if(angle <= 90.0){
            Segment C = new Segment(ori, dest, segments.size());    
            segments.add(segments.indexOf(A),C);
            mid.setType(2);
            Element e = new Element(A, B, C, elements.size());
            elements.add(e);
        }else if(angle > 90.0 && angle <= 120.0){
            int prevId = idAngle-1;
            int nextId = idAngle+1;
            if(idAngle == 0)
                prevId = angles.size()-1;
            else if(idAngle == angles.size()-1)
                nextId = 0;

            double d = (angles.get(prevId).getPrevSegment().getLongueur() + 2*A.getLongueur() + 2*B.getLongueur()+ angles.get(nextId).getNextSegment().getLongueur())/6;
            Point nP = new Point(mid.getX() + B.getSigneX()*d*Math.cos(angle*Math.PI/360) - B.getSigneY()*d*Math.sin(angle*Math.PI/360), mid.getY() + B.getSigneY()*d*Math.sin(angle*Math.PI/360) + B.getSigneY()*d*Math.cos(angle*Math.PI/360), 1, points.size());
            mid.setType(2);
            Segment newA = new Segment(A.getOrigine(), nP,segments.size());
            Segment newB = new Segment(nP, B.getDestination(), segments.size());
            Segment midS = new Segment(mid, nP, segments.size());
            points.add(nP);
            segments.add(segments.indexOf(A), newA);
            segments.add(segments.indexOf(B), newB);
            segments.add(segments.indexOf(B), midS);
            Element elmt1 = new Element(A, midS, newA, elements.size());
            Element elmt2 = new Element(midS, newB, B, elements.size());
            elements.add(elmt1);
            elements.add(elmt2);
            //System.out.println("90 < angle < 120.0 " + angle + "from -> x : " + mid.getX() + " y : " + mid.getY() + " to -> x :" + nP.getX() + "y :" + nP.getY());
        }else{
            Point p;
            Segment C;
            if(A.getLongueur() > B.getLongueur()){
                System.out.println("A>B : " + A.getLongueur() + " > " + B.getLongueur());
                p = new Point(mid.getX() + B.getSigneX()*B.getLongueur()*Math.cos(60) - B.getSigneY()*B.getLongueur()*Math.sin(60*Math.PI/180), mid.getY() + B.getSigneY()*B.getLongueur()*Math.sin(60*Math.PI/180) + B.getSigneY()*B.getLongueur()*Math.cos(60*Math.PI/180), 1, points.size());
                Segment newS = new Segment(mid, p, segments.size());
                Segment newB = new Segment(p, dest, segments.size());
                segments.add(segments.indexOf(B), newB);
                segments.add(segments.indexOf(B), newS);
                Element elmt = new Element(newS, newB, B, elements.size());
                elements.add(elmt);
                C = new Segment(ori, p, segments.size());
                segments.add(segments.indexOf(A),C);
                mid.setType(2);
                Element e = new Element(A, newS, C, elements.size());
                elements.add(e);
            }else{
                System.out.println("A <= B : " + A.getLongueur() + " < " + B.getLongueur());
                p = new Point(ori.getX() + A.getSigneX()*A.getLongueur()*Math.cos(60*Math.PI/180) - A.getSigneY()*A.getLongueur()*Math.sin(60*Math.PI/180), ori.getY() + A.getSigneY()*A.getLongueur()*Math.sin(60*Math.PI/180) + A.getSigneY()*A.getLongueur()*Math.cos(60*Math.PI/180), 1, points.size());
                Segment newS = new Segment(mid, p, segments.size());
                Segment newA = new Segment(ori, p, segments.size());
                segments.add(segments.indexOf(A), newA);
                segments.add(segments.indexOf(B), newS);
                Element elmt = new Element(A, newS, newA, elements.size());
                elements.add(elmt);
                C = new Segment(p, dest, segments.size());
                segments.add(segments.indexOf(B),C);
                mid.setType(2);
                Element e = new Element(newS, C, B, elements.size());
                elements.add(e);
            }
            points.add(p);
            //System.out.println("angle > 120.0 " + angle + " from -> x : " + mid.getX() + " y : " + mid.getY() + " to -> x :" + p.getX() + "y :" + p.getY());
        }
    }
    
    
    
    public void segmentation(Segment s, Segment nextS){
        boolean up = false;
        boolean down = false;
        boolean left = false;
        boolean right = false;
        double nbPoints = Math.floor(s.getLongueur()/PAS);
        if(nbPoints == 0.0)
            return;
        double l = s.getLongueur()/nbPoints;
        if(nbPoints == 1.0)
            l = s.getLongueur()/2;
        System.out.println(l + " -> " + s.getLongueur() + " / " + nbPoints );
        Point ori = s.getOrigine(); 
        Point des = s.getDestination();
        
        Point p = new Point(0,0,0,0);
        
        double nx = ori.getX()*PREC;
        double ny = ori.getY()*PREC;
        
        if(ori.getX() == des.getX()){
            if(ori.getY() > des.getY()){
                ny = Math.round((ori.getY()-l)*PREC);
                down = true;
                System.out.println("bas");
            }else{
                ny = Math.round((ori.getY()+l)*PREC);
                up = true;
                System.out.println("haut");
            }
        }else if(ori.getY() == des.getY()){
            if(ori.getX() > des.getX()){
                nx = Math.round((ori.getX()-l)*PREC);
                left = true;
                System.out.println("gauche");
            }else{
                nx = Math.round((ori.getX()+l)*PREC);
                right = true;
                System.out.println("droite");
            }
        }
        
        System.out.println("nx : " + nx/PREC + " ny : " + ny/PREC);
        
        p = new Point(nx/PREC, ny/PREC, 1, points.size());
        
        s.setDestination(p);
        points.add(p);
        ori = p;
        for(int i = 0; i < nbPoints; i++){
            if(nbPoints != 1 && i != nbPoints-2){
                nx = ori.getX()*PREC;
                ny = ori.getY()*PREC;
                if(down)
                    ny = Math.round((ori.getY()-l)*PREC);
                else if(up)
                    ny = Math.round((ori.getY()+l)*PREC);
                else if(left)
                    nx = Math.round((ori.getX()-l)*PREC);
                else if(right)
                    nx = Math.round((ori.getX()+l)*PREC);
                Point px = new Point(nx/PREC, ny/PREC, 1, points.size());
                System.out.println("nx : " + nx/PREC + " ny : " + ny/PREC);
                Segment ns = new Segment(ori, px, segments.size());
                ori = px;
                segments.add(segments.indexOf(s)+i+1, ns);
                points.add(px);
            }else{
               Segment nsl = new Segment(ori, nextS.getOrigine(), segments.size());
               segments.add(segments.indexOf(s)+i+1, nsl);
               break;
            }
        }
    }
    
}
