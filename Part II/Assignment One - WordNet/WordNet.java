/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;

public final class WordNet {
    private int synsetSize;
    private final HashMap<String, ArrayList<Integer>> sysnetNounVertcies;
    private final HashMap<Integer, String> sysnetVertexNouns;
    private final SAP sapgraph;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("");
        }

        // Read Sysnet into our hashmap
        sysnetNounVertcies = new HashMap<String, ArrayList<Integer>>();
        sysnetVertexNouns = new HashMap<Integer, String>();
        In in = new In(synsets);
        String s = "";
        while (in.hasNextLine()) {
            s = in.readLine();
            String[] values = s.split(",");
            int vertex = Integer.parseInt(values[0]);
            if (values.length <= 1) {
                throw new IllegalArgumentException("");
            }
            sysnetVertexNouns.put(vertex, values[1]);

            String[] nouns = values[1].split(" ");
            for (int j = 0; j < nouns.length; j++) {
                if (sysnetNounVertcies.get(nouns[j]) == null) {
                    sysnetNounVertcies.put(nouns[j], new ArrayList<>());
                }
                sysnetNounVertcies.get(nouns[j]).add(vertex);
            }
            synsetSize++;
        }
        in.close();
        // Read Hypernyms  into our digraph
        in = new In(hypernyms);
        s = "";

        Digraph hypernymsNet = new Digraph(synsetSize);
        while (in.hasNextLine()) {
            s = in.readLine();
            String[] values = s.split(",");
            int vertex = Integer.parseInt(values[0]);

            for (int i = 1; i < values.length; i++) {
                hypernymsNet.addEdge(vertex, Integer.parseInt(values[i]));
            }
        }
        DirectedCycle checkCycle = new DirectedCycle(hypernymsNet);
        if (checkCycle.hasCycle()) {
            throw new IllegalArgumentException("Cycle detected");
        }
        checkCycle = null;

        int foundRoot = -1;
        int rootcount = 0;
        for (int v = 0; v < hypernymsNet.V(); v++) {
            if (hypernymsNet.outdegree(v) == 0) {
                if (rootcount > 0) {
                    throw new IllegalArgumentException("More than one root");
                }
                foundRoot = v;
                rootcount = 1;
            }
        }
        if (foundRoot == -1) {
            throw new IllegalArgumentException("Not rooted");
        }

        sapgraph = new SAP(hypernymsNet);

    }


    public Iterable<String> nouns() {
        return sysnetNounVertcies.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("");
        }
        return sysnetNounVertcies.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        return sapgraph.length(sysnetNounVertcies.get(nounA),
                               sysnetNounVertcies.get(nounB));

    }

    public String sap(String nounA, String nounB) {
        return sysnetVertexNouns.get(
                sapgraph.ancestor(sysnetNounVertcies.get(nounA), sysnetNounVertcies.get(nounB)));
    }

    public static void main(String[] args) {

        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
        System.out.println(wn.distance("white_marlin", "mileage"));
        System.out.println(wn.distance("mileage", "white_marlin"));


        System.out.println(wn.distance("Black_Plague", "black_marlin"));
        System.out.println(wn.distance("black_marlin", "Black_Plague"));

        System.out.println(wn.distance("American_water_spaniel", "histology"));
        System.out.println(wn.distance("histology", "American_water_spaniel"));

        System.out.println(wn.distance("Brown_Swiss", "barrel_roll"));
        System.out.println(wn.distance("barrel_roll", "Brown_Swiss"));




    }
}
