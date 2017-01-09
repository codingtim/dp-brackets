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
        return if(contents.size == 1 && contents[0] is Group) {
            contents[0].print()
        } else {
            "(" + (contents.map { it.print() }.joinToString(separator = "") { it->it }) + ")"
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
    } else if (str[0] == '(') {
        var end = -1
        var deep = 1
        //find out where this bracket ends
        for (i in 1..str.length - 1) {
            if (str.get(i) == ')') deep--
            if (str.get(i) == '(') deep++
            if (deep == 0) {
                end = i
                break
            }
        }
        //parse what's inside this bracket
        val content = parsePart(str.substring(1, end))
        val more: List<Content> = if (end == str.length) {
            //if there is no more content behind this bracket empty
            mutableListOf()
        } else {
            //else parse what's behind this bracket ex: (this)(more)evenMore
            parsePart(str.substring(end + 1, str.length))
        }
        val list = if(content.isEmpty()) mutableListOf() else mutableListOf<Content>(Group(content))
        list.addAll(more)
        return  list
    } else {
        var end = -1
        //find out where this value ends ex: abc(more)
        for (i in 0..str.length - 1) {
            if (str[i] == '(') {
                end = i
                break
            }
        }
        val content = str.substring(0, if(end == -1) str.length else end)
        val more: List<Content> = if (end == -1) {
            //if there is no more content behind this value ex: abc
            mutableListOf()
        } else {
            //if there is more content behind this value ex: abc(more)
            parsePart(str.substring(end, str.length))
        }
        val list = mutableListOf<Content>(Simple(content))
        list.addAll(more)
        return list
    }
}