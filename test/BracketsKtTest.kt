import org.hamcrest.core.Is
import org.junit.Assert.assertThat
import org.junit.Test

class BracketsKtTest {

    @Test
    fun test() {
        assertThat(parse("((a((bc)(de)))f)").print(), Is.`is`("((a((bc)(de)))f)"))
        assertThat(parse("(((zbcd)(((e)fg))))").print(), Is.`is`("((zbcd)((e)fg))"))
        assertThat(parse("ab((c))").print(), Is.`is`("ab(c)"))
        assertThat(parse("((fgh()()()))").print(), Is.`is`("(fgh)"))
        assertThat(parse("()").print(), Is.`is`("NULL"))
        assertThat(parse("()(abc())").print(), Is.`is`("(abc)"))
    }
}