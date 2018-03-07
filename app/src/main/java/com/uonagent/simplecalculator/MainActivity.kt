package com.uonagent.simplecalculator

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val operators = "+-*/"

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var answer: Double?
        buttonEquals.setOnClickListener {
            try {
                answer = calculateRPN(inputText.text.toString())
                answerText.text = answer.toString()
                answerText.setBackgroundResource(R.color.colorPrimary)
            } catch (e: IllegalArgumentException) {
                answerText.setText(R.string.error)
                answerText.setBackgroundResource(R.color.colorError)
            }
        }

    }

    /**
     * <p>This function calculates an expression in reverse polish notation</p>
     * <p>First expression is tokenized by String.tokenize method
     * @see String.tokenize
     * Than all tokens are going to RPN stack machine
     * If everything is ok, than return first element in stack</p>
     *
     * @param rpn string representation of expression. All tokens must be separated with whitespaces
     *
     * @return expression result
     *
     * @throws IllegalArgumentException if expressions has mistakes
     *
     * @author me
     */
    private fun calculateRPN(rpn: String): Double? {
        val stack = Stack<Double>()
        try {
            val tokens = rpn.tokenize()
            for (token in tokens) {
                when {
                    token.isOperator() && stack.size >= 2 -> {
                        val o2 = stack.pop()
                        val o1 = stack.pop()
                        when (token) {
                            "+" -> stack.push(o1 + o2)
                            "-" -> stack.push(o1 - o2)
                            "*" -> stack.push(o1 * o2)
                            "/" -> stack.push(o1 / o2)
                        }
                    }
                    token.isNumberic() -> stack.push(token.toDouble())
                    else -> throw IllegalArgumentException()
                }
            }
        } catch (e: IllegalArgumentException) {
            return null
        }
        if (stack.size == 1) {
            return stack.peek()
        } else {
            throw IllegalArgumentException()
        }
    }

    /**
     * <p>Splits expression to tokens</p>
     *
     * @author me
     *
     * @return List<String> of tokens
     */
    private fun String.tokenize() = this.split(" ")

    private fun String.isOperator() = operators.contains(this)

    private fun String.isNumberic() = this.matches(kotlin.text.Regex("-?\\d+(\\.\\d+)?"))
}