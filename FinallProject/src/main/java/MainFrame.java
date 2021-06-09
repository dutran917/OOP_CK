
import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ASUS
 */
public class MainFrame extends javax.swing.JFrame {
    Room newRoom;
    ArrayList<Obstacle> listObstacles = new ArrayList<>();
    ArrayList<Camera> listCameras = new ArrayList<>();
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        this.setLocationRelativeTo(null);
        startPanel.setVisible(true);
        createPanel.setVisible(false);
        optionPanel.setVisible(false);
        optimalPanel.setVisible(false);
        resultPanel.setVisible(false);
    }
    
    public void createParameters(String fileAddress){
        File file = new File(fileAddress);
        try {
            Scanner scanner = new Scanner(file);
            ArrayList<Integer> listXYZ;
            
            String string = "";
            String str = "";
            
            //Tạo phòng
            string = scanner.nextLine();
            //System.out.println(string);
            str = string.substring(1, string.length()-1);
            newRoom = createRoom(str);
                                  
            //Lấy số lượng các vật
            string = scanner.nextLine();
            //System.out.println(string);
            int numberOfObs = Integer.parseInt(string);
            
            //Tạo các vật
            ArrayList<Obstacle> obstacles = new ArrayList<>();
            for(int i = 0 ; i < numberOfObs; i++){
                string = scanner.nextLine();
                //System.out.println(string);
                str = string.substring(1, string.length()-1);
                Obstacle obs = createObs(str);
                obstacles.add(obs);
                newRoom.addObstacle(obs);
            }
            listObstacles = obstacles;
            
            //Lấy số lượng camera
            string = scanner.nextLine();
            //System.out.println(string);
            int numberOfCameras = Integer.parseInt(string);
            
            //Tạo camera
            ArrayList<Camera> cameras = new ArrayList<>();
            for(int i = 0; i < numberOfCameras; i++){
                string = scanner.nextLine();
                //System.out.println(string);
                str = string.substring(1);
                Camera camera = createCamera(str);
                cameras.add(camera);
                newRoom.addCamera(camera);
            }
            listCameras = cameras;
            
            double rate = newRoom.RateOfSighting() * 100;
            String optimalString = String.format("Tỷ lệ thể tích vật nhìn thấy được: %.2f", rate) + "%";
            JOptionPane.showMessageDialog(null, optimalString, "Scan successful", JOptionPane.INFORMATION_MESSAGE);
                        
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "",JOptionPane.ERROR_MESSAGE);
        }
        
        fileInput.setText("");
    }
    
    public Room createRoom(String str){
        String[] str1 = str.split("\\) \\(");
        ArrayList<ArrayList<Integer>> a = new ArrayList<>();
        for(int i = 0; i < 8; i ++){
           String[] str2 = str1[i].split(", ");
           //System.out.println(str1[i]);
           int x = Integer.parseInt(str2[0]) * 10;
           int y = Integer.parseInt(str2[1]) * 10;
           int z = Integer.parseInt(str2[2]) * 10;
           ArrayList<Integer> b = new ArrayList<>();
           b.add(x);
           b.add(y);
           b.add(z);
           a.add(b);
        }
        
        int l = 0, w = 0, h = 0;
        for(ArrayList<Integer> b : a){
            if(b.get(1) == 0 && b.get(2) == 0)
                l = b.get(0);
            else if (b.get(0) == 0 && b.get(2) == 0)
                w = b.get(1);
            else if (b.get(0) == 0 && b.get(1) == 0)
                h = b.get(2);
        }
        
        return new Room(l, w, h);
    }
    
    public Obstacle createObs(String str){
        String[] str1 = str.split("\\) \\(");
        ArrayList<ArrayList<Double>> a = new ArrayList<>();
        
        for(int i = 0; i < 8; i ++){
           String[] str2 = str1[i].split(", ");
           //System.out.println(str1[i]);
           double x = Double.parseDouble(str2[0]) * 10;
           double y = Double.parseDouble(str2[1]) * 10;
           double z = Double.parseDouble(str2[2]) * 10;
           ArrayList<Double> b = new ArrayList<>();
           b.add(x);
           b.add(y);
           b.add(z);
           a.add(b);
        }
        
        //Lấy ra danh sách các điểm trên(dưới)
        ArrayList<ArrayList<Double>> list1 = new ArrayList<>();
        ArrayList<ArrayList<Double>> list2 = new ArrayList<>();
               
        double numberZ = a.get(0).get(2);
        list1.add(a.get(0));
        for(int i = 1; i < 8; i++){
            if(a.get(i).get(2) == numberZ){
                list1.add(a.get(i));
            }else list2.add(a.get(i));
        }
             
       ArrayList<ArrayList<Double>> bottoms;
       ArrayList<ArrayList<Double>> tops;
       
       if(list1.get(0).get(2) > list2.get(0).get(2)){
           tops = list1;
           bottoms = list2;
       }else{
           tops = list2;
           bottoms = list1;
       }
       
       //Lấy ra tọa độ các điểm ở đáy
       double min = distanceFromO(bottoms.get(0));
       int index = 0;
       for(int i = 1; i < 4; i++){
           if(distanceFromO(bottoms.get(i)) < min)
               index = i;
       }
       
       ArrayList<Double> b1 = bottoms.get(index);
       ArrayList<Double> b2 = new ArrayList<>();
       ArrayList<Double> b3 = new ArrayList<>();
       ArrayList<Double> b4 = new ArrayList<>();
       
       double numberX = b1.get(0);
       double numberY = b1.get(1);
       
       for(int i = 1; i < 4; i++){
           if(bottoms.get(i).get(0) != numberX && bottoms.get(i).get(1) == numberY)
               b2 = bottoms.get(i);
           else if (bottoms.get(i).get(0) == numberX && bottoms.get(i).get(1) != numberY)
               b4 = bottoms.get(i);
           else b3 = bottoms.get(i);
       }
       
       //Lấy ra tọa độ các điểm ở trên
       ArrayList<Double> t1 = new ArrayList<>();
       ArrayList<Double> t2 = new ArrayList<>();
       ArrayList<Double> t3 = new ArrayList<>();
       ArrayList<Double> t4 = new ArrayList<>();
       
       for(int i = 0; i < 4; i++){
           double x = b1.get(0);
           double y = b1.get(1);
           if(tops.get(i).get(0) == x && tops.get(i).get(1) == y)
               t1 = tops.get(i);
       }
       
       for(int i = 0; i < 4; i++){
           double x = b2.get(0);
           double y = b2.get(1);
           if(tops.get(i).get(0) == x && tops.get(i).get(1) == y)
               t2 = tops.get(i);
       }
       
       for(int i = 0; i < 4; i++){
           double x = b3.get(0);
           double y = b3.get(1);
           if(tops.get(i).get(0) == x && tops.get(i).get(1) == y)
               t3 = tops.get(i);
       }
       
       for(int i = 0; i < 4; i++){
           double x = b4.get(0);
           double y = b4.get(1);
           if(tops.get(i).get(0) == x && tops.get(i).get(1) == y)
               t4 = tops.get(i);
       }
       
       double[] x = {b1.get(0), b2.get(0), b3.get(0), b4.get(0), t1.get(0), t2.get(0), t3.get(0), t4.get(0)};
       double[] y = {b1.get(1), b2.get(1), b3.get(1), b4.get(1), t1.get(1), t2.get(1), t3.get(1), t4.get(1)};
       double[] z = {b1.get(2), b2.get(2), b3.get(2), b4.get(2), t1.get(2), t2.get(2), t3.get(2), t4.get(2)};
        

       
       return new Obstacle(x,y,z);

    }
    
    public Double distanceFromO(ArrayList<Double> list){
        double distance = 0;
        for(Double x : list){
            distance += x*x;
        }
        return distance;
    }
    
    public Camera createCamera(String str){
        String[] str1 = str.split("\\) ");
        String[] str2 = str1[0].split(", ");
        double x = Double.parseDouble(str2[0]) * 10;
        double y = Double.parseDouble(str2[1]) * 10;
        double z = Double.parseDouble(str2[2]) * 10;
        
        String[] str3 = str1[1].split(" ");
        
        float a = Float.parseFloat(str3[0]);
        float b = Float.parseFloat(str3[1]);
        
        return new Camera(x, y, z, a, b, newRoom);
    }
    
    public void resetRoom(){
        int l = newRoom.getLength();
        int w = newRoom.getWidth();
        int h = newRoom.getHeight();
        
        newRoom = null;
        newRoom = new Room(l, w, h);
        
        for(Obstacle obs : listObstacles){
            newRoom.addObstacle(obs);
        }
        
        for(Camera cam : listCameras){
            newRoom.addCamera(cam);
        }
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnStart = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        createPanel = new javax.swing.JPanel();
        btnScan = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        fileInput = new javax.swing.JTextField();
        btnNext1 = new javax.swing.JButton();
        btnReturn1 = new javax.swing.JButton();
        optionPanel = new javax.swing.JPanel();
        btnDrawSlideshow = new javax.swing.JButton();
        btnOptimal = new javax.swing.JButton();
        btnReturn2 = new javax.swing.JButton();
        optimalPanel = new javax.swing.JPanel();
        btnCamera = new javax.swing.JButton();
        btnReEnterCamera = new javax.swing.JButton();
        btnReturn3 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        numberCameraTF = new javax.swing.JTextField();
        resultPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        resultTextArea = new javax.swing.JTextArea();
        btnReturn4 = new javax.swing.JButton();
        inforPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        startPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("FINAL PROJECT");

        btnStart.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnStart.setText("Start");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        btnExit.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout startPanelLayout = new javax.swing.GroupLayout(startPanel);
        startPanel.setLayout(startPanelLayout);
        startPanelLayout.setHorizontalGroup(
            startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, startPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(377, 377, 377))
        );
        startPanelLayout.setVerticalGroup(
            startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startPanelLayout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addComponent(btnStart)
                .addGap(64, 64, 64)
                .addComponent(btnExit)
                .addGap(112, 112, 112))
        );

        createPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnScan.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnScan.setText("Scan File");
        btnScan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnScanActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("File Address:");

        fileInput.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        btnNext1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnNext1.setText("Next");
        btnNext1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNext1ActionPerformed(evt);
            }
        });

        btnReturn1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnReturn1.setText("Return");
        btnReturn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturn1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout createPanelLayout = new javax.swing.GroupLayout(createPanel);
        createPanel.setLayout(createPanelLayout);
        createPanelLayout.setHorizontalGroup(
            createPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createPanelLayout.createSequentialGroup()
                .addGap(407, 407, 407)
                .addComponent(btnScan, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(256, 428, Short.MAX_VALUE))
            .addGroup(createPanelLayout.createSequentialGroup()
                .addGap(160, 160, 160)
                .addGroup(createPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(createPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(fileInput, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(createPanelLayout.createSequentialGroup()
                        .addComponent(btnReturn1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnNext1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(157, 157, 157))
        );
        createPanelLayout.setVerticalGroup(
            createPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, createPanelLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(createPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(fileInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(87, 87, 87)
                .addComponent(btnScan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                .addGroup(createPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNext1)
                    .addComponent(btnReturn1))
                .addGap(59, 59, 59))
        );

        optionPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnDrawSlideshow.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnDrawSlideshow.setText("Draw Slideshow");
        btnDrawSlideshow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDrawSlideshowActionPerformed(evt);
            }
        });

        btnOptimal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnOptimal.setText("Optimal");
        btnOptimal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOptimalActionPerformed(evt);
            }
        });

        btnReturn2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnReturn2.setText("Return");
        btnReturn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturn2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout optionPanelLayout = new javax.swing.GroupLayout(optionPanel);
        optionPanel.setLayout(optionPanelLayout);
        optionPanelLayout.setHorizontalGroup(
            optionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionPanelLayout.createSequentialGroup()
                .addGap(166, 166, 166)
                .addComponent(btnReturn2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, optionPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(optionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnDrawSlideshow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnOptimal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(392, 392, 392))
        );
        optionPanelLayout.setVerticalGroup(
            optionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionPanelLayout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addComponent(btnDrawSlideshow)
                .addGap(74, 74, 74)
                .addComponent(btnOptimal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                .addComponent(btnReturn2)
                .addGap(72, 72, 72))
        );

        optimalPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnCamera.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnCamera.setText("Available Camera");
        btnCamera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCameraActionPerformed(evt);
            }
        });

        btnReEnterCamera.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnReEnterCamera.setText("Re-enter Camera");
        btnReEnterCamera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReEnterCameraActionPerformed(evt);
            }
        });

        btnReturn3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnReturn3.setText("Return");
        btnReturn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturn3ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Enter number of cameras:");

        numberCameraTF.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        javax.swing.GroupLayout optimalPanelLayout = new javax.swing.GroupLayout(optimalPanel);
        optimalPanel.setLayout(optimalPanelLayout);
        optimalPanelLayout.setHorizontalGroup(
            optimalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, optimalPanelLayout.createSequentialGroup()
                .addGap(0, 392, Short.MAX_VALUE)
                .addGroup(optimalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCamera)
                    .addComponent(btnReEnterCamera))
                .addGap(382, 382, 382))
            .addGroup(optimalPanelLayout.createSequentialGroup()
                .addGroup(optimalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(optimalPanelLayout.createSequentialGroup()
                        .addGap(168, 168, 168)
                        .addComponent(btnReturn3, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(optimalPanelLayout.createSequentialGroup()
                        .addGap(267, 267, 267)
                        .addComponent(jLabel5)
                        .addGap(90, 90, 90)
                        .addComponent(numberCameraTF, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        optimalPanelLayout.setVerticalGroup(
            optimalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optimalPanelLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(optimalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numberCameraTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addComponent(btnCamera)
                .addGap(56, 56, 56)
                .addComponent(btnReEnterCamera)
                .addGap(41, 41, 41)
                .addComponent(btnReturn3)
                .addGap(47, 47, 47))
        );

        resultPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("Result");

        resultTextArea.setColumns(20);
        resultTextArea.setRows(5);
        jScrollPane1.setViewportView(resultTextArea);

        btnReturn4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnReturn4.setText("Return");
        btnReturn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturn4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout resultPanelLayout = new javax.swing.GroupLayout(resultPanel);
        resultPanel.setLayout(resultPanelLayout);
        resultPanelLayout.setHorizontalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultPanelLayout.createSequentialGroup()
                .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(resultPanelLayout.createSequentialGroup()
                        .addGap(441, 441, 441)
                        .addComponent(jLabel4))
                    .addGroup(resultPanelLayout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 812, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnReturn4))))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        resultPanelLayout.setVerticalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultPanelLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel4)
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(btnReturn4)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        inforPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Team 1");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText("Leader:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setText("Member:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setText("Nguyễn Xuân Nghĩa");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setText("Trần Quốc Du");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setText("Hoàng Ngọc Bảo");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel11.setText("Trịnh Tiến Đạt");

        javax.swing.GroupLayout inforPanelLayout = new javax.swing.GroupLayout(inforPanel);
        inforPanel.setLayout(inforPanelLayout);
        inforPanelLayout.setHorizontalGroup(
            inforPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inforPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(453, 453, 453))
            .addGroup(inforPanelLayout.createSequentialGroup()
                .addGap(317, 317, 317)
                .addGroup(inforPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(101, 101, 101)
                .addGroup(inforPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                    .addGroup(inforPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        inforPanelLayout.setVerticalGroup(
            inforPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inforPanelLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jLabel3)
                .addGap(48, 48, 48)
                .addGroup(inforPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8))
                .addGap(46, 46, 46)
                .addGroup(inforPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9))
                .addGap(29, 29, 29)
                .addComponent(jLabel10)
                .addGap(30, 30, 30)
                .addComponent(jLabel11)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(startPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(createPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(optionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(optimalPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(resultPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(inforPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(startPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(createPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optimalPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resultPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inforPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(714, 714, 714))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        // TODO add your handling code here:
        startPanel.setVisible(false);
        createPanel.setVisible(true);
    }//GEN-LAST:event_btnStartActionPerformed

    private void btnScanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnScanActionPerformed
        // TODO add your handling code here:
        String fileAddress = fileInput.getText();
        if(fileAddress.equals(""))
            JOptionPane.showMessageDialog(null, "You dont enter the input file address", "", JOptionPane.ERROR_MESSAGE);
        else
            createParameters(fileAddress);
    }//GEN-LAST:event_btnScanActionPerformed

    private void btnReturn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturn1ActionPerformed
        // TODO add your handling code here:
        startPanel.setVisible(true);
        createPanel.setVisible(false);
        
    }//GEN-LAST:event_btnReturn1ActionPerformed

    private void btnNext1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNext1ActionPerformed
        // TODO add your handling code here:
        if(newRoom != null){
            createPanel.setVisible(false);
            optionPanel.setVisible(true);
        }else
            JOptionPane.showMessageDialog(null, "You dont create parameters", "", JOptionPane.ERROR_MESSAGE);
        
    }//GEN-LAST:event_btnNext1ActionPerformed

    private void btnReturn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturn2ActionPerformed
        // TODO add your handling code here:
        createPanel.setVisible(true);
        optionPanel.setVisible(false);
//        newRoom = null;
//        //listCameras.removeAll(listCameras);
//        //listObstacles.removeAll(listObstacles);
//        listCameras = null;
//        listObstacles = null;
    }//GEN-LAST:event_btnReturn2ActionPerformed

    private void btnReturn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturn3ActionPerformed
        // TODO add your handling code here:
        optionPanel.setVisible(true);
        optimalPanel.setVisible(false);
    }//GEN-LAST:event_btnReturn3ActionPerformed

    private void btnOptimalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOptimalActionPerformed
        // TODO add your handling code here:
        optionPanel.setVisible(false);
        optimalPanel.setVisible(true);
    }//GEN-LAST:event_btnOptimalActionPerformed

    private void btnReEnterCameraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReEnterCameraActionPerformed
        // TODO add your handling code here:
        optimalPanel.setVisible(false);
        resultPanel.setVisible(true);
        int limit = Integer.parseInt(numberCameraTF.getText());
        String string = newRoom.caculateCamera(limit, true);
        resultTextArea.append(string);
        numberCameraTF.setText("");
    }//GEN-LAST:event_btnReEnterCameraActionPerformed

    private void btnCameraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCameraActionPerformed
        // TODO add your handling code here:
        optimalPanel.setVisible(false);
        resultPanel.setVisible(true);
        int limit = Integer.parseInt(numberCameraTF.getText());
        String string = newRoom.caculateCamera(limit, false);
        resultTextArea.append(string);
        numberCameraTF.setText("");
    }//GEN-LAST:event_btnCameraActionPerformed

    private void btnReturn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturn4ActionPerformed
        // TODO add your handling code here:
        optimalPanel.setVisible(true);
        resultPanel.setVisible(false);
        resultTextArea.setText("");
        resetRoom();
    }//GEN-LAST:event_btnReturn4ActionPerformed

    private void btnDrawSlideshowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDrawSlideshowActionPerformed
        // TODO add your handling code here:
        Hinhchieu hc = new Hinhchieu(newRoom, listCameras, listObstacles);
        hc.setVisible(true);
    }//GEN-LAST:event_btnDrawSlideshowActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCamera;
    private javax.swing.JButton btnDrawSlideshow;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnNext1;
    private javax.swing.JButton btnOptimal;
    private javax.swing.JButton btnReEnterCamera;
    private javax.swing.JButton btnReturn1;
    private javax.swing.JButton btnReturn2;
    private javax.swing.JButton btnReturn3;
    private javax.swing.JButton btnReturn4;
    private javax.swing.JButton btnScan;
    private javax.swing.JButton btnStart;
    private javax.swing.JPanel createPanel;
    private javax.swing.JTextField fileInput;
    private javax.swing.JPanel inforPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField numberCameraTF;
    private javax.swing.JPanel optimalPanel;
    private javax.swing.JPanel optionPanel;
    private javax.swing.JPanel resultPanel;
    private javax.swing.JTextArea resultTextArea;
    private javax.swing.JPanel startPanel;
    // End of variables declaration//GEN-END:variables
}
