package graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

import javax.swing.JFrame;

public class Graph {

	private ArrayList<Position> knotenPosition;
	private int[][] capacity;

	public Graph(){
		knotenPosition = new ArrayList<Position>();
	}

	public Graph(ArrayList<Position> pos, int[][] c){
		knotenPosition = pos;
		capacity = c;
	}

	public Graph(Graph g, int[][] c){
		knotenPosition = g.getKnotenPosition();
		capacity = c;
	}

	public ArrayList<Position> getKnotenPosition() {
		return knotenPosition;
	}
	public void setKnotenPosition(ArrayList<Position> knotenPosition) {
		this.knotenPosition = knotenPosition;
	}
	public int[][] getCapacity() {
		return capacity;
	}
	public void setCapacity(int[][] capacity) {
		this.capacity = capacity;
	}

	public void drawGraph(String name){
		JFrame frame = new JFrame(name);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(new DrawGraph(this));
		frame.setSize(1000,600);
		frame.setVisible(true);
	}

	public boolean intersectKanten(Position pos1, Position pos2) {
		boolean result = false;
		Position posi, posj;
		int d, x3, x4, y3, y4, xi, yi;
		int x1 = pos1.getX();
		int x2 = pos2.getX();
		int y1 = pos1.getY();
		int y2 = pos2.getY();

		int n = capacity.length;
		for (int i = 0; i < n; i++){
			for (int j = 0; j< n ; j++){
				if (capacity[i][j] !=0){
					posi =  knotenPosition.get(i);
					posj =  knotenPosition.get(j);
					x3 = posi.getX();
					x4 = posj.getX();
					y3 = posi.getY();
					y4 = posj.getY();
					d = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
					if (d!=0 ){
						xi = ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/d;
						yi = ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/d;
						if (!(xi <= Math.min(x1,x2) || xi >= Math.max(x1,x2))){
							if (!(xi <= Math.min(x3,x4) || xi >= Math.max(x3,x4))){
								if (!(yi <= Math.min(y1,y2) || yi >= Math.max(y1,y2))){
									if (!(yi <= Math.min(y3,y4) || yi >= Math.max(y3,y4))){
										//System.out.println("intersection de ? " + knotenPosition.indexOf(pos1) + "," + knotenPosition.indexOf(pos2));
										//System.out.println("(" +  i + "," + j + ")");
										//System.out.println("xi = " +  xi);
										//System.out.println("yi = " +  yi);
										result = true;
									}
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	public ArrayList<Integer> getAdjacent(int node){
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < this.getCapacity().length; i++){
			if (this.getCapacity()[node][i] > 0){
				result.add(i);
			}			
		}
		return result;
	}

	public ArrayList<Integer> getAdjacent(int[][] flow, int node){
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < this.getResidualCapacity(flow).length; i++){
			if (this.getResidualCapacity(flow)[node][i] > 0){
				result.add(i);
			}			
		}
		return result;
	}

	public int residualCapacity(int[][] flow, int u, int v){
		return this.getCapacity()[u][v] - flow [u][v];
	}

	public int[][] getResidualCapacity(int[][] flow){
		int n = this.getCapacity().length;
		int[][] residualCapacity = new int [n][n];
		for (int i=0; i<n; i++){
			for (int j=0; j<n; j++){
				residualCapacity[i][j] = this.getCapacity()[i][j] - flow[i][j];
			}	
		}
		return residualCapacity;
	}

	public int getCapacityPath(int[] path, int target, int start) {
		int capacityPath = 0;
		int minCapa = Integer.MAX_VALUE;
		int current = target;
		while (current != start){
			capacityPath = this.getCapacity()[path[current]][current];
			current = path[current];
			if (capacityPath < minCapa){
				minCapa = capacityPath;
			}
		}
		return minCapa;
	}

	public int getCapacityPath(ArrayList<Integer> path, int target, int start) {

		int minCapacityPath = Integer.MAX_VALUE;
		int capacity = 0;

		for (int i = 0; i < path.size() - 1; i++){
			capacity = this.getCapacity()[path.get(i)][path.get(i+1)];
			if (capacity < minCapacityPath){
				minCapacityPath = capacity;
			}
		}
		return minCapacityPath;
	}

	public ArrayList<Integer> DFS(int start, int target) {

		int n = this.getCapacity().length;
		Stack<Integer> stack = new Stack<Integer>();
		int[] isSeen = new int[n];
		int[] nbAdj = new int[n];
		int current = start;

		stack.push(start);

		for (int i=0; i<n; i++){
			isSeen[i] = 0;
			nbAdj[i] = 0;
		}

		isSeen[start]=1;

		if (start != target){

			while (!stack.isEmpty()){
				current = stack.peek();

				ArrayList<Integer> adjacents = this.getAdjacent(current); 
				int i = nbAdj[current];
				int nextAdj;
				if (i==adjacents.size() ){
					isSeen[current]=2;
					stack.pop();
				} else {
					nextAdj = adjacents.get(i);
					if (isSeen[nextAdj]==0){
						isSeen[nextAdj] = 1;
						stack.push(nextAdj);
						if (nextAdj==target){
							return  new ArrayList<Integer>(stack);
						}
					}
					nbAdj[current] ++;
				}
			}
		}
		return null;
	}	
	
	public void printGraph(){
		for (int i=0; i < capacity.length; i++){
			System.out.print("{ ");
			for (int j=0; j < capacity.length; j++){
				System.out.print(capacity[i][j] + "  ");
			}
			System.out.print("}\n");
		}		
	}

	public void drawGraph(String name, int[] flowExcesses) {
		JFrame frame = new JFrame(name);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(new DrawGraph(this, flowExcesses));
		frame.setSize(1000,600);
		frame.setVisible(true);
	}

}
