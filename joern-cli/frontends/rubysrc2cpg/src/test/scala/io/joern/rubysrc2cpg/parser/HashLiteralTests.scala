package io.joern.rubysrc2cpg.parser

class HashLiteralTests extends RubyParserAbstractTest {

  "A standalone hash literal" should {

    "be parsed as a primary expression" when {

      "it contains no elements" in {
        val code = "{ }"
        printAst(_.primary(), code) shouldEqual
          """HashConstructorPrimary
            | HashConstructor
            |  {
            |  WsOrNl
            |  }""".stripMargin
      }

      "it contains a single splatting identifier" in {
        val code = "{ **x }"
        printAst(_.primary(), code) shouldEqual
          """HashConstructorPrimary
            | HashConstructor
            |  {
            |  WsOrNl
            |  HashConstructorElements
            |   HashConstructorElement
            |    **
            |    PrimaryExpression
            |     VariableReferencePrimary
            |      VariableIdentifierVariableReference
            |       VariableIdentifier
            |        x
            |  }""".stripMargin
      }

      "it contains two consecutive splatting identifiers" in {
        val code = "{**x, **y}"
        printAst(_.primary(), code) shouldEqual
          """HashConstructorPrimary
            | HashConstructor
            |  {
            |  HashConstructorElements
            |   HashConstructorElement
            |    **
            |    PrimaryExpression
            |     VariableReferencePrimary
            |      VariableIdentifierVariableReference
            |       VariableIdentifier
            |        x
            |   ,
            |   WsOrNl
            |   HashConstructorElement
            |    **
            |    PrimaryExpression
            |     VariableReferencePrimary
            |      VariableIdentifierVariableReference
            |       VariableIdentifier
            |        y
            |  }""".stripMargin

      }

      "it contains an association between two splatting identifiers" in {
        val code = "{**x, y => 1, **z}"
        printAst(_.primary(), code) shouldEqual
          """HashConstructorPrimary
            | HashConstructor
            |  {
            |  HashConstructorElements
            |   HashConstructorElement
            |    **
            |    PrimaryExpression
            |     VariableReferencePrimary
            |      VariableIdentifierVariableReference
            |       VariableIdentifier
            |        x
            |   ,
            |   WsOrNl
            |   HashConstructorElement
            |    Association
            |     PrimaryExpression
            |      VariableReferencePrimary
            |       VariableIdentifierVariableReference
            |        VariableIdentifier
            |         y
            |     =>
            |     WsOrNl
            |     PrimaryExpression
            |      LiteralPrimary
            |       NumericLiteralLiteral
            |        NumericLiteral
            |         UnsignedNumericLiteral
            |          1
            |   ,
            |   WsOrNl
            |   HashConstructorElement
            |    **
            |    PrimaryExpression
            |     VariableReferencePrimary
            |      VariableIdentifierVariableReference
            |       VariableIdentifier
            |        z
            |  }""".stripMargin
      }

      "it contains a single splatting method invocation" in {
        val code = "{**group_by_type(some)}"
        printAst(_.primary(), code) shouldEqual
          """HashConstructorPrimary
            | HashConstructor
            |  {
            |  HashConstructorElements
            |   HashConstructorElement
            |    **
            |    PrimaryExpression
            |     InvocationWithParenthesesPrimary
            |      MethodIdentifier
            |       group_by_type
            |      ArgsOnlyArgumentsWithParentheses
            |       (
            |       Arguments
            |        ExpressionArgument
            |         PrimaryExpression
            |          VariableReferencePrimary
            |           VariableIdentifierVariableReference
            |            VariableIdentifier
            |             some
            |       )
            |  }""".stripMargin
      }
    }
  }

}
