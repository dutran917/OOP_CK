import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class Room {
    private int length;
    private int width;
    private int height;

    public Coordinate[] getCoordinates() {
        return coordinates;
    }

    private final Coordinate[] coordinates;
    private ArrayList<Camera> cameras;
    private ArrayList<Obstacle> obstacles;

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Room(int length, int width, int height) {
        //test git pull
        //test git pull again
        int index = 0;
        setWidth(width);
        setLength(length);
        setHeight(height);

        coordinates = new Coordinate[length * width * height];

        for (int i = 0; i < length; ++i)
        {
            for (int j = 0; j < width; ++j)
            {
                for (int t = 0; t < height; ++t)
                {
                    coordinates[index++] = new Coordinate(i, j, t, length, width, height);
                }
            }
        }
    }

//    public void camerasPlacement(double deepVision, double widthVision,double lengthVision, float angle) {
//        int x;
//        int y;
//
//        Scanner sn = new Scanner(System.in);
//        numberCameras = sn.nextInt();
//
//        for (int i = 0; i < numberCameras; ++i) {
//            x = sn.nextInt();
//            y = sn.nextInt();
//
//            cameras.add(new Camera(x, y, getHeight(),angle, this));
//        }
//    }

    public void obstaclesPlacement(Coordinate bottom1, Coordinate bottom2, Coordinate bottom3, Coordinate bottom4, int height)
    {
        Coordinate top1 = Coordinate.getCoordinate(bottom1.getX(), bottom1.getY(), bottom1.getZ() + height, getCoordinates());
        Coordinate top2 = Coordinate.getCoordinate(bottom2.getX(), bottom2.getY(), bottom2.getZ() + height, getCoordinates());
        Coordinate top3 = Coordinate.getCoordinate(bottom3.getX(), bottom3.getY(), bottom3.getZ() + height, getCoordinates());
        Coordinate top4 = Coordinate.getCoordinate(bottom4.getX(), bottom4.getY(), bottom4.getZ() + height, getCoordinates());

//        obstacles.add(new Obstacle());
    }

    //limit: số lượng camera tối đa được đặt
    //reset: true nếu cần xóa đi tất cả camera và đặt từ đầu; false nếu tính toán dựa trên camera hiện tại
    public String caculateCamera(int limit, boolean reset)
    {
        String stringResult = "";
        int numberOfCamera = Camera.CameraNumber;
        if(!reset)
            limit = limit - cameras.size();
        ArrayList<Camera> cameras2 = new ArrayList<>(); //dùng để đánh dấu những camera sẽ thêm vào chỉ trong hàm này
        //thêm camera trên tường tại những vị trí trống

        ArrayList<Camera> result = new ArrayList<>();
        if(!reset)
        {
            for (Camera camera : cameras)
            {
                result.add(camera);
            }
        }

        int count2 = 0;
        for (Coordinate c : coordinates)
        {
            if (c.getSeenByCameras_().contains(true)) {
                count2++;
            }
        }

        for (int i = 1; i < getHeight() - 1; ++i)
        {
            for (int j = 1; j < getWidth() - 1; ++j)
            {
                if(Camera.findCamera(j, 0, i, cameras) == null)
                {
                    Camera camera = new Camera(j, 0, i, cameras.get(0).getAngle1(), cameras.get(0).getAngle2(), this);
                    cameras.add(camera);
                    cameras2.add(camera);
                }

                if(Camera.findCamera(j, getLength(), i, cameras) == null)
                {
                    Camera camera = new Camera(j, getLength(), i, cameras.get(0).getAngle1(), cameras.get(0).getAngle2(), this);
                    cameras.add(camera);
                    cameras2.add(camera);
                }
            }

            for (int j = 1; j < getLength(); ++j)
            {
                if(Camera.findCamera(0, j, i, cameras) == null)
                {
                    Camera camera = new Camera(0, j, i, cameras.get(0).getAngle1(), cameras.get(0).getAngle2(), this);
                    cameras.add(camera);
                    cameras2.add(camera);
                }

                if(Camera.findCamera(getWidth(), j, i, cameras) == null)
                {
                    Camera camera = new Camera(getWidth(), j, i, cameras.get(0).getAngle1(), cameras.get(0).getAngle2(), this);
                    cameras.add(camera);
                    cameras2.add(camera);
                }
            }
        }

        for (int i = 1; i < getWidth() - 1; ++i)
        {
            for (int j = 1; j < getHeight() - 1; ++j)
            {
                Camera camera = new Camera(i, j, getHeight(), cameras.get(0).getAngle1(), cameras.get(0).getAngle2(), this);
                cameras.add(camera);
                cameras2.add(camera);
            }
        }

        NumberFormat formatter = new DecimalFormat("#0.00");
        double rate = RateOfSighting();

        //đã có length * width camera => loại bớt để phù hợp vs đề bài
        ArrayList<Coordinate> copy_coordiantes = new ArrayList<>(Arrays.asList(coordinates));
        if(!reset)
        {
            for (Coordinate coordinate : coordinates)
            {
                if(coordinate.isCanSee())
                {
                    int camera_id = coordinate.getSeenByCameras_().indexOf(true);
                    if(Camera.findCamera(camera_id + 1, cameras2) == null) {
                        copy_coordiantes.remove(coordinate);
                    }
                }
            }
        }
        count2 = 0;
//        //Thuật toán nhóm theo từng nhóm sao cho số camera = limit
//        //Tính tổng số tọa độ mà mỗi camera nhìn được
        List<Integer> sum = new ArrayList<Integer>();
        for (int i = 0; i < cameras.size(); ++i)
        {
            int count = 0;
            for (Coordinate coordinate : copy_coordiantes)
            {
                if(coordinate.getSeenByCameras_().get(i)) {
                    ++count;
                }
            }
//            if(count == 0)
//                System.out.println(cameras.get(i).getX() + " " + cameras.get(i).getY() + " " + cameras.get(i).getZ());
            sum.add(count);
        }

        int group = 0;

        //cần viết lại tránh remove trong list
        while(group < limit && copy_coordiantes.size() != 0 && Collections.max(sum) != 0)
        {
            //Tìm được nhóm mới
            int max = Collections.max(sum);
            int pos = sum.indexOf(max);

            Camera camera = Camera.findCamera(pos + 1, cameras);
            if (camera != null) {
                if(reset) {
                    //System.out.println("Lap tai x = " + camera.getX() + "; y = " + camera.getY() + "; z = " + camera.getZ());
                    stringResult = stringResult + "Lap tai x = " + camera.getX() + "; y = " + camera.getY() + "; z = " + camera.getZ() + "\n";
                }
                else {
                    //System.out.println("Lap them tai x = " + camera.getX() + "; y = " + camera.getY() + "; z = " + camera.getZ());
                    stringResult = stringResult + "Lap them tai x = " + camera.getX() + "; y = " + camera.getY() + "; z = " + camera.getZ() + "\n";
                }
                if(!result.contains(camera))
                {
                    result.add(camera);
                }
            }

            for (Coordinate coordinate : copy_coordiantes)
            {
                if(coordinate.getSeenByCameras_().get(pos) && !coordinate.isChecked())
                {
                    coordinate.setChecked(true);
                    for (int j = 0; j < cameras.size(); ++j)
                    {
                        if(coordinate.getSeenByCameras_().get(j))
                        {
                            int tmp = sum.get(j);
                            sum.set(j, tmp -1);
                        }
                    }
                }
            }
            ++group;
        }

        int count_checked = 0;
        int inside = 0;
        for (Coordinate coordinate : copy_coordiantes)
        {
            for (Camera camera : result)
            {
                if(coordinate.getSeenByCameras_().get(camera.getID() - 1))
                {
                    ++count_checked;
                    break;
                }
            }

            if(coordinate.isInside_obstacle())
                ++inside;
        }

        if(reset)
        {
//            System.out.println("Ty le cao nhat dat duoc voi " + limit + " camera: " +
//                    formatter.format( (double) count_checked / (double) (copy_coordiantes.size() - inside) * 100) + "%");
            stringResult = stringResult + "Ty le cao nhat dat duoc voi " + limit + " camera: " +
                    formatter.format( (double) count_checked / (double) (copy_coordiantes.size() - inside) * 100) + "%" + "\n";
        }else{
            int diff = coordinates.length - copy_coordiantes.size();

//            System.out.println("Ty le cao nhat dat duoc voi " + limit + " camera: " +
//                    formatter.format((double) (diff + count_checked) / (double) (coordinates.length - inside) * 100) + "%");
            stringResult = stringResult + "Ty le cao nhat dat duoc voi " + limit + " camera: " +
                    formatter.format((double) (diff + count_checked) / (double) (coordinates.length - inside) * 100) + "%" + "\n";
        }
        
        for (Camera camera : cameras2)
        {
            cameras.remove(camera);
        }
        
        Camera.CameraNumber = numberOfCamera;
        
        return stringResult;
    }
    public double RateOfSighting()
    {
        ArrayList<Coordinate> insideObstacle = new ArrayList<>();
        int count = 0;
        for(Coordinate coordinate: coordinates)
        {
            if(obstacles != null)
            {
                for(Obstacle obstacle: obstacles)
                {
                    if(coordinate.getX()>=obstacle.getBottom1().get(0)&&coordinate.getX()<=obstacle.getBottom2().get(0)
                            &&coordinate.getY()>=obstacle.getBottom2().get(1)&&coordinate.getY()<=obstacle.getBottom3().get(1)
                            &&coordinate.getZ()>=obstacle.getBottom1().get(2)&&coordinate.getZ()<=obstacle.getTop1().get(2))
                    {
                        insideObstacle.add(coordinate);
                        coordinate.setInside_obstacle(true);
                        break;
                    }
                }
            }
            if(!coordinate.isInside_obstacle())
            {
                coordinate.beSeen(cameras, obstacles);
            }
            int seen_in = coordinate.getSeenByCameras_().indexOf(true);
            if(seen_in >= 0)
                ++count;
        }

        int qtyCoordinateOutside = length*width*height - insideObstacle.size();
        return (double)count/qtyCoordinateOutside;
    }
    public void addCamera(Camera camera)
    {
        if(this.cameras==null)
        {
            ArrayList<Camera> temp = new ArrayList<Camera>();
            this.cameras=temp;
        }
        this.cameras.add(camera);
    }
    public void addObstacle(Obstacle obstacle)
    {
        if(this.obstacles==null)
        {
            ArrayList<Obstacle> temp = new ArrayList<Obstacle>();
            this.obstacles=temp;
        }
        this.obstacles.add(obstacle);
    }
}
