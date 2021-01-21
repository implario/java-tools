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
			if (this.playerPartyMap.containsKey(member)) {
				throw new IllegalStateException("Tried to add a member that is already in the queue: " + member);
			}
		}
		for (U member : party) {
			this.playerPartyMap.put(member, party);
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

	public List<? extends Collection<U>> pass(int teamsAmount, int playersPerTeam) {

		if (playerPartyMap.size() < teamsAmount * playersPerTeam) return null;

		for (P head : queue) {

			List<P> variants = queue.stream()
					.filter(party -> matcher.test(party, head))
					.collect(Collectors.toList());

			MatchMaker<U, P> maker = new MatchMaker<>(variants, playersPerTeam, teamsAmount);

			if (!maker.search()) continue;

			Map<P, Iterator<U>> iterators = variants.stream().collect(Collectors.toMap(party -> party, Collection::iterator));

			List<List<U>> teams = new ArrayList<>();
			for (P[] partyMap : maker.partyMaps) {
				List<U> team = new ArrayList<>();
				for (P party : partyMap) {
					team.add(iterators.get(party).next());
					queue.remove(party);
					playerPartyMap.keySet().removeAll(party);
				}
				teams.add(team);
			}

			return teams;
		}

		return null;

	}

	private static class MatchMaker<U, P extends Collection<U>> {

		private final Collection<P> consideredParties = new ArrayList<>();
		private final List<P> variants;
		private P[][] partyMaps;
		private final int playersPerTeam;

		@SuppressWarnings ("unchecked")
		public MatchMaker(List<P> variants, int playersPerTeam, int numberOfTeams) {
			this.variants = variants;
			this.playersPerTeam = playersPerTeam;
			this.partyMaps = (P[][]) new Collection[numberOfTeams][playersPerTeam];
		}

		boolean search() {return search(0);}

		boolean search(int deep) {

			int totalFreeSlots = 0;
			for (P[] partyMap : partyMaps) {
				for (P party : partyMap) {
					if (party == null) totalFreeSlots++;
				}
			}
			if (totalFreeSlots == 0) return true;

			for (P party : variants) {
				if (party.isEmpty() || consideredParties.contains(party)) continue;

				// If this party is too large then discard it.
				if (party.size() > totalFreeSlots) continue;

				P[][] previous = MatchMaking.clone(partyMaps);

				if (tryFitParty(party)) {
					consideredParties.add(party);
					if (search(deep+1)) return true;
					consideredParties.remove(party);
				}

				partyMaps = previous;

			}

			return false;
		}

		boolean tryFitParty(P party) {

			int size = party.size();

			while (size > 0) {

				// Limiting allocation to maximum players amount in one team
				int allocationSize = Math.min(size, playersPerTeam);

				// Trying to fit players in a single team
				if (!tryFitGroup(allocationSize, party))
					// Failed, aborting
					return false;

				size -= allocationSize;

			}

			return true;
		}

		/**
		 * Fits a group of players into one team
		 */
		boolean tryFitGroup(int groupSize, P fromParty) {

			int bestTeam = -1;
			int bestTeamFreeSlots = Integer.MAX_VALUE;

			for (int team = 0; team < partyMaps.length; team++) {

				int freeSlots = 0;

				for (P party : partyMaps[team])
					if (party == null) freeSlots++;

				if (freeSlots >= groupSize && freeSlots < bestTeamFreeSlots) {
					bestTeamFreeSlots = freeSlots;
					bestTeam = team;
				}
			}

			if (bestTeam == -1) return false;

			P[] partyMap = partyMaps[bestTeam];

			for (int i = 0; i < partyMap.length && groupSize != 0; i++) {
				if (partyMap[i] != null) continue;
				partyMap[i] = fromParty;
				groupSize--;
			}

			return true;

		}

	}

	@SuppressWarnings ("unchecked")
	private static <T> T[][] clone(T[][] array) {
		if (array == null) return null;

		T[][] result = (T[][]) new Collection[array.length][];
		for (int i = 0; i < array.length; i++) {
			result[i] = Arrays.copyOf(array[i], array[i].length);
		}
		return result;
	}

	public void clear() {
		queue.clear();
		playerPartyMap.clear();
	}

}
