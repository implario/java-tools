package implario;


import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class Tree<T extends Tree.Branch> {

	@Getter
	private final T rootElement;

	private List<T> lastUnloadResolution;
	private List<T> lastLoadResolution;

	@SuppressWarnings("unchecked")
	public Tree<T> rebuild() {
		List<T> list = new ArrayList<>();
		rootElement.recursiveResolve((List<Branch>) list, new ArrayList<>());
		lastUnloadResolution = list;
		lastLoadResolution = new ArrayList<>(list);
		Collections.reverse(lastLoadResolution);
		return this;
	}

	@SuppressWarnings ("unchecked")
	public List<T> buildUnloadingFrom(Branch branch) {
		List<T> list = new ArrayList<>();
		branch.recursiveResolve((List<Branch>) list, new ArrayList<>());
		return list;
	}

	public List<T> buildLoadingFrom(Branch branch) {
		List<T> list = buildUnloadingFrom(branch);
		Collections.reverse(list);
		return list;
	}

	public List<T> loadingOrder() {
		return lastLoadResolution;
	}

	public List<T> unloadingOrder() {
		return lastUnloadResolution;
	}

	@Data
	public static class Branch {
		private final List<Branch> dependents = new ArrayList<>();

		public void growBranch(Branch branch) {
			dependents.add(branch);
		}

		List<Branch> recursiveResolve(List<Branch> cache, List<Branch> hanged) {
			hanged.add(this);
			for (Branch branch : getDependents()) {
				if (cache.contains(branch)) continue;
				if (hanged.contains(branch)) throw new RuntimeException("Cyclic dependency detected: " + branch); // Циклическая зависимость
				branch.recursiveResolve(cache, hanged);
			}
			cache.add(this);
			hanged.remove(this);
			return cache;
		}

	}

}