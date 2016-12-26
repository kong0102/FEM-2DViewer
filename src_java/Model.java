/* 
Model description
Kong
*/
import java.awt.*;
import java.io.*;
import java.util.*;


class Model
{   //Node
	int nNode = 0;
	double nodePos[][];//node coordinate 
	int nodePosSc[][];
	double nodeValue[];//node value
	double nvMax = 0;
	double nvMin = 0;
	double nvScaleMax = 0;//range of node value
	double nvScaleMin = 0;

	//Element
	int nElement = 0;
	int element[][];//element

	static final int ELM_DM = 0;
	static final int ELM_N0 = 1;	
	static final int ELM_N1 = 2;
	static final int ELM_N2 = 3;
	static final int SIZEOF_ELM = 4;
	
	//Option
	double cx=0.0;
	double cy=0.0;//center coordinate
	double zoom=1.0;//zoom scale
	double modelSize=1.0;

	int elmCol[]= new int[3];
	int edgCol ;
	int bgCol = 0xffffff;

	boolean fElm = true;
	boolean fEdg = false;
	boolean fNdv = false;
	boolean fNdvScl = false;

	double zm;
	double xm;
	int xOffset = 0;
	int	yOffset = 0;

/*---------------------------------

-----------------------------------*/
	public int loadSetting(String fileName)
	{
    
		try
		{
		Scanner sc=new Scanner (new File (fileName));
		while(sc.hasNext())
		{
			String ss=sc.next();
			if(ss.equals("elementColor"))
			{
				int n = 0;
				if (sc.hasNext()) 
					n = sc.nextInt()-1;
				int c = 0;
					for (int i=0; i<3; i++)
					{
						if (sc.hasNext()) 
							{
							c = c*256+(int)Math.min(Math.max(sc.nextInt(), 0), 255);
							}
					}
					elmCol[n] = c;
			}
			else if (ss.equals("edgeColor")) 
				{	
					int c = 0;
					for (int i=0; i<3; i++)
						{
						if (sc.hasNext()) 
							{
							c = c*256+(int)Math.min(Math.max(sc.nextInt(), 0), 255);
							}
						}
					edgCol = c;
					
				}
			else if (ss.equals("backgroundColor")) 
				{	
					int c = 0;
					for (int i=0; i<3; i++)
						{
						if (sc.hasNext()) 
							{
							c = c*256+(int)Math.min(Math.max(sc.nextInt(), 0), 255);
							}
						}
					bgCol = c;
					
				}
			else if (ss.equals("zoom")) 
				{	
					if (sc.hasNext()) zoom = sc.nextDouble();
				}

			
			else if (ss.equals("nodalValueScaleMax")) 
				{
					if (sc.hasNext()) nvScaleMax = sc.nextDouble();
				}
			else if (ss.equals("nodalValueScaleMin")) 
				{
					if (sc.hasNext()) nvScaleMin = sc.nextDouble();
				}
		}
		sc.close();

		}
	
	catch (Exception e)
	{
		try {
				PrintWriter fout = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
				
				fout.println("elementColor 1 128 192 255");
				fout.println("elementColor 2 128 255 128");
				fout.println("elementColor 3 255 255 128");
				
				fout.println("edgeColor  32 48 64");

				fout.println("backgroundColor  255 255 255");

				fout.println("zoom 1.0");

				fout.println("nodalValueScaleMax 0.0");
				fout.println("nodalValueScaleMin 0.0");
				
				fout.close();
				
				return 1;
			}
			catch (Exception e2) 
			{
				return 2;
			}
		}
		return 0;
	}

/*---------------------------------

-----------------------------------*/
public int loadElm(String fileName) //load element 
	{
		try {
			
			Scanner sc = new Scanner(new File(fileName));
			
			nNode = sc.nextInt();
			nodePos = new double[nNode][2];
			nodePosSc = new int[nNode][2];
			nodeValue = new double[nNode];
			for (int i=0; i<nNode; i++) 
				{
				for (int j=0; j<2; j++) 
					{
					nodePos[i][j] = sc.nextDouble();
				    }
			    }
			
			nElement = sc.nextInt();
			element = new int[nElement][SIZEOF_ELM];
			for (int i=0; i<nElement; i++) 
				{
				for (int j=0; j<4; j++) 
					{
					element[i][j] = sc.nextInt()-1;
				    }
				sc.next();
			    }
			
			sc.close();
		}
		catch (Exception e) 
			{
			e.printStackTrace();
			nNode = 0;
			nElement = 0;
			return 1;
		    }

		
		double xmax = nodePos[0][0];
		double ymax = nodePos[0][1];
		double xmin=xmax;
		double ymin=ymax;

		for (int i=1; i<nNode; i++) 
			{
			double x = nodePos[i][0];
			double y = nodePos[i][1];
			xmax = Math.max(xmax, x);
			xmin = Math.min(xmin, x);
			ymax = Math.max(ymax, y);
			ymin = Math.min(ymin, y);
		   }
		
		cx = (xmax+xmin)/2.0;
		cy = (ymax+ymin)/2.0;
		
		
		modelSize = Math.max(xmax-xmin, ymax-ymin);

		return 0;
	
	}

/*---------------------------------

-----------------------------------*/
public int loadNv(String fileName)//load node value
  {
	try
	{
		Scanner sc = new Scanner(new File(fileName));
		int nNodeNv=sc.nextInt();
	 
	 if(nNodeNv==nNode)
	 {
		for(int i=0;i<nNode;i++){nodeValue[i]=sc.nextDouble();}
		
		if(nvScaleMax==nvScaleMin)
		{
			nvMax=nodeValue[0];
			nvMin=nvMax;
			for(int i=1;i<nNode;i++)
				{if(nodeValue[i]>nvMax) nvMax=nodeValue[i];
			     if(nodeValue[i]<nvMin) nvMin=nodeValue[i];}
		}
		else 
		{
			nvMax=nvScaleMax;
			nvMin=nvScaleMin;
		}
		//regularization
		double nvrange = nvMax-nvMin;
		if (nvrange == 0.0) nvrange = 1.0;
		for (int i=0; i<nNode; i++) 
		{nodeValue[i] = (nodeValue[i]-nvMin)/nvrange;}

	 }
	 sc.close();
	}
	catch (Exception e)
	{
		e.printStackTrace();
		return 1;
	}
	return 0;
  }


/*---------------------------------

-----------------------------------*/

public static int getGradColor(double c) 
	{
		c *= 4.0;
		int colr = 0, colg = 0, colb = 0;
		
		if (c < 1.0) 
			{		// blue - cyan
			colb = 255;
			colg = (int)(c*255);
			}			
		else if (c < 2.0) 
			{		// cyan - green
			colg = 255;
			colb = 255-(int)((c-1.0)*255);
			}
		else if (c < 3.0) 
			{	   // green - yellow
			colg = 255;
			colr = (int)((c-2.0)*255);
			}

		else  
			{	// yellow - red
			colr = 255;
			colg = 255-(int)((c-3.0)*255);
			}
		return (colr<<16) | (colg<<8) | colb;
	}   

/*---------------------------------

-----------------------------------*/
public int draw(Graphics g,int width,int height)
	{
	int scx = width/2+(int)(width*xm)+xOffset;
	int scy = height/2+yOffset;

	Color colorBg = new Color(bgCol);
	g.setColor(colorBg);
	g.fillRect(0,0,width,height);

	double zm=width*0.5/modelSize*zoom;
	for (int i=0; i<nNode; i++) 
		{
			nodePosSc[i][0] = scx+(int)((nodePos[i][0]-cx)*zm);
			nodePosSc[i][1] = scy-(int)((nodePos[i][1]-cy)*zm);
		}
	
		for(int i=0;i<nElement;i++)
			{
			int dm = Math.abs(element[i][ELM_DM])%3;
			//node number of 1 element
			int n0 = element[i][1];
			int n1 = element[i][2];
			int n2 = element[i][3];

			//node coordinate of 1 element
			int px[]=new int[3];
			int py[]=new int[3];
			px[0] = nodePosSc[n0][0];
			px[1] = nodePosSc[n1][0];
			px[2] = nodePosSc[n2][0];
			py[0] = nodePosSc[n0][1];
			py[1] = nodePosSc[n1][1];
			py[2] = nodePosSc[n2][1];

			if (fElm) 
				{
				if (fNdv) 
					{	
						double cc[] = new double[3];
						cc[0] = nodeValue[n0];
						cc[1] = nodeValue[n1];
						cc[2] = nodeValue[n2];
						drawGrad(g, px, py, cc);
					}
					else {
						Color colorElm = new Color(elmCol[dm]);
						g.setColor(colorElm);
						g.fillPolygon(px, py, 3);
					}
				}
				if (fEdg) 
					{
					
					g.setColor(new Color(edgCol));
					g.drawPolygon(px, py, 3);
				
					}
				
			}

	if(fNdvScl)
		{
		int px=20;
		int py=height/4;
		int sx=16; //width of scale
		int sy=height/2;
		for(int i=0;i<sy;i++)
			{
			double c = (double)(sy-i-1)/(sy-1);
			g.setColor(new Color(getGradColor(c)));
			g.fillRect(px,py+i,sx,1);
			}
		g.setColor(Color.BLACK);
		g.drawString(""+(float)nvMax,px+sx+3,py+4);
		g.drawString(""+(float)nvMin,px+sx+3,py+sy+4);
		}
		return 0;
	}



/*---------------------------------

-----------------------------------*/				
public static int drawGrad(Graphics g, int px[], int py[], double cc[])
	 {
		
		double s = (py[1]-py[0])*(px[2]-px[0])-(px[1]-px[0])*(py[2]-py[0]);//if Collinear
		double cu = 0.0, cv = 0.0;
	if(s != 0.0)
		{
			cu = ((cc[1]-cc[0])*(py[2]-py[0])-(py[1]-py[0])*(cc[2]-cc[0]))/s;
			cv = ((px[1]-px[0])*(cc[2]-cc[0])-(cc[1]-cc[0])*(px[2]-px[0]))/s;
			
	
		if (cu != 0.0 || cv != 0.0) 
			{	
			double cmin = cc[0], cminx = px[0], cminy = py[0];
			double cmax = cc[0], cmaxx = px[0], cmaxy = py[0];
			
			for (int i=1; i<=2; i++) 
				{
				if (cc[i] < cmin) 
					{
					cmin = cc[i]; cminx = px[i]; cminy = py[i];
					}
				else if (cc[i] > cmax) 
					{
					cmax = cc[i]; cmaxx = px[i]; cmaxy = py[i];
					}
			}
			
			if (cmax-cmin < 0.01) 
				{
				Color colorElm = new Color(getGradColor(cmax));
				g.setColor(colorElm);
				g.fillPolygon(px, py, 3);
				}
			else
				{
				double invcl = 1.0/Math.sqrt(cu*cu+cv*cv);
				cu *= invcl; cv *= invcl;
				double p = cu*(cmaxx-cminx)+cv*(cmaxy-cminy);
				cmaxx = cminx+cu*p;
				cmaxy = cminy+cv*p;
					
				Graphics2D g2 = (Graphics2D)g;
				GradientPaint gp = new GradientPaint(
					(float)cminx, (float)cminy, new Color(getGradColor(cmin)),
					(float)cmaxx, (float)cmaxy, new Color(getGradColor(cmax)));
				g2.setPaint(gp);
				g.fillPolygon(px, py, 3);
				}
			}
		else
			{
			Color colorElm = new Color(getGradColor(cc[0]));
			g.setColor(colorElm);
			g.fillPolygon(px, py, 3);
			}
		}
		
		return 0;
	}

/*---------------------------------

-----------------------------------*/	
	public int getnNode() 
	{
		return nNode;
	}
	public int getnElement() 
	{
		return nElement;
	}
	public double getZoom() 
	{
		return zoom;
	}
	public int getxOffset()
	{
		return xOffset;
	}
	public int getyOffset() 
	{
		return yOffset;
	}
	public void setxOffset(int v) 
	{
		xOffset = v;
	}
	public void setyOffset(int v) 
	{
		yOffset = v;
	}

	
	public void setfelm(boolean f) 
	{	
		fElm = f;
	}
	public void setfedg(boolean f) 
	{	
		fEdg = f;
	}

	public void setfndv(boolean f) 
	{	
		fNdv = f;
	}
	public void setfndvscl(boolean f) 
	{
		fNdvScl = f;
	}
	
	public void setxm(double f) 
	{
		xm = f;
	}
	

}
	