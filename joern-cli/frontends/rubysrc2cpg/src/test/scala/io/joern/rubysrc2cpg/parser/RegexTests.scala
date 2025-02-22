package io.joern.rubysrc2cpg.parser

class RegexTests extends RubyParserAbstractTest {

  "An empty regex literal" when {

    "by itself" should {
      val code = "//"

      "be parsed as a primary expression" in {
        printAst(_.primary(), code) shouldEqual
          """LiteralPrimary
            | RegularExpressionLiteral
            |  /
            |  /""".stripMargin
      }
    }

    "on the RHS of an assignment" should {
      val code = "x = //"

      "be parsed as a single assignment to a regex literal" in {
        printAst(_.expressionOrCommand(), code) shouldEqual
          """ExpressionExpressionOrCommand
            | SingleAssignmentExpression
            |  VariableIdentifierOnlySingleLeftHandSide
            |   VariableIdentifier
            |    x
            |  =
            |  WsOrNl
            |  MultipleRightHandSide
            |   ExpressionOrCommands
            |    ExpressionExpressionOrCommand
            |     PrimaryExpression
            |      LiteralPrimary
            |       RegularExpressionLiteral
            |        /
            |        /""".stripMargin
      }
    }

    "as the argument to a `puts` command" should {
      val code = "puts //"

      "be parsed as such" in {
        printAst(_.expressionOrCommand(), code) shouldEqual
          """InvocationExpressionOrCommand
            | SingleCommandOnlyInvocationWithoutParentheses
            |  SimpleMethodCommand
            |   MethodIdentifier
            |    puts
            |   ArgumentsWithoutParentheses
            |    Arguments
            |     ExpressionArgument
            |      PrimaryExpression
            |       LiteralPrimary
            |        RegularExpressionLiteral
            |         /
            |         /""".stripMargin
      }
    }

    "as the sole argument to a parenthesized invocation" should {
      val code = "puts(//)"

      "be parsed as such" in {
        printAst(_.expressionOrCommand(), code) shouldEqual
          """ExpressionExpressionOrCommand
            | PrimaryExpression
            |  InvocationWithParenthesesPrimary
            |   MethodIdentifier
            |    puts
            |   ArgsOnlyArgumentsWithParentheses
            |    (
            |    Arguments
            |     ExpressionArgument
            |      PrimaryExpression
            |       LiteralPrimary
            |        RegularExpressionLiteral
            |         /
            |         /
            |    )""".stripMargin
      }
    }

    "as the second argument to a parenthesized invocation" should {
      val code = "puts(1, //)"

      "be parsed as such" in {
        printAst(_.expressionOrCommand(), code) shouldEqual
          """ExpressionExpressionOrCommand
            | PrimaryExpression
            |  InvocationWithParenthesesPrimary
            |   MethodIdentifier
            |    puts
            |   ArgsOnlyArgumentsWithParentheses
            |    (
            |    Arguments
            |     ExpressionArgument
            |      PrimaryExpression
            |       LiteralPrimary
            |        NumericLiteralLiteral
            |         NumericLiteral
            |          UnsignedNumericLiteral
            |           1
            |     ,
            |     WsOrNl
            |     ExpressionArgument
            |      PrimaryExpression
            |       LiteralPrimary
            |        RegularExpressionLiteral
            |         /
            |         /
            |    )""".stripMargin
      }
    }

    "used in a `when` clause" should {
      val code =
        """case foo
            |      when /^ch_/
            |        bar
            |end""".stripMargin

      "be parsed as such" in {
        printAst(_.primary(), code) shouldEqual
          """CaseExpressionPrimary
              | CaseExpression
              |  case
              |  WsOrNl
              |  ExpressionExpressionOrCommand
              |   PrimaryExpression
              |    VariableReferencePrimary
              |     VariableIdentifierVariableReference
              |      VariableIdentifier
              |       foo
              |  Separators
              |   Separator
              |  WhenClause
              |   when
              |   WsOrNl
              |   WhenArgument
              |    Expressions
              |     PrimaryExpression
              |      LiteralPrimary
              |       RegularExpressionLiteral
              |        /
              |        ^ch_
              |        /
              |   ThenClause
              |    Separator
              |    WsOrNl
              |    CompoundStatement
              |     Statements
              |      ExpressionOrCommandStatement
              |       ExpressionExpressionOrCommand
              |        PrimaryExpression
              |         VariableReferencePrimary
              |          VariableIdentifierVariableReference
              |           VariableIdentifier
              |            bar
              |     Separators
              |      Separator
              |  end""".stripMargin
      }

      "used in a `unless` clause" should {
        val code =
          """unless /\A([^@\s]+)@((?:[-a-z0-9]+\.)+[a-z]{2,})\z/i.match?(value)
            |end""".stripMargin

        "be parsed as such" in {
          printAst(_.primary(), code) shouldEqual
            """UnlessExpressionPrimary
              | UnlessExpression
              |  unless
              |  WsOrNl
              |  ExpressionExpressionOrCommand
              |   PrimaryExpression
              |    ChainedInvocationPrimary
              |     LiteralPrimary
              |      RegularExpressionLiteral
              |       /
              |       \A([^@\s]+)@((?:[-a-z0-9]+\.)+[a-z]{2,})\z
              |       /i
              |     .
              |     MethodName
              |      MethodIdentifier
              |       MethodOnlyIdentifier
              |        match
              |        ?
              |     ArgsOnlyArgumentsWithParentheses
              |      (
              |      Arguments
              |       ExpressionArgument
              |        PrimaryExpression
              |         VariableReferencePrimary
              |          VariableIdentifierVariableReference
              |           VariableIdentifier
              |            value
              |      )
              |  ThenClause
              |   Separator
              |   CompoundStatement
              |  end""".stripMargin
        }
      }
    }
  }

  "A non-interpolated regex literal" when {

    "by itself" should {
      val code = "/(eu|us)/"

      "be parsed as a primary expression" in {
        printAst(_.primary(), code) shouldEqual
          """LiteralPrimary
            | RegularExpressionLiteral
            |  /
            |  (eu|us)
            |  /""".stripMargin
      }
    }

    "on the RHS of an assignment" should {
      val code = "x = /(eu|us)/"

      "be parsed as a single assignment to a regex literal" in {
        printAst(_.expressionOrCommand(), code) shouldEqual
          """ExpressionExpressionOrCommand
            | SingleAssignmentExpression
            |  VariableIdentifierOnlySingleLeftHandSide
            |   VariableIdentifier
            |    x
            |  =
            |  WsOrNl
            |  MultipleRightHandSide
            |   ExpressionOrCommands
            |    ExpressionExpressionOrCommand
            |     PrimaryExpression
            |      LiteralPrimary
            |       RegularExpressionLiteral
            |        /
            |        (eu|us)
            |        /""".stripMargin
      }
    }

    "as the argument to a `puts` command" should {
      val code = "puts /(eu|us)/"

      "be parsed as such" in {
        printAst(_.expressionOrCommand(), code) shouldEqual
          """InvocationExpressionOrCommand
            | SingleCommandOnlyInvocationWithoutParentheses
            |  SimpleMethodCommand
            |   MethodIdentifier
            |    puts
            |   ArgumentsWithoutParentheses
            |    Arguments
            |     ExpressionArgument
            |      PrimaryExpression
            |       LiteralPrimary
            |        RegularExpressionLiteral
            |         /
            |         (eu|us)
            |         /""".stripMargin
      }
    }

    "as the argument to an parenthesized invocation" should {
      val code = "puts(/(eu|us)/)"

      "be parsed as such" in {
        printAst(_.expressionOrCommand(), code) shouldEqual
          """ExpressionExpressionOrCommand
            | PrimaryExpression
            |  InvocationWithParenthesesPrimary
            |   MethodIdentifier
            |    puts
            |   ArgsOnlyArgumentsWithParentheses
            |    (
            |    Arguments
            |     ExpressionArgument
            |      PrimaryExpression
            |       LiteralPrimary
            |        RegularExpressionLiteral
            |         /
            |         (eu|us)
            |         /
            |    )""".stripMargin
      }
    }
  }

  "A (numeric literal)-interpolated regex literal" when {

    "by itself" should {
      val code = "/x#{1}y/"

      "be parsed as a primary expression" in {
        printAst(_.primary(), code) shouldEqual
          """RegexInterpolationPrimary
              | RegexInterpolation
              |  /
              |  x
              |  InterpolatedRegexSequence
              |   #{
              |   CompoundStatement
              |    Statements
              |     ExpressionOrCommandStatement
              |      ExpressionExpressionOrCommand
              |       PrimaryExpression
              |        LiteralPrimary
              |         NumericLiteralLiteral
              |          NumericLiteral
              |           UnsignedNumericLiteral
              |            1
              |   }
              |  y
              |  /""".stripMargin
      }
    }

    "on the RHS of an assignment" should {
      val code = "x = /x#{1}y/"

      "be parsed as a single assignment to a regex literal" in {
        printAst(_.expressionOrCommand(), code) shouldEqual
          """ExpressionExpressionOrCommand
            | SingleAssignmentExpression
            |  VariableIdentifierOnlySingleLeftHandSide
            |   VariableIdentifier
            |    x
            |  =
            |  WsOrNl
            |  MultipleRightHandSide
            |   ExpressionOrCommands
            |    ExpressionExpressionOrCommand
            |     PrimaryExpression
            |      RegexInterpolationPrimary
            |       RegexInterpolation
            |        /
            |        x
            |        InterpolatedRegexSequence
            |         #{
            |         CompoundStatement
            |          Statements
            |           ExpressionOrCommandStatement
            |            ExpressionExpressionOrCommand
            |             PrimaryExpression
            |              LiteralPrimary
            |               NumericLiteralLiteral
            |                NumericLiteral
            |                 UnsignedNumericLiteral
            |                  1
            |         }
            |        y
            |        /""".stripMargin
      }
    }

    "as the argument to a `puts` command" should {
      val code = "puts /x#{1}y/"

      "be parsed as such" in {
        printAst(_.expressionOrCommand(), code) shouldEqual
          """InvocationExpressionOrCommand
            | SingleCommandOnlyInvocationWithoutParentheses
            |  SimpleMethodCommand
            |   MethodIdentifier
            |    puts
            |   ArgumentsWithoutParentheses
            |    Arguments
            |     ExpressionArgument
            |      PrimaryExpression
            |       RegexInterpolationPrimary
            |        RegexInterpolation
            |         /
            |         x
            |         InterpolatedRegexSequence
            |          #{
            |          CompoundStatement
            |           Statements
            |            ExpressionOrCommandStatement
            |             ExpressionExpressionOrCommand
            |              PrimaryExpression
            |               LiteralPrimary
            |                NumericLiteralLiteral
            |                 NumericLiteral
            |                  UnsignedNumericLiteral
            |                   1
            |          }
            |         y
            |         /""".stripMargin
      }
    }

    "as the argument to an parenthesized invocation" should {
      val code = "puts(/x#{1}y/)"

      "be parsed as such" in {
        printAst(_.expressionOrCommand(), code) shouldEqual
          """ExpressionExpressionOrCommand
            | PrimaryExpression
            |  InvocationWithParenthesesPrimary
            |   MethodIdentifier
            |    puts
            |   ArgsOnlyArgumentsWithParentheses
            |    (
            |    Arguments
            |     ExpressionArgument
            |      PrimaryExpression
            |       RegexInterpolationPrimary
            |        RegexInterpolation
            |         /
            |         x
            |         InterpolatedRegexSequence
            |          #{
            |          CompoundStatement
            |           Statements
            |            ExpressionOrCommandStatement
            |             ExpressionExpressionOrCommand
            |              PrimaryExpression
            |               LiteralPrimary
            |                NumericLiteralLiteral
            |                 NumericLiteral
            |                  UnsignedNumericLiteral
            |                   1
            |          }
            |         y
            |         /
            |    )""".stripMargin
      }
    }
  }
}
