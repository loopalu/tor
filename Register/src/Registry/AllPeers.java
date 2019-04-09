package Registry;

import java.util.ArrayList;

public class AllPeers {
    private ArrayList<String> peers = new ArrayList<>();

    public ArrayList<String> getPeers() {
        return peers;
    }

    public void setPeers(ArrayList<String> peers) {
        this.peers = peers;
    }

    public void addPeers(String peer) {
        peers.add(peer);
    }

    public void removePeer(String peer) {
        peers.remove(peer);
    }

    public boolean existsPeer(String peer) {
        return peers.contains(peer);
    }

    @Override
    public String toString() {
        return String.valueOf(peers);
    }
}
