/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;


public class BaseballElimination {
    private ArrayList<String> eliminationCertificate;
    private final int numTeams;
    // stored in "teamName:[teamid,wins,losses,gamesleft,games to play against team X]"
    private final HashMap<String, int[]> teams = new HashMap<>();
    private final HashMap<Integer, String> teamIds = new HashMap<Integer, String>();
    private final int arrayOffest = 4;

    public BaseballElimination(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Invalid Filename");
        }
        In in = new In(filename);

        numTeams = Integer.parseInt(in.readLine());
        System.out.println(numTeams);
        int teamid = 0;
        while (in.hasNextLine()) {
            String s = in.readLine();
            s = s.trim();
            int[] teamData = new int[arrayOffest + numTeams];
            String[] data = s.split("\\s+");
            teamData[0] = teamid;
            for (int i = 1; i < data.length; i++) {

                teamData[i] = Integer.parseInt(data[i]);
            }
            teams.put(data[0], teamData);
            teamIds.put(teamid, data[0]);
            teamid++;
        }

    }

    public Iterable<String> teams() {
        return teams.keySet();
    }

    public int numberOfTeams() {
        return numTeams;
    }

    public int wins(String team) {
        int[] data = teams.get(team);
        if (data == null) {
            throw new IllegalArgumentException("Team Does not exist");
        }
        // wins location
        return data[1];
    }

    public int losses(String team) {
        int[] data = teams.get(team);
        if (data == null) {
            throw new IllegalArgumentException("Team Does not exist");
        }
        // wins location
        return data[2];
    }

    public int remaining(String team) {
        int[] data = teams.get(team);
        if (data == null) {
            throw new IllegalArgumentException("Team Does not exist");
        }
        // wins location
        return data[3];
    }

    public int against(String team1, String team2) {
        int[] dataT1 = teams.get(team1);
        int[] dataT2 = teams.get(team2);
        if (dataT1 == null || dataT2 == null) {
            throw new IllegalArgumentException("Team Does not exist");
        }
        // wins location
        int id = dataT2[0];
        int gamesAgainst = dataT1[arrayOffest + id];
        return gamesAgainst;
    }

    public boolean isEliminated(String team) {

        // Check trivial case:
        eliminationCertificate = new ArrayList<String>();
        int winsPossible = wins(team) + remaining(team);

        for (String t : teams()) {
            if (winsPossible < wins(t)) {
                eliminationCertificate.add(t);
                return true;
            }
        }

        int teamVertices = numTeams - 1;
        int st = 2;
        int numRemainingGameVertices = ((numTeams - 1) * (numTeams - 1) - (numTeams - 1)) / 2;
        int totalNumVertices = numRemainingGameVertices + teamVertices + st;
        FlowNetwork f = new FlowNetwork(totalNumVertices);

        int teamId = teams.get(team)[0];
        int gameFlow = 0;
        int vertcount = 1;
        for (String t : teams()) {
            if (t.equals(team)) {
                continue;
            }
            else {
                int[] data = teams.get(t);
                int currentId = data[0];

                for (int i = arrayOffest; i < data.length; i++) {
                    // if our index is equal to our team id
                    if (currentId == i - arrayOffest || i - arrayOffest == teamId
                            || currentId - (i - arrayOffest) < 0) {
                        continue;
                    }
                    else {

                        int team1 = data[0];
                        int team2 = i - arrayOffest;

                        if (team1 > teamId) {
                            team1--;
                        }
                        if (team2 > teamId) {
                            team2--;
                        }
                        gameFlow += data[i];
                        FlowEdge fe = new FlowEdge(0, vertcount, data[i]);
                        f.addEdge(fe);
                        FlowEdge fgOne = new FlowEdge(vertcount,
                                                      (numRemainingGameVertices + 1) + team1,
                                                      Double.POSITIVE_INFINITY);
                        f.addEdge(fgOne);
                        FlowEdge fgTwo = new FlowEdge(vertcount,
                                                      (numRemainingGameVertices + 1) + team2,
                                                      Double.POSITIVE_INFINITY);
                        f.addEdge(fgTwo);
                        vertcount++;
                    }

                }
            }
            int teamVertexIndex = teams.get(t)[0];
            if (teamVertexIndex > teamId) {
                teamVertexIndex--;
            }
            FlowEdge e = new FlowEdge(numRemainingGameVertices + teamVertexIndex + 1,
                                      totalNumVertices - 1,
                                      wins(team) + remaining(team) - wins(t));
            f.addEdge(e);

        }

        FordFulkerson ff = new FordFulkerson(f, 0, f.V() - 1);
        double value = ff.value();
        for (int v = numRemainingGameVertices + 1; v < f.V() - 1; v++) {
            if (ff.inCut(v)) {
                int index = v - (numRemainingGameVertices + 1);
                if (index >= teamId) {
                    index++;
                }
                eliminationCertificate.add(teamIds.get(index));
            }
        }

        return value < gameFlow;
    }


    public Iterable<String> certificateOfElimination(String team) {
        if (team == null) {
            throw new IllegalArgumentException("");
        }
        if (isEliminated(team)) {
            return eliminationCertificate;
        }
        return null;


    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
