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

		for (int v : graph.getAdjacent(start)){
			flow[start][v] = graph.getCapacity()[start][v];
			flow[v][start] = -flow[start][v];
			set.add(v);
		}
		//System.out.println(set);
		//printFlow(flow);
		//graph.printGraph();
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
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				gtGraphCapacity[i][j] = graph.getCapacity()[i][j] - flow[i][j];
			}
		}
		gtGraph.setCapacity(gtGraphCapacity);
		gtGraph.setKnotenPosition(graph.getKnotenPosition());
		//gtGraph.printGraph();
		//printHeights(heights);
		//printFlow(flow);
		//for(int i=0; i<flowExcesses.length; i++){
		//	System.out.print(flowExcesses[i] + "   ");			
		//}
		//System.out.println("It�rations :");

		int u;
		while ((u = activeVertice2(gtGraph, flow, set)) != -1){
			//System.out.println("Nouveau sommet actif : "+ u);

			for (int v = 0; v < n; v++) {
				if (gtGraph.residualCapacity(flow, u, v) > 0 && heights[u] == heights[v] + 1){
					//System.out.println("push de : " + u + " � " + v);
					push2(graph, flow, u, v, start, target, flowExcesses, set);
					//System.out.println("Graph apr�s push : " );
					//graph.printGraph();
					if (!set.contains(u)){
						break;
					}
					//break; //??
				} else if (heights[u] <= heights[v]){
					//System.out.println("relabel de : " + u);
					relabel(graph, flow, u, heights);
					//System.out.println("Graph apr�s relabel : " );
					//graph.printGraph();
					//printHeights(heights);
				}
			} 
		}

		//printFlow(flow);

		for (int i = 0; i < n; i++) {
			maxFlow += flow[i][target];
		}

		System.out.println("Flow Max Goldberg-Tarjan : "+maxFlow);
		return maxFlow;


	}

	public void push(Graph graph, int[][] flow, int u, int v, int[] flowExcesses){
		int m;
		if (flowExcesses[u] > graph.residualCapacity(flow, u, v)) m = graph.residualCapacity(flow, u, v);
		else m = flowExcesses[u];
		//System.out.println("push de " + u + " � " + v +" de " + m);
		flowExcesses[u] -= m;
		flowExcesses[v] += m;
		flow[u][v] += m;
		flow[v][u] -= m;
		//printFlow(flow);
		//System.out.println("flowExcesses["+u+"] : " + flowExcesses[u]);
		//System.out.println("flowExcesses["+v+"] : " + flowExcesses[v]);
	}

	public void push2(Graph graph, int[][] flow, int u, int v, int start, int target, int[] flowExcesses, ArrayList<Integer> set){
		int m;
		if (v!=target && v!=start && flowExcesses[v]==0 && !set.contains(v)){
			set.add(v);
			//System.out.println("add : " + v);
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
		//System.out.println("push de " + u + " � " + v +" de " + m);
		flowExcesses[u] -= m;
		flowExcesses[v] += m;
		flow[u][v] += m;
		flow[v][u] -= m;
		if (flowExcesses[u]==0){
			//System.out.println("remove : " + set.get(set.indexOf(u)) + " - u : " + u);
			set.remove(set.indexOf(u));
		}
		//for (int i = 0 ; i< flowExcesses.length; i++){
		//	System.out.println("flowExcesses["+i+"] = "+ flowExcesses[i]);
		//}
		//System.out.println("flowExcesses["+u+"] =" + flowExcesses[u]);
		//System.out.println("flowExcesses["+v+"] =" + flowExcesses[v]);
		//System.out.println("capacity["+u+"]["+v+"] =" + (graph.getCapacity()[u][v]));
		//System.out.println(("flow["+u+"]["+v+"] =" +flow[u][v]));
		//System.out.println("residualCapacity["+u+"]["+v+"] =" + graph.residualCapacity(flow, u, v));
		//System.out.println("capacity["+v+"]["+u+"] =" + (graph.getCapacity()[v][u]));
		//System.out.println(("flow["+v+"]["+u+"] =" +flow[v][u]));
		//printFlow(flow);
	}

	public void relabel(Graph graph, int[][] flow, int u, int[] heights){
		int minHeight = Integer.MAX_VALUE;

		//graph.printGraph();
		//printFlow(flow);
		//System.out.println("adjacents to " +u + " : " + graph.getAdjacent(flow, u));
		for (int v : graph.getAdjacent(flow, u)){ 
			if (graph.residualCapacity(flow, u, v)>0){
				if (heights[v]<minHeight) {
					minHeight = heights[v];
					//System.out.println("heights["+v+"] =" + minHeight);
				}
			}
		}
		if (heights[u]>minHeight) {
			//System.out.print("nouvelle hauteur plus grande que la pr�c�dente !");
		} else {
			heights[u] = minHeight + 1;			
		}
		//System.out.println("heights["+u+"] =" + heights[u]);
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
		//System.out.println("set active vertix : " + set);
		for (int i = 0; i < n; i++){
			//System.out.println("flowExcess("+i+") = "+flowExcess(graph, flow, set.get(i)));
			if (flowExcess(graph, flow, set.get(i)) > 0) {
				return set.get(i);
			}
		}
		return -1;
	}

	public int activeVertice(Graph graph, int[][] flow){
		int n = graph.getCapacity().length;
		for (int i = 1; i < n-1; i++){
			//System.out.println("flowExcess("+i+") = "+flowExcess(graph, flow, i));
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
