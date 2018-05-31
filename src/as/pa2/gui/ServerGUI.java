/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.gui;

import as.pa2.gui.validation.AbstractValidate;
import as.pa2.server.Server;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 *
 * @author bruno
 */
public class ServerGUI extends javax.swing.JFrame {
    
    private Server serverobj;
    private boolean estado = false;
    private AbstractValidate validator;

    /**
     * Creates new form ServerGUI
     */
    public ServerGUI() {
        initComponents();
        validator = new AbstractValidate();
        serverobj = new Server(this);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jMonitorIP = new javax.swing.JTextField();
        jMonitorPort = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        JLogs = new javax.swing.JTextArea();
        jLoadBPort = new javax.swing.JTextField();
        jQueueSize = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jServerIP = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jServerPort = new javax.swing.JTextField();

        jScrollPane1.setViewportView(jEditorPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Start");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Server:");

        jButton2.setText("Stop");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jMonitorIP.setText("127.0.0.2");
        jMonitorIP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMonitorIPActionPerformed(evt);
            }
        });

        jMonitorPort.setText("5001");

        jLabel3.setText("Monitor IP:");

        jLabel4.setText("Monitor Port:");

        jLabel5.setText("Load-Balancer Port:");

        jLabel6.setText("Queue Size:");

        JLogs.setEditable(false);
        JLogs.setColumns(20);
        JLogs.setRows(5);
        jScrollPane2.setViewportView(JLogs);

        jLoadBPort.setText("5002");

        jQueueSize.setText("10");

        jLabel2.setText("Server IP:");

        jServerIP.setText("127.0.0.1");

        jLabel7.setText("Server Port:");

        jServerPort.setText("5000");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 436, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLoadBPort)
                    .addComponent(jMonitorIP, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                    .addComponent(jServerIP))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jMonitorPort, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                    .addComponent(jQueueSize, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                    .addComponent(jServerPort))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jServerIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jServerPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jMonitorIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jMonitorPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLoadBPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jQueueSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        new SwingWorker<Server, Object> (){
            @Override
            protected Server doInBackground() throws Exception {
                if (!estado){
                    
                    String serverIP = jServerIP.getText();
                    String serverPort = jServerPort.getText();
                    String monitorIP = jMonitorIP.getText();
                    String monitorPort = jMonitorPort.getText();
                    String loadBPort = jLoadBPort.getText();
                    String queueSize = jQueueSize.getText();
                                   
                    if(!validator.validateIP(serverIP)){
                        JLogs.append("Server IP is not valid! \n");
                        return null;
                    }else if(!validator.validatePort(serverPort)){
                        JLogs.append("Server Port is not valid! \n");
                        return null;
                    }else if(!validator.validateIP(monitorIP)){
                        JLogs.append("Monitor IP is not valid! \n");
                        return null;
                    }else if(!validator.validatePort(monitorPort)){
                        JLogs.append("Monitor Port is not valid! \n");
                        return null;
                    }else if(!validator.validatePort(loadBPort)){
                        JLogs.append("Load Balancer Port is not valid! \n");
                        return null;
                    }else if (!validator.validateQueueSize(queueSize)){
                        JLogs.append("Queue Size is not valid! \n");
                        return null;
                    }else if(serverIP.equals(monitorIP)){
                        JLogs.append("Server IP can't be equal to Monitor IP! \n");
                        return null;   
                    }else if(serverPort.equals(monitorPort) || serverPort.equals(loadBPort) || monitorPort.equals(loadBPort)){
                        JLogs.append("Ports need to be diferent! \n");
                        return null;
                    }else{
                        estado = true;
                        
                        jServerIP.setEnabled(false);
                        jServerPort.setEnabled(false);
                        jMonitorIP.setEnabled(false);
                        jMonitorPort.setEnabled(false);
                        jLoadBPort.setEnabled(false);
                        jQueueSize.setEnabled(false);
                        
                        JLogs.append("-------------------------------------------------------------------------------------------------------------------- \n");
                        JLogs.append("Server started with IP: " + serverIP + " and Port: " + serverPort + " \n");
                        JLogs.append("Server connected to Monitor with IP: " + monitorIP + " on Port: " + monitorPort + " \n");
                        JLogs.append("Server connected Load-balancer on Port: " + loadBPort + " \n");
                        JLogs.append("Server queue size is " + queueSize + " \n");
                        JLogs.append("-------------------------------------------------------------------------------------------------------------------- \n");
                                                
                        serverobj.setHost(serverIP);
                        serverobj.setPort(Integer.parseInt(serverPort));
                        serverobj.setMonitorIp(monitorIP);
                        serverobj.setMonitorPort(Integer.parseInt(monitorPort));
                        serverobj.setLoadBalancerPort(Integer.parseInt(loadBPort));
                        serverobj.setQueueSize(Integer.parseInt(queueSize));
                                
                        //serverobj = new Server(serverIP, Integer.parseInt(serverPort), monitorIP, Integer.parseInt(monitorPort), Integer.parseInt(loadBPort), Integer.parseInt(queueSize));
                        serverobj.run();
                        
                        return serverobj;
                    }
                }else{
                    JLogs.append("Server already started \n");
                    return null;
                }
            }
        }.execute();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new SwingWorker<Server, Object> (){
            @Override
            protected Server doInBackground() throws Exception {
                if (!estado){
                    JLogs.append("Server is already down \n");
                    return null;
                }else{
                    estado = false;
                    JLogs.append("Connection ended by server \n");
                    
                    jServerIP.setEnabled(true);
                    jServerPort.setEnabled(true);
                    jMonitorIP.setEnabled(true);
                    jMonitorPort.setEnabled(true);
                    jLoadBPort.setEnabled(true);
                    jQueueSize.setEnabled(true);
                    
                    serverobj.stop();
                    
                    return serverobj;
                }
            }
        }.execute();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMonitorIPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMonitorIPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMonitorIPActionPerformed

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
            java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea JLogs;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField jLoadBPort;
    private javax.swing.JTextField jMonitorIP;
    private javax.swing.JTextField jMonitorPort;
    private javax.swing.JTextField jQueueSize;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jServerIP;
    private javax.swing.JTextField jServerPort;
    // End of variables declaration//GEN-END:variables
}
