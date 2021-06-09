public class Node implements Comparable<Node> {
    public double x;
    public double y;
    public boolean isLeft;
    public double coefficient;

    public Node(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public void setK(Node from)
    {
//        System.out.print(this.x + " " + this.y + " ");
        this.coefficient = (from.x - this.x) / Math.abs(this.y - from.y);
//        System.out.println(this.coefficient);
    }

    public void setK2(Node from)
    {
        this.coefficient = (from.y - this.y) / Math.abs(this.x - from.x);
    }

    public Node scaleUp(Node from, Line line)
    {
        if(line.left.x == line.right.x)
        {
            double ratio = Math.abs(line.left.x - from.x) / Math.abs(this.x - from.x) * (this.y - from.y);
            Node node = new Node(line.left.x, from.y + ratio);
            return node;
        }else{
            double ratio = Math.abs(line.left.y - from.y) / Math.abs(this.y - from.y) * (this.x - from.x);
            Node node = new Node(from.x + ratio, line.left.y);
            return node;
        }
    }

    public Node xSymmetric(double axis)
    {
        return new Node(x, axis * 2 - y);
    }

    public Node ySymmetric(double axis)
    {
        return new Node(axis * 2 - x, y);
    }
    //C:\Users\Admin\Desktop\input.txt
    public void getNode()
    {
        System.out.println(this.x + " " + this.y + " " + this.coefficient);
    }

    @Override
    public int compareTo(Node node) {
        return Double.compare(node.coefficient, this.coefficient);
    }
}
