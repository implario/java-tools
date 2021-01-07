package implario.gaming;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MatchMakingTest {

	private final Deque<String> games = new LinkedList<>();

	private final MatchMaking<Player, Collection<Player>> matchMaking = new MatchMaking<>(
			(a, b) -> Math.abs(averageRating(a) - averageRating(b)) < 100,
			teams -> games.addLast(getFormat(teams))
	);

	@Data
	private static class Player {

		private final UUID uuid = UUID.randomUUID();
		private final double rating;

		@Override
		public String toString() {
			return uuid + "(" + rating + ")";
		}

	}

	@Test
	public void testSimpleMatchMaking() {

		matchMaking.add(party(new Player(10), new Player(10), new Player(10)));
		matchMaking.add(party(new Player(10), new Player(10)));
		matchMaking.add(party(new Player(10)));
		matchMaking.add(party(new Player(10)));
		matchMaking.add(party(new Player(10)));
		matchMaking.add(party(new Player(10)));
		matchMaking.add(party(new Player(10)));
		matchMaking.add(party(new Player(10)));
		matchMaking.add(party(new Player(10)));

		matchMaking.update(2, 3);

		assertEquals("3x2", games.pop());
		assertEquals("3x2", games.pop());
		assertTrue(matchMaking.getQueue().isEmpty());

	}

	@Test
	public void testLeaves() {
		Player leaver = new Player(10);
		matchMaking.add(party(new Player(10), new Player(10), new Player(10)));
		matchMaking.add(party(new Player(10), new Player(10), leaver));
		matchMaking.add(party(new Player(10)));

		matchMaking.remove(leaver);

		matchMaking.update(2, 3);
		assertEquals("3x2", games.pop());
		assertTrue(matchMaking.getQueue().isEmpty());

	}

	public double averageRating(Collection<Player> players) {
		return players.stream().mapToDouble(Player::getRating).average().orElse(0);
	}

	public List<Player> party(Player... players) {
		return new ArrayList<>(Arrays.asList(players));
	}

	public String getFormat(Collection<? extends Collection<Player>> teams) {
		int size = 0;
		for (Collection<Player> team : teams) {
			if (team.size() == 0) fail("Game started with an empty team: " + teams);
			if (size > 0 && team.size() != size) fail("Teams had different sizes: " + teams);
			size = team.size();
		}
		return size + "x" + teams.size();
	}

}
