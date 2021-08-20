import java.awt.*;

public class Point {
	public int x, y;
	public double longitude, latitude;
	public int size = 10;
	
	public boolean hover = false; //if the point is being hovered over
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
		calculateCoordiantes();
	}
	
	public Point(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
		calculatePosition();
	}
	
	public Point() {
		this.x = 0;
		this.y = 0;
	}
	
	public void drawPoint(Graphics2D graphics) {
		graphics.setPaint(Color.black);
		graphics.setStroke(new BasicStroke(5));
		graphics.drawOval(x - (size/2), y - (size/2), size, size);
		graphics.setPaint(Color.red);
		graphics.fillOval(x - (size/2), y - (size/2), size, size);
	}
	
	public void drawInfo(Graphics2D graphics) {
		if(hover) {
			graphics.setPaint(Color.black);
			graphics.setFont(new Font("Arial", Font.BOLD, 15));
			graphics.drawString("longitude: " + Math.round(longitude * 10000.0) / 10000.0 + " " + "latitude: " + Math.round(latitude * 10000.0) / 10000.0, x + size , y);
		}
	}
	
	public void calculatePosition() {
		double xPercent = ( (longitude - Map.mapLongitudeMin) / (Map.mapLongitudeMax - Map.mapLongitudeMin) ); //Min left and Max right of screen
		double yPercent = ( (latitude - Map.mapLatitudeMax) / (Map.mapLatitudeMin - Map.mapLatitudeMax) ); //Since (0,0) is on the top left Max is a the bottom and Min is at the top of the screen

		x = (int) (xPercent * GUI.map.getWidth());
		y = (int) (yPercent * GUI.map.getHeight());
	}
	
	public void calculateCoordiantes() {
		double longPercent = ((double) this.x) / ((double) GUI.map.getWidth());
		double latPercent = ((double) this.y) / ((double) GUI.map.getHeight());
		
		this.longitude = (longPercent * (Map.mapLongitudeMax - Map.mapLongitudeMin) ) + Map.mapLongitudeMin;
		this.latitude = (latPercent * (Map.mapLatitudeMin - Map.mapLatitudeMax) ) + Map.mapLatitudeMax; //Since (0,0) is on the top left Max is a the bottom and Min is at the top of the screen
	}
	
	public double calculateDistance(Point nextPoint) { //Calculates the distance in Kms from point 1 to point 2. Takes into Consideration the curvature of the Earth
		//Equation from https://en.wikipedia.org/wiki/Great-circle_distance
		
		double lat1 = Math.toRadians(this.latitude);
		double long1 = Math.toRadians(this.longitude);
		double lat2 = Math.toRadians(nextPoint.latitude);
		double long2 = Math.toRadians(nextPoint.longitude);		
        
        double distance = Math.acos(Math.sin(lat1) * Math.sin(lat2) + 
                          Math.cos(lat1) * Math.cos(lat2) *
                          Math.cos(long1 - long2));
        
        distance *= 6371.01;
        
        return Math.abs(distance);
		
	}
	
	public boolean equals(Point comparePoint) {
		if( this.x == comparePoint.x && this.y == comparePoint.y) {
			return true;
		}
		return false;
	}
	
}
