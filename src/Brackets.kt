//https://www.reddit.com/r/dailyprogrammer/comments/5llkbj/2017012_challenge_298_easy_too_many_parentheses/
interface Content {
    fun print(): String
}

data class Simple(val value: String) : Content {
    override fun print(): String {
        return value
    }
}

data class Group(val contents: List<Content>) : Content {
    override fun print(): String {
        if(contents.size == 1 && contents.get(0) is Group) {
            return contents.get(0).print()
        } else {
            return "(" + (contents.map { it.print() }.joinToString(separator = "") { it->it }) + ")"
        }
    }
}

data class Root(val contents: List<Content>) : Content {
    override fun print(): String {
        return if(contents.isEmpty()) "NULL" else contents.map { it.print() }.joinToString(separator = "") { it -> it }
    }
}

fun parse(str: String): Root {
    return Root(parsePart(str))
}

fun parsePart(str: String): MutableList<Content> {
    if (str.isEmpty()) {
        return mutableListOf()
    } else if (str.get(0) == '(') {
        var end = -1
        var deep = 1
        for (i in 1..str.length - 1) {
            if (str.get(i) == ')') deep--
            if (str.get(i) == '(') deep++
            if (deep == 0) {
                end = i
                break
            }
        }
        val content = parsePart(str.substring(1, end))
        val more: List<Content> = if (end == str.length) {
            mutableListOf()
        } else {
            parsePart(str.substring(end + 1, str.length))
        }
        val list = if(content.isEmpty()) mutableListOf() else mutableListOf<Content>(Group(content))
        list.addAll(more)
        return  list
    } else {
        var end = -1
        for (i in 0..str.length - 1) {
            if (str.get(i) == '(') {
                end = i
                break
            }
        }
        val content = str.substring(0, if(end == -1) str.length else end)
        val more: List<Content> = if (end == -1) {
            mutableListOf()
        } else {
            parsePart(str.substring(end, str.length))
        }
        val list = mutableListOf<Content>(Simple(content))
        list.addAll(more)
        return list
    }
}