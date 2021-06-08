import java.awt.Color;
import java.util.ArrayList;

public class Obstacle {
    private ArrayList<Double> bottom1;
    private ArrayList<Double> bottom2;
    private ArrayList<Double> bottom3;
    private ArrayList<Double> bottom4;

    private ArrayList<Double> top2;
    private ArrayList<Double> top1;
    private ArrayList<Double> top3;
    private ArrayList<Double> top4;

    private ArrayList<Double> surface1;
    private ArrayList<Double> surface2;
    private ArrayList<Double> surface3;
    private ArrayList<Double> surface4;
    private ArrayList<Double> surface5;
//    private String color;
    private Color cl;
    private int height;
    public Obstacle(double[] x, double[] y, double[] z)
    {
        ArrayList<Double> bot_1 = new ArrayList<>();
        ArrayList<Double> bot_2 = new ArrayList<>();
        ArrayList<Double> bot_3 = new ArrayList<>();
        ArrayList<Double> bot_4 = new ArrayList<>();
        ArrayList<Double> top_1 = new ArrayList<>();
        ArrayList<Double> top_2 = new ArrayList<>();
        ArrayList<Double> top_3 = new ArrayList<>();
        ArrayList<Double> top_4 = new ArrayList<>();

        bot_1.add(x[0]);bot_1.add(y[0]);bot_1.add(z[0]);
        bot_2.add(x[1]);bot_2.add(y[1]);bot_2.add(z[1]);
        bot_3.add(x[2]);bot_3.add(y[2]);bot_3.add(z[2]);
        bot_4.add(x[3]);bot_4.add(y[3]);bot_4.add(z[3]);
        top_1.add(x[4]);top_1.add(y[4]);top_1.add(z[4]);
        top_2.add(x[5]);top_2.add(y[5]);top_2.add(z[5]);
        top_3.add(x[6]);top_3.add(y[6]);top_3.add(z[6]);
        top_4.add(x[7]);top_4.add(y[7]);top_4.add(z[7]);

        setBottom1(bot_1);
        setBottom2(bot_2);
        setBottom3(bot_3);
        setBottom4(bot_4);
        setTop1(top_1);
        setTop2(top_2);
        setTop3(top_3);
        setTop4(top_4);

        this.cl = randomColor();

        //Tạo các mặt phẳng tọa độ
        setSurface1();
        setSurface2();
        setSurface3();
        setSurface4();
        setSurface5();
    }

    //Trả về mặt phẳng đi qua 4 tọa độ c1, c2, c3, c4
    //ax + by + cz = d
    //12 giá trị sau là biên của mặt phẳng

    private ArrayList<Double> getSurface(ArrayList<Double> A, ArrayList<Double> B, ArrayList<Double> C, ArrayList<Double> D)
    {
        ArrayList<Double> result = new ArrayList<>();
        if(B.get(2).equals(C.get(2)))
        {
            result.add(0.0);  //a
            result.add(0.0);  //b
            result.add(1.0);  //c

            result.add(A.get(0));  //d
        }else{
            double x1 = A.get(0) - B.get(0);
            double y1 = A.get(1) - B.get(1);
            double z1 = A.get(2) - B.get(2);

            double x2 = A.get(0) - C.get(0);
            double y2 = A.get(1) - C.get(1);
            double z2 = A.get(2) - C.get(2);

            double n1 = y1 * z2 - y2 * z1;
            double n2 = z1 * x2 - z2 * x1;
            double n3 = x1 * y2 - y1 * x2;

            result.add(n1);  //a
            result.add(n2); //b
            result.add(n3);  //c

            result.add(n1 * A.get(0) + n2 * A.get(1) + n3 * A.get(2));  //d
        }

        result.add(A.get(0)); //4
        result.add(A.get(1)); //5
        result.add(A.get(2)); //6

        result.add(B.get(0)); //7
        result.add(B.get(1));//8
        result.add(B.get(2));//9

        result.add(C.get(0));//10
        result.add(C.get(1));//11
        result.add(C.get(2));//12

        result.add(D.get(0));//13
        result.add(D.get(1));//14
        result.add(D.get(2));//15

        return result;
    }
    
    public Color randomColor()
    {
        int a = (int)(Math.random()*256);
        int b = (int)(Math.random()*256);
        int c = (int)(Math.random()*256);
        return (new Color(a,b,c));
    }
    public ArrayList<Double> getSurface1() {
        return surface1;
    }
    public ArrayList<Double> getSurface2() {
        return surface2;
    }

    public ArrayList<Double> getSurface3() {
        return surface3;
    }

    public ArrayList<Double> getSurface4() {
        return surface4;
    }

    public ArrayList<Double> getSurface5() {
        return surface5;
    }

    public ArrayList<Double> getTop1() {
        return top1;
    }

    public ArrayList<Double> getTop2() {
        return top2;
    }
    public ArrayList<Double> getTop3() {
        return top3;
    }

    public ArrayList<Double> getTop4() {
        return top4;
    }

    public ArrayList<Double> getBottom1() {
        return bottom1;
    }

    public ArrayList<Double> getBottom2() {
        return bottom2;
    }

    public ArrayList<Double> getBottom3() {
        return bottom3;
    }

    public ArrayList<Double> getBottom4() {
        return bottom4;
    }

    public int getHeight() {
        return height;
    }

//    public String getColor() {
//        return color;
//    }

    public void setBottom1(ArrayList<Double> bottom1) {
        this.bottom1 = bottom1;
    }

    public void setBottom2(ArrayList<Double> bottom2) {
        this.bottom2 = bottom2;
    }

    public void setBottom3(ArrayList<Double> bottom3) {
        this.bottom3 = bottom3;
    }

    public void setBottom4(ArrayList<Double> bottom4) {
        this.bottom4 = bottom4;
    }

    public void setTop1(ArrayList<Double> top1) {
        this.top1 = top1;
    }

    public void setTop2(ArrayList<Double> top2) {
        this.top2 = top2;
    }

    public void setTop3(ArrayList<Double> top3) {
        this.top3 = top3;
    }

    public void setTop4(ArrayList<Double> top4) {
        this.top4 = top4;
    }

    public void setHeight(int height) {
        this.height = height;
    }

//    public void setColor(String color) {
//        this.color = color;
//    }

    public void setSurface1() {
        this.surface1 = getSurface(bottom1, bottom2, top1, top2);
    }

    public void setSurface2() {

        this.surface2 = getSurface(bottom2, bottom3, top2, top3);
    }

    public void setSurface3() {
        this.surface3 = getSurface(bottom3, bottom4, top3, top4);
    }

    public void setSurface4() {
        this.surface4 = getSurface(bottom4, bottom1, top4, top1);
    }

    public void setSurface5() {
        this.surface5 = getSurface(top1, top2, top3, top4);
    }

    public Color getCl() {
        return cl;
    }
    
}
