package io.joern.rubysrc2cpg.passes.cfg

import io.joern.rubysrc2cpg.testfixtures.RubyCfgTestCpg
import io.joern.x2cpg.passes.controlflow.cfgcreation.Cfg.AlwaysEdge
import io.joern.x2cpg.testfixtures.CfgTestFixture
import io.shiftleft.codepropertygraph.Cpg

class SimpleCfgCreationPassTest extends CfgTestFixture(() => new RubyCfgTestCpg()) {

  "CFG generation for simple fragments" should {
    "have correct structure for empty array literal" ignore {
      implicit val cpg: Cpg = code("x = []")
      succOf(":program") shouldBe expected(("x", AlwaysEdge))
      succOf("x") shouldBe expected(("=", AlwaysEdge))
      succOf("=") shouldBe expected(("<empty>", AlwaysEdge))
    }

    "have correct structure for array literal with values" in {
      implicit val cpg: Cpg = code("x = [1, 2]")
      succOf("1") shouldBe expected(("2", AlwaysEdge))
      succOf("x = [1, 2]") shouldBe expected(("<empty>", AlwaysEdge))
    }

    "assigning a literal value" in {
      implicit val cpg: Cpg = code("x = 1")
      succOf(":program") shouldBe expected(("x", AlwaysEdge))
      succOf("=") shouldBe expected(("<empty>", AlwaysEdge))
    }

    "assigning a string literal value" in {
      implicit val cpg: Cpg = code("x = 'some literal'")
      succOf(":program") shouldBe expected(("x", AlwaysEdge))
      succOf("=") shouldBe expected(("<empty>", AlwaysEdge))
    }

    "addition of two numbers" in {
      implicit val cpg: Cpg = code("x = 1 + 2")
      succOf(":program") shouldBe expected(("x", AlwaysEdge))
      succOf("=") shouldBe expected(("<empty>", AlwaysEdge))
      succOf("x") shouldBe expected(("1", AlwaysEdge))
      succOf("2") shouldBe expected(("1 + 2", AlwaysEdge))
      succOf("1") shouldBe expected(("2", AlwaysEdge))
      succOf("1 + 2") shouldBe expected(("=", AlwaysEdge))
    }

    "addition of two string" in {
      implicit val cpg: Cpg = code("x = 1 + 2")
      succOf(":program") shouldBe expected(("x", AlwaysEdge))
      succOf("=") shouldBe expected(("<empty>", AlwaysEdge))
      succOf("x") shouldBe expected(("1", AlwaysEdge))
      succOf("2") shouldBe expected(("1 + 2", AlwaysEdge))
      succOf("1") shouldBe expected(("2", AlwaysEdge))
      succOf("1 + 2") shouldBe expected(("=", AlwaysEdge))
    }

    "addition of multiple string" in {
      implicit val cpg: Cpg = code("""
          |a = "Nice to meet you"
          |b = ", "
          |c = "do you like blueberries?"
          |a+b+c
          |""".stripMargin)
      succOf(":program") shouldBe expected(("a", AlwaysEdge))
      succOf("a") shouldBe expected(("\"Nice to meet you\"", AlwaysEdge))
      succOf("b") shouldBe expected(("\", \"", AlwaysEdge))
      succOf("c") shouldBe expected(("\"do you like blueberries?\"", AlwaysEdge))
      succOf("a+b+c") shouldBe expected(("<empty>", AlwaysEdge))
      succOf("a+b") shouldBe expected(("c", AlwaysEdge))
      succOf("\"Nice to meet you\"") shouldBe expected(("=", AlwaysEdge))
      succOf("\", \"") shouldBe expected(("=", AlwaysEdge))
      succOf("\"do you like blueberries?\"") shouldBe expected(("=", AlwaysEdge))
    }

    "addition of multiple string and assign to variable" in {
      implicit val cpg: Cpg = code("""
          |a = "Nice to meet you"
          |b = ", "
          |c = "do you like blueberries?"
          |x = a+b+c
          |""".stripMargin)
      succOf(":program") shouldBe expected(("a", AlwaysEdge))
      succOf("a") shouldBe expected(("\"Nice to meet you\"", AlwaysEdge))
      succOf("b") shouldBe expected(("\", \"", AlwaysEdge))
      succOf("c") shouldBe expected(("\"do you like blueberries?\"", AlwaysEdge))
      succOf("a+b+c") shouldBe expected(("=", AlwaysEdge))
      succOf("a+b") shouldBe expected(("c", AlwaysEdge))
      succOf("\"Nice to meet you\"") shouldBe expected(("=", AlwaysEdge))
      succOf("\", \"") shouldBe expected(("=", AlwaysEdge))
      succOf("\"do you like blueberries?\"") shouldBe expected(("=", AlwaysEdge))
      succOf("x") shouldBe expected(("a", AlwaysEdge))
    }

    "single hierarchy of if else statement" in {
      implicit val cpg: Cpg = code("""
          |x = 1
          |if x > 2
          |   puts "x is greater than 2"
          |end
          |""".stripMargin)
      succOf(":program") shouldBe expected(("x", AlwaysEdge))
      succOf("1") shouldBe expected(("=", AlwaysEdge))
      succOf("x") shouldBe expected(("1", AlwaysEdge))
      succOf("2") shouldBe expected(("x > 2", AlwaysEdge))
    }

    "multiple hierarchy of if else statement" in {
      implicit val cpg: Cpg = code("""
          |x = 1
          |if x > 2
          |   puts "x is greater than 2"
          |elsif x <= 2 and x!=0
          |   puts "x is 1"
          |else
          |   puts "I can't guess the number"
          |end
          |""".stripMargin)
      succOf(":program") shouldBe expected(("x", AlwaysEdge))
      succOf("1") shouldBe expected(("=", AlwaysEdge))
      succOf("x") shouldBe expected(("1", AlwaysEdge))
      succOf("2") shouldBe expected(("x > 2", AlwaysEdge))
      succOf("x <= 2 and x!=0") subsetOf expected(("\"x is 1\"", AlwaysEdge))
      succOf("x <= 2 and x!=0") subsetOf expected(("<empty>", AlwaysEdge))
    }

  }
}
