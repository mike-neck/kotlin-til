import java.util.*

fun <T, C: MutableCollection<T>> Collection<T>.partitionTo(left: C, right: C, f: (T) -> Boolean): Pair<C, C> {
    for (item in this) {
        if (f(item)) {
            left.add(item)
        } else {
            right.add(item)
        }
    }
    return Pair(left, right)
}

fun partitionWordsAndLines() {
    val (words, lines) = listOf("a", "a b", "c", "d e").
            partitionTo(ArrayList<String>(), ArrayList()) { s -> !s.contains(" ") }
    words == listOf("a", "c")
    lines == listOf("a b", "d e")
}

fun partitionLettersAndOtherSymbols() {
    val (letters, other) = setOf('a', '%', 'r', '}').
            partitionTo(HashSet<Char>(), HashSet()) { c -> c in 'a'..'z' || c in 'A'..'Z'}
    letters == setOf('a', 'r')
    other == setOf('%', '}')
}