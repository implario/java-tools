package implario.gaming;

import lombok.Data;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Data
public class MatchMaking<U, P extends Collection<U>> {

	private final BiPredicate<P, P> matcher;
	private final Consumer<? super List<? extends Collection<U>>> gameCreator;
	private final List<P> queue = new LinkedList<>();
	private final Map<U, P> playerPartyMap = new HashMap<>();

	public void add(P party) {
		for (U member : party) {
			if (this.playerPartyMap.putIfAbsent(member, party) != null) {
				throw new IllegalStateException("Tried to add a member that is already in the queue: " + member);
			}
		}
		queue.add(party);
	}

	@SafeVarargs
	public final void remove(U... players) {
		remove(Arrays.asList(players));
	}

	public void remove(Iterable<U> players) {
		List<P> queue = this.queue;
		for (U player : players) {

			P party = this.playerPartyMap.remove(player);
			if (party == null) continue;

			party.remove(player);

			if (party.isEmpty()) {
				queue.remove(party);
			}
		}
	}

	public int getSize() {
		return queue.stream().mapToInt(Collection::size).sum();
	}

	public void update(int teamsAmount, int playersPerTeam) {
		List<? extends Collection<U>> teams;

		while ((teams = pass(teamsAmount, playersPerTeam)) != null) {
			gameCreator.accept(teams);
		}
	}

	private List<? extends Collection<U>> pass(int teamsAmount, int playersPerTeam) {

		search:
		for (P head : queue) {

			List<P> variants = queue.stream()
					.filter(party -> matcher.test(party, head))
					.collect(Collectors.toList());

			List<P> considered = new ArrayList<>();

			List<List<P>> teams = new ArrayList<>();

			for (int i = 0; i < teamsAmount; i++) {

				if (!recursiveSearch(variants, considered, playersPerTeam))
					continue search;

				variants.removeAll(considered);
				teams.add(considered);
				considered = new ArrayList<>();
			}

			for (List<P> team : teams) {
				for (P party : team) {
					queue.remove(party);
					playerPartyMap.keySet().removeAll(party);
				}
			}

			List<List<U>> teamsOfUsers = new ArrayList<>();
			for (List<P> team : teams) {
				List<U> users = new ArrayList<>();
				for (P party : team) users.addAll(party);
				teamsOfUsers.add(users);
			}

			return teamsOfUsers;
		}

		return null;

	}

	private static <U, P extends Collection<U>> boolean recursiveSearch(Collection<P> variants, Collection<P> considered, int requiredPlayers) {
		for (P party : variants) {
			if (party.size() > requiredPlayers || party.isEmpty() || considered.contains(party)) continue;
			considered.add(party);
			if (party.size() == requiredPlayers) return true;
			if (recursiveSearch(variants, considered, requiredPlayers - party.size())) return true;
			considered.remove(party);
		}
		return false;
	}

}
