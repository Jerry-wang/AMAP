/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ModelGuideDialog1.java
 *
 * Created on 2011-7-17, 16:08:21
 */

package pipe.gui.widgets.avionic;

import java.awt.event.ActionEvent;

import pipe.gui.CreateGui;

/**
 *
 * 建模向导的第三个步骤，补全终端的信息
 * @author Jerry-wang
 */
public class ModelGuideDialog3 extends javax.swing.JDialog {

    /** Creates new form ModelGuideDialog1 */
    public ModelGuideDialog3(java.awt.Frame parent, boolean modal, int numOfRT) {
        super(parent, modal);
        this.numOfRT = numOfRT;
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
     private void initComponents() {
    		setResizable(false);
    		
    		jLabel1_1 = new javax.swing.JLabel();
    		jLabel1_2 = new javax.swing.JLabel();
    		jLabel1_3 = new javax.swing.JLabel();
    		jLabel1_4 = new javax.swing.JLabel();
    		jLabel1_5 = new javax.swing.JLabel();
    		jLabel1_6 = new javax.swing.JLabel();
    		jLabel1_7 = new javax.swing.JLabel();
    		jLabel1_8 = new javax.swing.JLabel();
    		jLabel1_9 = new javax.swing.JLabel();
    		jLabel1_10 = new javax.swing.JLabel();

            jTextField2_1 = new javax.swing.JTextField();
            jTextField2_2 = new javax.swing.JTextField();
            jTextField2_3 = new javax.swing.JTextField();
            jTextField2_4 = new javax.swing.JTextField();
            jTextField2_5 = new javax.swing.JTextField();
            jTextField2_6 = new javax.swing.JTextField();
            jTextField2_7 = new javax.swing.JTextField();
            jTextField2_8 = new javax.swing.JTextField();
            jTextField2_9 = new javax.swing.JTextField();
            jTextField2_10 = new javax.swing.JTextField();
            
            jComboBox3_1 = new javax.swing.JComboBox();
            jComboBox3_2 = new javax.swing.JComboBox();
            jComboBox3_3 = new javax.swing.JComboBox();
            jComboBox3_4 = new javax.swing.JComboBox();
            jComboBox3_5 = new javax.swing.JComboBox();
            jComboBox3_6 = new javax.swing.JComboBox();
            jComboBox3_7 = new javax.swing.JComboBox();
            jComboBox3_8 = new javax.swing.JComboBox();
            jComboBox3_9 = new javax.swing.JComboBox();
            jComboBox3_10 = new javax.swing.JComboBox();
     		
            buttonGroup1 = new javax.swing.ButtonGroup();
            jButton6 = new javax.swing.JButton();
            jPanel1 = new javax.swing.JPanel();
            jLabel1 = new javax.swing.JLabel();
            jLabel2 = new javax.swing.JLabel();
            jLabel3 = new javax.swing.JLabel();
            jSeparator1 = new javax.swing.JSeparator();
            jButtonNextStep = new javax.swing.JButton();
            jButton3 = new javax.swing.JButton();
            jButton4 = new javax.swing.JButton();
            jPanel2 = new javax.swing.JPanel();
            
             
            jLabel5 = new javax.swing.JLabel();
            jLabel7 = new javax.swing.JLabel();
            jLabel8 = new javax.swing.JLabel();
            jLabel9 = new javax.swing.JLabel();
            
            jTextField4_1 = new javax.swing.JTextField();
            jTextField4_2 = new javax.swing.JTextField();
            jTextField4_3 = new javax.swing.JTextField();
            jTextField4_4 = new javax.swing.JTextField();
            jTextField4_5 = new javax.swing.JTextField();
            jTextField4_6 = new javax.swing.JTextField();
            jTextField4_7 = new javax.swing.JTextField();
            jTextField4_8 = new javax.swing.JTextField();
            jTextField4_9 = new javax.swing.JTextField();
            jTextField4_10 = new javax.swing.JTextField();
            
            jButton5_1 = new javax.swing.JButton();
            jButton5_2 = new javax.swing.JButton();
            jButton5_3 = new javax.swing.JButton();
            jButton5_4 = new javax.swing.JButton();
            jButton5_5 = new javax.swing.JButton();
            jButton5_6 = new javax.swing.JButton();
            jButton5_7 = new javax.swing.JButton();
            jButton5_8 = new javax.swing.JButton();
            jButton5_9 = new javax.swing.JButton();
            jButton5_10 = new javax.swing.JButton();

            jButton6.setText("添加备注");

            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setTitle("建模向导");

            jPanel1.setBackground(new java.awt.Color(255, 255, 255));

            jLabel1.setText("第三步");

            jLabel2.setText("  补全终端信息(类型，名称，参数等)");

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
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 217, Short.MAX_VALUE)
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
            jButtonNextStep.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButtonNextStepActionPerformed(evt);
                }
            });

            jButton3.setText("取消");
            jButton3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton3ActionPerformed(evt);
                }
            });

            jButton4.setText("上一步");
            jButton4.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButtonPrevStepActionPerformed(evt);
                }
            });
            
            jLabel5.setText("编号");
            jLabel7.setText("ID");
            jLabel8.setText("终端消息类型");
            jLabel9.setText("参数(周期/频度)");
            
            //这块一下把十个都设置好吧  然后 这里4-10的响应函数 没调整呢 注意
            jLabel1_1.setText("   1");
            jComboBox3_1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "周期型", "事件型" }));
            jComboBox3_1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jComboBox3_1ActionPerformed(evt);
                }
            });
            jTextField4_1.setToolTipText("周期：单位ms");
            jButton5_1.setText("添加备注");
            jButton5_1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton5_1ActionPerformed(evt);
                }
            });
         

            jLabel1_2.setText("   2");
            jComboBox3_2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "周期型", "事件型" }));
            jComboBox3_2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jComboBox3_2ActionPerformed(evt);
                }
            });
            jTextField4_2.setToolTipText("周期：单位ms");
            jButton5_2.setText("添加备注");
            jButton5_2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton5ActionPerformed(evt);
                }
            });

            jLabel1_3.setText("   3");
            jComboBox3_3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "周期型", "事件型" }));
            jComboBox3_3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jComboBox3_3ActionPerformed(evt);
                }
            });
            jTextField4_3.setToolTipText("周期：单位ms");
            jButton5_3.setText("添加备注");
            jButton5_3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton7ActionPerformed(evt);
                }
            });
            
            jLabel1_4.setText("   4");
            jComboBox3_4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "周期型", "事件型" }));
            jComboBox3_4.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jComboBox3_3ActionPerformed(evt);//方法没 调整
                }
            });
            jTextField4_4.setToolTipText("周期：单位ms");
            jButton5_4.setText("添加备注");
            jButton5_4.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton7ActionPerformed(evt);
                }
            });
            
            jLabel1_5.setText("   5");
            jComboBox3_5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "周期型", "事件型" }));
            jComboBox3_5.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jComboBox3_3ActionPerformed(evt);
                }
            });
            jTextField4_5.setToolTipText("周期：单位ms");
            jButton5_5.setText("添加备注");
            jButton5_5.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton7ActionPerformed(evt);
                }
            });
            
            jLabel1_6.setText("   6");
            jComboBox3_6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "周期型", "事件型" }));
            jComboBox3_6.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jComboBox3_3ActionPerformed(evt);
                }
            });
            jTextField4_6.setToolTipText("周期：单位ms");
            jButton5_6.setText("添加备注");
            jButton5_6.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton7ActionPerformed(evt);
                }
            });
            
            jLabel1_7.setText("   7");
            jComboBox3_7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "周期型", "事件型" }));
            jComboBox3_7.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jComboBox3_3ActionPerformed(evt);
                }
            });
            jTextField4_7.setToolTipText("周期：单位ms");
            jButton5_7.setText("添加备注");
            jButton5_7.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton7ActionPerformed(evt);
                }
            });
            
            jLabel1_8.setText("   8");
            jComboBox3_8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "周期型", "事件型" }));
            jComboBox3_8.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jComboBox3_3ActionPerformed(evt);
                }
            });
            jTextField4_8.setToolTipText("周期：单位ms");
            jButton5_8.setText("添加备注");
            jButton5_8.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton7ActionPerformed(evt);
                }
            });
            
            jLabel1_9.setText("   9");
            jComboBox3_9.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "周期型", "事件型" }));
            jComboBox3_9.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jComboBox3_3ActionPerformed(evt);
                }
            });
            jTextField4_9.setToolTipText("周期：单位ms");
            jButton5_9.setText("添加备注");
            jButton5_9.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton7ActionPerformed(evt);
                }
            });
            
            jLabel1_10.setText("   10");
            jComboBox3_10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "周期型", "事件型" }));
            jComboBox3_10.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jComboBox3_3ActionPerformed(evt);
                }
            });
            jTextField4_10.setToolTipText("周期：单位ms");
            jButton5_10.setText("添加备注");
            jButton5_10.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton7ActionPerformed(evt);
                }
            });

            
            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel1_1))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel1_2))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel1_3))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(20, 20, 20)
                            .addComponent(jLabel5)))
                    .addGap(32, 32, 32)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField2_1, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                            .addComponent(jTextField2_2, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                            .addComponent(jTextField2_3))
                        .addComponent(jLabel7))
                    .addGap(35, 35, 35)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jComboBox3_3, 0, 72, Short.MAX_VALUE)
                            .addGap(47, 47, 47)
                            .addComponent(jTextField4_3, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(47, 47, 47)
                            .addComponent(jButton5_3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jComboBox3_2, 0, 72, Short.MAX_VALUE)
                            .addGap(47, 47, 47)
                            .addComponent(jTextField4_2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(47, 47, 47)
                            .addComponent(jButton5_2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jComboBox3_1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(47, 47, 47)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jTextField4_1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                                    .addComponent(jButton5_1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))
                            .addContainerGap())))
            );
            jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addContainerGap(20, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(21, 21, 21)
                                    .addComponent(jTextField2_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(21, 21, 21)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButton5_1)
                                            .addComponent(jTextField4_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel1_1))))
                            .addGap(18, 18, 18))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel9)
                                .addComponent(jLabel8)
                                .addComponent(jLabel7)
                                .addComponent(jLabel5))
                            .addGap(16, 16, 16)
                            .addComponent(jComboBox3_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(20, 20, 20)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox3_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5_2))
                        .addComponent(jLabel1_2))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox3_3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2_3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4_3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5_3))
                        .addComponent(jLabel1_3))
                    .addGap(58, 58, 58))
            );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(318, Short.MAX_VALUE)
                    .addComponent(jButton4)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jButtonNextStep)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jButton3)
                    .addGap(8, 8, 8))
                .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton3)
                        .addComponent(jButtonNextStep)
                        .addComponent(jButton4))
                    .addContainerGap())
            );

            pack();
         
    }// </editor-fold>

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        doClose(RET_CANCEL);
    }                                        

    private void jButtonPrevStepActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    	//上一步
    	doClose(RET_CANCEL);
    	ModelGuideDialog2 guiDialog1 =  new ModelGuideDialog2(CreateGui.getApp(), true);  
        guiDialog1.pack();
        guiDialog1.setLocationRelativeTo(null);
        guiDialog1.setVisible(true);
    }                                        

    private void jComboBox3_1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    	System.out.println("jComboBox3_1:"+jComboBox3_1.getSelectedIndex());
    	typeOfRT[1] = jComboBox3_1.getSelectedIndex();
    	
    	
    }

    private void jComboBox3_2ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    	System.out.println("jComboBox3_2:"+jComboBox3_2.getSelectedIndex());
    	typeOfRT[2] = jComboBox3_2.getSelectedIndex(); 
    }

    private void jComboBox3_3ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    	System.out.println("jComboBox3_3:"+jComboBox3_3.getSelectedIndex());
    	typeOfRT[3] = jComboBox3_3.getSelectedIndex();
    }

    private void jButton5_1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    	System.out.println("弹出一个小框，然后填写备注");
    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jButtonNextStepActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    	System.out.println("应该是根据参数合成 相应数量的 RT实体类， 然后传到下一步去");
    	doClose(RET_OK);
    	ModelGuideDialog4 guiDialog =  new ModelGuideDialog4(CreateGui.getApp(), true);  
    	guiDialog.pack();
    	guiDialog.setLocationRelativeTo(null);
    	guiDialog.setVisible(true);
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
    //添加备注button
    private javax.swing.JButton jButton5_1;
    private javax.swing.JButton jButton5_2;
    private javax.swing.JButton jButton5_3;
    private javax.swing.JButton jButton5_4;
    private javax.swing.JButton jButton5_5;
    private javax.swing.JButton jButton5_6;
    private javax.swing.JButton jButton5_7;
    private javax.swing.JButton jButton5_8;
    private javax.swing.JButton jButton5_9;
    private javax.swing.JButton jButton5_10;
    
    private javax.swing.JButton jButtonNextStep;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    
    private javax.swing.JButton jButton6;
    
    private javax.swing.JComboBox jComboBox10;
    
    //终端消息类型
    private javax.swing.JComboBox jComboBox3_1;
    private javax.swing.JComboBox jComboBox3_2;
    private javax.swing.JComboBox jComboBox3_3; 
    private javax.swing.JComboBox jComboBox3_4; 
    private javax.swing.JComboBox jComboBox3_5; 
    private javax.swing.JComboBox jComboBox3_6; 
    private javax.swing.JComboBox jComboBox3_7; 
    private javax.swing.JComboBox jComboBox3_8; 
    private javax.swing.JComboBox jComboBox3_9; 
    private javax.swing.JComboBox jComboBox3_10; 

    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    
    //第一列的编号
    private javax.swing.JLabel jLabel1_1;
    private javax.swing.JLabel jLabel1_2; 
    private javax.swing.JLabel jLabel1_3;
    private javax.swing.JLabel jLabel1_4;
    private javax.swing.JLabel jLabel1_5; 
    private javax.swing.JLabel jLabel1_6;
    private javax.swing.JLabel jLabel1_7;
    private javax.swing.JLabel jLabel1_8; 
    private javax.swing.JLabel jLabel1_9;
    private javax.swing.JLabel jLabel1_10;
    
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    
    //第二列 ID
    private javax.swing.JTextField jTextField2_1;
    private javax.swing.JTextField jTextField2_2;
    private javax.swing.JTextField jTextField2_3;
    private javax.swing.JTextField jTextField2_4;
    private javax.swing.JTextField jTextField2_5;
    private javax.swing.JTextField jTextField2_6;
    private javax.swing.JTextField jTextField2_7;
    private javax.swing.JTextField jTextField2_8;
    private javax.swing.JTextField jTextField2_9;
    private javax.swing.JTextField jTextField2_10;
    
//    第四列参数
    private javax.swing.JTextField jTextField4_1;
    private javax.swing.JTextField jTextField4_2;
    private javax.swing.JTextField jTextField4_3;
    private javax.swing.JTextField jTextField4_4;
    private javax.swing.JTextField jTextField4_5;
    private javax.swing.JTextField jTextField4_6;
    private javax.swing.JTextField jTextField4_7;
    private javax.swing.JTextField jTextField4_8;
    private javax.swing.JTextField jTextField4_9;
    private javax.swing.JTextField jTextField4_10;

    
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration
    private int returnStatus = RET_CANCEL;

       /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    
    private int numOfRT = 2;
    
    private int[] typeOfRT = {0,0,0,0,0,0,0,0,0,0,0};  //第0位置放弃不用   0：周期型  1：事件型  与那个JComboBox 选项的index刚好一样
}
