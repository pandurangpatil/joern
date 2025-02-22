package io.joern.rubysrc2cpg.parser

class TernaryConditionalTests extends RubyParserAbstractTest {

  "Ternary conditional expressions" should {

    "be parsed as expressions" when {

      "they are a standalone one-line expression" in {
        val code = "x ? y : z"
        printAst(_.expression(), code) shouldEqual
          """ConditionalOperatorExpression
            | PrimaryExpression
            |  VariableReferencePrimary
            |   VariableIdentifierVariableReference
            |    VariableIdentifier
            |     x
            | ?
            | WsOrNl
            | PrimaryExpression
            |  VariableReferencePrimary
            |   VariableIdentifierVariableReference
            |    VariableIdentifier
            |     y
            | WsOrNl
            | :
            | WsOrNl
            | PrimaryExpression
            |  VariableReferencePrimary
            |   VariableIdentifierVariableReference
            |    VariableIdentifier
            |     z""".stripMargin

      }

      "they are a standalone multi-line expression" in {
        val code =
          """x ?
            |  y
            |: z
            |""".stripMargin
        printAst(_.expression(), code) shouldEqual
          """ConditionalOperatorExpression
            | PrimaryExpression
            |  VariableReferencePrimary
            |   VariableIdentifierVariableReference
            |    VariableIdentifier
            |     x
            | ?
            | WsOrNl
            | WsOrNl
            | PrimaryExpression
            |  VariableReferencePrimary
            |   VariableIdentifierVariableReference
            |    VariableIdentifier
            |     y
            | WsOrNl
            | :
            | WsOrNl
            | PrimaryExpression
            |  VariableReferencePrimary
            |   VariableIdentifierVariableReference
            |    VariableIdentifier
            |     z""".stripMargin
      }
    }
  }

}
