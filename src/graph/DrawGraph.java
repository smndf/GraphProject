package graph;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class DrawGraph  extends JPanel {

	private static final long serialVersionUID = 1L;
	private Graph graph;
	private ArrayList<Integer> pathGraph;

	public DrawGraph(Graph residualGraph, ArrayList<Integer> path) {
		graph = residualGraph;
		pathGraph = path;
	}

	public DrawGraph(Graph residualGraph) {
		graph = residualGraph;
		pathGraph = null;
	}

	protected void paintComponent(Graphics g){

		super.paintComponent(g);
		
		
		//// AFFICHAGE NOEUDS GRAPHE /////
		ArrayList<Position> positions = graph.getKnotenPosition();
		g.setColor(Color.blue);
		int count = 0;
		for (Position p : positions){
			g.drawString(Integer.toString(count), p.getX()-5 ,p.getY()-5);
			g.fillOval(p.getX()-4, p.getY()-4, 7, 7);
			count++;
		}
		int[][] capacities = graph.getCapacity();
		/*System.out.println("Graphe residual draw: " );
		for (int i=0; i < capacities.length; i++){
			System.out.println("{" + capacities[i][0] + " , " + capacities[i][1] + " , " + capacities[i][2] + " , " + capacities[i][3] + " , " + capacities[i][4] + " , " + capacities[i][5] + "}");
		}*/
		
		
		//// AFFICHAGE ARETES GRAPHE /////
		g.setColor(Color.black);
		int flow;
		int n = capacities.length;
		int x1;
		int x2;
		int y1;
		int y2;
		for (int i = 0; i < n ; i++){
			for (int j = 0; j < n ; j++){
				flow = capacities[i][j];
				//System.out.println("flow[" + i + "][" + j + "] : " + flow);
				if (flow > 0){
					x1 = positions.get(i).getX();
					x2 = positions.get(j).getX();
					y1 = positions.get(i).getY();
					y2 = positions.get(j).getY();
					g.drawLine(x1, y1 , x2, y2);
					if (i>j){
						g.drawString(Integer.toString(flow), Math.min(x1, x2)+Math.abs((x1-x2)/2)+10, Math.min(y1, y2)+Math.abs((y1-y2)/2)+10);												
					} else {
						g.drawString(Integer.toString(flow), Math.min(x1, x2)+Math.abs((x1-x2)/2)-10, Math.min(y1, y2)+Math.abs((y1-y2)/2)-10);						
					}
				}
			}	
		}
		
		//// AFFICHAGE PATH /////
		g.setColor(Color.red);
		if (pathGraph != null){
			for (int k = 0; k < pathGraph.size()-1; k++){
				x1 = graph.getKnotenPosition().get(pathGraph.get(k)).getX();
				y1 = graph.getKnotenPosition().get(pathGraph.get(k)).getY();
				x2 = graph.getKnotenPosition().get(pathGraph.get(k+1)).getX();
				y2 = graph.getKnotenPosition().get(pathGraph.get(k+1)).getY();
				flow = capacities[pathGraph.get(k)][pathGraph.get(k+1)];
				g.drawLine(x1, y1 , x2, y2);
				/// la ligne en dessous donne des choses fausses
				//System.out.println("path : flow[" + pathGraph.get(k) + "][" + pathGraph.get(k+1) + "] : " + flow);
				g.drawString(Integer.toString(flow), Math.min(x1, x2)+Math.abs((x1-x2)/2)-5, Math.min(y1, y2)+Math.abs((y1-y2)/2)-5);						
			}
		}
	}


}
