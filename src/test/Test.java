package test;

import java.util.ArrayList;

import algorithmen.Dinic;
import algorithmen.EdmondsKarp;
import algorithmen.FordFulkerson;
import algorithmen.GoldbergTarjan;
import zufallsgenerator.Zufallsgenerator;
import graph.Graph;
import graph.Position;

public class Test {
/*		public void	paintComponent(Graphics	g, Graphbis graph){
			super.paintComponent(g);
			Graph graph = Zufallsgenerator.createZG(10, 10);
			System.out.println("nb knoten" + graph.getKnoten().size());
			int i=0;
			ArrayList<Position> positions = graph.getKnotenPosition();
			for (Position p : positions){
				g.fillOval(p.getX()-5, p.getY()-5, 10, 10);
			}
			int[][] capacities = graph.getCapacity();
			int flow;
			int n = capacities.length;
			for (int i = 1; i < n ; i++){
				for (int j = 1; j < n ; j++){
					flow = capacities[i][j];
					if (flow != 0){
						g.drawLine(positions.get(i).getX(), positions.get(i).getY(), positions.get(j).getX(), positions.get(j).getY());
					}
				}	
			}
			
			for (Kanten kanten: graph.getKanten()){
				Knoten head = kanten.getHead();
				int headX = head.getPosX();
				int headY = head.getPosY();
				Knoten tail = kanten.getTail();
				int tailX = tail.getPosX();
				int tailY = tail.getPosY();
				int flow = kanten.getCapacity();
				g.drawLine(headX, headY, tailX, tailY);
				// g.drawString(Integer.toString(flow), Math.min(headX, tailX)+Math.abs(headX-tailX/2), Math.min(headY, tailY)+Math.abs(headY-tailY/2));
			}
	}
*/
		public static void main(String[] args){
			ArrayList<Position> pos = new ArrayList<Position>();
			pos.add(new Position(50,150));
			pos.add(new Position(150,50));
			pos.add(new Position(250,250));
			pos.add(new Position(350,50));
			pos.add(new Position(450,250));
			pos.add(new Position(650,150));
			
			// flow max ff = 12
			//int[][] capacity = {{0,9,9,0,0,0},{0,0,10,8,0,0},{0,0,0,1,3,0},{0,0,0,0,0,10},{0,0,0,8,0,7},{0,0,0,0,0,0}};
			
			// flow max ff = 19
			//int[][] capacity = {{0,10,10,0,0,0},{0,0,2,4,8,0},{0,0,0,0,9,0},{0,0,0,0,0,10},{0,0,0,6,0,10},{0,0,0,0,0,0}};
			
			// flow max ff = 7
			//int[][] capacity = {{0,10,0,0,7,0},{0,0,2,0,0,0},{0,0,0,6,0,0},{0,0,0,0,0,0},{0,0,0,0,0,10},{0,0,0,0,0,0}}; 

			
			//int[][] capacity = {{0 , 3 , 1 , 0 , 0 , 0},{7 , 0 , 2 , 4 , 1 , 0},{9 , 0 , 0 , 0 , 0 , 0},{0 , 0 , 0 , 0 , 6 , 4},{0 , 7 , 9 , 0 , 0 , 0},{0 , 0 , 0 , 6 , 10 , 0}};
			
			// graphe exemple wikipedia : http://fr.wikipedia.org/wiki/Algorithme_de_Dinic
			//int[][] capacity = {{0 , 10 , 10 , 0 , 0 , 0},{0 , 0 , 2 , 4 , 8 , 0},{0 , 0 , 0 , 0 , 9 , 0},{0 , 0 , 0 , 0 , 0 , 10},{0 , 0 , 0 , 6 , 0 , 10},{0 , 0 , 0 , 0 , 0 , 0}};
			
			//int[][] capacity = {{0 , 5 , 3 , 0 , 0 , 0},{0 , 0 , 0 , 4 , 0 , 0},{0 , 0 , 0 , 0 , 4 , 0},{0 , 0 , 0 , 0 , 0 , 6},{0 , 0 , 0 , 0 , 0 , 3},{0 , 0 , 0 , 0 , 0 , 0}};

			// graphe exemple wikipedia : http://en.wikipedia.org/wiki/Push–relabel_maximum_flow_algorithm
			int[][] capacity = {{0 , 15 , 0 , 4 , 0 , 0},{0 , 0 , 12 , 0 , 0 , 0},{0 , 0 , 0 , 3 , 0 , 7},{0 , 0 , 0 , 0 , 10, 0},{0 , 5 , 0 , 0 , 0 , 10},{0 , 0 , 0 , 0 , 0 , 0}};

			
			Graph graph = new Graph(pos, capacity);
			
			//Graph graph = Zufallsgenerator.createZG(4, 10);
			
			
/*			JFrame frame = new JFrame("Graph Visualiesierung graphe de base");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.getContentPane().add(new DrawGraph(new Graph(graph.getKnotenPosition(),graph.getCapacity())));
			frame.setSize(1000,600);
			frame.setVisible(true);*/
			
			System.out.println("Graph : ");
			graph.printGraph();
			graph.drawGraph("Visualiesierung Graph");
			
			FordFulkerson ff = new FordFulkerson();
			ff.fordFulkerson(graph, 0, 5);
						
			EdmondsKarp ek = new EdmondsKarp(graph);
			ek.edmondsKarp(0, 5);
			
			Dinic di = new Dinic();
			di.dinic(graph, 0, 5);
			
			GoldbergTarjan gt = new GoldbergTarjan();
			gt.goldbergTarjan(graph, 0, 5);
		}
}
