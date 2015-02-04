package algorithmen;

import graph.Graph;

import java.util.ArrayList;

public class GoldbergTarjan {


	public GoldbergTarjan(){
	}

	public int goldbergTarjan(Graph graph, int start, int target){
		//System.out.println("Goldberg-Tarjan");
		//System.out.println("Initialisation :");
		int n = graph.getKnotenPosition().size();
		// System.out.println("n = "+n);
		int maxFlow = 0;
		ArrayList<Integer> set = new ArrayList<Integer>();

		// Le pr�flot initial est comme �tant nul sur tous les arcs sauf ceux sortant de la source, et f(s,v)=c(s,v) pour tout arc (s,v). 
		int[][] flow = new int[n][n];
		for (int i=0; i<n; i++){
			for (int j=0; j<n; j++){
				flow[i][j] = 0;
			}	
		}
		flow[0][3] = 4;
		flow[1][2] = 4;
		flow[2][5] = 4;
		flow[3][4] = 4;
		flow[4][1] = 4;
		printFlow(flow);
		
		for (int v : graph.getAdjacent(start)){
			flow[start][v] = graph.getCapacity()[start][v];
			flow[v][start] = -flow[start][v];
			set.add(v);
		}
		//System.out.println(set);
		//printFlow(flow);
		
		//la hauteur de tout sommet autre que la racine est d�finie comme �tant nulle, et pour la racine s, on d�finit h(s)=|V|.
		int[] heights = new int[n];
		int[] flowExcesses = new int[n];

		for (int i=0; i<n; i++){
			heights[i] = 0;
			flowExcesses[i] = flow[start][i];
			//System.out.println("flowExcesses[" + i + "] = " + flowExcesses[i]);
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
		
		//System.out.println("It�rations :");

		int u;
		u = activeVertice2(gtGraph, flow, set);
		while ((u = activeVertice2(gtGraph, flow, set)) != -1){
			System.out.println("Nouveau sommet actif : "+ u);
			
			
			for (int v = 0; v < n; v++) {
				if (gtGraph.residualCapacity(flow, u, v) > 0 && heights[u] > heights[v]){
					System.out.println("push");
					push2(gtGraph, flow, u, v, start, flowExcesses, set);
					//break; //??
				} else {
					System.out.println("relabel : ");
					relabel(gtGraph, flow, u, heights);
					//printHeights(heights);								
				}
			} 
		}

		for (int i = 0; i < n; i++) {
			maxFlow += flow[i][target];
		}

		System.out.println("Fin Goldberg-Tarjan : flot total = "+maxFlow);
		return maxFlow;


	}

	public void push(Graph graph, int[][] flow, int u, int v, int[] flowExcesses){
		int m;
		if (flowExcesses[u] > graph.residualCapacity(flow, u, v)) m = graph.residualCapacity(flow, u, v);
		else m = flowExcesses[u];
		System.out.println("push de " + u + " � " + v +" de " + m);
		flowExcesses[u] -= m;
		flowExcesses[v] += m;
		flow[u][v] += m;
		flow[v][u] -= m;
		//printFlow(flow);
	}

	public void push2(Graph graph, int[][] flow, int u, int v, int start, int[] flowExcesses, ArrayList<Integer> set){
		int m;
		if (v!=start && flowExcesses[v]!=0 && !set.contains(v)){
			set.add(v);
			System.out.println("add : " + set.size());
		}
		//printFlow(flow);
		//for (int i=0; i<graph.getCapacity().length; i++ ){
		//	for (int j=0; j<graph.getCapacity().length; j++ ){
		//		System.out.println("capacity["+i+"]["+j+"] = " +graph.getCapacity()[i][j]);	
		//	}
		//}
		//System.out.println(flowExcesses[u]);
		//System.out.println(graph.residualCapacity(flow, u, v));
		if (flowExcesses[u] > graph.residualCapacity(flow, u, v)) m = graph.residualCapacity(flow, u, v);
		else m = flowExcesses[u];
		System.out.println("push de " + u + " � " + v +" de " + m);
		flowExcesses[u] -= m;
		flowExcesses[v] += m;
		flow[u][v] += m;
		flow[v][u] -= m;
		if (flowExcesses[v]==0){
			set.remove(v);
			System.out.println("remove : " + set.size());
		}
		//for (int i = 0 ; i< flowExcesses.length; i++){
		//	System.out.println("flowExcesses["+i+"] = "+ flowExcesses[i]);
		//}
		System.out.println("flowExcesses["+u+"] =" + flowExcesses[u]);
		System.out.println("flowExcesses["+v+"] =" + flowExcesses[v]);
		printFlow(flow);
	}

	public void relabel(Graph graph, int[][] flow, int u, int[] heights){
		int minHeight = Integer.MAX_VALUE;
		for (int v : graph.getAdjacent(flow, u)){ 
			if (graph.residualCapacity(flow, u, v)>0){
				if (heights[v]<minHeight) minHeight = heights[v];
			}
		}
		if (heights[u]>minHeight) {
			//System.out.print("nouvelle hauteur plus grande que la pr�c�dente !");
		} else {
			heights[u] = minHeight + 1;			
		}
		System.out.println("heights["+u+"] =" + heights[u]);
	}

	public int flowExcess(Graph graph, int[][] flow, int u){
		int flowsSum = 0;
		for (int v = 0; v < graph.getCapacity().length ; v++) {
			flowsSum += flow[v][u];
		}
		return flowsSum;
	}

	public int activeVertice2(Graph graph, int[][] flow, ArrayList<Integer> set){
		int n = set.size();
		for (int i = 0; i < n-1; i++){
			//System.out.println("flowExcess("+i+") = "+flowExcess(graph, flow, set.get(i)));
			if (flowExcess(graph, flow, set.get(i)) > 0) return set.get(i);
		}
		return -1;
	}
	
	public int activeVertice(Graph graph, int[][] flow){
		int n = graph.getCapacity().length;
		for (int i = 1; i < n-1; i++){
			System.out.println("flowExcess("+i+") = "+flowExcess(graph, flow, i));
			if (flowExcess(graph, flow, i) > 0) return i;
		}
		return -1;
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
