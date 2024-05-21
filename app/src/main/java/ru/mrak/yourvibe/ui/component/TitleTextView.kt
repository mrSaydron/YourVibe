package ru.mrak.yourvibe.ui.component

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import ru.mrak.yourvibe.R

class TitleTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0): LinearLayout(context, attrs, defStyleAttr) {

    private var titleView: TextView?
    private var textView: TextView?

    var title = ""
        set(value) {
            titleView?.text = value
            field = value
        }
    var text  = ""
        set(value) {
            textView?.text = value
            field = value
        }
    var componentGravity: Int = 0
    var componentTextStyle: Int = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.view_title_text, this, true)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleTextView)

        typedArray.getString(R.styleable.TitleTextView_title)?.let { title = it }
        typedArray.getString(R.styleable.TitleTextView_text)?.let { text = it }
        componentGravity = typedArray.getInt(R.styleable.TitleTextView_gravity, 0)
        componentTextStyle = typedArray.getInt(R.styleable.TitleTextView_text_style, 0)

        typedArray.recycle()

        titleView = findViewById(R.id.view_title_text_title)
        textView = findViewById(R.id.view_title_text_text)

        titleView!!.text = title
        titleView!!.gravity = componentGravity
        textView!!.text = text
        textView!!.gravity = componentGravity
        textView!!.setTypeface(null, componentTextStyle) // Typeface.BOLD
    }
}