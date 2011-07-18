package pipe.gui.widgets.avionic;

import pipe.gui.CreateGui;

/**
 *
 * @author Jerry-wang
 */
public class ModelGuideDialog4 extends javax.swing.JDialog {

    /** Creates new form ModelGuideDialog1 */
    public ModelGuideDialog4(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButtonNextStep = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jButtonPrevStep = new javax.swing.JButton();
        jRadioButtonFIFO = new javax.swing.JRadioButton();
        jRadioButtonPriority = new javax.swing.JRadioButton();
        jRadioButtonOnebyone = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("建模向导");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("第四步");

        jLabel2.setText("  选择AFDX虚链路的调度策略");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icon_guide.png"))); // NOI18N
        jLabel3.setText(" ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 209, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                .addContainerGap())
        );

        jButtonNextStep.setText("下一步");
        jButtonNextStep.setFocusable(false);
        jButtonNextStep.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonNextStep.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonNextStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextStepActionPerformed(evt);
            }
        });

        jButtonCancel.setText("取消");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonPrevStep.setText("上一步");
        jButtonPrevStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrevStepActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButtonFIFO);
        jRadioButtonFIFO.setText("基于先来先服务的调度策略");
        jRadioButtonFIFO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonFIFOActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButtonPriority);
        jRadioButtonPriority.setText("基于优先级的调度策略");
        jRadioButtonPriority.setSelected(true);
        jRadioButtonPriority.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonPriorityActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButtonOnebyone);
        jRadioButtonOnebyone.setText("基于轮询的调度策略");
        jRadioButtonOnebyone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonOnebyoneActionPerformed(evt);
            }
        });

        jLabel4.setText("这里是对每种调度方式的简单描述");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(95, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(310, Short.MAX_VALUE)
                .addComponent(jButtonPrevStep)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonNextStep)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonCancel)
                .addGap(8, 8, 8))
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButtonOnebyone)
                    .addComponent(jRadioButtonFIFO)
                    .addComponent(jRadioButtonPriority))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jRadioButtonPriority)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButtonOnebyone)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButtonFIFO)))
                .addGap(37, 37, 37)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancel)
                    .addComponent(jButtonNextStep)
                    .addComponent(jButtonPrevStep))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        doClose(RET_CANCEL);
    }                                        

    private void jButtonPrevStepActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    	doClose(RET_CANCEL);
    	ModelGuideDialog3 guiDialog =  new ModelGuideDialog3(CreateGui.getApp(), true, 3);  
    	guiDialog.pack();
    	guiDialog.setLocationRelativeTo(null);
    	guiDialog.setVisible(true);
    }                                        

    private void jButtonNextStepActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    	//这里 根据typeOfSchedule 来判断是 完成 还是有下一步
    	System.out.println("这里 根据typeOfSchedule 来判断是 完成 还是有下一步");
    	doClose(RET_OK);
    	ModelGuideDialog5 guiDialog =  new ModelGuideDialog5(CreateGui.getApp(), true);  
    	guiDialog.pack();
    	guiDialog.setLocationRelativeTo(null);
    	guiDialog.setVisible(true);
    }                                        

    private void jRadioButtonPriorityActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    	System.out.println("优先级");
    	typeOfSchedule = 0;
    	jButtonNextStep.setText("下一步");
    }

    private void jRadioButtonOnebyoneActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    	System.out.println("轮询式");
    	typeOfSchedule = 1;
    	jButtonNextStep.setText("生成模型");
    }

    private void jRadioButtonFIFOActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    	System.out.println("先来先服务");
    	typeOfSchedule = 2;
    	jButtonNextStep.setText("生成模型");
    }
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ModelGuideDialog1 dialog = new ModelGuideDialog1(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonNextStep;
    private javax.swing.JButton jButtonPrevStep;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButtonFIFO;
    private javax.swing.JRadioButton jRadioButtonOnebyone;
    private javax.swing.JRadioButton jRadioButtonPriority;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration
    private int returnStatus = RET_CANCEL;

       /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    private int typeOfSchedule = 0;//0：优先级， 1：轮询 ，2：抢占
}