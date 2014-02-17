package robotsimulator.ui;


import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import robotsimulator.FileUtility;
import robotsimulator.engine.LuaEngine;

/**
 * 
 * @author Stian Sandviknes
 */
public class Startup extends javax.swing.JFrame {
    final JFileChooser jFile = new JFileChooser();
    
    /** Creates new form Startup */
    public Startup() {
        initComponents();
        populateProjects();
        addDisplayModes();
        setupFileChooser();
    }

    private void setupFileChooser() {
        jFile.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.getName().toUpperCase().endsWith(".LUA")) return true;
                return false;
            }

            @Override
            public String getDescription() {
                return "Project main file";
            }
        });
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstProj = new javax.swing.JList();
        btnOther = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstDisplayMode = new javax.swing.JList();
        btnStart = new javax.swing.JButton();
        chkFullscreen = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Lua Robot Visualizer");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24));
        jLabel1.setText("Lua Robot Visualizer");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Projects"));

        lstProj.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(lstProj);

        btnOther.setText("Other...");
        btnOther.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOtherActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnOther, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOther)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Display Mode"));

        lstDisplayMode.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstDisplayMode.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                onDisplayChange(evt);
            }
        });
        jScrollPane2.setViewportView(lstDisplayMode);

        btnStart.setText("Start");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        chkFullscreen.setText("Fullscreen");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(chkFullscreen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnStart)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnStart)
                    .addComponent(chkFullscreen))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOtherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOtherActionPerformed
        if (jFile.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
            File f = jFile.getSelectedFile();
            VisProject proj = new VisProject(f.getParentFile().getName(), f.getParent(), f.getName());
            projects.add(proj);
            lstProj.setListData(projects.toArray());
            lstProj.setSelectedValue(proj, true);
        }
    }//GEN-LAST:event_btnOtherActionPerformed

    private void onDisplayChange(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_onDisplayChange
        
    }//GEN-LAST:event_onDisplayChange

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        VisProject proj= defaultProject;
        DisplayMode mode = new DisplayMode(640,480);
        if (lstProj.getSelectedValue()!=null) { proj=(VisProject) lstProj.getSelectedValue(); }
        if (lstDisplayMode.getSelectedValue()!=null) {
            mode = (DisplayMode) lstDisplayMode.getSelectedValue();
            if (!chkFullscreen.isSelected()) {
                mode = new DisplayMode(mode.getWidth(),mode.getHeight());
            }
        }
        DisplayManager manager = DisplayManager.getInstance();
        manager.setTitle("Robot Visualizer");
        manager.setDisplayMode(mode);
        FileUtility.addSearchPath(proj.path);
        LuaEngine engine = new LuaEngine(proj.mainFile);
        manager.addDisplayable(engine);
        manager.setVisible(true);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_btnStartActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOther;
    private javax.swing.JButton btnStart;
    private javax.swing.JCheckBox chkFullscreen;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JList lstDisplayMode;
    private javax.swing.JList lstProj;
    // End of variables declaration//GEN-END:variables
    
    private class VisProject {
        private String title;
        private String path;
        private String mainFile;

        public VisProject(String title, String path, String mainFile) {
            this.title = title;
            this.path = path;
            this.mainFile = mainFile;
        }
        @Override
        public String toString() {
            return title;
        }
    }
    
    private ArrayList<VisProject> projects = new ArrayList<VisProject>();
    private VisProject defaultProject = new VisProject("Default", FileUtility.findFile("default").getPath(), "main.lua");
    
    private void populateProjects() {
        
        projects.add(defaultProject);
        File f = FileUtility.findFile("projects");
        for (File child:f.listFiles()) {
            if (child.isDirectory()) {
                if (new File((child.getAbsolutePath()+File.separator+"main.lua")).exists()) {
                    projects.add(new VisProject(child.getName(), child.getPath(), "main.lua"));
                }
            }
        }
        for (VisProject proj:projects) {
            System.out.println(proj);
        }
        lstProj.setListData(projects.toArray());
        lstProj.setSelectedIndex(0);
    }
    
    private void addDisplayModes() {
        try {
            lstDisplayMode.setListData(Display.getAvailableDisplayModes());
            lstDisplayMode.setSelectedValue(Display.getDisplayMode(), true);
        } catch (LWJGLException ex) {
            lstDisplayMode.setListData(new Object[]{new DisplayMode(640, 480)});
        }
    }
    
}
