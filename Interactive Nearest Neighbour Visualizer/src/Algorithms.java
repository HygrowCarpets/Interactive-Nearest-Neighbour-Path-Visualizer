public class Algorithms  {
	
	public Algorithms() {

	}
	
	public static void nearestNeighbourAlgorithm() {
		if(Map.points.size() == 0) {
			return;
		}
		
		Map.shortestPath.clear();
		Map.shortestPath.add(Map.points.get(0));
		for(int i = 0; i < Map.points.size() - 1; i++) { 
			Map.shortestPath.add(findNearestNeighbour(Map.shortestPath.get(i)));
			GUI.map.repaint();
		}
	}
	
	public static Point findNearestNeighbour(Point point) {
		Point nearestNeighbour = null;
		double shortestSoFar = 1000000.0; 

		for(int i = 0; i < Map.points.size(); i++) {
			if(point != Map.points.get(i) && point.calculateDistance(Map.points.get(i)) < shortestSoFar && Map.shortestPath.contains(Map.points.get(i)) == false) { //if the point is not itself and is the closest distance so far and it has not yet been visited
				shortestSoFar = point.calculateDistance(Map.points.get(i));
				nearestNeighbour = Map.points.get(i);
			}
		}
		return nearestNeighbour;
	}
	
}
