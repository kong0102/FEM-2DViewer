/* 
window
Kong
*/
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.io.*;


public class MainFrame implements ItemListener,MouseMotionListener,MouseListener
{
	JFrame f = new JFrame("2D_Viewer_KONG");
	
	JCheckBox chboxElm, chboxEdg,  chboxNdv, chboxNdvscl;
	
	JLabel labelElm = new JLabel("Elements : 0    ");
	JLabel labelNod = new JLabel("Nodes : 0    ");
	JLabel labelZoom = new JLabel("Zoom : 1.0    ");
	
	ModelCanvas mc;

	int mousex, mousey;


	public void init()
	{
		JPanel bottom = new JPanel();
		bottom.add(labelElm);
		bottom.add(labelNod);
		bottom.add(labelZoom);
		f.add(bottom,BorderLayout.SOUTH);
		
		JPanel pnlSettings = new JPanel(new FlowLayout(0,20,2));

		pnlSettings.add(chboxElm = new JCheckBox(" Element", true));
		pnlSettings.add(chboxEdg = new JCheckBox(" Edge"));
		pnlSettings.add(chboxNdv = new JCheckBox(" Nodal Value"));
		pnlSettings.add(chboxNdvscl = new JCheckBox(" Nodal Value Scale"));

		f.add(pnlSettings,BorderLayout.NORTH);
		
		
		chboxElm.addItemListener(this);
		chboxEdg.addItemListener(this);
		chboxNdv.addItemListener(this);
		chboxNdvscl.addItemListener(this);
		
		

		mc = new ModelCanvas();
		mc.setPreferredSize(new Dimension(600,500));
        f.add(mc,BorderLayout.CENTER);
		mc.addMouseListener(this);
		mc.addMouseMotionListener(this);

		new DropTarget(mc, new DropTargetAdapter() 
			{
			public void drop(DropTargetDropEvent e) 
				{
				try {
					Transferable transfer = e.getTransferable();
					if ( transfer.isDataFlavorSupported(DataFlavor.javaFileListFlavor) )
						{
						e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
						java.util.List fileList = (java.util.List)
							( transfer.getTransferData( DataFlavor.javaFileListFlavor ) );
					
						String fileName = ((File)fileList.get(0)).getAbsolutePath();
						
						
						String fileType = "";
						int point = fileName.lastIndexOf(".");
						if (point != -1) 
							{
							fileType = fileName.substring(point+1, fileName.length());
							}
					
						
						if ( fileType.equals("nv2") ) 
							{
							Main.model.loadNv(fileName);
							mc.repaint();
						    }
						else if ( fileType.equals("el2") ) 
							{
							Main.model.loadElm(fileName);
							f.setTitle("2D_Viewer_KONG       "+fileName);
							labelElm.setText("Elements : " + Main.model.getnElement()+"    ");
							labelNod.setText("Nodes : " + Main.model.getnNode()+"    ");
							labelZoom.setText("Zoom : " + Main.model.getZoom()+"    ");
							mc.repaint();
							}
												
					}
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		

		

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600,500);
		f.setVisible(true);
	}



	public void mouseEntered(MouseEvent me) {}
	
	public void mouseExited(MouseEvent me) {}
	
	public void mouseReleased(MouseEvent me) {}
	
	public void mouseClicked(MouseEvent me) {}
	
	public void mouseMoved(MouseEvent me) {}
	
	public void mousePressed(MouseEvent me) 
	{	
		mousex = me.getX();
		mousey = me.getY();
	}
		
	public void mouseDragged(MouseEvent me) 
	{	
		int dx = me.getX()-mousex;
		int dy = me.getY()-mousey;

		Main.model.setxOffset(Main.model.getxOffset()+dx);
		Main.model.setyOffset(Main.model.getyOffset()+dy);
		mousex += dx;
		mousey += dy;
		mc.repaint();
		
		
		
	}
	

	public void itemStateChanged(ItemEvent e) 
		{

		
		if (chboxElm.isSelected()) 
			{
			Main.model.setfelm(true);
			}
		if (!chboxElm.isSelected()) 
			{
			Main.model.setfelm(false);
			}

		if (chboxEdg.isSelected()) 
			{
			Main.model.setfedg(true);
			}
		if (!chboxEdg.isSelected()) 
			{
			Main.model.setfedg(false);
			}
		
		if (chboxNdv.isSelected()) 
			{
			Main.model.setfndv(true);
			}
		if (!chboxNdv.isSelected()) 
			{
			Main.model.setfndv(false);
			}

	
		if (chboxNdvscl.isSelected()) 
			{
			Main.model.setxm(0.06);
			Main.model.setfndvscl(true);
			}
		if (!chboxNdvscl.isSelected()) 
			{
			Main.model.setxm(0);
			Main.model.setfndvscl(false);
			}

		mc.repaint();
		}

	

}

