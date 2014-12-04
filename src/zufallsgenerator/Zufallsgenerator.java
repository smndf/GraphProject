package zufallsgenerator;

import graph.Graph;
import graph.Position;

import java.util.ArrayList;
import java.util.Random;

public class Zufallsgenerator {


	public static Graph createZG(int knotenzahl, int maxKapazitat){
		ArrayList<Position> positions = new ArrayList<Position>();
		int[][] capacity = new int[knotenzahl][knotenzahl];
		Graph graph = new Graph(positions,capacity);
		Random r = new Random();
		int randomX;
		int randomY;
		for (int i = 0; i<knotenzahl; i++){
				randomX = r.nextInt(900);
				randomY = r.nextInt(500);
				positions.add(new Position(randomX, randomY));
				//System.out.println(i + " : [" + randomX + " , " + randomY+ "]");
		}
		graph.setKnotenPosition(positions);
		
		int randomKapazitat = 1 + r.nextInt(maxKapazitat+1);
		capacity[0][1] = randomKapazitat;
		randomKapazitat = 1 + r.nextInt(maxKapazitat+1);
		capacity[1][0] = randomKapazitat;
		for (int i=0; i<knotenzahl - 1; i++){
			for (int j=2; j<knotenzahl; j++){
				if (!graph.intersectKanten(positions.get(i), positions.get(j)) && i!=j){
					//System.out.println("Segment : [" + i + " , " + j + "]");
					//System.out.println("add Segment");
					randomKapazitat = 1 + r.nextInt(maxKapazitat+1);
					capacity[i][j] = randomKapazitat;
					randomKapazitat = 1 + r.nextInt(maxKapazitat+1);
					capacity[j][i] = randomKapazitat;
				}
			}
		}		
		graph.setCapacity(capacity);
		return graph;
	}
}
