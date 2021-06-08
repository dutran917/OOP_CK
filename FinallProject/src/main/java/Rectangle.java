public class Rectangle {
    public Line line1;
    public Line line2;

    public Line line3;
    public Line line4;

    public Rectangle(Node node1, Node node2, Node node3, Node node4)
    {
//        if(node1.x < node2.x)
//        {
//            node1.isLeft = true;
//            node2.isLeft = false;
//        }else{
//            node1.isLeft = false;
//            node2.isLeft = true;
//        }
//
//        if(node3.y < node4.y)
//        {
//            node3.isLeft = true;
//            node4.isLeft = false;
//        }else{
//            node3.isLeft = false;
//            node4.isLeft = true;
//        }
        line1 = new Line(node1, node2);
        line2 = new Line(node4, node3);

        line3 = new Line(node4, node1);
        line4 = new Line(node3, node2);
    }
}
