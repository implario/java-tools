package implario.gaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.Timeout;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class MatchMakingTest {

	private final Deque<String> games = new LinkedList<>();

	private final MatchMaking<Player, Collection<Player>> matchMaking = new MatchMaking<>(
			(a, b) -> Math.abs(averageRating(a) - averageRating(b)) < 130,
			teams -> {
				games.addLast(getFormat(teams));
				System.out.println("Created game " + getFormat(teams));
			}
	);

	@BeforeEach
	public void before(TestInfo info) {

		System.out.println("\nTest " + info.getDisplayName());
		games.clear();
		matchMaking.clear();
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	private static class Player {

		private final UUID uuid = UUID.randomUUID();
		private double rating;

		@Override
		public String toString() {
			return uuid + "(" + rating + ")";
		}

	}

	@Test
	public void testSimpleMatchMaking() {

		assertTimeout(Duration.of(1, ChronoUnit.SECONDS), () -> {
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
		});


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

	@Test
	public void testLeavesCleanup() {
		Player leaver = new Player(10);
		matchMaking.add(party(leaver));

		matchMaking.remove(leaver);

		assertTrue(matchMaking.getQueue().isEmpty());
	}

	@Test
	public void testRepeatedJoin() {
		Player joiner = new Player(10);
		assertThrows(IllegalStateException.class, () -> {
			matchMaking.add(party(joiner));
			matchMaking.add(party(joiner));
		});
		matchMaking.remove(joiner);
		assertDoesNotThrow(() -> {
			matchMaking.add(party(joiner, joiner));
		});
	}

	@Test
	public void testTeamRating() {
		List<Player> weakParty = party(new Player(10), new Player(20), new Player(30));
		List<Player> mediumParty = party(new Player(110), new Player(120), new Player(130));
		List<Player> strongParty = party(new Player(210), new Player(220), new Player(230));

		// Weak party joins first
		matchMaking.add(weakParty);
		matchMaking.add(strongParty);

		matchMaking.update(2, 3);
		assertTrue(games.isEmpty());

		matchMaking.add(mediumParty);
		matchMaking.update(2, 3);
		// So weak party should get into the game first
		assertEquals(strongParty, matchMaking.getQueue().get(0));
		assertEquals("3x2", games.pop());

	}

	@Test
	public void testSoloRating() {
		List<Player> strongParty = party(new Player(210), new Player(220), new Player(230));
		matchMaking.add(strongParty);

		matchMaking.add(party(new Player(200)));
		matchMaking.add(party(new Player(200)));
		matchMaking.add(party(new Player(10)));
		matchMaking.add(party(new Player(10)));
		matchMaking.add(party(new Player(10)));
		matchMaking.add(party(new Player(200)));
		matchMaking.update(2, 3);

		Iterator<Collection<Player>> iterator = matchMaking.getQueue().iterator();

		for (int i = 0; i < 3; i++) {
			assertEquals(10, averageRating(iterator.next()));
		}

		assertFalse(iterator.hasNext());
		assertEquals("3x2", games.pop());

		matchMaking.update(3, 1);
		assertTrue(matchMaking.getQueue().isEmpty());
		assertTrue(matchMaking.getPlayerPartyMap().isEmpty());

	}

	@Test
	public void testFragmentation() {

		matchMaking.add(party(new Player(), new Player(), new Player(), new Player()));
		matchMaking.add(party(new Player(), new Player()));

		matchMaking.update(2, 3);
		assertTrue(matchMaking.getQueue().isEmpty());
		
		matchMaking.add(party(new Player(), new Player(), new Player(), new Player()));
		matchMaking.add(party(new Player(), new Player(), new Player()));
		matchMaking.add(party(new Player(), new Player()));
		matchMaking.add(party(new Player()));

		matchMaking.update(5, 2);
		assertTrue(matchMaking.getQueue().isEmpty());

	}

	@Test
	public void testOrder() {
		Player firstPlayer = new Player();
		matchMaking.add(party(firstPlayer));
		for (int i = 0; i < 10000; i++) {
			matchMaking.add(party(new Player()));
		}

		assertEquals(firstPlayer, matchMaking.pass(1, 1).get(0).iterator().next());
	}

	@Test
	public void testIntegrity() {
		matchMaking.add(party(new Player()));
		matchMaking.add(party(new Player(), new Player(), new Player()));
		matchMaking.update(2, 1);
		assertTrue(games.isEmpty());
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
