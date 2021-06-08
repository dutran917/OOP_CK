import java.util.ArrayList;
import java.util.Collections;

public class TestShadow {
    public ArrayList<Rectangle> rects;
    public ArrayList<Line> lines_horizon;
    public ArrayList<Line> lines_vertical;
    public double angle;
    public ArrayList<Node> tmp;
    public ArrayList<Node> result;
    public final double P_INFINITE = 1000000.0;
    public final double N_INFINITE = -1000000.0;
    public final double LIMIT = 400.0;

    public ArrayList<Node> solve(ArrayList<Rectangle> r, Node from)
    {
        this.rects = r;
        lines_horizon = new ArrayList<>();
        lines_vertical = new ArrayList<>();
        tmp = new ArrayList<>();
        result = new ArrayList<>();

        Node base_line_left = new Node(N_INFINITE, LIMIT);
        Node base_line_right = new Node(P_INFINITE, LIMIT);
        Line base_line = new Line(base_line_left, base_line_right);

        for (Rectangle rect : rects)
        {
            rect.line1.left.setK(from);
            rect.line1.right.setK(from);
            rect.line2.left.setK(from);
            rect.line2.right.setK(from);

            lines_horizon.add(rect.line1);
            lines_horizon.add(rect.line2);

            lines_vertical.add(rect.line3);
            lines_vertical.add(rect.line4);
        }

        //get limit
        double left_limit = from.x - Math.tan(angle / 2) * LIMIT;
        double right_limit = from.x + Math.tan(angle / 2) * LIMIT;

        Node left_node_limit_original = new Node(left_limit, LIMIT);
        Node left_node_limit1 = new Node(left_limit, LIMIT);
        Node left_node_limit2 = new Node(left_limit, LIMIT);
        Node right_node_limit_original = new Node(right_limit, LIMIT);
        Node right_node_limit1 = new Node(right_limit, LIMIT);
        Node right_node_limit2 = new Node(right_limit, LIMIT);

        for (Line line : lines_horizon)
        {
            if(line.cover(from, left_node_limit1))
            {
                Node a = left_node_limit1.scaleUp(from, line);
                if(a.y < left_node_limit1.y)
                {
                    left_node_limit1 = a;
                }
            }
        }

        for (Line line : lines_vertical)
        {
            if(line.cover(from, left_node_limit2))
            {
                Node a = left_node_limit2.scaleUp(from, line);
                if((left_node_limit2.x < a.x && a.x < from.x) || (left_node_limit2.x > a.x && a.x > from.x))
                {
                    left_node_limit2 = a;
                }
            }
        }
//        System.out.println("left:");
//        left_node_limit1.getNode();
//        left_node_limit2.getNode();
        if(left_node_limit1.x > left_node_limit2.x) {
            left_node_limit_original = left_node_limit1;
        }
        else {
            left_node_limit_original = left_node_limit2;
        }

        for (Line line : lines_horizon)
        {
            if(line.cover(from, right_node_limit1))
            {
                Node a = right_node_limit1.scaleUp(from, line);
//                a.getNode();
                if(a.y < right_node_limit1.y)
                {
                    right_node_limit1 = a;
                }
            }
        }

        for (Line line : lines_vertical)
        {
            if(line.cover(from, right_node_limit2))
            {
                Node a = right_node_limit2.scaleUp(from, line);
                if((right_node_limit2.x < a.x && a.x < from.x) || (right_node_limit2.x > a.x && a.x > from.x))
                {
                    right_node_limit2 = a;
                }
            }
        }
//        System.out.println("right:");
//        right_node_limit1.getNode();
//        right_node_limit2.getNode();
        if(right_node_limit1.x < right_node_limit2.x) {
            right_node_limit_original = right_node_limit1;
        }
        else {
            right_node_limit_original = right_node_limit2;
        }

        left_node_limit_original.setK(from);
        right_node_limit_original.setK(from);

        tmp.add(left_node_limit_original);
        tmp.add(right_node_limit_original);

//        left_node_limit_original.getNode();
//        right_node_limit_original.getNode();

        for (Line line : lines_horizon)
        {
            boolean beCovered = false;
            for (Line line_ : lines_horizon)
            {
                beCovered = line_.cover(from, line.left);
                if(beCovered) {
                    beCovered = line_.left.y < line.left.y;
                }

                if(beCovered && line_ != line) {
                    break;
                }
            }

            if(!beCovered)
            {
                for (Line line_ : lines_vertical)
                {
                    if(((line_.left.x > line.left.x) && (line_.left.x < from.x))
                            || ((line_.left.x < line.left.x) && (line_.left.x > from.x)))
                    {
                        beCovered = line_.cover(from, line.left);
                        if(beCovered)
                        {
                            beCovered = (line_.left.x > line.left.x) && (from.x < line_.left.x);
                        }

                        if(beCovered && line_ != line)
                        {
                            break;
                        }
                    }
                }

                if(!beCovered && line.left.coefficient < left_node_limit_original.coefficient
                        && line.left.coefficient > right_node_limit_original.coefficient)
                {
                    line.left.isLeft = true;
                    tmp.add(line.left);
                }
            }
            for (Line line_ : lines_horizon)
            {
                beCovered = line_.cover(from, line.right);
                if(beCovered) {
                    beCovered = line_.left.y < line.right.y;
                }

                if(beCovered && line_ != line) {
                    break;
                }
            }

//            System.out.println();

            if(!beCovered)
            {
                for (Line line_ : lines_vertical)
                {
                    if(((line_.left.x < from.x) && (line_.left.x > line.right.x))
                            || ((line_.left.x > from.x) && (line_.left.x < line.right.x)))
                    {
//                        line.right.getNode();
//                    line_.left.getNode();
//                    line_.right.getNode();
                        beCovered = line_.cover(from, line.right);
//                    System.out.println(beCovered);
                        if(beCovered)
                            beCovered = (line_.left.x > line.right.x);
//                    System.out.println(beCovered);
                        if(beCovered && line_ != line)
                        {
//                        System.out.println("122get cut at: ");
//                        line_.left.getNode();
//                        line_.right.getNode();
//                        System.out.println();
                            break;
                        }
                    }
                }

//                System.out.println(beCovered);
                if(!beCovered && line.right.coefficient < left_node_limit_original.coefficient
                        && line.right.coefficient > right_node_limit_original.coefficient)
                    line.right.isLeft = false;
                    tmp.add(line.right);
            }
        }

        Collections.sort(tmp);

        for (Node node : tmp)
        {
            node.getNode();
        }

        result.add(left_node_limit_original);

        for (Node node : tmp)
        {
//            node.getNode();
            if(node != left_node_limit_original && node != right_node_limit_original)
            {
                Node possible_node = node.scaleUp(from, base_line);
                double possible_Y = LIMIT;

                for (Line line : lines_horizon)
                {
                    if(line.left != node && line.right != node)
                    {
                        Node imagine_node = node.scaleUp(from, line);
                        if((imagine_node.x >= line.left.x) && (imagine_node.x <= line.right.x))
                        {
                            if(imagine_node.y < possible_Y)
                            {
                                possible_Y = imagine_node.y;
                                possible_node = imagine_node;
                            }
                        }
                    }
                }

                for (Line line : lines_vertical)
                {
                    if(line.left != node && line.right != node)
                    {
                        Node imagine_node = node.scaleUp(from, line);
                        if((imagine_node.y >= line.left.y) && (imagine_node.y <= line.right.y))
                        {
                            if((imagine_node.x >= possible_node.x && imagine_node.x <= from.x)
                            || (imagine_node.x <= possible_node.x && imagine_node.x >= from.x))
                            {
                                possible_node = imagine_node;
                            }
                        }
                    }
                }

                if(node.isLeft)
                {
                    result.add(possible_node);
//                System.out.println(possible_node.x + " " + possible_node.y);
                    result.add(node);
                }else{
                    result.add(node);
                    result.add(possible_node);
//                System.out.println(possible_node.x + " " + possible_node.y);
                }
            }
        }

        result.add(right_node_limit_original);

        return result;
    }

    public ArrayList<Node> solve2(ArrayList<Rectangle> r, Node from)
    {
        this.rects = r;
        lines_horizon = new ArrayList<>();
        lines_vertical = new ArrayList<>();
        tmp = new ArrayList<>();
        result = new ArrayList<>();

        Node base_line_left = new Node(N_INFINITE, LIMIT);
        Node base_line_right = new Node(P_INFINITE, LIMIT);
        Line base_line = new Line(base_line_left, base_line_right);

        for (Rectangle rect : rects)
        {
            rect.line1.left.setK2(from);
            rect.line1.right.setK2(from);
            rect.line2.left.setK2(from);
            rect.line2.right.setK2(from);

            lines_horizon.add(rect.line3);
            lines_horizon.add(rect.line4);

            lines_vertical.add(rect.line1);
            lines_vertical.add(rect.line2);
        }

        //get limit
        double left_limit = from.y - Math.tan(angle / 2) * LIMIT;
        double right_limit = from.y + Math.tan(angle / 2) * LIMIT;

        Node left_node_limit_original = new Node(left_limit, LIMIT);
        Node left_node_limit1 = new Node(LIMIT, left_limit);
        Node left_node_limit2 = new Node(LIMIT, left_limit);
//        left_node_limit1.getNode();
        Node right_node_limit_original = new Node(right_limit, LIMIT);
        Node right_node_limit1 = new Node(LIMIT, right_limit);
        Node right_node_limit2 = new Node(LIMIT, right_limit);
//        right_node_limit1.getNode();
        for (Line line : lines_horizon)
        {
            if(line.cover(from, left_node_limit1))
            {
                Node a = left_node_limit1.scaleUp(from, line);
                if(a.x < left_node_limit1.x)
                {
                    left_node_limit1 = a;
                }
            }
        }

        for (Line line : lines_vertical)
        {
            if(line.cover(from, left_node_limit2))
            {
                Node a = left_node_limit2.scaleUp(from, line);
                if((left_node_limit2.y < a.y && a.y < from.y) || (left_node_limit2.y > a.y && a.y > from.y))
                {
                    left_node_limit2 = a;
                }
            }
        }

        if(left_node_limit1.y > left_node_limit2.y) {
            left_node_limit_original = left_node_limit1;
        }
        else {
            left_node_limit_original = left_node_limit2;
        }

        for (Line line : lines_horizon)
        {
            if(line.cover(from, right_node_limit1))
            {
                Node a = right_node_limit1.scaleUp(from, line);
//                a.getNode();
                if(a.x < right_node_limit1.x)
                {
                    right_node_limit1 = a;
                }
            }
        }

        for (Line line : lines_vertical)
        {
            if(line.cover(from, right_node_limit2))
            {
                Node a = right_node_limit2.scaleUp(from, line);
                if((right_node_limit2.x < a.x && a.x < from.x) || (right_node_limit2.x > a.x && a.x > from.x))
                {
                    right_node_limit2 = a;
                }
            }
        }
        if(right_node_limit1.x < right_node_limit2.x) {
            right_node_limit_original = right_node_limit1;
        }
        else {
            right_node_limit_original = right_node_limit2;
        }

        left_node_limit_original.setK2(from);
        right_node_limit_original.setK2(from);

//        left_node_limit_original.getNode();
//        right_node_limit_original.getNode();

        tmp.add(left_node_limit_original);
        tmp.add(right_node_limit_original);

        for (Line line : lines_horizon)
        {
            boolean beCovered = false;
            line.left.getNode();
            for (Line line_ : lines_horizon)
            {
                beCovered = line_.cover(from, line.left);
                if(beCovered) {
                    beCovered = line_.left.x < line.left.x;
                }

                if(beCovered && line_ != line) {
                    break;
                }
            }

            if(!beCovered)
            {
                for (Line line_ : lines_vertical)
                {
                    if(((line_.left.y > line.left.y) && (line_.left.y < from.y))
                            || ((line_.left.y < line.left.y) && (line_.left.y > from.y)))
                    {
                        beCovered = line_.cover(from, line.left);
                        if(beCovered)
                        {
                            beCovered = (line_.left.y > line.left.y) && (from.y < line_.left.y);
                        }

                        if(beCovered && line_ != line)
                        {
                            break;
                        }
                    }
                }

                if(!beCovered && line.left.coefficient < left_node_limit_original.coefficient
                        && line.left.coefficient > right_node_limit_original.coefficient)
                {
                    tmp.add(line.left);
                }
            }
            for (Line line_ : lines_horizon)
            {
                beCovered = line_.cover(from, line.right);
                if(beCovered) {
                    beCovered = line_.left.x < line.right.x;
                }

                if(beCovered && line_ != line) {
                    break;
                }
            }

//            System.out.println();

            if(!beCovered)
            {
                for (Line line_ : lines_vertical)
                {
                    if(((line_.left.y < from.y) && (line_.left.y > line.right.y))
                            || ((line_.left.y > from.y) && (line_.left.y < line.right.y)))
                    {
//                        line.right.getNode();
//                    line_.left.getNode();
//                    line_.right.getNode();
                        beCovered = line_.cover(from, line.right);
//                    System.out.println(beCovered);
                        if(beCovered)
                            beCovered = (line_.left.y > line.right.y);
//                    System.out.println(beCovered);
                        if(beCovered && line_ != line)
                        {
//                        System.out.println("122get cut at: ");
//                        line_.left.getNode();
//                        line_.right.getNode();
//                        System.out.println();
                            break;
                        }
                    }
                }

//                System.out.println(beCovered);
                if(!beCovered && line.right.coefficient < left_node_limit_original.coefficient
                        && line.right.coefficient > right_node_limit_original.coefficient)
                    tmp.add(line.right);
            }
        }

        Collections.sort(tmp);

//        for (Node node : tmp)
//        {
//            node.getNode();
//        }

        result.add(left_node_limit_original);

        for (Node node : tmp)
        {
//            node.getNode();
            if(node != left_node_limit_original && node != right_node_limit_original)
            {
                Node possible_node = node.scaleUp(from, base_line);
                double possible_X = LIMIT;

                for (Line line : lines_horizon)
                {
                    if(line.left != node && line.right != node)
                    {
                        Node imagine_node = node.scaleUp(from, line);
                        if((imagine_node.y >= line.left.y) && (imagine_node.y <= line.right.y))
                        {
                            if(imagine_node.x < possible_X && line.left.y != imagine_node.y && line.right.y != imagine_node.y)
                            {
                                possible_X = imagine_node.x;
                                possible_node = imagine_node;
                            }
                        }
                    }
                }
                for (Line line : lines_vertical)
                {
                    if(line.left != node && line.right != node)
                    {
                        Node imagine_node = node.scaleUp(from, line);
                        if((imagine_node.x >= line.left.x) && (imagine_node.x <= line.right.x))
                        {
                            if((imagine_node.y >= possible_node.y && imagine_node.y <= from.y)
                                    || (imagine_node.y <= possible_node.y && imagine_node.y >= from.y))
                            {
                                possible_node = imagine_node;
                            }
                        }
                    }
                }
                if(node.isLeft)
                {
                    result.add(possible_node);
//                System.out.println(possible_node.x + " " + possible_node.y);
                    result.add(node);
                }else{
                    result.add(node);
                    result.add(possible_node);
//                System.out.println(possible_node.x + " " + possible_node.y);
                }
            }
        }

        result.add(right_node_limit_original);

        return result;
    }
}
