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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.GroupLayout;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout.SequentialGroup;

import pipe.gui.CreateGui;
import pipe.gui.widgets.avionic.GuideVO.RTInfo;

/**
 *
 * 建模向导的第三个步骤，补全终端的信息
 * @author Jerry-wang
 */
public class ModelGuideDialog3 extends javax.swing.JDialog {

    /** Creates new form ModelGuideDialog1 */
    public ModelGuideDialog3(java.awt.Frame parent, boolean modal, GuideVO vo) {
    	
        super(parent, modal);
        nowVo = vo;
        for(int i=0; i<typeOfMessage.length; i++)
        {
        	typeOfMessage[i] = GuideVO.PERIOD_MESSAGE;
        }
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
     private void initComponents() {
    	
    		int num = nowVo.getNumOfRTs();
    		setResizable(false);
    		
    		for(int i=0; i<num; i++)
    		{
    			jLabelArray1[i] = new javax.swing.JLabel();
    			jTextFieldArray2[i] = new javax.swing.JTextField();
    			jComboBoxArray3[i] = new javax.swing.JComboBox();
    			jTextFieldArray4[i] = new javax.swing.JTextField();
    			jButtonArray5[i] = new javax.swing.JButton();
    		}
    		
    		
    		
    		
//    		jLabel1_1 = new javax.swing.JLabel();
//    		jLabel1_2 = new javax.swing.JLabel();
//    		jLabel1_3 = new javax.swing.JLabel();
//    		
//    		jLabel1_4 = new javax.swing.JLabel();
//    		jLabel1_5 = new javax.swing.JLabel();
//    		jLabel1_6 = new javax.swing.JLabel();
//    		jLabel1_7 = new javax.swing.JLabel();

//            jTextField2_1 = new javax.swing.JTextField();
//            jTextField2_2 = new javax.swing.JTextField();
//            jTextField2_3 = new javax.swing.JTextField();
            
//            jComboBox3_1 = new javax.swing.JComboBox();
//            jComboBox3_2 = new javax.swing.JComboBox();
//            jComboBox3_3 = new javax.swing.JComboBox();
     		
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
            
//            jTextField4_1 = new javax.swing.JTextField();
//            jTextField4_2 = new javax.swing.JTextField();
//            jTextField4_3 = new javax.swing.JTextField();
            
//            jButton5_1 = new javax.swing.JButton();
//            jButton5_2 = new javax.swing.JButton();
//            jButton5_3 = new javax.swing.JButton();

            jButton6.setText("添加备注");

            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setTitle("建模向导");

            jPanel1.setBackground(new java.awt.Color(255, 255, 255));

            jLabel1.setText("第三步");

            jLabel2.setText("  补全终端信息(类型，名称，参数等)");

            jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icon_guide.png"))); // NOI18N
            jLabel3.setText(" ");

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
//            jPanel1Layout.setAutoCreateGaps(true);
//            jPanel1Layout.setAutoCreateContainerGaps(true);
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
            jLabel9.setText("周期(单位:ms)");
            
            //这块一下把十个都设置好吧  然后 这里4-10的响应函数 没调整呢 注意
            
            
            for(int i=0; i<num; i++)
            {
            	jLabelArray1[i].setText("   "+(i+1));
           // 	jTextFieldArray2[i].setActionCommand("jTextFieldArray2_"+(i));
            	jTextFieldArray2[i].addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextField2_1FocusLost(evt);
                    }
                });
                jComboBoxArray3[i].setModel(new javax.swing.DefaultComboBoxModel(new String[] { "周期型", "事件型" }));
                jComboBoxArray3[i].setActionCommand("jComboBoxArray_"+(i));
                jComboBoxArray3[i].addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jComboBox3_1ActionPerformed(evt);
                    }
                });
                jTextFieldArray4[i].setToolTipText("周期：单位ms");
                jButtonArray5[i].setText("添加备注");
                jButtonArray5[i].setActionCommand("jButtonArray_"+(i));
                jButtonArray5[i].addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton5_1ActionPerformed(evt);
                    }
                });
            }

            if(nowVo.getPriorityQueue().size()!=0)
            {//这里是 可能上一步 下一步中 已经有数据的 给他添上
            	Queue<RTInfo> queueTemp = new LinkedList<RTInfo>(); 
            	while(!nowVo.getPriorityQueue().isEmpty())
            	{
            		queueTemp.add(nowVo.getPriorityQueue().remove());
            	}
            	for(int i=0; i<num; i++)
                {
            		if(queueTemp.isEmpty())
            			break;
            		GuideVO.RTInfo info= queueTemp.remove();
            		nowVo.getPriorityQueue().add(info);
            		if(info.delay!=0) 
            			jTextFieldArray4[i].setText(info.delay+"");
            		jTextFieldArray2[i].setText(info.id);
            		if(info.typeOfMessage==GuideVO.EVENT_MESSAGE)
            			jComboBoxArray3[i].setSelectedIndex(1);
                }
            }

//            jLabel1_2.setText("   2");
//            jComboBox3_2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "周期型", "事件型" }));
//            jComboBox3_2.addActionListener(new java.awt.event.ActionListener() {
//                public void actionPerformed(java.awt.event.ActionEvent evt) {
//                    jComboBox3_2ActionPerformed(evt);
//                }
//            });
//            jTextField4_2.setToolTipText("周期：单位ms");
//            jButton5_2.setText("添加备注");
//            jButton5_2.addActionListener(new java.awt.event.ActionListener() {
//                public void actionPerformed(java.awt.event.ActionEvent evt) {
//                    jButton5ActionPerformed(evt);
//                }
//            });
//
//            jLabel1_3.setText("   3");
//            jComboBox3_3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "周期型", "事件型" }));
//            jComboBox3_3.addActionListener(new java.awt.event.ActionListener() {
//                public void actionPerformed(java.awt.event.ActionEvent evt) {
//                    jComboBox3_3ActionPerformed(evt);
//                }
//            });
//            jTextField4_3.setToolTipText("周期：单位ms");
//            jButton5_3.setText("添加备注");
//            jButton5_3.addActionListener(new java.awt.event.ActionListener() {
//                public void actionPerformed(java.awt.event.ActionEvent evt) {
//                    jButton7ActionPerformed(evt);
//                }
//            });
           

            
            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2Layout.setAutoCreateGaps(true);
            jPanel2Layout.setAutoCreateContainerGaps(true);
            jPanel2.setLayout(jPanel2Layout);
            GroupLayout.ParallelGroup gp1 = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING);
            gp1.addComponent(jLabel5);
            for(int i=0; i<num; i++)
            {
            	gp1.addComponent(jLabelArray1[i]);
            }
//            gp1.addComponent(jLabel1_1);
//            gp1.addComponent(jLabel1_2);
//            gp1.addComponent(jLabel1_3);
            
            GroupLayout.ParallelGroup gp2 = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING);
            gp2.addComponent(jLabel7);
            for(int i=0; i<num; i++)
            {
            	gp2.addComponent(jTextFieldArray2[i]);
            }
//            gp2.addComponent(jTextField2_1);
//            gp2.addComponent(jTextField2_2);
//            gp2.addComponent(jTextField2_3);
            
            GroupLayout.ParallelGroup gp3 = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING);
            gp3.addComponent(jLabel8);
            for(int i=0; i<num; i++)
            {
            	gp3.addComponent(jComboBoxArray3[i]);
            }
//            gp3.addComponent(jComboBox3_1);
//            gp3.addComponent(jComboBox3_2);
//            gp3.addComponent(jComboBox3_3);
            
            GroupLayout.ParallelGroup gp4 = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING);
            gp4.addComponent(jLabel9);
            for(int i=0; i<num; i++)
            {
            	gp4.addComponent(jTextFieldArray4[i]);
            }
//            gp4.addComponent(jTextField4_1,javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE);
//            gp4.addComponent(jTextField4_2,javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE);
//            gp4.addComponent(jTextField4_3,javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE);
            
            GroupLayout.ParallelGroup gp5 = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING);
            for(int i=0; i<num; i++)
            {
            	gp5.addComponent(jButtonArray5[i]);
            }
//            gp5.addComponent(jButton5_1);
//            gp5.addComponent(jButton5_2);
//            gp5.addComponent(jButton5_3);
            
            jPanel2Layout.setHorizontalGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(gp1).addGap(30).addGroup(gp2).addGap(30).addGroup(gp3).addGap(30).addGroup(gp4).addGap(30).addGroup(gp5));
            
            
            GroupLayout.ParallelGroup gp6 = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
            gp6.addComponent(jLabel5);
            gp6.addComponent(jLabel7);
            gp6.addComponent(jLabel8);
            gp6.addComponent(jLabel9);
            
            SequentialGroup group = jPanel2Layout.createSequentialGroup().addGroup(gp6);
            
            for(int i=0; i<num; i++)
            {
            	GroupLayout.ParallelGroup gpTemp = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
            	gpTemp.addComponent(jLabelArray1[i]);
            	gpTemp.addComponent(jTextFieldArray2[i]);
            	gpTemp.addComponent(jComboBoxArray3[i]);
            	gpTemp.addComponent(jTextFieldArray4[i]);
            	gpTemp.addComponent(jButtonArray5[i]);
            	group.addGroup(gpTemp);
            }
            
            jPanel2Layout.setVerticalGroup(group);
//            GroupLayout.ParallelGroup gp7 = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
//            gp7.addComponent(jLabel1_1);
//            gp7.addComponent(jTextField2_1);
//            gp7.addComponent(jComboBox3_1);
//            gp7.addComponent(jTextField4_1);
//            gp7.addComponent(jButton5_1);
//            
//            GroupLayout.ParallelGroup gp8 = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
//            gp8.addComponent(jLabel1_2);
//            gp8.addComponent(jTextField2_2);
//            gp8.addComponent(jComboBox3_2);
//            gp8.addComponent(jTextField4_2);
//            gp8.addComponent(jButton5_2);
//            
//            GroupLayout.ParallelGroup gp9 = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
//            gp9.addComponent(jLabel1_3);
//            gp9.addComponent(jTextField2_3);
//            gp9.addComponent(jComboBox3_3);
//            gp9.addComponent(jTextField4_3);
//            gp9.addComponent(jButton5_3);
            
//            jPanel2Layout.setVerticalGroup(jPanel2Layout.createSequentialGroup()
//                    .addGroup(gp6).addGap(15).addGroup(gp7).addGap(10).addGroup(gp8).addGap(10).addGroup(gp9));
            
           /* jPanel2Layout.setHorizontalGroup(
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
                    .addGap(58, 58, 58)
                )
            );*/
            
            
            

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

    
    private void jTextField2_1FocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
 //    	System.out.println(command+"||||||||||||||||||||");//这里原本想 失去焦点 然后判断是否重复  但是因为不知道这个事件是哪个 textField发出来的 所以作罢
    	   	
    }
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        doClose(RET_CANCEL);
    }                                        

    private void jButtonPrevStepActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    	//上一步
    	int num = nowVo.getNumOfRTs();
    	for(int i=0; i<num; i++)
    	{
    		id[i] = jTextFieldArray2[i].getText().trim();
    	}
    	
    	for(int i=0; i<num; i++)
    	{
    		if(jTextFieldArray4[i].getText().trim().equals(""))
        		delay[i]=0;
        	else
        		delay[i] = Integer.parseInt(jTextFieldArray4[i].getText());
    	}
    	
    	Queue<GuideVO.RTInfo> priorityQueue = new LinkedList<GuideVO.RTInfo>();
    	for(int i=0; i<num; i++)
    	{
    		GuideVO.RTInfo info = new GuideVO.RTInfo(this.id[i], this.typeOfMessage[i], this.delay[i]);//备注暂时先没加
    		priorityQueue.add(info);
    	}
    	
    	this.nowVo.setPriorityQueue(priorityQueue);
    	doClose(RET_CANCEL);
    	ModelGuideDialog2 guiDialog1 =  new ModelGuideDialog2(CreateGui.getApp(), true, this.nowVo);  
        guiDialog1.pack();
        guiDialog1.setLocationRelativeTo(null);
        guiDialog1.setVisible(true);
    }                                        

    private void jComboBox3_1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    	String command = evt.getActionCommand();
    	int comboNumber = Integer.parseInt(command.substring(command.length()-1));
    	if(jComboBoxArray3[comboNumber].getSelectedIndex()==1)
    	{
    		typeOfMessage[comboNumber] = GuideVO.EVENT_MESSAGE;
    	}
    	else if(jComboBoxArray3[comboNumber].getSelectedIndex()==0)
    	{
    		typeOfMessage[comboNumber] = GuideVO.PERIOD_MESSAGE;
    		jTextFieldArray4[comboNumber].setEditable(true);
    	}
    	if(jComboBoxArray3[comboNumber].getSelectedIndex()==1)
    	{
    		jTextFieldArray4[comboNumber].setText("");
    		jTextFieldArray4[comboNumber].setEditable(false);
    	}
 
    }


    private void jButton5_1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    	System.out.println("弹出一个小框，然后填写备注");
    	
    	String command = evt.getActionCommand();
    	int buttonNumber = Integer.parseInt(command.substring(command.length()-1));
    	
    	String  remark = JOptionPane.showInputDialog("请输入备注");
    	if(remark!=null)
    		remarkOfRT[buttonNumber] = remark;
    	System.out.println(remark+buttonNumber);
    	
    }

    

    private void jButtonNextStepActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
//    	id1 = jTextField2_1.getText();
//    	id2 = jTextField2_2.getText();
//    	id3 = jTextField2_3.getText();
    	
    	int num = nowVo.getNumOfRTs();
    	for(int i=0; i<num; i++)
    	{
    		id[i] = jTextFieldArray2[i].getText().trim();
    		if(id[i].equals(""))
    		{
    			JOptionPane.showMessageDialog(this, "ID不能为空", "Warning",
    					JOptionPane.WARNING_MESSAGE);
    			return;
    		}
    		if(this.idCheckSet.contains(id[i]))
    		{
    			JOptionPane.showMessageDialog(this, "ID不能重复", "Warning",
    					JOptionPane.WARNING_MESSAGE);
    			
    			this.idCheckSet.clear();
     	    	return;
    		}
    		this.idCheckSet.add(id[i]);
    	}
    	this.idCheckSet.clear();
    	
    	for(int i=0; i<num; i++)
    	{
    		if(!jTextFieldArray4[i].isEditable())//这个说明是 事件消息
        		delay[i]=0;
        	else
        	{
        		if(jTextFieldArray4[i].getText().trim().equals(""))
        		{
        			JOptionPane.showMessageDialog(this, "周期参数不能为空", "Warning",
        					JOptionPane.WARNING_MESSAGE);
        			return;
        		}
        		Pattern pattern = Pattern.compile("[1|2|3|4|5|6|7|8|9][0-9]*");
         	    if(!pattern.matcher(jTextFieldArray4[i].getText()).matches()) 
         	    {
         	    	JOptionPane.showMessageDialog(this, "周期参数格式有误，请重新确认", "Warning",
        					JOptionPane.WARNING_MESSAGE);
         	    	return;
         	    }
        		delay[i] = Integer.parseInt(jTextFieldArray4[i].getText());
    	
        	}
    	}
    	
//    	if(jTextField4_1.getText().trim().equals(""))
//    		delay1=0;
//    	else
//    		delay1 = Integer.parseInt(jTextField4_1.getText());
//    	
//    	if(jTextField4_2.getText().trim().equals(""))
//    		delay2=0;
//    	else 
//    		delay2 = Integer.parseInt(jTextField4_2.getText());
//    	
//    	if(jTextField4_3.getText().trim().equals(""))
//    		delay3=0;
//    	else
//    		delay3 = Integer.parseInt(jTextField4_3.getText());
    	
    	Queue<GuideVO.RTInfo> priorityQueue = new LinkedList<GuideVO.RTInfo>();
    	for(int i=0; i<num; i++)
    	{
    		GuideVO.RTInfo info = new GuideVO.RTInfo(this.id[i], this.typeOfMessage[i], this.delay[i] , this.remarkOfRT[i]);//备注暂时先没加
    		priorityQueue.add(info);
    	}
//    	GuideVO.RTInfo info1 = new GuideVO.RTInfo(this.id1, this.typeOfMessage1, this.delay1);//备注暂时先没加
//    	GuideVO.RTInfo info2 = new GuideVO.RTInfo(this.id2, this.typeOfMessage2, this.delay2);
//    	GuideVO.RTInfo info3 = new GuideVO.RTInfo(this.id3, this.typeOfMessage3, this.delay3); 
////    	 
//    	
//    	priorityQueue.add(info1);
//    	priorityQueue.add(info2);
//    	priorityQueue.add(info3);
    	
    	this.nowVo.setPriorityQueue(priorityQueue);
    	
    	doClose(RET_OK);
    	ModelGuideDialog4 guiDialog =  new ModelGuideDialog4(CreateGui.getApp(), true, this.nowVo);  System.out.println(nowVo);
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
    private javax.swing.JButton jButtonArray5[] = new javax.swing.JButton[100];
    
    private javax.swing.JButton jButtonNextStep;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    
    private javax.swing.JButton jButton6;
    
    private javax.swing.JComboBox jComboBox10;
    
    //终端消息类型
    private javax.swing.JComboBox[] jComboBoxArray3 = new javax.swing.JComboBox[100];

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
    private javax.swing.JLabel[] jLabelArray1 = new javax.swing.JLabel[100];

    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    
    //第二列 ID
    private javax.swing.JTextField[] jTextFieldArray2 = new javax.swing.JTextField[100];

//    第四列参数
    private javax.swing.JTextField[] jTextFieldArray4 = new javax.swing.JTextField[100];

    
    // End of variables declaration
    private int returnStatus = RET_CANCEL;

       /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    
     private GuideVO nowVo;
     
    private String[] id = new String[100];
   
    private int[] typeOfMessage = new int[100];
    
    private String[] remarkOfRT = new String[100];
    
    private Set<String> idCheckSet = new HashSet<String>();
    
    private int[] delay = new int[100];
     
    
 }
