import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.ArrayList;

public class GUI extends JFrame { //Class for managing the GUI
	
	public static final Map map = new Map();
	private static final Toolbar toolbar = new Toolbar();
	
	public GUI() {
		this.setLayout(new BorderLayout());
		this.setTitle("Shortest Path");
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		this.setSize(map.getWidth() + toolbar.getWidth() + 15, map.getHeight() + 40);

		this.add(map, BorderLayout.CENTER);
		this.add(toolbar, BorderLayout.WEST);
	}
}

class Map extends JPanel implements MouseListener, MouseMotionListener {
	
	public static final double mapLongitudeMin = -6.71319, 
							   mapLongitudeMax = -6.45509,
							   mapLatitudeMin = 53.28271,
							   mapLatitudeMax = 53.41318;
	
	public static ArrayList<Point> points = new ArrayList<Point>(); //array for the points
	public static ArrayList<Point> shortestPath = new ArrayList<Point>(); //array for the points arranged in the shortest path
	
	private static ImageIcon image;
	
	public static Graphics2D graphics;
	
	public Map() {
		this.setBackground(Color.green);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		mapImage();
	    
	    this.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
	}
	
	private void mapImage() { //Scaling an setting the map image
		Image imageToScale =  new ImageIcon("map.png").getImage();
	    double scale = 1.1; //scale
	    int x = (int) (imageToScale.getWidth(null) * scale); 
	    int y = (int) (imageToScale.getHeight(null) * scale);
	    Image scaledImage = imageToScale.getScaledInstance( x, y, java.awt.Image.SCALE_SMOOTH); //scale the image
	    image = new ImageIcon(scaledImage);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		graphics = (Graphics2D) g;
		graphics.drawImage(image.getImage(), 0, 0, null);
		drawPoints();
		drawShortestPath();	
		drawPointInfo();
		Toolbar.displayDistance();
	}
	
	private void drawPoints() {
		for(int i = 0; i < points.size(); i++) {
			points.get(i).drawPoint(graphics);
		}
	}
	
	private void drawPointInfo() {
		for(int i = 0; i < points.size(); i++) {
			points.get(i).drawInfo(graphics);
		}
	}
	
	private void drawShortestPath() {
		graphics.setPaint(Color.magenta);
		graphics.setStroke(new BasicStroke(4));
		for(int i = 0; i < shortestPath.size() - 1; i++) {
			graphics.drawLine(shortestPath.get(i).x, shortestPath.get(i).y, shortestPath.get(i+1).x, shortestPath.get(i+1).y); //draw line from each point
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) { //Left click for adding new points
			Point point = new Point(e.getX(), e.getY());
			points.add(point);
			repaint();
		}
		else if(e.getButton() == MouseEvent.BUTTON3) { //Right click for removing points
			for(int i = points.size() - 1; i >= 0; i--) { //loop backwards so if points overlap remove the one on top first
				Point current = points.get(i);
				if( Math.abs( e.getX() - current.x ) < current.size/2 && Math.abs( e.getY() - current.y ) < current.size/2 ) {
					points.remove(i);
					repaint();
					break;
				}
			}
		}
		//After a point was added or removed. Run the algorithm again
		Algorithms.nearestNeighbourAlgorithm(); 
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		pointHover(e);
	}
	
	public void pointHover(MouseEvent e) { //if a point is hovered over with the mouse
		for(int i = points.size() - 1; i >= 0; i--) { //loop backwards so if points overlap prioritze the one on top first
			points.get(i).hover = false;
			repaint();
			if( Math.abs( e.getX() - points.get(i).x ) < points.get(i).size/2 && Math.abs( e.getY() - points.get(i).y ) < points.get(i).size/2 ) { //if the mouse is over the point
				points.get(i).hover = true;
				repaint();
				break;
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}
	
}

class Toolbar extends JPanel{
	
	private static final JTextField distanceField = new JTextField();
	private static final JLabel label = new JLabel("Distance");
	
	public Toolbar() {
		this.setBackground(Color.gray);
		this.setPreferredSize(new Dimension(300, 100));
		this.setSize(new Dimension(300, 100));
		
		this.add(label);
		
		distanceField.setPreferredSize(new Dimension(100, 30));
		distanceField.setEditable(false);
		this.add(distanceField);
	}
	
	public static void displayDistance() { //display the path as the output 
		double distance = 0.0;
		
		for(int i = 0; i < Map.shortestPath.size() - 1; i++) {
			distance +=  Map.shortestPath.get(i).calculateDistance(Map.shortestPath.get(i + 1));
		}

		distanceField.setText(Math.round(distance * 1000.0) / 1000.0 + " KM");
	}
}
