import java.util.ArrayList;

public class Camera {
    public static int CameraNumber;    //số lượng camera
    private int id;
    private double x;
    private double y;
    private double z;

//    private Coordinate position;
    private double deepVision;   //tầm xa của camera
    private double widthVision;  //độ rộng của vision
    private double lengthVision;  // độ dài của Vision

    private int wall;

    //= 0 thì ở trần, = 1 thì ở tường x = 0, = 2 thì ở tường y = 0, = 3 thì ở tường x = length,= 4 thì ở tường y = width
    private float angle1;
    private float angle2;

    public void setAngle1(float angle1) {
        this.angle1 = angle1;
    }

    public float getAngle2() {
        return angle2;
    }

    public void setAngle2(float angle2) {
        this.angle2 = angle2;
    }

    public Camera(double x, double y, double z, float angle1, float angle2, Room r) {
        ++CameraNumber;
        setX(x);
        setY(y);
        setZ(z);
        setDeepVision(deepVision);
        setLengthVision(lengthVision);
        setAngle1(angle1);
        setAngle2(angle2);
        setID(CameraNumber);
//        Coordinate camPosition = Coordinate.getCoordinate(x, y, z, r.getCoordinates());
//        if(camPosition != null)
//        {
//            setPosition(camPosition);
//        }
        if(z==r.getHeight() && x > 0 && x < r.getLength() && y > 0 && y < r.getWidth())
            setWall(0);
        else if(x==0)
            setWall(1);
        else if(x==r.getLength())
            setWall(3);
        else if(y==0)
            setWall(2);
        else if(y==r.getWidth())
            setWall(4);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDeepVision() { return deepVision; }

    public int getID() {
        return id;
    }

    public double getWidthVision() { return widthVision; }

    public float getAngle1() { return angle1; }

    public void setID(int id) {
        this.id = id;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setDeepVision(double deepVision) {
        this.deepVision = deepVision;
    }

//    public Coordinate getPosition() {
//        return position;
//    }
//
//    public void setPosition(Coordinate position) {
//        this.position = position;
//    }

    public static Camera findCamera(double x, double y, double z, ArrayList<Camera> cameras)    //tìm camera theo x, y trong cameras
    {
        for (Camera c : cameras)
        {
            if(c.getX() == x && c.getY() == y && c.getZ() == z)
            {
                return c;
            }
        }
        return null;
    }
    public static Camera findCamera(int id, ArrayList<Camera> cameras)
    {
        for (Camera c : cameras)
        {
            if(c.getID() == id)
            {
                return c;
            }
        }
        return null;
    }
    public double getLengthVision() {
        return lengthVision;
    }
    public int getWall() {
        return wall;
    }

    public void setWall(int wall) {
        this.wall = wall;
    }

    public void setLengthVision(double lengthVision) {
        this.lengthVision = lengthVision;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
