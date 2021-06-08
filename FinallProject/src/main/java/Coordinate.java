import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class Coordinate {
    private int x;
    private int y;
    private int z;
    private boolean checked;
    private boolean inside_obstacle;
//    private StringBuilder seenByCameras;    //cho biết điểm này có thể nhìn thấy bởi camera nào
    private ArrayList<Boolean> seenByCameras_;

    private boolean canSee; // điểm có thể nhìn thấy không

    public Coordinate(int x, int y, int z, int length, int width, int height) {
        setX(x);
        setY(y);
        setZ(z);
        setChecked(false);
        setInside_obstacle(false);

//        seenByCameras = new StringBuilder(length * width);
        seenByCameras_ = new ArrayList<>();
        //ban đầu set tất cả phần tử của chuỗi bằng 0
        for (int i = 0; i < (length + width) * 2 * height; ++i)
        {
            seenByCameras_.add(false);
//            seenByCameras.setCharAt(i, '0');
        }
//        System.out.println(seenByCameras);
    }

    public boolean isInside_obstacle() {
        return inside_obstacle;
    }

    public void setInside_obstacle(boolean inside_obstacle) {
        this.inside_obstacle = inside_obstacle;
    }

    public ArrayList<Boolean> getSeenByCameras_() {
        return seenByCameras_;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isCanSee() {
        return canSee;
    }

    public void setCanSee(boolean canSee) {
        this.canSee = canSee;
    }

    public void setZ(int z) {
        this.z = z;
    }

//    public void setSeenByCameras(StringBuilder seenByCameras) {
//        this.seenByCameras = seenByCameras;
//    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setSeenByCameras_(ArrayList<Boolean> seenByCameras_) {
        this.seenByCameras_ = seenByCameras_;
    }
//    public StringBuilder getSeenByCameras() {
//        return seenByCameras;
//    }

    //3 số đầu là tọa độ vector, 3 số sau là điểm mà vector đi qua
    //x = a' * t + a
    //y = b' * t + b
    //z = c' * t + c
    public ArrayList<Double> getVector(double x, double y ,double z)
    {
        ArrayList<Double> result = new ArrayList<>();
        result.add(x - getX());    //a'
        result.add(y - getY());    //b'
        result.add(z - getZ());    //c'
        result.add(x);             //a
        result.add(y);             //b
        result.add(z);             //c
        return result;
    }

    //Lấy bình phương khoảng cách từ this đến destination
//    public double getDistances(Coordinate destination)
//    {
//        int _x = (destination.getX() - getX()) * (destination.getX() - getX());
//        int _y = (destination.getY() - getY()) * (destination.getY() - getY());
//        int _z = (destination.getZ() - getZ()) * (destination.getZ() - getZ());
//
//        return _x + _y + _z;
//    }

    public double angleXOY(double xX, double yX, double zX, double xY, double yY, double zY, double xO, double yO, double zO)
    {
        double xVectorOX = xX-xO;
        double yVectorOX = yX-yO;
        double zVectorOX = zX-zO;
        double xVectorOY = xY-xO;
        double yVectorOY = yY-yO;
        double zVectorOY = zY-zO;
        double cosXOY = (xVectorOX*xVectorOY+yVectorOX*yVectorOY+zVectorOX*zVectorOY)
                /(Math.sqrt(xVectorOX*xVectorOX+yVectorOX*yVectorOX+zVectorOX*zVectorOX)
                *Math.sqrt(xVectorOY*xVectorOY+yVectorOY*yVectorOY+zVectorOY*zVectorOY));
        double angle = Math.acos(cosXOY);
        return angle;
    }
    public int throughFace(ArrayList<Double> line, ArrayList<Double> surface)
    {
        double t = (surface.get(3)-surface.get(0)*line.get(0)-surface.get(1)*line.get(1)
                -surface.get(2)*line.get(2))/(surface.get(0)*line.get(3)+surface.get(1)*line.get(4)
                +surface.get(2)*line.get(5));
//        System.out.println(t);
        double xO = t*line.get(3)+line.get(0);
        double yO = t*line.get(4)+line.get(1);
        double zO = t*line.get(5)+line.get(2);
//        double AOB = angleXOY(surface.get(4),surface.get(5),surface.get(6),surface.get(7),surface.get(8),surface.get(9)
//                ,xO,yO,zO);
//        double BOC = angleXOY(surface.get(7),surface.get(8),surface.get(9),surface.get(10),surface.get(11),surface.get(12)
//                ,xO,yO,zO);
//        double COD = angleXOY(surface.get(10),surface.get(11),surface.get(12),surface.get(13),surface.get(14),surface.get(15)
//                ,xO,yO,zO);
//        double DOA = angleXOY(surface.get(13),surface.get(14),surface.get(15),surface.get(4),surface.get(5),surface.get(6)
//                ,xO,yO,zO);
//        double totalAngle = Math.toDegrees(AOB + BOC + COD + DOA);
//
//        if(totalAngle >=359.99999 && totalAngle <=360.00001)
//            return 1;
//        return 0;

        List<Double> x = Arrays.asList(surface.get(4), surface.get(7), surface.get(10), surface.get(13));
        List<Double> y = Arrays.asList(surface.get(5), surface.get(8), surface.get(11), surface.get(14));
        List<Double> z = Arrays.asList(surface.get(6), surface.get(9), surface.get(12), surface.get(15));

        if(xO <= Collections.max(x) && xO >= Collections.min(x) && yO <= Collections.max(y) && yO >= Collections.min(y)
            && zO >= Collections.min(z) && zO <= Collections.max(z))
            return 1;
        return 0;
    }
    //    public int cutFace(ArrayList<Integer> line, ArrayList<Integer> surface)
//    {
//        double k = (double) (surface.get(3)-surface.get(0)*line.get(0)-surface.get(1)*line.get(1)
//                -surface.get(2)*line.get(2)/(surface.get(0)*line.get(3)+surface.get(1)*line.get(4)
//                +surface.get(2)*line.get(5)));
//        double x = k*line.get(3)+line.get(0);
//        double y = k*line.get(4)+line.get(1);
//        double z = k*line.get(5)+line.get(2);
//        double d1,d2;
//        if(surface.get(6)==surface.get(9))
//        {
//            d1=(surface.get(7)-surface.get(4))*(y-surface.get(5))-(x-surface.get(4))*(surface.get(8)-surface.get(5));
//            d2=(surface.get(10)-surface.get(13))*(y-surface.get(14))-(x-surface.get(13))*(surface.get(11)-surface.get(14));
//            if(d1*d2<0)
//            {
//                d1=(surface.get(10)-surface.get(7))*(y-surface.get(8))-(x-surface.get(7))*(surface.get(11)-surface.get(8));
//                d2=(surface.get(13)-surface.get(4))*(y-surface.get(5))-(x-surface.get(4))*(surface.get(14)-surface.get(5));
//                if(d1*d2<0)
//                    return 1;
//                else return 0;
//            }
//            else
//                return 0;
//        }
//        else
//            if(surface.get(5)==surface.get(8))
//            {
//                d1=(surface.get(7)-surface.get(4))*(z-surface.get(6))-(x-surface.get(4))*(surface.get(9)-surface.get(6));
//                d2=(surface.get(10)-surface.get(13))*(z-surface.get(15))-(x-surface.get(13))*(surface.get(12)-surface.get(15));
//                if(d1*d2<0)
//                {
//                    d1=(surface.get(10)-surface.get(7))*(z-surface.get(9))-(x-surface.get(7))*(surface.get(12)-surface.get(9));
//                    d2=(surface.get(13)-surface.get(4))*(z-surface.get(6))-(x-surface.get(4))*(surface.get(15)-surface.get(6));
//                    if(d1*d2<0)
//                        return 1;
//                    else return 0;
//                }
//                else
//                    return 0;
//            }
//            else
//            {
//                d1=(surface.get(8)-surface.get(5))*(z-surface.get(6))-(y-surface.get(5))*(surface.get(9)-surface.get(6));
//                d2=(surface.get(11)-surface.get(14))*(z-surface.get(15))-(y-surface.get(14))*(surface.get(12)-surface.get(15));
//                if(d1*d2<0)
//                {
//                    d1=(surface.get(11)-surface.get(8))*(z-surface.get(9))-(y-surface.get(8))*(surface.get(12)-surface.get(9));
//                    d2=(surface.get(14)-surface.get(5))*(z-surface.get(6))-(y-surface.get(5))*(surface.get(15)-surface.get(6));
//                    if(d1*d2<0)
//                        return 1;
//                    else return 0;
//                }
//                else
//                    return 0;
//            }
//    }
//    public boolean inVision(Camera camera){
//        double deltaR;
//        double deltaH;
//        double maxR;
//        if(camera.isInWall())
//        {
//            deltaR=Math.abs(camera.getPosition().getZ()-this.z);
//            deltaH=Math.sqrt((camera.getPosition().getX()-this.x)*(camera.getPosition().getX()-this.x)*
//                    +(camera.getPosition().getY()-this.y)*(camera.getPosition().getY()-this.y));
//
//        }
//        else
//        {
//            deltaR=Math.sqrt((camera.getPosition().getX()-this.x)*(camera.getPosition().getX()-this.x)*
//                    +(camera.getPosition().getY()-this.y)*(camera.getPosition().getY()-this.y));
//            deltaH=Math.abs(camera.getPosition().getZ()-this.z);
//        }
//        maxR=deltaH*Math.tan(camera.getWidthVision());
//        if(maxR>=deltaR)
//        {
//            return true;
//        }
//        return false;
//    }

    public boolean inVision(Camera camera)
    {
        double v1x = getX() - camera.getX();
        double v1y = getY() - camera.getY();
        double v1z = getZ() - camera.getZ();
//        System.out.println(v1x);
//        System.out.println(v1y);
//        System.out.println(v1z);
        double length1 = Math.sqrt(v1x * v1x + v1z * v1z);  //xOz
        double length2 = Math.sqrt(v1y * v1y + v1z * v1z);  //yOz
        double length3 = Math.sqrt(v1x * v1x + v1y * v1y);  //xOy
//        System.out.println(camera.getWall());
        if(getX() == camera.getX() && getY() == camera.getY() && getZ() == camera.getZ())
            return false;
        if(camera.getWall() == 2 || camera.getWall() == 4)
        {
            double cos = Math.abs(v1y / length3);
            double limit = Math.toRadians(camera.getAngle1() / 2);

            boolean condition1 = (cos >= Math.cos(limit) && cos <= 1);

            cos = Math.abs(v1y / length2);
            limit = Math.toRadians(camera.getAngle2() / 2);
//            System.out.println(Math.cos(limit));
            boolean condition2 = (cos >= Math.cos(limit) && cos <= 1);
//            System.out.println(cos);
            return condition1 && condition2;
        }

        if(camera.getWall() == 1 || camera.getWall() == 3)
        {
            double cos = Math.abs(v1x / length3);

            double limit = Math.toRadians(camera.getAngle1() / 2);

            boolean condition1 = (cos >= Math.cos(limit) && cos <= 1);

            cos = v1x / length1;
            limit = Math.toRadians(camera.getAngle2() / 2);

            boolean condition2 = (cos >= Math.cos(limit) && cos <= 1);

            return condition1 && condition2;
        }

        if(camera.getWall() == 0)
        {
            double cos = Math.abs(v1z / length1);

            double limit = Math.toRadians(camera.getAngle1()) / 2;

            boolean condition1 = (cos >= Math.cos(limit) && cos <= 1);

            cos = Math.abs(v1z / length2);
            limit = Math.toRadians(camera.getAngle2() / 2);

            boolean condition2 = (cos >= Math.cos(limit) && cos <= 1);

            return condition1 && condition2;
        }
        return false;
    }

//    public boolean inObstacle(Obstacle obstacle)
//    {
//        if(x>=obstacle.getBottom1().getX()&&x<=obstacle.getBottom4().getX())
//            if(y>=obstacle.getBottom1().getY()&&y<=obstacle.getBottom2().getY())
//                if(z>=obstacle.getBottom1().getZ()&&z<=obstacle.getTop1().getZ())
//                    return true;
//        return false;
//    }


    //sau hàm này xác định được chuỗi seenByCamera của từng coordinate
    public void beSeen(ArrayList<Camera> cameras, ArrayList<Obstacle> obstacles)
    {
        for (Camera camera : cameras)
        {
            int id = camera.getID();
            if(inVision(camera)) //xét xem có trong góc nhìn camera không
            {
                int check = 1;
                if(obstacles != null)
                {
                    for(Obstacle obstacle : obstacles )
                    {
                        if(obstacle.getTop1().get(2) > getZ())
                        {

                            ArrayList<Double> line = getVector(camera.getX(),camera.getY(),camera.getZ());
                            ArrayList<Double> face1 = obstacle.getSurface1();
                            ArrayList<Double> face2 = obstacle.getSurface2();
                            ArrayList<Double> face3 = obstacle.getSurface3();
                            ArrayList<Double> face4 = obstacle.getSurface4();
                            ArrayList<Double> face5 = obstacle.getSurface5();
                            if(throughFace(line,face1)+throughFace(line,face2)+throughFace(line,face3)+throughFace(line,face4)
                                    +throughFace(line,face5)!=0)
                            {
                                check=0;
                                break;
                            }
                        }
                    }
                }
                if(check==1)
                {
                    setCanSee(true);
//                    System.out.println(seenByCameras);
//                    seenByCameras.setCharAt(id-1, '1');
                    seenByCameras_.set(id-1, true);
                }
                else
                    setCanSee(false);
            }
        }
    }

    public static Coordinate getCoordinate(int x, int y, int z, Coordinate[] point)
    {
        for(Coordinate p : point)
        {
            if(p.getX()==x && p.getY()==y && p.getZ() == z)
                return p;
        }
        return null;
    }
}