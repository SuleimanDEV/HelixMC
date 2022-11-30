package club.helix.components.command.completer

class CommandCompleterHandle(
    private val completer: CommandCompleter,
    private val input: String,
) {
    fun execute() = mutableListOf<String>().apply {
        val suggestions = mutableListOf(*completer.suggestions.toTypedArray()).takeIf {
            it.isNotEmpty()
        } ?: return@apply

        if (input.isNotEmpty()) {
            suggestions.removeIf {
                !it.lowercase().startsWith(input.lowercase())
            }
        }

        addAll(suggestions)
    }
}