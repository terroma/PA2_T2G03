/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.gui;

import as.pa2.gui.validation.AbstractValidate;
import as.pa2.loadbalancer.LoadBalancer;
import as.pa2.monitor.Monitor;
import as.pa2.server.Server;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 *
 * @author bruno
 */
public class MonitorLBGUI extends javax.swing.JFrame {
    
    private Monitor monitor;
    private LoadBalancer loadBalancer;
    private boolean estado = false;
    private AbstractValidate validator;

    
    /**
     * Creates new form MonitorLBGUI
     */
    public MonitorLBGUI() {
        initComponents();
        validator = new AbstractValidate();
        this.monitor = new Monitor(this);
        this.loadBalancer = new LoadBalancer(this);
    }

    /*
    * Updates server list
    */
    public void updateServerList(List<Server> list){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                jServerList.setText("");
                //System.out.println(list.toString());
                for(Server srv : list) {
                   jServerList.append(srv.getId() + " " + (srv.isAlive() ? "ALIVE\n" : "DEAD\n"));
                } 
            }
        });
    } 
    
    /*&& !allServersList.contains(newServer)
    * Updates server list
    */
    public void updateLogs(String line){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
               jLogs.append(line + " \n");
            }
        });
    } 
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLogs = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jServerList = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jClientPort = new javax.swing.JTextField();
        jServerPort = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jMonitorIP = new javax.swing.JTextField();
        jLoadBalancerIP = new javax.swing.JTextField();

        jScrollPane3.setViewportView(jTree1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLogs.setEditable(false);
        jLogs.setColumns(20);
        jLogs.setRows(5);
        jScrollPane1.setViewportView(jLogs);

        jServerList.setEditable(false);
        jServerList.setColumns(20);
        jServerList.setRows(5);
        jScrollPane2.setViewportView(jServerList);

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel1.setText("Monitor and Load Balancer");

        jLabel2.setText("Client Port:");

        jClientPort.setText("5000");

        jServerPort.setText("5001");

        jLabel3.setText("Server Port:");

        jButton1.setText("Start");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Server List:");

        jLabel5.setText("Logs:");

        jButton2.setText("Stop");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel6.setText("Monitor IP:");

        jLabel7.setText("Load Balancer IP:");

        jMonitorIP.setText("127.0.0.2");

        jLoadBalancerIP.setText("127.0.0.3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(36, 36, 36))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(9, 9, 9)
                                        .addComponent(jClientPort))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jMonitorIP, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(40, 40, 40)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jServerPort)
                            .addComponent(jLoadBalancerIP, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 598, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jMonitorIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLoadBalancerIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jServerPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jClientPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new SwingWorker<LoadBalancer, Object> (){
            @Override
            protected LoadBalancer doInBackground() throws Exception {
                 if (!estado){
                          
                    String monitorIP = jMonitorIP.getText();
                    String loadBalancerIP = jLoadBalancerIP.getText();
                    String clientPort = jClientPort.getText();
                    String serverPort = jServerPort.getText();
                    
                    if(!validator.validateIP(monitorIP)){
                        jLogs.append("Monitor IP is not valid! \n");
                        return null;
                    }else if(!validator.validateIP(loadBalancerIP)){
                        jLogs.append("Load Balancer IP is not valid! \n");
                        return null;
                    }else if(monitor.equals(loadBalancerIP)){
                        jLogs.append("Monitor IP and Load Balancer IP need to be different! \n");
                        return null;
                    }else if(!validator.validatePort(clientPort)){
                        jLogs.append("Client Port is not valid! \n");
                        return null;
                    }else if(!validator.validatePort(serverPort)){
                        jLogs.append("Server Port is not valid! \n");
                        return null;
                    }else if(!validator.validateMLBfields(monitorIP, loadBalancerIP, clientPort, serverPort)){
                        jLogs.append("Monitor and Load Balancer need to have different ports if they are on the same ip address! \n");
                        return null;
                    }else{
                        
                        estado = true;
                        jMonitorIP.setEnabled(false);
                        jLoadBalancerIP.setEnabled(false);
                        jClientPort.setEnabled(false);
                        jServerPort.setEnabled(false);
                        //jLogs.append("Monitor and Load Balancer started! \n");

                        loadBalancer.setIp(loadBalancerIP);
                        loadBalancer.setPort(Integer.parseInt(clientPort));
                        loadBalancer.setMonitorIp(monitorIP);
                        loadBalancer.setMonitorPort(Integer.parseInt(serverPort));
                        loadBalancer.run();
                        
                        
                        
                        return loadBalancer;
                    }
                }else{
                    jLogs.append("Monitor and Load Balancer have already started! \n");
                    return null;
                }
            }
        }.execute();
        
        
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new SwingWorker<Monitor, Object> (){
            @Override
            protected Monitor doInBackground() throws Exception {
                 if (estado){
                    
                    
                    
                    estado=false;
                   
                    jMonitorIP.setEnabled(true);
                    jLoadBalancerIP.setEnabled(true);
                    jClientPort.setEnabled(true);
                    jServerPort.setEnabled(true);
                    
                    loadBalancer.stop();
                    //jLogs.append("Monitor and Load Balancer stoped! \n");
                    
                    
                     
                    return monitor;
                 }else{
                     jLogs.append("Monitor and Load Balancer already stoped! \n");
                     return null;
                 }
            }
        }.execute();     
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(MonitorLBGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MonitorLBGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MonitorLBGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MonitorLBGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MonitorLBGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JTextField jClientPort;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField jLoadBalancerIP;
    private javax.swing.JTextArea jLogs;
    private javax.swing.JTextField jMonitorIP;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jServerList;
    private javax.swing.JTextField jServerPort;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
