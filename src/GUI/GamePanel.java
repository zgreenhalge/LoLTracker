/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import java.awt.Color;

/**
 *
 * @author zgree_000
 */
public class GamePanel extends javax.swing.JFrame {

    public static final Color RED = new Color(242, 146, 124);
    public static final Color GREEN = new Color(91, 255, 96);
    /**
     * Creates new form GameSummarySmallPanel
     */
    public GamePanel(){
        initComponents();
    }

    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0));
        summaryPane = new javax.swing.JLayeredPane();
        queueLabel = new javax.swing.JLabel();
        playedAsName_Level = new javax.swing.JLabel();
        killLabel = new javax.swing.JLabel();
        deathLabel = new javax.swing.JLabel();
        assistsLabel = new javax.swing.JLabel();
        killValue = new javax.swing.JLabel();
        deathValue = new javax.swing.JLabel();
        assistValue = new javax.swing.JLabel();
        damageDealtLabel = new javax.swing.JLabel();
        damageTakenLabel = new javax.swing.JLabel();
        damageTakenValue = new javax.swing.JLabel();
        damageDealtValue = new javax.swing.JLabel();
        kdaRatioLabel = new javax.swing.JLabel();
        kdaRatioValue = new javax.swing.JLabel();
        goldLabel = new javax.swing.JLabel();
        goldValue = new javax.swing.JLabel();
        csLabel = new javax.swing.JLabel();
        csValue = new javax.swing.JLabel();
        bottomSeparator = new javax.swing.JSeparator();
        topSeparator = new javax.swing.JSeparator();
        championInfo = new javax.swing.JPanel();
        wardsPlacedLabel = new javax.swing.JLabel();
        wardsPlacedValue = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 240, 7));
        setForeground(java.awt.Color.pink);
        setPreferredSize(new java.awt.Dimension(806, 120));

        summaryPane.setForeground(new java.awt.Color(240, 240, 240));
        summaryPane.setOpaque(true);

        queueLabel.setFont(new java.awt.Font("Corbel", 1, 24)); // NOI18N
        queueLabel.setText("QueueType - Win/Loss");

        playedAsName_Level.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        playedAsName_Level.setText("SummonerName - Level ");

        killLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        killLabel.setText("Kills:");

        deathLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        deathLabel.setText("Deaths:");

        assistsLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        assistsLabel.setText("Assists:");

        killValue.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        killValue.setText("00");

        deathValue.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        deathValue.setText("00");

        assistValue.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        assistValue.setText("00");

        damageDealtLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        damageDealtLabel.setText("Damage dealt: ");

        damageTakenLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        damageTakenLabel.setText("Damage taken: ");

        damageTakenValue.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        damageTakenValue.setText("000000");

        damageDealtValue.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        damageDealtValue.setText("000000");

        kdaRatioLabel.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        kdaRatioLabel.setText("KDA:");

        kdaRatioValue.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        kdaRatioValue.setText("00.00");

        goldLabel.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        goldLabel.setText("Gold:");

        goldValue.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        goldValue.setText("000000");

        csLabel.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        csLabel.setText("CS: ");

        csValue.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        csValue.setText("000");

        javax.swing.GroupLayout championInfoLayout = new javax.swing.GroupLayout(championInfo);
        championInfo.setLayout(championInfoLayout);
        championInfoLayout.setHorizontalGroup(
            championInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 192, Short.MAX_VALUE)
        );
        championInfoLayout.setVerticalGroup(
            championInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        wardsPlacedLabel.setText("Wards Placed:");

        wardsPlacedValue.setText("00");

        javax.swing.GroupLayout summaryPaneLayout = new javax.swing.GroupLayout(summaryPane);
        summaryPane.setLayout(summaryPaneLayout);
        summaryPaneLayout.setHorizontalGroup(
            summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bottomSeparator)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, summaryPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(summaryPaneLayout.createSequentialGroup()
                        .addComponent(queueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kdaRatioLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kdaRatioValue)
                        .addGap(18, 18, 18)
                        .addComponent(goldLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(goldValue, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(csLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(csValue))
                    .addGroup(summaryPaneLayout.createSequentialGroup()
                        .addComponent(playedAsName_Level)
                        .addGap(16, 16, 16)
                        .addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(summaryPaneLayout.createSequentialGroup()
                                .addComponent(killLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(killValue)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(deathLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deathValue)
                                .addGap(18, 18, 18)
                                .addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(assistValue, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, summaryPaneLayout.createSequentialGroup()
                                        .addComponent(assistsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(19, 19, 19)))
                                .addGap(18, 18, 18)
                                .addComponent(wardsPlacedLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(wardsPlacedValue))
                            .addGroup(summaryPaneLayout.createSequentialGroup()
                                .addComponent(damageDealtLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(damageDealtValue)
                                .addGap(18, 18, 18)
                                .addComponent(damageTakenLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(damageTakenValue)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(championInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(topSeparator)
        );
        summaryPaneLayout.setVerticalGroup(
            summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(summaryPaneLayout.createSequentialGroup()
                .addComponent(topSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(summaryPaneLayout.createSequentialGroup()
                        .addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(kdaRatioValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(queueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(kdaRatioLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(goldLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(goldValue, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(csLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(csValue)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(killLabel)
                                .addComponent(deathLabel)
                                .addComponent(deathValue)
                                .addComponent(assistsLabel)
                                .addComponent(assistValue)
                                .addComponent(killValue)
                                .addComponent(wardsPlacedLabel)
                                .addComponent(wardsPlacedValue))
                            .addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(playedAsName_Level, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(damageDealtLabel)
                                .addComponent(damageDealtValue)
                                .addComponent(damageTakenLabel)
                                .addComponent(damageTakenValue))))
                    .addComponent(championInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bottomSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        summaryPane.setLayer(queueLabel, javax.swing.JLayeredPane.PALETTE_LAYER);
        summaryPane.setLayer(playedAsName_Level, javax.swing.JLayeredPane.PALETTE_LAYER);
        summaryPane.setLayer(killLabel, javax.swing.JLayeredPane.PALETTE_LAYER);
        summaryPane.setLayer(deathLabel, javax.swing.JLayeredPane.PALETTE_LAYER);
        summaryPane.setLayer(assistsLabel, javax.swing.JLayeredPane.PALETTE_LAYER);
        summaryPane.setLayer(killValue, javax.swing.JLayeredPane.PALETTE_LAYER);
        summaryPane.setLayer(deathValue, javax.swing.JLayeredPane.PALETTE_LAYER);
        summaryPane.setLayer(assistValue, javax.swing.JLayeredPane.PALETTE_LAYER);
        summaryPane.setLayer(damageDealtLabel, javax.swing.JLayeredPane.PALETTE_LAYER);
        summaryPane.setLayer(damageTakenLabel, javax.swing.JLayeredPane.PALETTE_LAYER);
        summaryPane.setLayer(damageTakenValue, javax.swing.JLayeredPane.PALETTE_LAYER);
        summaryPane.setLayer(damageDealtValue, javax.swing.JLayeredPane.PALETTE_LAYER);
        summaryPane.setLayer(kdaRatioLabel, javax.swing.JLayeredPane.PALETTE_LAYER);
        summaryPane.setLayer(kdaRatioValue, javax.swing.JLayeredPane.PALETTE_LAYER);
        summaryPane.setLayer(goldLabel, javax.swing.JLayeredPane.PALETTE_LAYER);
        summaryPane.setLayer(goldValue, javax.swing.JLayeredPane.PALETTE_LAYER);
        summaryPane.setLayer(csLabel, javax.swing.JLayeredPane.PALETTE_LAYER);
        summaryPane.setLayer(csValue, javax.swing.JLayeredPane.PALETTE_LAYER);
        summaryPane.setLayer(bottomSeparator, javax.swing.JLayeredPane.DEFAULT_LAYER);
        summaryPane.setLayer(topSeparator, javax.swing.JLayeredPane.DEFAULT_LAYER);
        summaryPane.setLayer(championInfo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        summaryPane.setLayer(wardsPlacedLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        summaryPane.setLayer(wardsPlacedValue, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(summaryPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(summaryPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel assistValue;
    private javax.swing.JLabel assistsLabel;
    private javax.swing.JSeparator bottomSeparator;
    private javax.swing.JPanel championInfo;
    private javax.swing.JLabel csLabel;
    private javax.swing.JLabel csValue;
    private javax.swing.JLabel damageDealtLabel;
    private javax.swing.JLabel damageDealtValue;
    private javax.swing.JLabel damageTakenLabel;
    private javax.swing.JLabel damageTakenValue;
    private javax.swing.JLabel deathLabel;
    private javax.swing.JLabel deathValue;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel goldLabel;
    private javax.swing.JLabel goldValue;
    private javax.swing.JLabel kdaRatioLabel;
    private javax.swing.JLabel kdaRatioValue;
    private javax.swing.JLabel killLabel;
    private javax.swing.JLabel killValue;
    private javax.swing.JLabel playedAsName_Level;
    private javax.swing.JLabel queueLabel;
    private javax.swing.JLayeredPane summaryPane;
    private javax.swing.JSeparator topSeparator;
    private javax.swing.JLabel wardsPlacedLabel;
    private javax.swing.JLabel wardsPlacedValue;
    // End of variables declaration//GEN-END:variables
    
    public void setAssistValue(String s){
        assistValue.setText(s);
    }
    
    public void setCSValue(String s){
        csValue.setText(s);
    }
    
    public void setDamageDealtValue(String s){
        damageDealtValue.setText(s);
    }
    
    public void setDamageTakenValue(String s){
        damageTakenValue.setText(s);
    }
    
    public void setDeathValue(String s){
        deathValue.setText(s);
    }
    
    public void setGoldValue(String s){
        goldValue.setText(s);
    }
    
    public void setKDARatioValue(String s){
        kdaRatioValue.setText(s);
    }
    
    public void setKillValue(String s){
        killValue.setText(s);
    }
    
    public void setPlayedAsName_Level(String s){
        playedAsName_Level.setText(s);
    }
    
    public void setQueueLabel(String s){
        queueLabel.setText(s);
    }
    
    public void setWardsPlacedValue(String s){
        wardsPlacedValue.setText(s);
    }
    
    public void topSeparator(){
        topSeparator.setVisible(!topSeparator.isVisible());
    }
    
    public void bottomSeparator(){
        bottomSeparator.setVisible(bottomSeparator.isVisible());
    }
    
    public void color(Color c){
        summaryPane.setBackground(c);
        championInfo.setBackground(c);
    }
    
}
