package algorithmen;

import graph.Graph;

import java.util.ArrayList;

public class GoldbergTarjan {


	public GoldbergTarjan(){
	}

	public void goldbergTarjan(Graph graph, int start, int target){
		System.out.println("Goldberg-Tarjan");
		System.out.println("Initialisation :");
		int n = graph.getKnotenPosition().size();
		System.out.println("n = "+n);
		int maxFlow = 0;

		// Le préflot initial est comme étant nul sur tous les arcs sauf ceux sortant de la source, et f(s,v)=c(s,v) pour tout arc (s,v). 
		int[][] flow = new int[n][n];
		for (int i=0; i<n; i++){
			for (int j=0; j<n; j++){
				flow[i][j] = 0;
			}	
		}
		for (int v : getAdjacent(graph.getCapacity(), start)){
			flow[start][v] = graph.getCapacity()[start][v];
			flow[v][start] = -flow[start][v];
		}
		printFlow(flow);
		
		//la hauteur de tout sommet autre que la racine est définie comme étant nulle, et pour la racine s, on définit h(s)=|V|.
		int[] heights = new int[n];
		int[] flowExcesses = new int[n];
		for (int i=0; i<n; i++){
			heights[i] = 0;
			flowExcesses[i] = flow[start][i];
		}
		heights[0] = n;

		Graph gtGraph = new Graph();
		int[][] gtGraphCapacity = new int[n][n];
		for (int i = 0; i < n-1; i++) {
			for (int j = 0; j < n; j++) {
				gtGraphCapacity[i][j] = graph.getCapacity()[i][j];
			}
		}
		gtGraph.setCapacity(gtGraphCapacity);
		gtGraph.setKnotenPosition(graph.getKnotenPosition());
		
		System.out.println("Itérations :");

		int u;
		while ((u = activeVertice(gtGraph, flow)) != -1){
			System.out.println("Nouveau sommet actif : "+u);
			for (int v = 0; v < n; v++) {
				if (residualCapacity(gtGraph, flow, u, v) > 0 && heights[u] > heights[v]){
					push(gtGraph, flow, u, v, flowExcesses);
					break; //??
				}
			}
			relabel(gtGraph, flow, u, heights);
			printHeights(heights);
		}

		for (int i = 0; i < n; i++) {
			maxFlow += flow[i][target];
		}

		System.out.println("Fin Goldberg-Tarjan : flot total = "+maxFlow);


	}

	public void push(Graph graph, int[][] flow, int u, int v, int[] flowExcesses){
		int m;
		if (flowExcesses[u] > residualCapacity(graph, flow, u, v)) m = residualCapacity(graph, flow, u, v);
		else m = flowExcesses[u];
		System.out.println("push de " + u + " à " + v +" de " + m);
		flowExcesses[u] -= m;
		flowExcesses[v] += m;
		flow[u][v] += m;
		flow[v][u] -= m;
		printFlow(flow);
	}

	public void relabel(Graph graph, int[][] flow, int u, int[] heights){
		int minHeight = Integer.MAX_VALUE;
		for (int v : getAdjacent(getResidualCapacity(graph,flow), u)){ 
			if (residualCapacity(graph, flow, u, v)>0){
				if (heights[v]<minHeight) minHeight = heights[v];
			}
		}
		if (heights[u]>minHeight) {
			System.out.print("nouvelle hauteur plus grande que la précédente !");
		} else {
			heights[u] = minHeight + 1;			
		}
	}
	
	public int[][] getResidualCapacity(Graph graph, int[][] flow){
		int n = graph.getCapacity().length;
		int[][] residualCapacity = new int [n][n];
		for (int i=0; i<n; i++){
			for (int j=0; j<n; j++){
				residualCapacity[i][j] = graph.getCapacity()[i][j] - flow[i][j];
			}	
		}
		return residualCapacity;
	}

	public int residualCapacity(Graph graph, int[][] flow, int u, int v){
		return graph.getCapacity()[u][v] - flow [u][v];
	}

	public int flowExcess(Graph graph, int[][] flow, int u){
		int flowsSum = 0;
		for (int v = 0; v < graph.getCapacity().length ; v++) {
			flowsSum += flow[v][u];
		}
		return flowsSum;
	}

	public int activeVertice(Graph graph, int[][] flow){
		int n = graph.getCapacity().length;
		for (int i = 1; i < n-1; i++){
			System.out.println("flowExcess("+i+") = "+flowExcess(graph, flow, i));
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
	
	public void printFlow(int[][] flow){
		for (int i=0; i < flow[0].length; i++){
			System.out.print("{ ");
			for (int j=0; j < flow[0].length; j++){
				System.out.print(flow[i][j] + "  ");
			}
			System.out.print("}\n");
		}
	}
	
	public void printHeights(int[] heights){
		System.out.println("heights : ");
		for (int i=0; i < heights.length; i++){
			System.out.print("{ ");
				System.out.print(heights[i] + "  ");
			System.out.print("}\n");
		}
	}

}
