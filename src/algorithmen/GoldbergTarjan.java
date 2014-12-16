package algorithmen;

import graph.Graph;

import java.util.ArrayList;

public class GoldbergTarjan {

	private ArrayList<ArrayList<Integer>> pathList;
	public int i = 0;

	public GoldbergTarjan(){
		pathList = new ArrayList<ArrayList<Integer>>();
	}

	public GoldbergTarjan(ArrayList<ArrayList<Integer>> list){
		pathList = list;
	}

	public void goldbergTarjan(Graph graph, int start, int target){

		int n = graph.getKnotenPosition().size();
		int maxFlow = 0;

		int[][] flow = new int[n][n];
		for (int i=0; i<n; i++){
			for (int j=0; j<n; j++){
				flow[i][j] = 0;
			}	
		}

		int[] heights = new int[n];
		int[] flowExcesses = new int[n];
		
		for (int i=0; i<n; i++){
			heights[i] = 0;
			flowExcesses[i] = 0;
		}
		heights[0] = n;

		Graph gtGraph = new Graph();
		int[][] gtGraphCapacity = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				gtGraphCapacity[i][j] = graph.getCapacity()[i][j];
			}
		}
		gtGraph.setCapacity(gtGraphCapacity);
		gtGraph.setKnotenPosition(graph.getKnotenPosition());
		int u;
		while ((u = activeVertice(gtGraph, flow)) != -1){
			for (int v = 0; v < n || v != u; i++) {
				if (residualCapacity(gtGraph, flow, u, v) > 0){
					push(gtGraph, flow, u, v, flowExcesses);
					break; //??
				}
			}
			relabel(gtGraph, flow, u, heights);
		}


	}

	public void push(Graph graph, int[][] flow, int u, int v, int[] flowExcesses){
		int m;
		if (flowExcesses[u] > residualCapacity(graph, flow, u, v)) m = residualCapacity(graph, flow, u, v);
		else m = flowExcesses[u];
		flowExcesses[u] -= m;
		flowExcesses[v] += m;
		flow[u][v] += m;
		flow[v][u] -= m;
	}

	public void relabel(Graph graph, int[][] flow, int u, int[] heights){
		int minHeight = Integer.MAX_VALUE;
		for (int v : getAdjacent(graph.getCapacity(), u)){ 
			if (residualCapacity(graph, flow, u, v)>0){
				if (heights[v]<minHeight) minHeight = heights[v];
			}
		}
		if (heights[u]>minHeight) {
			System.out.print("nouvelle hauteur plus grande que la précédente !");
		} else {
			heights[u] = minHeight;			
		}
	}

	public int residualCapacity(Graph graph, int[][] flow, int u, int v){
		return graph.getCapacity()[u][v] - flow [u][v];
	}

	public int flowExcess(Graph graph, int[][] flow, int u){
		int flowsSum = 0;
		for (int v = 0; v < graph.getCapacity().length ; i++) {
			flowsSum += flow[u][v];
		}
		return flowsSum;
	}

	public int activeVertice(Graph graph, int[][] flow){
		int n = graph.getCapacity().length;
		for (int i = 0; i < n; i++){
			if (flowExcess(graph, flow, i) > 0) return i;
		}
		return -1;
	}

	public ArrayList<Integer> getAdjacent(int[][] graphCapacity, int node){
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < graphCapacity.length; i++){
			if (graphCapacity[node][i] > 0){
				result.add(i);
			}			
		}
		return result;
	}

}
